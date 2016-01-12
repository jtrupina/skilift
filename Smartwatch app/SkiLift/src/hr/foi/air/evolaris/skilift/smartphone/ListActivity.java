package hr.foi.air.evolaris.skilift.smartphone;

import hr.foi.air.evolaris.skilift.LiftDataManager;
import hr.foi.air.evolaris.skilift.R;
import hr.foi.air.evolaris.skilift.data.Lift;
import hr.foi.air.evolaris.skilift.utils.Constants;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ExpandableListView;

// Activity which contains list of lifts which is used for filter visibility on smartwatch
public class ListActivity extends Activity {

	public static ListAdapter ExpAdapter;
	private static ArrayList<Lift> ExpListItems;
	public static ExpandableListView ExpandList;
	LiftDataManager liftDataManager = LiftDataManager.getInstance();
	UserInterfaceManager userIterfaceController = UserInterfaceManager
			.getInstance();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expandable_main);
		ExpandList = (ExpandableListView) findViewById(R.id.ExpList);
		ExpListItems = liftDataManager.getLiftData();
		ExpAdapter = new ListAdapter(this, ExpListItems);
		ExpandList.setAdapter(ExpAdapter);
	}

	// OnClick method used for show sort dialog
	/*public void onSortButtonClick(View view) {
		AlertDialog diaBox = makeAndShowDialogBox(Constants.SORTING);
		diaBox.show();
	}*/


	/*public void openSettings(View v) {
		AlertDialog diaBox = makeAndShowDialogBox(Constants.SENSOR);
		diaBox.show();

	}*/

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
