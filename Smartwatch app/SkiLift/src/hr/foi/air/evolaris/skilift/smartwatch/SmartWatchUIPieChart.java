package hr.foi.air.evolaris.skilift.smartwatch;

import hr.foi.air.evolaris.skilift.R;
import hr.foi.air.evolaris.skilift.data.Lift;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

//Class that draw Detailed list (extends our abstract class ListControlManager)
public class SmartWatchUIPieChart extends ChartControlManager {

	float[] values;

	// Constructor
	public SmartWatchUIPieChart(Context context, String hostAppPackageName,
			Intent intent) {
		super(context, hostAppPackageName, intent);
	}

	// Setter that store data into this user interface
	@Override
	public void setData(ArrayList<Lift> liftsData) {
		this.liftsData = liftsData;
	}

	// Abstract method that is used to draw chart (important -
	// showBitmap(bitmap))
	@Override
	void drawChart() {
		Bitmap bitmap = getBitmap(); // moja metoda
		Canvas canvas = drawPie(bitmap);

		// Inflate an existing layout to use as a base.
		RelativeLayout root = new RelativeLayout(mContext);
		root.setLayoutParams(new LayoutParams(220, 176));

		RelativeLayout layout = (RelativeLayout) RelativeLayout.inflate(
				mContext, R.layout.bitmapp, root);
		// Set dimensions of the layout to use in the UI. We use the same
		// dimensions as the bitmap.
		layout.measure(bitmap.getWidth(), bitmap.getHeight());
		layout.layout(0, 0, layout.getMeasuredWidth(),
				layout.getMeasuredHeight());

		LinearLayout layoutUsed = (LinearLayout) layout.findViewById(R.id.moj);
		layoutUsed.draw(canvas);
		showBitmap(bitmap);
	}

	// Abstract method that is used to draw Piechart
	private Canvas drawPie(Bitmap bitmap) {
		values = scaleData();

		Paint paint = getPaint(Color.BLACK); // moja metoda
		Canvas canvas = getCanvas(bitmap); // moja metoda
		canvas.drawPaint(paint);

		float[] value_degree;
		int[] COLORS = { Color.GREEN, Color.YELLOW, Color.RED };
		RectF rectf = new RectF(15, 0, bitmap.getWidth() - 15,
				bitmap.getHeight());
		int temp = 0;

		value_degree = new float[values.length];
		for (int i = 0; i < values.length; i++) {
			value_degree[i] = values[i];
		}

		float centerX = (rectf.left + rectf.right) / 2;
		float centerY = (rectf.top + rectf.bottom) / 2;
		float radius = (rectf.right - rectf.left) / 2;

		radius *= 0.5;

		for (int i = 0; i < value_degree.length; i++) {// values2.length; i++) {
			int liftCapacity = liftsData.get(i).getLiftCapacity();
			if (i == 0) {
				// Set color
				if (checkIndicatorColor(liftCapacity, 0, 25))
					paint.setColor(COLORS[0]);
				if (checkIndicatorColor(liftCapacity, 25, 75))
					paint.setColor(COLORS[1]);
				if (checkIndicatorColor(liftCapacity, 75, 100))
					paint.setColor(COLORS[2]);
				canvas.drawArc(rectf, 0, value_degree[i], true, paint);
			} else {
				temp += (int) value_degree[i - 1];
				// Set color
				if (checkIndicatorColor(liftCapacity, 0, 25))
					paint.setColor(COLORS[0]);
				if (checkIndicatorColor(liftCapacity, 25, 75))
					paint.setColor(COLORS[1]);
				if (checkIndicatorColor(liftCapacity, 75, 100))
					paint.setColor(COLORS[2]);
				canvas.drawArc(rectf, temp, value_degree[i], true, paint);
			}

			if (liftsData.get(i).getLiftCapacity() != 200) {
				paint.setColor(Color.BLACK); // set this to the text color.
				paint.setTextSize(16);
				paint.setTextAlign(Align.CENTER);
				float medianAngle = (temp + (value_degree[i] / 2f))
						* (float) Math.PI / 180f; // this angle will place the
													// text
													// in the center of the arc.
				canvas.drawText(liftsData.get(i).getLiftName().substring(5),
						(float) (centerX + (radius * Math.cos(medianAngle))),
						(float) (centerY + (radius * Math.sin(medianAngle))),
						paint);
			}
		}
		return canvas;
	}

	// Method that is used to scale lift data to fit on PieChart
	private float[] scaleData() {
		// TODO Auto-generated method stub
		float val[] = new float[liftsData.size()];
		float total = 0;
		for (int i = 0; i < liftsData.size(); i++) {
			if (liftsData.get(i).getLiftCapacity() != 200)
				total += liftsData.get(i).getLiftCapacity();

		}
		for (int i = 0; i < liftsData.size(); i++) {
			if (liftsData.get(i).getLiftCapacity() != 200)
				val[i] = 360 * ((liftsData.get(i).getLiftCapacity() + 20) / total);
			else
				val[i] = 0;
		}
		return val;
	}
}
