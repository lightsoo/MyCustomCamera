package com.example.lightsoo.mycustomcamera.Camera;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.example.lightsoo.mycustomcamera.Util.CameraHelper.getCameraInstance;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback  {

    private static final String TAG = "CameraPreview";
    private OnCameraStatusListener listener;

    private Camera camera;
    private SurfaceHolder holder;

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try {
                camera.stopPreview();
            } catch (Exception e) {
            }

            if (null != listener) {
                listener.onCameraStopped(data);
            }
        }
    };

    public void takePicture() {
        Log.d(TAG, "===takePicture()===");
        if (camera != null) {
            try {
                camera.takePicture(null, null, pictureCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    public void init(Camera camera) {
        this.camera = camera;
    }

    public interface OnCameraStatusListener {
        void onCameraStopped(byte[] data);
    }
    public void setOnCameraStatusListener(OnCameraStatusListener listener) {
        this.listener = listener;
    }

    public void start() {
        if (camera != null) {
            camera.startPreview();
        }
    }


    //surface생성시 카메라의 인스턴스를 받아온후 preview를 출력할 위치 설정
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "==surfaceCreated==");
        camera = getCameraInstance();

        try {
            camera.setPreviewDisplay(holder);
        } catch (Exception e) {
            Log.d(TAG, "Error setting camera preview",e);
            camera.release();
            camera = null;
        }
        //update()

        if(camera!=null){
            camera.startPreview();
        }
    }

//    private void initCamera(SurfaceHolder holder) {
//        try {
//            camera.setPreviewDisplay(holder);
//            camera.startPreview();
//        } catch (Exception e) {
//            camera.release();
//            camera =null;
//            Log.d(TAG, "Error setting camera preview", e);
//        }
//    }

    //화면을 portrait로 고정해서 camera.s
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        Log.e(TAG, "==surfaceChanged==");
        try {
            camera.stopPreview();
        }catch (Exception e){

        }
        //start preview with new settings
        //update()
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.e(TAG, "==surfaceDestroyed==");
        camera.release();
        camera = null;
    }
}
