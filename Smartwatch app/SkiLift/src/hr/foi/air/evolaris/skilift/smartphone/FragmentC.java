package hr.foi.air.evolaris.skilift.smartphone;

import hr.foi.air.evolaris.skilift.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//A Fragment which represents a portion of user interface in an FragmentActivity
public class FragmentC extends Fragment {

	//Constructor
	public FragmentC() {}
	
	// Inflate the layout for this fragment
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.user_interface_manager_three, container, false);
	}

}