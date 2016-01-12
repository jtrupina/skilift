package hr.foi.air.evolaris.skilift.smartwatch;

import hr.foi.air.evolaris.skilift.R;
import hr.foi.air.evolaris.skilift.smartphone.MainActivity;

import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.sonyericsson.extras.liveware.aef.registration.Registration;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;

//Class that extends abstract class RegistrationInformation and
//Class that is called (inside service) when smartwatch extension is isntalled and
//Class that setup RegistrationConfiguration for smartwatch (icons, names, keys, etc.)
public class SmartWatchRegistrationInformation extends RegistrationInformation {

	final Context mContext;
	private String extensionKey;
	private static final String EXTENSION_KEY_PREF = "EXTENSION_KEY_PREF";

	// Constructor
	protected SmartWatchRegistrationInformation(Context context) {
		if (context == null) {
			throw new IllegalArgumentException("context == null");
		}
		mContext = context;
	}

	// ///////////////////////////////////////////////////////////////////////
	// Abstract methods from RegistrationInformation
	// ////////////////////////////////////////////////////////////////////////
	@Override
	public int getRequiredControlApiVersion() {
		// This extension supports all accessories from Control API level 2 and
		// up.
		return 2;
	}

	@Override
	public int getRequiredSensorApiVersion() {
		return API_NOT_REQUIRED;
	}

	@Override
	public int getRequiredNotificationApiVersion() {
		return API_NOT_REQUIRED;
	}

	@Override
	public int getRequiredWidgetApiVersion() {
		return API_NOT_REQUIRED;
	}

	@Override
	public boolean controlInterceptsBackButton() {
		// Extension has it's own navigation, handles back presses.
		return true;
	}

	// Method that Get the extension registration information.
	@Override
	public ContentValues getExtensionRegistrationConfiguration() {
		Log.d(SmartWatchService.LOG_TAG,
				"getExtensionRegistrationConfiguration");
		String iconHostapp = ExtensionUtils.getUriString(mContext,
				R.drawable.logo);
		String iconExtension = ExtensionUtils.getUriString(mContext,
				R.drawable.icon_smartwatch);

		ContentValues values = new ContentValues();

		values.put(Registration.ExtensionColumns.CONFIGURATION_ACTIVITY,
				MainActivity.class.getName());
		values.put(Registration.ExtensionColumns.CONFIGURATION_TEXT,
				mContext.getString(R.string.configuration_text));
		values.put(Registration.ExtensionColumns.NAME,
				mContext.getString(R.string.extension_name));
		values.put(Registration.ExtensionColumns.EXTENSION_KEY,
				getExtensionKey());
		values.put(Registration.ExtensionColumns.HOST_APP_ICON_URI, iconHostapp);
		values.put(Registration.ExtensionColumns.EXTENSION_ICON_URI,
				iconExtension);
		values.put(Registration.ExtensionColumns.EXTENSION_48PX_ICON_URI,
				iconExtension);
		values.put(Registration.ExtensionColumns.NOTIFICATION_API_VERSION,
				getRequiredNotificationApiVersion());
		values.put(Registration.ExtensionColumns.PACKAGE_NAME,
				mContext.getPackageName());

		return values;
	}

	@Override
	public boolean isDisplaySizeSupported(int width, int height) {
		Log.d(SmartWatchService.LOG_TAG,
				"isDisplaySizeSupported: "
						+ ((width == SmartWatchController
								.getSupportedControlWidth(mContext) && height == SmartWatchController
								.getSupportedControlHeight(mContext))));
		return ((width == SmartWatchController
				.getSupportedControlWidth(mContext) && height == SmartWatchController
				.getSupportedControlHeight(mContext)));
	}

	// A basic implementation of getExtensionKey Returns and saves a random
	// string based on UUID.randomUUID()
	@Override
	public synchronized String getExtensionKey() {
		if (TextUtils.isEmpty(extensionKey)) {
			// Retrieve key from preferences
			SharedPreferences pref = mContext.getSharedPreferences(
					EXTENSION_KEY_PREF, Context.MODE_PRIVATE);
			extensionKey = pref.getString(EXTENSION_KEY_PREF, null);
			if (TextUtils.isEmpty(extensionKey)) {
				// Generate a random key if not found
				extensionKey = UUID.randomUUID().toString();
				pref.edit().putString(EXTENSION_KEY_PREF, extensionKey)
						.commit();
			}
		}
		return extensionKey;
	}
	// ///////////////////////////////////////////////////////////////////////
	// Abstract methods from RegistrationInformation
	// ////////////////////////////////////////////////////////////////////////

}