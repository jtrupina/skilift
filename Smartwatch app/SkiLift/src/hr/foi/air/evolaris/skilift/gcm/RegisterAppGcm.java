package hr.foi.air.evolaris.skilift.gcm;

import hr.foi.air.evolaris.skilift.smartphone.MainActivity;
import hr.foi.air.evolaris.skilift.utils.Constants;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

//Class for register user smartphone on GCM
public class RegisterAppGcm {

	private Context ctx;
	private GoogleCloudMessaging gcm;
	private String regid;

	// Constructor
	public RegisterAppGcm(Context ctx) {
		this.ctx = ctx;
		this.regid = null;
		register();
	}

	// Method for register to GCM (called from constructor)
	private void register() {
		if (checkPlayServices()) {
			gcm = GoogleCloudMessaging.getInstance(ctx);
			regid = getRegistrationId(ctx);

			if (regid.isEmpty())
				new RegisterAppGcmBackground().execute();
			else
				Toast.makeText(ctx, "Device already Registered",
						Toast.LENGTH_SHORT).show();
		} else
			Log.i(Constants.TAG, "No valid Google Play Services APK found.");

	}

	// Method for checking if smartphone support GooglePlayServices
	private boolean checkPlayServices() {
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(ctx);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
				GooglePlayServicesUtil.getErrorDialog(resultCode,
						(Activity) ctx,
						Constants.PLAY_SERVICES_RESOLUTION_REQUEST).show();
			} else {
				Log.i(Constants.TAG, "This device is not supported.");
				((Activity) ctx).finish();
			}
			return false;
		}
		return true;
	}

	// Getter for GCM registration id which is stored in smartphone memory
	// (SharedPreferences)
	private String getRegistrationId(Context context) {
		final SharedPreferences prefs = getGCMPreferences(context);
		String registrationId = prefs.getString(Constants.PROPERTY_REG_ID, "");

		if (registrationId.isEmpty()) {
			Log.i(Constants.TAG, "Registration not found.");
			return "";
		}
		// Check if app was updated; if so, it must clear the registration ID
		// since the existing regID is not guaranteed to work with the new
		// app version.
		int registeredVersion = prefs.getInt(Constants.PROPERTY_APP_VERSION,
				Integer.MIN_VALUE);
		int currentVersion = getAppVersion(ctx);
		if (registeredVersion != currentVersion) {
			Log.i(Constants.TAG, "App version changed.");
			return "";
		}
		return registrationId;
	}

	// Getter for used shared preferences
	private SharedPreferences getGCMPreferences(Context context) {
		return ctx.getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);
	}

	// Getter for version of application
	private static int getAppVersion(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			// should never happen
			throw new RuntimeException("Could not get package name: " + e);
		}
	}

	// Background gcm registration operations
	private class RegisterAppGcmBackground extends
			AsyncTask<Void, Void, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected String doInBackground(Void... arg0) {
			String msg = "";
			try {
				if (gcm == null) {
					gcm = GoogleCloudMessaging.getInstance(ctx);
				}
				regid = gcm.register(Constants.SENDER_ID);
				msg = "Device registered, registration ID=" + regid;

				sendRegistrationIdToBackend();
				storeRegistrationId(ctx, regid);
			} catch (IOException ex) {
				msg = "Error :" + ex.getMessage();
			}
			return msg;
		}

		// Method for storing registration id inside smartphpone memory
		private void storeRegistrationId(Context ctx, String regid) {
			int appVersion = getAppVersion(ctx);
			final SharedPreferences prefs = ctx.getSharedPreferences(
					MainActivity.class.getSimpleName(), Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(Constants.PROPERTY_REG_ID, regid);
			editor.putInt("appVersion", appVersion);
			editor.commit();

		}

		// Method for sending registration id to backend server (save
		// registration id to database)
		private void sendRegistrationIdToBackend() {
			URI url = null;
			try {
				url = new URI(
						"http://arka.foi.hr/WebDiP/2013_projekti/WebDiP2013_079/register.php?regId="
								+ regid);
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet request = new HttpGet();
			request.setURI(url);
			try {
				httpclient.execute(request);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(
					ctx,
					"Registration Completed. Now you can see the notifications",
					Toast.LENGTH_SHORT).show();
		}
	}

}