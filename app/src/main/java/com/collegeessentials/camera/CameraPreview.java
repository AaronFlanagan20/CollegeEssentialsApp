package com.collegeessentials.camera;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
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

    public CameraPreview(Context context, Camera camera) {
        super(context);
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
        } catch (IOException e) {
            Log.d("CameraView", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
            try{
                if(mCamera != null){
                    mCamera.stopPreview();
                    mCamera.release();
                }
            }catch(Exception e) {
                Log.e("Camera Destroyed", e.getMessage());
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
            //setCameraDisplay(mCamera);//TODO: Get this working
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e) {
            Log.d("Problem", "Error starting camera preview: " + e.getMessage());
        }
    }

    public static int rotate = 0;

    public void setCameraDisplay(Camera camera){
        Camera.Parameters params = camera.getParameters();
        Camera.CameraInfo info = new Camera.CameraInfo();

        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);

        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; rotate = 0; break;
            case Surface.ROTATION_90: degrees = 90; rotate = 90; break;
            case Surface.ROTATION_180: degrees = 180; rotate = 180; break;
            case Surface.ROTATION_270: degrees = 270; rotate = 270; break;
        }

        int result = (info.orientation - degrees + 360) % 360;
        camera.setDisplayOrientation(result);
    }

    public void onPause(){
        mCamera.release();
        mCamera = null;
    }
}