package ch.p3n.apps.appfront.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.security.PrivateKey;

import ch.p3n.apps.appfront.api.dto.ActivationDTO;
import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.dto.InterestDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.facade.controller.ActivationControllerFacade;
import ch.p3n.apps.appfront.facade.exception.ContentDecryptionException;
import ch.p3n.apps.appfront.facade.exception.ContentEncryptionException;
import ch.p3n.apps.appfront.facade.security.DecryptionUtil;
import ch.p3n.apps.appfront.facade.security.EncryptionUtil;
import project2.appfront.R;

/**
 * Activity which shows the current active interests.
 *
 * @author michael
 * @author claudio
 */
public class ActiveInterests extends AppCompatActivity {

    public static final String ACTIVE_INTEREST = "ActiveInterest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_interests);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String activity = intent.getStringExtra(ActiveInterests.ACTIVE_INTEREST);

        TextView textView = new TextView(this);
        textView.setTextSize(20);
        textView.setText(activity);

        LinearLayout layout = (LinearLayout) findViewById(R.id.content);
        layout.addView(textView);
    }

    public void goInactive(View view) {
        Intent intent = new Intent(this, MainActivity.class);

        try {

            // Get and decrypt client and interest id
            final PrivateKey privateKey = AppUtil.getSavedPrivateKey(view.getContext());
            final String encryptedClientId = AppUtil.getUserID(view.getContext());
            final String encryptedInterestId = AppUtil.getInterestID(view.getContext());
            final String decryptedClientId = DecryptionUtil.decrypt(privateKey, encryptedClientId);
            final String decryptedInterestId = DecryptionUtil.decrypt(privateKey, encryptedInterestId);

            // Create authentication dto
            final AuthenticationDTO authenticationDTO = new AuthenticationDTO();
            authenticationDTO.setClientId(decryptedClientId);

            // Create interest dto
            final InterestDTO interestDTO = new InterestDTO();
            interestDTO.setInterestId(decryptedInterestId);

            // Create activation dto
            final ActivationDTO activationDTO = new ActivationDTO();
            activationDTO.setInterest(interestDTO);
            activationDTO.setAuthentication(authenticationDTO);
            activationDTO.setClientPushToken(AppUtil.PUSH_TOKEN);
            activationDTO.setClientRandom(AppUtil.CLIENT_SECRET);

            // Activate user.
            final ActivationDTO encryptedActivationDTO = EncryptionUtil.encryptForBackend(activationDTO);
            new ActivationControllerFacade().postDeactivate(encryptedActivationDTO);
            AppUtil.deleteInterestID(view.getContext());

        } catch (BusinessException e) {
            Log.e(ACTIVE_INTEREST, "unable to deactivate the user", e);
        } catch (ContentEncryptionException e) {
            Log.e(ACTIVE_INTEREST, "unable to encrypt activation for backend", e);
        } catch (ContentDecryptionException e) {
            Log.e(ACTIVE_INTEREST, "unable to decrypt required data", e);
        }

        startActivity(intent);
    }
}
