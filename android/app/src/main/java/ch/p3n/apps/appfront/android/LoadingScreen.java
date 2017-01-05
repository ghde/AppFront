package ch.p3n.apps.appfront.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import java.io.File;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Collection;

import ch.p3n.apps.appfront.api.dto.ActivityDTO;
import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.exception.BusinessException;
import ch.p3n.apps.appfront.facade.controller.LoginControllerFacade;
import ch.p3n.apps.appfront.facade.controller.RegistrationControllerFacade;
import ch.p3n.apps.appfront.facade.exception.ContentDecryptionException;
import ch.p3n.apps.appfront.facade.exception.ContentEncryptionException;
import ch.p3n.apps.appfront.facade.security.DecryptionUtil;
import ch.p3n.apps.appfront.facade.security.EncryptionUtil;
import project2.appfront.R;

/**
 * Loading screen activity.
 *
 * @author michael
 * @author claudio
 */

public class LoadingScreen extends Activity {

    private static final String TAG = "LoadingScreen";

    private static final String CAUGHT_EXCEPTION = "caught exception";

    private static final String SHARED_PREFERENCE = "ch.p3n.apps.appfront.DANGEROUS_PREFERENCE";

    private static SharedPreferences sharedPref = null;

    private static String entry = null;

    private static final String PREFERENCE_SUCCESS_MESSAGE = "# Saved in SharedPreferences";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove the Title Bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Context context = getBaseContext();
        sharedPref = context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Get the view from loading_screen.xml
        setContentView(R.layout.loading_screen);

        final Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {

                    // Get context to handover for AppUtil
                    Context context = getBaseContext();

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("start", "# Start of log");
                    editor.commit();

                    entry = sharedPref.getString("start", "Log malfunctioned");

                    // Check if client public key exists. If no -> register.
                    File clientKeyFiles = new File(getFilesDir(), AppUtil.PUBLIC_KEY_FILE);
                    if (!clientKeyFiles.exists()) {
                        doRegister(context);
                    }

                    doLoginOrRegister(context);
                } catch (Exception e) {
                    Log.d(TAG, CAUGHT_EXCEPTION, e);
                    Log.v(TAG, "login and registration process failed");
                    TextView errorView = (TextView) findViewById(R.id.error);
                    updateView(errorView, "Error occurred: " + e.getMessage());
                } finally {

                    try {
                        // Wait another 5 seconds
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        Log.v(TAG, "sleep failed", e);
                    }

                    Intent myIntent = new Intent(LoadingScreen.this,
                            MainActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        };
        splashTread.start();
    }

    private void doLoginOrRegister(Context context) throws ContentDecryptionException, ContentEncryptionException, BusinessException  {
        // Try to login, in case of error, register new
        try {
            doLogin(context);
        } catch (ContentDecryptionException | ContentEncryptionException | BusinessException e) {
            Log.d(TAG, CAUGHT_EXCEPTION, e);
            Log.e(TAG, "initial login request failed, restarting with registration");
            doRegister(context);
            doLogin(context);
        }
    }

    private void doRegister(Context context) throws BusinessException {

        TextView registrationView = (TextView) findViewById(R.id.registrationStatus);

        // Debugging purpose
        StringBuilder sb = new StringBuilder();
        updateView(registrationView, sb.append("# Registration process started").append("\n"));

        // Test shared pref
        updateView(registrationView, sb.append(entry).append("\n"));

        AppUtil.generateKeyPairClient(context);
        final String publicKeyString = AppUtil.getSavedPublicKeyAsString(context);
        final String privateKeyString = AppUtil.getSavedPrivateKeyAsString(context);

        //Writing PublicKeyString into SharedPreferences
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("PublicKeyString", publicKeyString);
        editor.putString("PrivateKeyString", privateKeyString);
        editor.putString("PublicKeyStringSaved", PREFERENCE_SUCCESS_MESSAGE);
        editor.apply();

        // Debugging purpose
        updateView(registrationView, sb.append("# Public key generated: ").append(publicKeyString).append("\n"));

        //Show publicKeyString-saving process in SharedPreferences
        updateView(registrationView, sb.append(sharedPref.getString("PublicKeyStringSaved", " # Could not be saved")).append("\n"));

        //Show privateKeyString out of SharedPreferences
        updateView(registrationView, sb.append("# Private Key String: ").append(sharedPref.getString("PrivateKeyString", "# Could not be saved")).append("\n"));

        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        authenticationDTO.setClientPublicKey(publicKeyString);

        // save user ID
        updateView(registrationView, sb.append("# Backend call register api").append("\n"));

        // Backend call
        final AuthenticationDTO response = new RegistrationControllerFacade().postRegister(authenticationDTO);

        //Writing ClientID into SharedPreferences
        String clientID = response.getClientId();
        editor.putString("ClientIDString", clientID);
        editor.putString("ClientIDStringSaved", PREFERENCE_SUCCESS_MESSAGE);
        editor.apply();

        // Debugging purpose
        updateView(registrationView, sb.append("# Client ID received: ").append(clientID).append("\n"));

        //Show clientID in SharedPreferences
        updateView(registrationView, sb.append(sharedPref.getString("ClientIDStringSaved", "# Could not be saved")).append("\n"));

        // Save user Id
        AppUtil.saveUserID(context, response);

        editor.commit();
    }

    private void doLogin(Context context) throws ContentDecryptionException, ContentEncryptionException, BusinessException {

        TextView loginView = (TextView) findViewById(R.id.loginStatus);

        // Debugging purpose
        StringBuilder sb = new StringBuilder();
        updateView(loginView, sb.append("# Login process started").append("\n"));

        String decryptedClientID;

        // Decrypt client id
        final String encryptedUserId = AppUtil.getUserID(context);

        // Just for Debugging
        updateView(loginView, sb.append("# Encrypted client id : ").append(encryptedUserId).append("\n"));

        decryptedClientID = DecryptionUtil.decrypt(AppUtil.getSavedPrivateKey(context), encryptedUserId);

        //Writing PublicKeyString into SharedPreferences
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("DecryptedClientID", decryptedClientID);
        editor.putString("DecryptedClientIDSaved", PREFERENCE_SUCCESS_MESSAGE);
        editor.apply();

        //just for debugging
        updateView(loginView, sb.append("# Client id decrypted : ").append(decryptedClientID).append("\n"));

        //Show clientID in SharedPreferences
        updateView(loginView, sb.append(sharedPref.getString("DecryptedClientIDSaved", "# Could not be saved")).append("\n"));

        // Create dto and encrypt.
        AuthenticationDTO clientLogin = new AuthenticationDTO();
        clientLogin.setClientId(decryptedClientID);
        AuthenticationDTO encryptedClientLogin = EncryptionUtil.encryptForBackend(clientLogin);

        // Process login
        updateView(loginView, sb.append("# Backend call login api").append("\n"));
        Collection<ActivityDTO> activities = new LoginControllerFacade().postLogin(encryptedClientLogin);

        // Save activities
        final PrivateKey privateKey = AppUtil.getSavedPrivateKey(context);
        for (ActivityDTO activity : activities) {
            final ActivityDTO decryptedActivity = DecryptionUtil.decrypt(privateKey, activity);
            AppUtil.saveActivity(context, decryptedActivity.getName());
        }

        // Debugging purpose
        updateView(loginView, sb.append("# Activities received : ").append(Arrays.toString(AppUtil.getActivities().toArray())).append("\n"));
        editor.commit();
    }

    private void updateView(final TextView textView, final StringBuilder sb) {
        updateView(textView, sb.toString());
    }

    private void updateView(final TextView textView, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.setTextSize(12);
                textView.setText(text);
            }
        });
    }

}