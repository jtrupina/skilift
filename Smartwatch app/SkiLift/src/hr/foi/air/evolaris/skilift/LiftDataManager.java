package hr.foi.air.evolaris.skilift;

import hr.foi.air.evolaris.skilift.data.Lift;
import hr.foi.air.evolaris.skilift.smartphone.UserInterfaceManager;
import hr.foi.air.evolaris.skilift.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;

//Class for managing lift data (name, capacity and visibility) and
//Class for parse, sort and filter data
public class LiftDataManager {
	
	JSONObject json;
	public static LiftDataManager instance;
	private ArrayList<Lift> liftsData;
	public boolean sortAscending = false;
	private boolean sortDescending = false;
	UserInterfaceManager userInterfaceController = UserInterfaceManager
			.getInstance();

	// Constructor
	private LiftDataManager() {
		liftsData = new ArrayList<>();
		// setDefaultVlues();
	}

	// Singleton pattern
	public static LiftDataManager getInstance() {
		if (instance == null)
			instance = new LiftDataManager();
		return instance;
	}

	// Getter for lift data
	public ArrayList<Lift> getLiftData() {
		return liftsData;
	}
	
	public ArrayList<Lift> getSortedLiftData() {
		ArrayList<Lift> liftsDataSorted = new ArrayList<>();
		liftsDataSorted = this.liftsData;

		Collections.sort(liftsDataSorted, LiftNameComparator);
		
		return liftsDataSorted;
	}
	
	

	// Getter for filtered (visible) lift data
	public ArrayList<Lift> getLiftDataWatch() {
		ArrayList<Lift> liftDataWatch = new ArrayList<Lift>();
		for (Lift lift : liftsData) {
			if (lift.isVisibility()) {
				liftDataWatch.add(lift);
			}
		}
		return liftDataWatch;
	}

	// Setter for lift data
	private void setLiftData(ArrayList<Lift> liftsData) {
		this.liftsData = liftsData;
	}

	// Method for sort lift data ascending
	public void sortAscending() {
		sortAscending = true;
		sortDescending = false;
		Collections.sort(liftsData, LiftCapacityComparator);
		Intent initialIntent = userInterfaceController.getCurrentIntent();
		userInterfaceController.changeUserInterface(initialIntent);
	}

	// Method for sort lift data descending
	public void sortDescending() {
		sortAscending = false;
		sortDescending = true;
		Collections.sort(liftsData, LiftCapacityComparator);
		Intent initialIntent = userInterfaceController.getCurrentIntent();
		userInterfaceController.changeUserInterface(initialIntent);
	}

	// Method for parsing lift data from bundle to ArrayList
	public void parseFromBundleToArray(Bundle liftsBundle) {
		if (liftsData.isEmpty()) {
			
			ArrayList<Lift> liftsData = new ArrayList<>();
			for (String key : liftsBundle.keySet()) {
				Object value = liftsBundle.get(key);
				if (key.startsWith(Constants.LIFT_TAG)) {
					Lift liftData = new Lift();
					String string_to_cut = value.toString();
					String[] array_container = string_to_cut.split(";");
					liftData.setLiftName(array_container[0]);
					liftData.setLiftCapacity(Integer.parseInt(array_container[1]));
					liftData.setVisibility(true);
					liftData.setLongitude(Double.parseDouble(array_container[2]));
					liftData.setLatitude(Double.parseDouble(array_container[3]));
					liftsData.add(liftData);
				}
			}
			this.setLiftData(liftsData);

			
		} else {
			for (Lift lift : liftsData) {
				for (String key : liftsBundle.keySet()) {
					Object value = liftsBundle.get(key);
					if (key.startsWith(Constants.LIFT_TAG)) {
						String string_to_cut = value.toString();
						String[] array_container = string_to_cut.split(";");

						if (lift.getLiftName().equals(array_container[0])) {

							lift.setLiftCapacity(Integer
									.parseInt(array_container[1]));
						}
					}

				}
			}
		}
		if (sortAscending)
			Collections.sort(liftsData, LiftCapacityComparator);
		else if (sortDescending)
			Collections.sort(liftsData, LiftCapacityComparator);
	}

	// Comparator used to sort lift data by capacity
	public static Comparator<Lift> LiftCapacityComparator = new Comparator<Lift>() {

		public int compare(Lift liftData1, Lift liftData2) {

			Integer lift1 = liftData1.getLiftCapacity();
			Integer lift2 = liftData2.getLiftCapacity();
			;

			// ascending or descending order
			if (LiftDataManager.getInstance().sortAscending)
				return lift1.compareTo(lift2);
			else
				return lift2.compareTo(lift1);
		}

	};
	
	// Comparator used to sort lift data by capacity
		public static Comparator<Lift> LiftNameComparator = new Comparator<Lift>() {

			public int compare(Lift liftData1, Lift liftData2) {

				Integer lift1 = Integer.parseInt(liftData1.getLiftName().substring(5));
				Integer lift2 = Integer.parseInt(liftData2.getLiftName().substring(5));
				

					return lift1.compareTo(lift2);
			}

		};
	
}
