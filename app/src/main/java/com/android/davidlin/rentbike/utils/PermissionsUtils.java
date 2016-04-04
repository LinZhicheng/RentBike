package com.android.davidlin.rentbike.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Check and Request permissions
 * Created by David Lin on 2015/11/19.
 */
public class PermissionsUtils {

    // Storage Permissions
    public static final int REQUEST_EXTERNAL_STORAGE = 0;
    public static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    // Camera Permissions
    public static final int REQUEST_CAMERA = 0;
    public static final String[] PERMISSION_CAMERA = {
            Manifest.permission.CAMERA
    };

    /**
     * Checks if the app has permission to write to external storage
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity Activity to request permission
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission_write = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission_write != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    /**
     * Checks if the app has permission to use camera
     * <p/>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity Activity to request permission
     */
    public static void verifyCameraPermission(Activity activity) {
        int permission_camera = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (permission_camera != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSION_CAMERA, REQUEST_CAMERA);
        }
    }
}
