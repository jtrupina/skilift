package hr.foi.air.evolaris.skilift.smartphone;

import hr.foi.air.evolaris.skilift.R;
import hr.foi.air.evolaris.skilift.data.Lift;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

//ListAdapter used for show list on ListActivity
public class ListAdapter extends BaseExpandableListAdapter {

	private LayoutInflater inflater;
	private Context context;
	private ArrayList<Lift> groups;
	UserInterfaceManager userIterfaceController = UserInterfaceManager
			.getInstance();

	// Constructor
	public ListAdapter(Context context, ArrayList<Lift> groups) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.groups = groups;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parentView) {
		final Lift group = groups.get(groupPosition);

		convertView = inflater.inflate(R.layout.grouprow, parentView, false);

		((TextView) convertView.findViewById(R.id.tvGroup)).setText(group
				.getLiftName());
		
		((TextView) convertView.findViewById(R.id.tvCircle)).setText(group
				.getLiftName().substring(5));

		CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
		checkbox.setChecked(true);

		checkbox.setOnCheckedChangeListener(new CheckUpdateListener(group));

		return convertView;
	}

	// Check if check box is checked for given lift
	public boolean load(String key) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"MyData", Context.MODE_PRIVATE);
		boolean showLift = sharedPreferences.getBoolean(key, false);
		return showLift;
	}

	// This method used to inflate child rows view
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parentView) {
		Lift child = (Lift) getChild(groupPosition, childPosition);
		if (view == null) {
			LayoutInflater infalInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = infalInflater.inflate(R.layout.childrow, null);
		}
		TextView tv = (TextView) view.findViewById(R.id.tvChild);
		tv.setText(child.getLiftName());
		tv.setTag("1");
		// TODO Auto-generated method stub
		return view;
	}

	// Call when child row clicked
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	// Call when parent row clicked
	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public void notifyDataSetChanged() {
		// Refresh List rows
		super.notifyDataSetChanged();
	}

	@Override
	public boolean isEmpty() {
		return ((groups == null) || groups.isEmpty());
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	// Checkbox Checked Change Listener

	private final class CheckUpdateListener implements OnCheckedChangeListener {
		private final Lift parent;

		private CheckUpdateListener(Lift parent) {
			this.parent = parent;
		}

		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			parent.setVisibility(isChecked);
			save(parent.getLiftName(), isChecked);
			updateSmartWatch();
		}

		// Method for save data if checkbox is checked for given lift
		public void save(String key, boolean value) {
			SharedPreferences sharedPreferences = context.getSharedPreferences(
					"MyData", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean(key, value);
			editor.commit();
		}

		// Method which is used for call singleton that create changed(filtered)
		// control/user interface on smartwatch
		private void updateSmartWatch() {
			Intent initialIntent = userIterfaceController.getCurrentIntent();
			userIterfaceController.changeUserInterface(initialIntent);
		}
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return 0;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}
}
