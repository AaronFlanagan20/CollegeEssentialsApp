package com.gmit.gmit3D.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceView;

public class DrawView extends SurfaceView{

    private Paint textPaint = new Paint();

    public DrawView(Context context){
        super(context);

        textPaint.setARGB(255, 200, 0, 0);
        textPaint.setTextSize(60);

        /* This call is necessary, or else the
         * draw method will not be called.
         */
        setWillNotDraw(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("Hello", 50, 50, textPaint);
    }
}
