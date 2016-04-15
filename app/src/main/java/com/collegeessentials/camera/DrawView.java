package com.collegeessentials.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.util.Log;
import android.view.SurfaceView;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.collegeessentials.location.PhoneLocation;
import com.collegeessentials.main.R;

/**
 * This class will act as a canvas on top of the camera
 * It uses SurfaceView to sit on top and all animations are done in here.
 *
 * To identify where it's target point it must do two things
 * 1. Get the bearing/angle between the current point and point of interest
 * 2. Check the device orientation and combine them
 *
 * @version 1.0
 * @see SurfaceView
 * @see CameraActivity
 * @see CameraPreview
 */
public class DrawView extends SurfaceView implements SensorEventListener{

    private Paint textPaint = new Paint();//used to paint on screen
    private PhoneLocation loc;
    private Location to, from;
    private double degrees;
    private Bitmap arrow;

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 600;

    public DrawView(Context context){
        super(context);

        /* This call is necessary, or else the
         * draw method will not be called.
         */
        setWillNotDraw(false);

        loc = new PhoneLocation(context);

        senSensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                lastUpdate = curTime;

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private double getDegrees(double lat1, double long1, double lat2, double long2){

//        double dLon = Math.toRadians(long2 - long1);
//
            Log.i("Locs", lat1 + " " + lat2 + long1 + " " + long2);
//
//        lat1 = Math.toRadians(lat1);
//        lat2 = Math.toRadians(lat2);
//
//        double y = Math.sin(dLon) * Math.cos(lat2);
//        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
//
//        double brng = Math.toDegrees(Math.atan2(y, x));
//
//        brng = (brng + 360) % 360;
//        brng = 360 - brng;

        double brng = from.bearingTo(to);

        return brng;
    }

    //TODO: Fine tune, rotate dynamically
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        arrow = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);

        loc.getPreviousLocations();//stores last location

        to = loc.getToLocation();
        from = loc.getFromLocation();

        to.setLatitude(53.283728);
        to.setLongitude(-9.038544);

        degrees = getDegrees(from.getLatitude(), from.getLongitude(), to.getLatitude(), to.getLongitude());

        Matrix matrix = new Matrix();

        matrix.postRotate((float) degrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(arrow , 0, 0, arrow.getWidth(), arrow.getHeight(), matrix, true);

        canvas.drawBitmap(rotatedBitmap, 1200, 500, textPaint);

//        canvas.rotate((float) degrees, canvas.getWidth() / 2, canvas.getHeight() / 2);
//
//        //gmit canteen 53.2791608,-9.0105963
//        textPaint.setARGB(255, 200, 0, 0);//Colour red - ish
//        textPaint.setStyle(Paint.Style.FILL);
//
//        /* Top arrow*/
//        textPaint.setTextSize(70);
//        canvas.drawText(degrees + "", 1100, 500, textPaint);
//
//        textPaint.setStrokeWidth(20);
//        canvas.drawLine(1250, 100, 1250, 400, textPaint);
//
//        textPaint.setStyle(Paint.Style.STROKE);
//        textPaint.setStrokeWidth(2);
//        textPaint.setColor(Color.RED);
//
//        Path path = new Path();
//        path.moveTo(0, -100);
//        path.lineTo(50, 0);
//        path.lineTo(-50, 0);
//        path.close();
//
//        path.offset(1250, 100);
//        canvas.drawPath(path, textPaint);

    }
}
