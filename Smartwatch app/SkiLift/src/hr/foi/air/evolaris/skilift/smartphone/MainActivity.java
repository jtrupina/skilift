package hr.foi.air.evolaris.skilift.smartphone;

import hr.foi.air.evolaris.skilift.LiftDataManager;
import hr.foi.air.evolaris.skilift.R;
import hr.foi.air.evolaris.skilift.gcm.GcmBroadcastReceiver;
import hr.foi.air.evolaris.skilift.gcm.RegisterAppGcm;
import hr.foi.air.evolaris.skilift.utils.Constants;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

//MainActivty class
public class MainActivity extends Activity implements OnItemClickListener {

	LiftDataManager liftDataManager = LiftDataManager.getInstance();

	private String txtSortDialogNegBtn, txtSortDialogPosBtn;
	private String txtSortDialogTitle, txtSortDialogDesc;

	private String txtSensorDialogNegBtn, txtSensorDialogPosBtn;
	private String txtSensorDialogTitle, txtSensorDialogDesc;

	private String txtInfoTitle, txtInfoDesc, txtInfoBtn;
	UserInterfaceManager userIterfaceController = UserInterfaceManager
			.getInstance();

	private ListView listView;
	private DrawerLayout drawerLayout;
	@SuppressWarnings("deprecation")
	private ActionBarDrawerToggle drawerListener;

	final String[] fragments = { "hr.foi.air.evolaris.skilift.smartphone.MainFragment" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		setDialogText();
		setNavigationDrawer();
		registerToGCM();
	}

	private void setNavigationDrawer() {

		listView = (ListView) findViewById(R.id.drawerList);
		View localView = getLayoutInflater().inflate(R.layout.left_nav_header,
				null);

		listView.addHeaderView(localView);

		listView.setAdapter(new LeftNavAdapter(this, returnStringArray()));
		listView.setOnItemClickListener(this);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
		drawerListener = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_drawer, R.string.dialogPosBtnSensor,
				R.string.dialogNegBtnSensor) {
			@Override
			public void onDrawerOpened(View drawerView) {
				Toast.makeText(MainActivity.this, "Drawer opened",
						Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				Toast.makeText(MainActivity.this, "Drawer closed",
						Toast.LENGTH_SHORT).show();
			}
		};

		drawerLayout.setDrawerListener(drawerListener);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		FragmentTransaction tx = getFragmentManager().beginTransaction();
		tx.replace(R.id.mainContent,
				Fragment.instantiate(MainActivity.this, fragments[0]));
		tx.commit();

	}

	// Register to GCM
	public void registerToGCM() {
		new RegisterAppGcm(getApplicationContext());
	}

	// OnClick Event for open UserInterfaceView
	public void userInterface(View v) {
		Intent intent = new Intent(this, UserInterfaceView.class);
		startActivity(intent);
	}

	// OnClick Event for open ListActivity
	public void filterData(View v) {
		Intent intent = new Intent(this, ListActivity.class);
		startActivity(intent);
	}

	public void sort(View v) {
		AlertDialog diaBox = makeAndShowDialogBox(Constants.SORTING);
		diaBox.show();
	}
	
	public void sensor(View v){
		AlertDialog diaBox = makeAndShowDialogBox(Constants.SENSOR);
		diaBox.show();
	}
	
	public void exit(View v){
		finish();
        System.exit(0);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver(new GcmBroadcastReceiver(), new IntentFilter(
				"hr.foi.air.evolaris.skilift.gcm.USER_ACTION"));
	}

	private void setDialogText() {
		txtSortDialogNegBtn = getString(R.string.dialogNegBtnSort);
		txtSortDialogPosBtn = getString(R.string.dialogPosBtnSort);
		txtSortDialogTitle = getString(R.string.dialogTitleSort);
		txtSortDialogDesc = getString(R.string.dialogTextSort);

		txtSensorDialogNegBtn = getString(R.string.dialogNegBtnSensor);
		txtSensorDialogPosBtn = getString(R.string.dialogPosBtnSensor);
		txtSensorDialogTitle = getString(R.string.dialogTitleSensor);
		txtSensorDialogDesc = getString(R.string.dialogTextSensor);

		txtInfoTitle = getString(R.string.dialogInfoTitle);
		txtInfoDesc = getString(R.string.dialogTxtInfo);
		txtInfoBtn = getString(R.string.dialogBtnInfo);
	}

	private String[] returnStringArray() {
		Resources res = getResources();
		return res.getStringArray(R.array.smart_watch_designs);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (drawerListener.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		drawerListener.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		drawerListener.syncState();
	}

	// Method which returns dialog for sorting lift data on smartwatch
	private AlertDialog makeAndShowDialogBox(final String action) {

		// Ternary operators
		String title = (action.equals(Constants.SORTING)) ? getString(R.string.dialogTitleSort)
				: getString(R.string.dialogTitleSensor);
		String message = (action.equals(Constants.SORTING)) ? getString(R.string.dialogTextSort)
				: getString(R.string.dialogTextSensor);
		String btnPositiveText = (action.equals(Constants.SORTING)) ? getString(R.string.dialogPosBtnSort)
				: getString(R.string.dialogPosBtnSensor);
		String btnnegativeText = (action.equals(Constants.SORTING)) ? getString(R.string.dialogNegBtnSort)
				: getString(R.string.dialogNegBtnSensor);

		AlertDialog sortDialog =

		new AlertDialog.Builder(this)
				.setTitle(title)
				.setMessage(message)
				// .setIcon(R.drawable.logo_item)

				.setPositiveButton(btnPositiveText,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if ((action.equals(Constants.SORTING)))
									liftDataManager.sortAscending();
								else {
									save(Constants.SENSOR, true);
									refreshSmartWatch();
								}
							}
						})

				.setNegativeButton(btnnegativeText,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								if ((action.equals(Constants.SORTING)))
									liftDataManager.sortDescending();
								else {
									save(Constants.SENSOR, false);
									refreshSmartWatch();
								}
							}
						})

				.create();

		return sortDialog;
	}

	// Method for save data abou sensor (on/off)
	public void save(String key, boolean value) {
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"MyData", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	private void refreshSmartWatch() {
		// Intents is used for changing controls/user interfaces on smartwatch
		Intent initialIntent = userIterfaceController.getCurrentIntent();
		userIterfaceController.changeUserInterface(initialIntent);
	}

}
