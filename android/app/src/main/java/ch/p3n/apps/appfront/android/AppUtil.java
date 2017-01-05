package ch.p3n.apps.appfront.android;

import android.content.Context;
import android.util.Log;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;

import ch.p3n.apps.appfront.api.dto.AuthenticationDTO;
import ch.p3n.apps.appfront.api.dto.InterestDTO;
import ch.p3n.apps.appfront.facade.exception.KeyFileReadException;
import ch.p3n.apps.appfront.facade.security.KeyGeneratorUtil;

/**
 * App util with contains helper functions.
 *
 * @author michael
 * @author claudio
 */
public class AppUtil {

    private static Collection<String> activities = new ArrayList<>();

    public static final String CLIENT_SECRET = "CLI_SCRT";

    public static final String PUSH_TOKEN = "PSHTOK";

    public static final String PRK = "file-001";

    public static final String PUK = "file-002";

    public static final String UID = "file-003";

    public static final String IID = "file-004";

    private static final String TAG = "AppUtil";

    private static final String CAUGHT_EXCEPTION = "caught exception";

    private AppUtil() {
        // Nothing to do.
    }

    public static void saveActivity(String activity) {
        activities.add(activity);
    }

    public static Collection<String> getActivities() {
        return activities;
    }

    public static void generateKeyPairClient(Context context) {
        FileOutputStream privateKeyOutputStream = null;
        FileOutputStream publicKeyOutputStream = null;

        try {
            privateKeyOutputStream = context.openFileOutput(PRK, Context.MODE_PRIVATE);
            publicKeyOutputStream = context.openFileOutput(PUK, Context.MODE_PRIVATE);
            KeyGeneratorUtil.generateKeyPair(privateKeyOutputStream, publicKeyOutputStream);
            privateKeyOutputStream.close();
            publicKeyOutputStream.close();
        } catch (IOException e) {
            Log.d(TAG, CAUGHT_EXCEPTION, e);
            Log.e(TAG, "unable to generate keypair");
        } finally {
            IOUtils.closeQuietly(privateKeyOutputStream);
            IOUtils.closeQuietly(publicKeyOutputStream);
        }
    }

    public static String getSavedPublicKeyAsString(Context context) {
        String publicKeyString = null;
        FileInputStream publicKeyInputStream = null;
        PublicKey pubRecovered;

        try {
            publicKeyInputStream = context.openFileInput(PUK);
            pubRecovered = KeyGeneratorUtil.getPublicKey(publicKeyInputStream);
            publicKeyString = KeyGeneratorUtil.getKeyAsString(pubRecovered);
        } catch (FileNotFoundException e) {
            Log.d(TAG, CAUGHT_EXCEPTION, e);
            Log.e(TAG, "public key file not found");
        } catch (KeyFileReadException e) {
            Log.d(TAG, CAUGHT_EXCEPTION, e);
            Log.e(TAG, "unable to read public key");
        } finally {
            IOUtils.closeQuietly(publicKeyInputStream);
        }
        return publicKeyString;
    }

    public static PrivateKey getSavedPrivateKey(Context context) {
        FileInputStream privateKeyInputStream = null;
        PrivateKey pubRecovered = null;

        try {
            privateKeyInputStream = context.openFileInput(PRK);
            pubRecovered = KeyGeneratorUtil.getPrivateKey(privateKeyInputStream);
        } catch (FileNotFoundException e) {
            Log.d(TAG, CAUGHT_EXCEPTION, e);
            Log.e(TAG, "private key file not found");
        } catch (KeyFileReadException e) {
            Log.d(TAG, CAUGHT_EXCEPTION, e);
            Log.e(TAG, "unable to read private key");
        } finally {
            IOUtils.closeQuietly(privateKeyInputStream);
        }
        return pubRecovered;
    }

    public static void saveUserID(Context context, AuthenticationDTO response) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(UID, Context.MODE_PRIVATE);
            IOUtils.write(response.getClientId(), fos, Charset.defaultCharset());
        } catch (IOException e) {
            Log.d(TAG, CAUGHT_EXCEPTION, e);
            Log.e(TAG, "user id could not be written");
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    public static void saveInterestID(Context context, InterestDTO response) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(IID, Context.MODE_PRIVATE);
            IOUtils.write(response.getInterestId(), fos, Charset.defaultCharset());
        } catch (IOException e) {
            Log.d(TAG, CAUGHT_EXCEPTION, e);
            Log.e(TAG, "interest id could not be written");
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    public static String getUserID(Context context) {
        String userId = null;
        InputStream is = null;
        try {
            is = context.openFileInput(UID);
            userId = IOUtils.toString(is, Charset.defaultCharset());
        } catch (FileNotFoundException e) {
            Log.d(TAG, CAUGHT_EXCEPTION, e);
            Log.e(TAG, "user id file could not be found");
        } catch (IOException e) {
            Log.d(TAG, CAUGHT_EXCEPTION, e);
            Log.e(TAG, "unable to read user id file");
        } finally {
            IOUtils.closeQuietly(is);
        }
        return userId;
    }

    public static String getInterestID(Context context) {
        String userId = null;
        InputStream is = null;
        try {
            is = context.openFileInput(IID);
            userId = IOUtils.toString(is, Charset.defaultCharset());
        } catch (FileNotFoundException e) {
            Log.d(TAG, CAUGHT_EXCEPTION, e);
            Log.e(TAG, "user id file could not be found");
        } catch (IOException e) {
            Log.d(TAG, CAUGHT_EXCEPTION, e);
            Log.e(TAG, "unable to read user id file");
        } finally {
            IOUtils.closeQuietly(is);
        }
        return userId;
    }

    public static void deleteInterestID(Context context) {
        context.deleteFile(IID);
    }

}