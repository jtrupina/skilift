package hr.foi.air.evolaris.skilift.smartphone;

import hr.foi.air.evolaris.skilift.smartwatch.SmartWatchController;

import java.util.ArrayList;

import android.content.Intent;

// UserInterface Manager singleton class which manage smartwatch intents/controls inside list and getters
// Singleton class which holds instance of ControlManagerSmartwatch class (that holds controls for change smartwatch ui) 
public class UserInterfaceManager {

	private SmartWatchController smartwatchControlManager;
	public static UserInterfaceManager instance;
	private Intent currentIntent;
	public ArrayList<Intent> liftsIntent;

	// Constructor
	private UserInterfaceManager() {
		liftsIntent = new ArrayList<Intent>();
	}

	// Singleton pattern
	public static UserInterfaceManager getInstance() {
		if (instance == null)
			instance = new UserInterfaceManager();
		return instance;
	}

	// Setter that add intent item inside list (intent is used for change ui on
	// smartwatch)
	public void setItem(Intent newItem) {
		liftsIntent.add(newItem);
	}

	// Getter for selected intent inide list (intent is used for change ui on
	// smartwatch)
	public Intent getSelectedIntent(int selectedIndex) {
		return liftsIntent.get(selectedIndex);
	}

	// Getter for ControlManagerSmartwatch class
	public SmartWatchController getSmartwatchControlManager() {
		return smartwatchControlManager;
	}

	// Setter for ControlManagerSmartwatch class
	public void setSmartwatchControlManager(
			SmartWatchController smartwatchControlManager) {
		this.smartwatchControlManager = smartwatchControlManager;
	}

	// Method that is used to call singleton SmartWatchControlManager class
	// which change smartwatch ui using intents
	public void changeUserInterface(Intent intent) {
		
		smartwatchControlManager.startControl(intent);
		this.setCurrentIntent(intent);
	}

	// Getter for current intent that is ued for showing ui on smartwatch
	public Intent getCurrentIntent() {
		return currentIntent;
	}

	// Setter for current intent that is ued for showing ui on smartwatch
	public void setCurrentIntent(Intent currentIntent) {
		this.currentIntent = currentIntent;
	}

}
