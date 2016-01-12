package hr.foi.air.evolaris.skilift.smartwatch;

import hr.foi.air.evolaris.skilift.R;
import hr.foi.air.evolaris.skilift.data.Lift;
import hr.foi.air.evolaris.skilift.utils.Constants;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.control.ControlListItem;

//Class that draw Detailed list (extends our abstract class ListControlManager)
public class SmartWatchUIListDetailed extends ListControlManager{

	// Constructor
	public SmartWatchUIListDetailed(Context context, String hostAppPackageName,
			Intent intent) {
		super(context, hostAppPackageName, intent);
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

	// Abstract Method that check limit for lift capacity and returns
	// appropriate
	// colored layout for each item
	public int drawUserInterface(int liftCapaity) {
		if (checkIndicatorColor(liftCapaity, 0, 25))
			return R.layout.ui2_item_green;
		else if (checkIndicatorColor(liftCapaity, 25, 75))
			return R.layout.ui2_item_yellow;
		else if (checkIndicatorColor(liftCapaity, 75, 100))
			return R.layout.ui2_item_orange;
		else
			return R.layout.ui2_item_red;
	}

	// Abstract method that insert data into (for example) text view of item
	// list
	public Bundle[] addDataToList(int position) {
		ControlListItem item = new ControlListItem();
		Bundle bodyBundle = new Bundle();
		item.layoutData = new Bundle[2];
		bodyBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.body);
		bodyBundle.putString(Control.Intents.EXTRA_TEXT, liftsData
				.get(position).getLiftName().substring(5));
		item.layoutData[0] = bodyBundle;

		Bundle availabilityBundle = new Bundle();
		availabilityBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE,
				R.id.tvavailability);
		availabilityBundle.putString(Control.Intents.EXTRA_TEXT, "Capacity: "
				+ liftsData.get(position).getLiftCapacity() + "%"); // capacity

		item.layoutData[1] = availabilityBundle;
		return item.layoutData;
	}
}
