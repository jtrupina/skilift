package hr.foi.air.evolaris.skilift.smartphone;

import hr.foi.air.evolaris.skilift.R;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MainFragment extends Fragment  {

	public static Fragment newInstance(Context context) {
		MainFragment f = new MainFragment();

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_main2,
				null);
		return root;
	}
}
