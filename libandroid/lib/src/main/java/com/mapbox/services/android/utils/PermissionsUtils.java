package com.mapbox.services.android.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

/**
 * Utilities to handle the new permission system in 6.0. Weonly focus on ACCESS_FINE_LOCATION.
 * http://developer.android.com/training/permissions/index.html
 */
public class PermissionsUtils {

    private final static String permission = Manifest.permission.ACCESS_FINE_LOCATION;

    private final static int LOCATION_PERMISSIONS_REQUEST = 0;

    private final static String MESSAGE_RATIONALE =
            "This app needs access to your GPS location to locate you on the map.";

    private final static String MESSAGE_FALLBACK =
            "Access to your GPS location has been disabled.";

    /**
     * We only need to check for ACCESS_FINE_LOCATION as this implies ACCESS_COARSE_LOCATION:
     * http://developer.android.com/guide/topics/location/strategies.html#Permission
     * https://developers.google.com/maps/documentation/android-api/location?#location_permissions
     */
    public static boolean isLocationGranted(Activity activity) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);
        return (permissionCheck == PackageManager.PERMISSION_GRANTED);
    }

    public static void startPermissionFlow(Activity activity) {
        startPermissionFlow(activity, MESSAGE_RATIONALE);
    }

    public static void startPermissionFlow(Activity activity, String rationale) {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            Toast.makeText(activity, rationale, Toast.LENGTH_LONG).show();
            requestPermissions(activity);
        } else {
            // No explanation needed, we can request the permission.
            requestPermissions(activity);
        }
    }

    private static void requestPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{ permission },
                LOCATION_PERMISSIONS_REQUEST);
    }

    public static boolean isRequestSuccessful(int requestCode, String[] permissions, int[] grantResults) {
        boolean result = false;

        switch (requestCode) {
            case LOCATION_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    result = true;
                }
            }
        }

        return result;
    }

    public static void explainFallback(Activity activity) {
        explainFallback(activity, MESSAGE_FALLBACK);
    }

    public static void explainFallback(Activity activity, String message) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
    }

}
