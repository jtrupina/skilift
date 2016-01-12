package hr.foi.air.evolaris.skilift.smartwatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

//BroadcastReceiver that is called when extension is installed or opened on smartwatch
public class SmartWatchReceiver extends BroadcastReceiver {

	// Method which is called when extension on smartwatch is opened
	@Override
	public void onReceive(final Context context, final Intent intent) {
		intent.setClass(context, SmartWatchService.class);
		context.startService(intent);
	}
}