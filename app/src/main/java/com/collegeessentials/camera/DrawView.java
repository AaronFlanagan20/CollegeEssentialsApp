package com.collegeessentials.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.SurfaceView;

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

    public DrawView(Context context){
        super(context);

        /* This call is necessary, or else the
         * draw method will not be called.
         */
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //gmit canteen 53.2791608,-9.0105963

        textPaint.setARGB(255, 200, 0, 0);//Colour red - ish

        /* Top arrow*/
        textPaint.setTextSize(70);
        canvas.drawText("300 Rooms", 1100, 500, textPaint);

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
