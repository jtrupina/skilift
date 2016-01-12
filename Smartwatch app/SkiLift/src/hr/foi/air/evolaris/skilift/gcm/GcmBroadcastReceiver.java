package hr.foi.air.evolaris.skilift.gcm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

//BroadcastReceiver for GCM push messages
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

	// Method which is called when message (intent) is pushed by GCM
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("asd", "asd");
		ComponentName comp = new ComponentName(context.getPackageName(),
				GcmIntentService.class.getName());
		startWakefulService(context, (intent.setComponent(comp)));
		setResultCode(Activity.RESULT_OK);
	}
}