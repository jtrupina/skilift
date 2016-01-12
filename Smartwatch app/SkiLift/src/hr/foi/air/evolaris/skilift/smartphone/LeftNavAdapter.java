package hr.foi.air.evolaris.skilift.smartphone;

import hr.foi.air.evolaris.skilift.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeftNavAdapter extends BaseAdapter {
	private Context context;
	private String[] items;

	public LeftNavAdapter(Context paramContext, String[] paramArrayList) {
		this.context = paramContext;
		this.items = paramArrayList;
	}

	public int getCount() {
		return this.items.length;
	}

	public String getItem(int paramInt) {
		return (String) this.items[paramInt];
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		if (paramView == null) {
			paramView = LayoutInflater.from(this.context).inflate(
					R.layout.left_nav_item, null);
		}
		((TextView) paramView.findViewById(R.id.lbl))
				.setText(getItem(paramInt));
		((ImageView) paramView.findViewById(R.id.img))
				.setImageResource(R.drawable.ic_chat);
		return paramView;
	}
}