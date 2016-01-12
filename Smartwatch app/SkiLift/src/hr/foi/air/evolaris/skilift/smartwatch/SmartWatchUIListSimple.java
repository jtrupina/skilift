package hr.foi.air.evolaris.skilift.smartwatch;

import hr.foi.air.evolaris.skilift.R;
import hr.foi.air.evolaris.skilift.data.Lift;
import hr.foi.air.evolaris.skilift.utils.Constants;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlListItem;

//Class that draw simple list (extends our abstract class ListControlManager)
public class SmartWatchUIListSimple extends ListControlManager{

	String className = "";

	public SmartWatchUIListSimple(Context context, String hostAppPackageName,
			Intent intent) {
		super(context, hostAppPackageName, intent);
		Log.d("cont", this.getClass().getSimpleName());
	}

	// Setter that store data into this user interface
	@Override
	public void setData(ArrayList<Lift> liftsData) {
		this.liftsData = liftsData;
	}

	// Abstract method that set layout for item and listView
	// On this layout is showed each lift item
	@Override
	public void setLayout() {
		this.layoutID = Constants.layoutIdOne;
		this.listViewID = Constants.listViewIdOne;
	}

	// Abstract Method that check limit for lift capacity and returns appropriate
	// colored layout for each item
	public int drawUserInterface(int capacity) {
		if (checkIndicatorColor(capacity, 0, 25))
			return R.layout.item_list_green;
		else if (checkIndicatorColor(capacity, 25, 75))
			return R.layout.item_list_yellow;
		else if (checkIndicatorColor(capacity, 75, 100))
			return R.layout.item_list_orange;
		else
			return R.layout.item_list_red;
	}

	// Abstract method that insert data into text view of item list
	public Bundle[] addDataToList(int position) {
		ControlListItem item = new ControlListItem();
		item.layoutData = new Bundle[2];

		Bundle bodyBundle = new Bundle();
		bodyBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.body);
		bodyBundle.putString(Control.Intents.EXTRA_TEXT, liftsData
				.get(position).getLiftName());
		item.layoutData[0] = bodyBundle;
		return item.layoutData;
	}
}
