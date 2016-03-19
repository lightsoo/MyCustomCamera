package com.example.lightsoo.mycustomcamera.Camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback  {

    private static final String TAG = "CameraPreview";

    private Camera camera;
    private SurfaceHolder holder;

    public CameraPreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CameraPreview(Context context) {
        super(context);
    }

    public void init(Camera camera) {
        this.camera = camera;
        initSurfaceHolder();
    }

    @SuppressWarnings("deprecation") // needed for < 3.0
    private void initSurfaceHolder() {
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e(TAG, "==surfaceCreated==");
        initCamera(holder);
    }


    private void initCamera(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
//            Log.d("Error setting camera preview", String.valueOf(e));
        }
    }


    //화면을 portrait로 고정해서 camera.s
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        Log.e(TAG, "==surfaceChanged==");
//        try {
//            camera.stopPreview();
//        }catch (Exception e){
//
//        }

        //start preview with new settings
//        try {
//            camera.setPreviewDisplay(holder);
//            camera.startPreview();
//        } catch (Exception e) {
//            // intentionally left blank for a test
//        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        Log.e(TAG, "==surfaceDestroyed==");

//        camera.release();
//        camera = null;
//        camera.stopPreview();
//        camera.release();
    }
}
