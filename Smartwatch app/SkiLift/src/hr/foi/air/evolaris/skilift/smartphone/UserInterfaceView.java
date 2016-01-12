package hr.foi.air.evolaris.skilift.smartphone;

import hr.foi.air.evolaris.skilift.LiftDataManager;
import hr.foi.air.evolaris.skilift.R;
import hr.foi.air.evolaris.skilift.smartwatch.SmartWatchUIListDetailed;
import hr.foi.air.evolaris.skilift.smartwatch.SmartWatchUIListSimple;
import hr.foi.air.evolaris.skilift.smartwatch.SmartWatchUIPieChart;
import hr.foi.air.evolaris.skilift.smartwatch.SmartWatchUIRadar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

//Class that represents view for selecting smartwatch ui from smartphone (use our created abstract class SwipeTabManager) and
//Class that use list of intents for changing smartwatch ui from smartphone
public class UserInterfaceView extends SwipeTabManager {

	LiftDataManager liftDataManager = LiftDataManager.getInstance();
	UserInterfaceManager userInterfaceController = UserInterfaceManager
			.getInstance();

	// Sensor
	public final static String EXTRA_EVENT_TYPE = "EXTRA_EVENT_TYPE";

	public final static String EXTRA_EVENT_CONTENT = "EXTRA_EVENT_CONTENT";

	public final static String ACTION_UPDATE_ACTIVITY = "hr.foi.air.evolaris.skilift.smartphone.ACTION_UPDATE_ACTIVITY";

	private TextView smartWatchDesignName;

	Intent intent;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.ui_proba);
		smartWatchDesignName = (TextView) findViewById(R.id.textViewUI);
		setIntens();
		intent = getIntent();
		slideAccelerometerOffset = intent.getIntExtra(EXTRA_EVENT_TYPE, 0);
	}

	// Method for creating tab (called from our abstract class)
	@Override
	public void createTab() {
		String[] smartWatchDesignNames = returnStringArray();
		for (String designName : smartWatchDesignNames) {
			setTab(designName);
		}
	}

	// Method for creating swipe (called from our abstract class)
	@Override
	public void createSwipe() {
		addFragment(new FragmentA());
		addFragment(new FragmentB());
		addFragment(new FragmentC());
		addFragment(new FragmentD());
		viewPager = (ViewPager) findViewById(R.id.pager);
		setViewPager(viewPager);
	}

	// OnClick Event is called when user select smartwatch ui from smartwatch
	// selectedUIIndex is part of our SwipeTabManager abstract class
	public void changeUserInterface(View v) {
		userInterfaceController.changeUserInterface(userInterfaceController
				.getSelectedIntent(selectedUIIndex));
	}

	public void back(View v) {
		super.onBackPressed();
	}

	// Setter for storing intents (intent is used for starting
	// controls/userinterfaces on smartwatch)
	public void setIntens() {
		userInterfaceController.setItem(new Intent(this,
				SmartWatchUIListSimple.class));
		userInterfaceController.setItem(new Intent(this,
				SmartWatchUIListDetailed.class));
		userInterfaceController.setItem(new Intent(this,
				SmartWatchUIPieChart.class));
		userInterfaceController.setItem(new Intent(this,
				SmartWatchUIRadar.class));
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		super.onTabSelected(tab, ft);
		String[] smartWatchDesignNames = returnStringArray();
		smartWatchDesignName.setText(smartWatchDesignNames[tab.getPosition()]);
	}

	private String[] returnStringArray() {
		Resources res = getResources();
		return res.getStringArray(R.array.smart_watch_designs);
	}

}