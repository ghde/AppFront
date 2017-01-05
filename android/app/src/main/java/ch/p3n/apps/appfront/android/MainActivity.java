package ch.p3n.apps.appfront.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.security.PrivateKey;
import java.util.Arrays;

import ch.p3n.apps.appfront.api.dto.ActivationDTO;
import ch.p3n.apps.appfront.api.dto.ActivityDTO;
import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.dto.InterestDTO;
import ch.p3n.apps.appfront.api.dto.MatchType;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.facade.controller.ActivationControllerFacade;
import ch.p3n.apps.appfront.facade.exception.ContentDecryptionException;
import ch.p3n.apps.appfront.facade.exception.ContentEncryptionException;
import ch.p3n.apps.appfront.facade.security.DecryptionUtil;
import ch.p3n.apps.appfront.facade.security.EncryptionUtil;
import project2.appfront.R;

/**
 * Main activity.
 *
 * @author michael
 * @author claudio
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Show interests
        Spinner spinner = (Spinner) findViewById(R.id.interests);
        final String[] activities = AppUtil.getActivities().toArray(new String[AppUtil.getActivities().size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, activities);
        spinner.setAdapter(adapter);

        try {
            final String userId = AppUtil.getUserID(getBaseContext());
            final PrivateKey privateKey = AppUtil.getSavedPrivateKey(getBaseContext());
            final String decryptedUserIdText = "User-ID: ";
            final String decryptedUserId = DecryptionUtil.decrypt(privateKey, userId);
            TextView userBox2 = (TextView) findViewById(R.id.userIDfromMemory);
            userBox2.setText(decryptedUserIdText + decryptedUserId);
        } catch (ContentDecryptionException e) {
            Log.e(TAG, "unable to decrypt the interests", e);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void goActive(View view) {
        Intent intent = new Intent(this, ActiveInterests.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);

        final String activeInterest;
        if (editText.getText().toString().length() > 0) {
            activeInterest = editText.getText().toString();
        } else {
            Spinner spinner = (Spinner) findViewById(R.id.interests);
            activeInterest = spinner.getSelectedItem().toString();
        }
        intent.putExtra(ActiveInterests.ACTIVE_INTEREST, activeInterest);

        try {

            // Create activity dto
            final ActivityDTO activityDto = new ActivityDTO();
            activityDto.setName(activeInterest);

            // Create interest dto
            final InterestDTO interestDTO = new InterestDTO();
            interestDTO.setInterests(Arrays.asList(activityDto));

            // Get and decrypt client and interest id
            final PrivateKey privateKey = AppUtil.getSavedPrivateKey(view.getContext());
            final String encryptedClientId = AppUtil.getUserID(view.getContext());
            final String decryptedClientId = DecryptionUtil.decrypt(privateKey, encryptedClientId);

            // Create authentication dto
            final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
            authenticationDTO.setClientId(decryptedClientId);

            // Create activation dto
            final ActivationDTO activationDTO = new ActivationDTO();
            activationDTO.setAuthentication(authenticationDTO);
            activationDTO.setClientPushToken(AppUtil.PUSH_TOKEN);
            activationDTO.setClientRandom(AppUtil.CLIENT_SECRET);
            activationDTO.setInterest(interestDTO);
            activationDTO.setVisibilityDuration(10);
            activationDTO.setVisibilityType(MatchType.BLUETOOTH);

            // Activate user.
            final ActivationDTO encryptedActivationDTO = EncryptionUtil.encryptForBackend(activationDTO);
            final InterestDTO interest = new ActivationControllerFacade().postActivate(encryptedActivationDTO);
            AppUtil.saveInterestID(view.getContext(), interest);

        } catch (BusinessException e) {
            Log.e(TAG, "unable to activate the user", e);
        } catch (ContentDecryptionException e) {
            Log.e(TAG, "unable to decrypt interest data from backend", e);
        } catch (ContentEncryptionException e) {
            Log.e(TAG, "unable to encrypt activation for backend", e);
        }

        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
