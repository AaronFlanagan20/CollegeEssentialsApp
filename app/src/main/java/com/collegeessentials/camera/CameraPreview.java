package com.collegeessentials.camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;

/** The Camera Preview class
 * This is used to create a SurfaceHolder to hold our DrawView and manage everything
 *
 * @version 1.0
 * @see DrawView
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private boolean isPreviewRunning = false;
    private Context context;
    public static int rotate;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.context = context;
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            isPreviewRunning = true;
        } catch (IOException e) {
            Log.d("CameraView", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
            try{
                if(mCamera != null){
                    mCamera.stopPreview();
                    isPreviewRunning = false;
                    mCamera.release();
                }
            }catch(Exception e) {
                Log.e("Camera Destroyed", e.getMessage());
            }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if(isPreviewRunning) {
            mCamera.stopPreview();
        }

        Camera.Parameters parameters = mCamera.getParameters();
        Display display = ((WindowManager) context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();

        if(display.getRotation() == Surface.ROTATION_0)
        {
            parameters.setPreviewSize(height, width);
            mCamera.setDisplayOrientation(90);
        }

        if(display.getRotation() == Surface.ROTATION_90)
        {
            parameters.setPreviewSize(width, height);
        }

        if(display.getRotation() == Surface.ROTATION_180)
        {
            parameters.setPreviewSize(height, width);
        }

        if(display.getRotation() == Surface.ROTATION_270)
        {
            parameters.setPreviewSize(width, height);
            mCamera.setDisplayOrientation(180);
        }

        mCamera.setParameters(parameters);

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d("Problem", "Error starting camera preview: " + e.getMessage());
        }
    }


    //explained here: http://stackoverflow.com/questions/3841122/android-camera-preview-is-sideways
    public void setCameraDisplay(Camera camera, int width, int height){

    }

    public void onPause(){
        mCamera.release();
        mCamera = null;
    }
}