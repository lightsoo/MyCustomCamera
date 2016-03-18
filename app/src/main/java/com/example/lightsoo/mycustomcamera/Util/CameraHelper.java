package com.example.lightsoo.mycustomcamera.Util;

import android.hardware.Camera;
import android.util.Log;

/**
 * Created by LG on 2016-03-18.
 */
public class CameraHelper {

    public static boolean cameraAvailable(Camera camera) {
        return camera != null;
    }

    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            // Camera is not available or doesn't exist
            Log.d("getCamera failed", String.valueOf(e));
        }
        return camera;
    }
}
