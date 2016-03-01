package com.collegeessentials.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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

        textPaint.setARGB(255, 200, 0, 0);//Colour red - ish
        textPaint.setTextSize(60);//set size of thing being drawn

        /* This call is necessary, or else the
         * draw method will not be called.
         */
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("Hello", 50, 50, textPaint);//draw a string at x=50, y=50 with paint attributes
    }
}
