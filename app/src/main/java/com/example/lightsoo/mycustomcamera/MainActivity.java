package com.example.lightsoo.mycustomcamera;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lightsoo.mycustomcamera.Camera.CameraActivity;
import com.example.lightsoo.mycustomcamera.Util.BitmapHelper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActy";
    private static final int REQ_CAMERA_IMAGE = 123;
    Button  btn_camera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void onUseCameraClick(View button) {
        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, REQ_CAMERA_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //카메라 촬영이후 이미지의 경로를 준다,
        if (requestCode == REQ_CAMERA_IMAGE && resultCode == RESULT_OK) {
            String imgPath = data.getStringExtra(CameraActivity.EXTRA_IMAGE_PATH);
            Log.i(TAG, "Got image path: " + imgPath);

            //이미지를 앨범에 저장하는게 필요.
            saveImage(imgPath, MainActivity.this);
            //이미지뷰에 설정해서 출력해주는데
            displayImage(imgPath);

        } else if (requestCode == REQ_CAMERA_IMAGE && resultCode == RESULT_CANCELED) {
            Log.i(TAG,"User didn't take an image");
        }
    }

    private void displayImage(String path) {
        ImageView imageView = (ImageView) findViewById(R.id.captured_image);
        imageView.setImageBitmap(BitmapHelper.decodeSampledBitmap(path, 400, 450));
    }

    //이미지 설정 후 갤러리 저장
    private void saveImage(final String filePath, final Context context){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.ORIENTATION,90);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.MediaColumns.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }
}