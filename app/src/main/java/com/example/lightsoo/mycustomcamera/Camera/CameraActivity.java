package com.example.lightsoo.mycustomcamera.Camera;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import static com.example.lightsoo.mycustomcamera.Util.CameraHelper.getCameraInstance;
import static com.example.lightsoo.mycustomcamera.Util.CameraHelper.cameraAvailable;
import static com.example.lightsoo.mycustomcamera.Util.MediaHelper.getOutputMediaFile;
import static com.example.lightsoo.mycustomcamera.Util.MediaHelper.saveToFile;

import com.example.lightsoo.mycustomcamera.R;

public class CameraActivity extends Activity implements Camera.PictureCallback {

    private static final String TAG = "CameraActivity";
    public static final String EXTRA_IMAGE_PATH = "com.fitta.lightsoo.MyCustomCamera.Camera.CameraActivity.EXTRA_IMAGE_PATH";
    private Camera camera;

//    ImageView imgCapture ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setResult(RESULT_CANCELED);
        // Camera may be in use by another activity or the system or not available at all
        camera = getCameraInstance();
        if (cameraAvailable(camera)) {
            initCameraPreview();
        } else {
            finish();
        }

//        imgCapture = (ImageView)findViewById(R.id.onCaptureClick);
//        imgCapture.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onCaptureClick(v);
//            }
//        });




    }
    // Show the camera view on the activity
    private void initCameraPreview() {
        CameraPreview cameraPreview = (CameraPreview) findViewById(R.id.camera_preview);
        cameraPreview.init(camera);
    }

    public void onCaptureClick(View button) {
        // Take a picture with a callback when the photo has been created
        // Here you can add callbacks if you want to give feedback when the picture is being taken
        camera.takePicture(null, null, this);
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.d(TAG,"Picture taken");
        String path = savePictureToFileSystem(data);
        setResult(path);
        finish();
    }
    private static String savePictureToFileSystem(byte[] data) {
        File file = getOutputMediaFile();
        saveToFile(data, file);
        return file.getAbsolutePath();
    }

    private void setResult(String path) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_IMAGE_PATH, path);
        setResult(RESULT_OK, intent);
    }

    // ALWAYS remember to release the camera when you are finished
    @Override
    protected void onPause() {
        Log.e(TAG, "==onPause==");

        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

}
