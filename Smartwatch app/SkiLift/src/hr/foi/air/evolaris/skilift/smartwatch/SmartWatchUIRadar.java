package hr.foi.air.evolaris.skilift.smartwatch;

import hr.foi.air.evolaris.skilift.R;
import hr.foi.air.evolaris.skilift.data.Lift;
import hr.foi.air.evolaris.skilift.smartphone.GPSTracker;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

//Class that draw Detailed list (extends our abstract class ListControlManager)
public class SmartWatchUIRadar extends ChartControlManager {

	GPSTracker gpstraker = new GPSTracker(mContext);
	double [] distance = new double [10];
	static final double _eQuatorialEarthRadius = 6378.1370D;
    static final double _d2r = (Math.PI / 180D);
	// Constructor
	public SmartWatchUIRadar(Context context, String hostAppPackageName,
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
		

		Paint paint = getPaint(Color.BLACK); // moja metoda
		Canvas canvas = getCanvas(bitmap); // moja metoda
		canvas.drawPaint(paint);
		
		

		int[] COLORS = { Color.GREEN, Color.YELLOW, Color.RED };
		
		double myX = 40.00d;
		double myY = 41.20d;
		
		myX = gpstraker.getLongitude();
		myY = gpstraker.getLatitude();
		
		//begining screen "my position" - which is always center and design around it
		
		//paint.setColor(Color.rgb(105,74,2));
		paint.setColor(Color.MAGENTA);
		canvas.drawCircle(bitmap.getWidth() / 2 , bitmap.getHeight() / 2, 10, paint);
		
		paint.setColor(Color.LTGRAY);

		
		for(float i = 0.1f; i <= 1.0f;i+=0.1){
			
			canvas.drawLine((float)bitmap.getWidth() * i, 0f, (float)bitmap.getWidth() * i, (float)bitmap.getHeight(), paint);
			canvas.drawLine(0f, (float)bitmap.getHeight() * i, (float)bitmap.getWidth(), (float)bitmap.getHeight() * i, paint);
			if(i == 0.5f){
				
				canvas.drawLine(((float)bitmap.getWidth()/2) + 1, 0f, ((float)bitmap.getWidth()/2) + 1, (float)bitmap.getHeight(), paint);
				canvas.drawLine(((float)bitmap.getWidth()/2) - 1, 0f, ((float)bitmap.getWidth()/2) - 1, (float)bitmap.getHeight(), paint);
				
				canvas.drawLine(0f, ((float)bitmap.getHeight()/2) - 1, (float)bitmap.getWidth(), ((float)bitmap.getHeight()/2) - 1, paint);
				canvas.drawLine(0f, ((float)bitmap.getHeight()/2) + 1, (float)bitmap.getWidth(), ((float)bitmap.getHeight()/2) + 1, paint);
			}
		}
		
	
		 
		//Variable PERCENTAGE_TO_DRAW is the percentage we are drawing off all avaible width and height of the smartwatch
		float Percentage_to_draw = 0.8f;
		//radius 6
		float Max_pixel_to_width = (bitmap.getWidth()/2) * Percentage_to_draw;
		float Max_pixel_to_height = (bitmap.getHeight()/2) * Percentage_to_draw;
		
		//while we are sorting the array we also "save" maximum distance to display
		double Max_distance = 1.0d; //test_data0 3
		
		int j = 0;
		for (Lift lift : liftsData) {
			distance[j] = HaversineInKM(lift.getLatitude(), lift.getLongitude(), myY, myX);
			if(distance[j] >= Max_distance){
				Max_distance = distance[j];
			}
			j += 1;
		}
		
		int square_unit = (int) Max_distance / 4;
		
		paint.setColor(Color.MAGENTA);
		paint.setTextSize(15);
		canvas.drawText("N", bitmap.getWidth()/2 - 3, 12, paint);
		canvas.drawText("S", bitmap.getWidth()/2 - 4, bitmap.getHeight()-5, paint);
		canvas.drawText("E", bitmap.getWidth()-10, bitmap.getHeight()/2 + 3, paint);
		canvas.drawText("W", 4, bitmap.getHeight()/2 + 4, paint);
		
		canvas.drawText("Measure = ", 18, bitmap.getHeight() - 5, paint);
		canvas.drawText((int)square_unit + " km", bitmap.getWidth()/2 + 30, bitmap.getHeight() - 5, paint);
		
		for(int i = 0; i < liftsData.size() ; i++){
			
			double delta_distance = 0; //relative distanec in percentages from max. distance we are drawing and the current point :: current/max result is always < 1)
			delta_distance =  (distance[i] / Max_distance);
			
			
			
			int add_to_x_coordinate = (int) (Max_pixel_to_width * delta_distance);
			int add_to_y_coordinate = (int) (Max_pixel_to_height * delta_distance);
			
			//now we know how much we are moving but still don't know where to move, that's why we have to compare X and Y of the new spot, from our spot (which we get from GPS)
			//before that we are checking capacity for the right color 0-25 gree, 25-75 yellow, 75-100 orange
			if(liftsData.get(i).getLiftCapacity() < 25){
				paint.setColor(COLORS[0]);
			}
			if(liftsData.get(i).getLiftCapacity() < 75 && liftsData.get(i).getLiftCapacity() >= 25){
				
				paint.setColor(Color.rgb(255,202,0));
			}
			if(liftsData.get(i).getLiftCapacity() >= 75 && liftsData.get(i).getLiftCapacity()<=100){
				paint.setColor(COLORS[2]);
			}
			
			//now we are checking where is the X spot to draw, to do that we are checking if we are moving left or right from the center (our position), 
			//setting default values to the center, beacuse if the X is the same as our position that means we have to move along Y coordinate => lift is straight in front of us,
			//so on the radar we are moving only "up" while the x is untouched
			int point_X = bitmap.getWidth()/2;
			int point_Y = bitmap.getHeight()/2;
			
			if(liftsData.get(i).getLongitude() > myX){
				point_X +=  add_to_x_coordinate;
			}
			if(liftsData.get(i).getLongitude() < myX){
				point_X -= add_to_x_coordinate;
			}
			
			//checking the same for the Y coordinate
			
			if(liftsData.get(i).getLatitude() > myY){
				point_Y += add_to_y_coordinate;
			}
			if(liftsData.get(i).getLatitude()  < myY){
				point_Y -= add_to_y_coordinate;
			}
			
			canvas.drawCircle(point_X , point_Y, 6, paint);
			
		}
		
		return canvas;
	}

	public static double HaversineInKM(double lat1, double long1, double lat2, double long2) {
        double dlong = (long2 - long1) * _d2r;
        double dlat = (lat2 - lat1) * _d2r;
        double a = Math.pow(Math.sin(dlat / 2D), 2D) + Math.cos(lat1 * _d2r) * Math.cos(lat2 * _d2r)
                * Math.pow(Math.sin(dlong / 2D), 2D);
        double c = 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
        double d = _eQuatorialEarthRadius * c;

        return d;
    }
	
}
