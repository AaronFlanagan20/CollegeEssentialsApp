package com.gmit.gmit3D.camera;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** The Camera Preview class
 * All camera and on screen animations are done in this class. */
public class Preview extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private SurfaceHolder mHolder;
    private Camera mCamera;

    protected final Paint rectanglePaint = new Paint();
    private byte [] rgbbuffer = new byte[256 * 256];
    private int [] rgbints = new int[256 * 256];

    public Preview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        rectanglePaint.setARGB(100, 200, 0, 0);
        rectanglePaint.setStyle(Paint.Style.FILL);
        rectanglePaint.setStrokeWidth(2);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(new Rect((int) Math.random() * 100,
                (int) Math.random() * 100, 200, 200), rectanglePaint);
        Log.w(this.getClass().getName(), "On Draw Called");
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        synchronized (this){
            this.setWillNotDraw(false);
            try {
                Camera.Parameters p = mCamera.getParameters();
                p.setPreviewSize(240, 160);
                mCamera.setParameters(p);
                mCamera.startPreview();
            } catch (Exception e) {
                Log.d("Problem", "Error setting camera preview: " + e.getMessage());
            }
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        synchronized (this){
            try{
                if(mCamera != null){
                    mCamera.stopPreview();
                    mCamera.release();
                }
            }catch(Exception e){
                Log.e("Camera", e.getMessage());
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setDisplayOrientation(90);//by default preview is landscape set it back to vertical
            mCamera.startPreview();

        } catch (Exception e){
            Log.d("Problem", "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Log.d("Camera", "Got a camera frame");

        Canvas c = null;

        if(mHolder == null){
            return;
        }

        try {
            synchronized (mHolder) {
                c = mHolder.lockCanvas(null);

                // Do your drawing here
                // So this data value you're getting back is formatted in YUV format and you can't do much
                // with it until you convert it to rgb
                int bwCounter=0;
                int yuvsCounter=0;
                for (int y=0;y<160;y++) {
                    System.arraycopy(data, yuvsCounter, rgbbuffer, bwCounter, 240);
                    yuvsCounter=yuvsCounter+240;
                    bwCounter=bwCounter+256;
                }

                for(int i = 0; i < rgbints.length; i++){
                    rgbints[i] = (int)rgbbuffer[i];
                }

                //decodeYUV(rgbbuffer, data, 100, 100);
                c.drawBitmap(rgbints, 0, 256, 0, 0, 256, 256, false, new Paint());
            }
        } finally {
            // do this in a finally so that if an exception is thrown during the above, we don't leave the Surface in an inconsistent state
            if (c != null) {
                mHolder.unlockCanvasAndPost(c);
            }
        }
    }
}