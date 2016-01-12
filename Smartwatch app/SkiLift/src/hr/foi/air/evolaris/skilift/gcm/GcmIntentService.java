package hr.foi.air.evolaris.skilift.gcm;

import hr.foi.air.evolaris.skilift.LiftDataManager;
import hr.foi.air.evolaris.skilift.smartphone.UserInterfaceManager;
import android.app.IntentService;
import android.content.Intent;

//Service which is started by GcmBroadcastReceiver (broadcast pushed message/intent)
public class GcmIntentService extends IntentService {

	LiftDataManager liftDataManager = LiftDataManager.getInstance();
	UserInterfaceManager userIterfaceController = UserInterfaceManager
			.getInstance();

	// Constructor
	public GcmIntentService() {
		super("GcmIntentService");
	}

	// Method which is called when service is started by broadcast receiver
	@Override
	protected void onHandleIntent(Intent intent) {

		liftDataManager.parseFromBundleToArray(intent.getExtras());

		// Intents is used for changing controls/user interfaces on smartwatch
		Intent initialIntent = userIterfaceController.getCurrentIntent();
		userIterfaceController.changeUserInterface(initialIntent);
	}
}