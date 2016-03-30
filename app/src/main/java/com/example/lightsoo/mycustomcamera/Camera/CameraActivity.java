package com.example.lightsoo.mycustomcamera.Camera;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.lightsoo.mycustomcamera.Camera.CameraPreview.OnCameraStatusListener;
import com.example.lightsoo.mycustomcamera.R;

import java.io.File;

import static com.example.lightsoo.mycustomcamera.Util.MediaHelper.getOutputMediaFile;
import static com.example.lightsoo.mycustomcamera.Util.MediaHelper.saveToFile;

;

public class CameraActivity extends Activity implements OnCameraStatusListener {

    private static final String TAG = "CameraActivity";
    public static final String EXTRA_IMAGE_PATH = "com.fitta.lightsoo.MyCustomCamera.Camera.CameraActivity.EXTRA_IMAGE_PATH";
    private Camera camera;
    private CameraPreview cameraPreview;
    private ImageView Clothes, capturedImage;

    RelativeLayout takePhotoLayout, photoResultLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "===onCreate()===");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
//        setResult(RESULT_CANCELED);


        initCameraPreview();
//        if (cameraAvailable(camera)) {
//            initCameraPreview();
//        } else {
//            Log.d(TAG, "finish()");
//            finish();
//        }

    }

    // Show the camera view on the activity
    private void initCameraPreview() {
        Log.d(TAG, "===initCameraPreview()===");
        takePhotoLayout =(RelativeLayout)findViewById(R.id.take_photo_layout);
        photoResultLayout = (RelativeLayout)findViewById(R.id.photo_result_layout);
        capturedImage = (ImageView) findViewById(R.id.capturedImage);
        cameraPreview =(CameraPreview)findViewById(R.id.cameraPreview);
        cameraPreview.setOnCameraStatusListener(this);

        Clothes = (ImageView)findViewById(R.id.clothes);
        Glide.with(getApplicationContext())
                .load(R.drawable.t_90)
                .crossFade()
                .centerCrop()
//                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(Clothes);
    }

    //촬영이후 결과화면에서 결과화면에서 재촬영 버튼 클릭스
    private void showTakePhotoLayout(){
        takePhotoLayout.setVisibility(View.VISIBLE);
        photoResultLayout.setVisibility(View.GONE);

    }
    public void reTakePhoto(View button){
        showTakePhotoLayout();
    }

    //카메라에서 촬영버튼 눌럿을때, 화면 전환!
    private void showPhotoResultLayout(){

        Log.d(TAG, "===showPhotoResultLayout()===");
        //카메라 촬영 callback()
        takePhotoLayout.setVisibility(View.GONE);
        photoResultLayout.setVisibility(View.VISIBLE);
        cameraPreview.start();  //카메라는 계속 돌린다.
    }

    //단순히 화면 전환만 되고있어.
    public void takePhoto(View view) {
        Log.d(TAG, "===takePhoto()===");
        // Take a picture with a callback when the photo has been created
        // Here you can add callbacks if you want to give feedback when the picture is being taken
//        camera.takePicture(null, null, this);
        if(cameraPreview != null){
            cameraPreview.takePicture();
        }
    }


    //사진이 찍힌이후 동작
//    @Override
//    public void onPictureTaken(byte[] data, Camera camera) {
//        Log.d(TAG,"Picture taken");
//        String path = savePictureToFileSystem(data);
//        Glide.with(getApplicationContext())
//                .load(path)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                .skipMemoryCache(true)
//                .into(capturedImage);
//
////        setResult(path);
////        finish();
//    }
    @Override
    public void onCameraStopped(byte[] data) {
        Log.i("TAG", "===onCameraStopped===");

        File file = getOutputMediaFile();
        saveToFile(data, file);
        String path = savePictureToFileSystem(data);
        Glide.with(getApplicationContext())
                .load(path)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(capturedImage);
        showPhotoResultLayout();
//        showCropperLayout();
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
    public void close(View view) {
        finish();
    }
}
