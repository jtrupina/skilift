package hr.foi.air.evolaris.skilift.smartwatch;

import hr.foi.air.evolaris.skilift.LiftDataManager;
import hr.foi.air.evolaris.skilift.smartphone.UserInterfaceManager;
import android.content.Intent;

import com.sonyericsson.extras.liveware.extension.util.ExtensionService;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.registration.DeviceInfoHelper;
import com.sonyericsson.extras.liveware.extension.util.registration.RegistrationInformation;

//Service that is started by SmartWatchReceiver
public class SmartWatchService extends ExtensionService {

	public static final String LOG_TAG = "AdvancedLayoutsExtension";
	LiftDataManager liftDataManager = LiftDataManager.getInstance();

	// Constructor
	public SmartWatchService() {
		super();
	}

	@Override
	protected RegistrationInformation getRegistrationInformation() {
		return new SmartWatchRegistrationInformation(this);
	}

	@Override
	protected boolean keepRunningWhenConnected() {
		return false;
	}

	// Method which is called when service is started and
	// Method that instantiate SmartWatchControlManager class which is used for
	// creating controls/user interfaces on ssmartwatch (using intents)
	@Override
	public ControlExtension createControlExtension(String hostAppPackageName) {
		boolean advancedFeaturesSupported = DeviceInfoHelper
				.isSmartWatch2ApiAndScreenDetected(this, hostAppPackageName);
		if (advancedFeaturesSupported) {
			UserInterfaceManager uiController = UserInterfaceManager
					.getInstance();

			Intent initialListControlIntent = uiController.getCurrentIntent();

			SmartWatchController sm;
			if (initialListControlIntent == null) {
				initialListControlIntent = new Intent(this,
						SmartWatchUIListSimple.class);
				uiController.setCurrentIntent(initialListControlIntent);
				sm = new SmartWatchController(this, hostAppPackageName,
						initialListControlIntent);
			} else {
				sm = new SmartWatchController(this, hostAppPackageName,
						initialListControlIntent);
			}

			uiController.setSmartwatchControlManager(sm);
			return sm;
		} else {
			throw new IllegalArgumentException("No control for: "
					+ hostAppPackageName);
		}
	}

}
