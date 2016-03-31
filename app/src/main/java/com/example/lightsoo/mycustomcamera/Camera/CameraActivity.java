package com.example.lightsoo.mycustomcamera.Camera;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import java.io.FileNotFoundException;

import static com.example.lightsoo.mycustomcamera.Util.MediaHelper.getOutputMediaFile;
import static com.example.lightsoo.mycustomcamera.Util.MediaHelper.saveToFile;

;

public class CameraActivity extends Activity implements OnCameraStatusListener {

    private static final String TAG = "CameraActivity";
    public static final String EXTRA_IMAGE_PATH = "com.fitta.lightsoo.MyCustomCamera.Camera.CameraActivity.EXTRA_IMAGE_PATH";
    private CameraPreview cameraPreview;
    private ImageView Clothes, capturedImage;
    private File mSaveFile;
    private String cameraPath;
    RelativeLayout takePhotoLayout, photoResultLayout;

    private static final int REQUEST_CROP = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "===onCreate()===");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        initCameraPreview();
    }

    // Show the camera view on the activity
    private void initCameraPreview() {
        Log.d(TAG, "===initCameraPreview()===");
        takePhotoLayout =(RelativeLayout)findViewById(R.id.take_photo_layout);//기본화면
        photoResultLayout = (RelativeLayout)findViewById(R.id.photo_result_layout);//촬영결과 화면
        capturedImage = (ImageView) findViewById(R.id.capturedImage);//사진찍는 버튼의 이미지뷰
        cameraPreview =(CameraPreview)findViewById(R.id.cameraPreview); //카메라 surfaceview
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
        if(cameraPreview != null){
            cameraPreview.takePicture();
        }
    }

    @Override
    public void onCameraStopped(byte[] data) {
        Log.i("TAG", "===onCameraStopped===");
        //지정해둔 디렉토리에 현재 시간으로 파일객체 생성
        mSaveFile = getOutputMediaFile();
        //파일저장
        saveToFile(data, mSaveFile);
        //만든 파일의 절대 경로
        cameraPath = savePictureToFileSystem(data);
        Log.d(TAG, "cameraPath : " + cameraPath);

        Glide.with(getApplicationContext())
                .load(cameraPath)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(capturedImage);
        showPhotoResultLayout();
    }

    //사진촬영 이후 결과화면에서 확인 누른 다면 이후, 사진 크롭 처리로 이동할 함수
    public void cropImage(View button){

        try {
            String url = MediaStore.Images.Media.insertImage(getContentResolver(), cameraPath, "카메라 이미지", "기존 이미지");
            Uri photouri = Uri.parse(url);
            //ContentResolver가 처리할수있는 value들을 저장하는데 사용
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.ORIENTATION, 90);
            //뭔지 잘모르겟지만, URL의 value로 대체한다
            getContentResolver().update(photouri, values, null, null);


            if(photouri != null){
                Intent photoPickerIntent = new Intent(
                        "com.android.camera.action.CROP", photouri);
                photoPickerIntent.putExtra("scale", false);
                photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mSaveFile));
                photoPickerIntent.putExtra("outputFormat",
                        Bitmap.CompressFormat.JPEG.toString());
                startActivityForResult(photoPickerIntent, REQUEST_CROP);

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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


    //crop이미지 처리이후에 reqCode를 통해서 CROP처리된 이미지를 받는다!!!
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //액티비티 결과가 이상는경우
        if(resultCode != RESULT_OK){return;}
        switch (requestCode){
            //여기서 이미지 처리된걸 받아온다
            case  REQUEST_CROP :
                Log.d(TAG, "REQUEST_CROP");

                break;
        }

    }
}