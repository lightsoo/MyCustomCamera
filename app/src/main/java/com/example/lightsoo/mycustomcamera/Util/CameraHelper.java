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

    private static final String TAG = "CameraHelper";
    public static Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();
            Camera.Parameters parameters = camera.getParameters();
            camera.setDisplayOrientation(90);
            parameters.setRotation(90);
            camera.setParameters(parameters);
        } catch (Exception e) {
            // Camera is not available or doesn't exist
//            com.example.lightsoo.mycustomcamera.Util.Log
            Log.d(TAG, "getCamera failed");
        }
        return camera;
    }

}
