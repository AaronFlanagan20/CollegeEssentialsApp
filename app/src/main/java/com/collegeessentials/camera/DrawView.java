package com.collegeessentials.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.location.Location;
import android.util.Log;
import android.view.SurfaceView;

import com.collegeessentials.location.PhoneLocation;

/**
 * This class will act as a canvas on top of the camera
 * It uses SurfaceView to sit on top and all animations are done in here.
 *
 * @version 1.0
 * @see SurfaceView
 * @see CameraActivity
 * @see CameraPreview
 */
public class DrawView extends SurfaceView{

    private Paint textPaint = new Paint();//used to paint on screen
    private PhoneLocation loc;
    private Location to, from;
    private double head, degrees;

    public DrawView(Context context){
        super(context);

        /* This call is necessary, or else the
         * draw method will not be called.
         */
        setWillNotDraw(false);

        loc = new PhoneLocation(context);
    }

    private double getDegrees(double lat1, double long1, double lat2, double long2){
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(long2 - long1);

        Log.i("Locs", lat1 + " " + lat2 + long1 + " " + long2);

        lat1 = Math.toDegrees(lat1);
        lat2 = Math.toDegrees(lat2);

        double x = Math.sin(dLon) * Math.cos(dLat);
        double y = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);

        double brng = Math.toDegrees(Math.atan2(y, x));

        if(brng<0)
            brng = 360 - Math.abs(brng);

        return brng;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(0, PorterDuff.Mode.CLEAR);

        loc.getPreviousLocations();//stores last location

        to = loc.getToLocation();
        from = loc.getFromLocation();

        to.setLatitude(53.2831252);
        to.setLongitude(-9.0414227);

        degrees = getDegrees(from.getLatitude(), from.getLongitude(), to.getLatitude(), to.getLongitude());

        canvas.rotate((float) degrees, canvas.getWidth() / 2, canvas.getHeight() / 2);

        //gmit canteen 53.2791608,-9.0105963
        textPaint.setARGB(255, 200, 0, 0);//Colour red - ish
        textPaint.setStyle(Paint.Style.FILL);

        /* Top arrow*/
        textPaint.setTextSize(70);
        canvas.drawText(degrees + "", 1100, 500, textPaint);

        textPaint.setStrokeWidth(20);
        canvas.drawLine(1250, 100, 1250, 400, textPaint);

        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(2);
        textPaint.setColor(Color.RED);

        Path path = new Path();
        path.moveTo(0, -100);
        path.lineTo(50, 0);
        path.lineTo(-50, 0);
        path.close();

        path.offset(1250, 100);
        canvas.drawPath(path, textPaint);

//
//        /* Bottom arrow*/
//        textPaint.setStrokeWidth(20);
//        canvas.drawLine(1250, 1100, 1250, 1400, textPaint);
//
//        textPaint.setTextSize(70);
//        canvas.drawText("300 Rooms", 1100, 1050, textPaint);
    }
}
