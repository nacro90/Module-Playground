package com.avacodelab.moduleplayground.module;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  This class contains various of methods that helps to grant permissions for the application. <br><br>
 *
 *  <b> First of all, you must declare all the permissions of the app to the manifest! </b> <br><br>
 *
 *  <b> PermissionHelper.onRequestPermissionsResult() must be called within Activity.onRequestPermissionsResult() with its parameters. </b> <br><br>
 *
 *  - If the permissions is not dangerous (Check documentation) permission already granted at app install.
 *    So Android does not ask the permission. <br><br>
 *
 *    <strong>Usage:</strong> <br>
 *
 *    <pre>{@code
 *
 *    mPermissionHelper = new PermissionHelper(PermissionActivity.this);
 *
 *    mPermissionHelper.checkDeviceAndRequestPermissions(PERMISSION_REQUEST_CODE, new String[] {
 *
 *       Manifest.permission.PERMISSION_1
 *       Manifest.permission.PERMISSION_2
 *       Manifest.permission.PERMISSION_3
 *
 *    }, new PermissionHelper.PermissionResultCallback {
 *
 *          public void permissionsGranted(int requestCode, List<String> permissionsGranted) {
 *              // Do the jobs which demands permission.
 *          }
 *
 *          public void permissionsDenied(int requestCode, List<String> permissionsDenied) {
 *              // Handle the case of permissions denied. Show an explanation for why this permission is required.
 *          }
 *
 *          public void permissionsNeverAskAgain(int requestCode, List<String> permissionsDenied) {
 *              // Handle the case of permissions denied PERMANENTLY. Try to redirect the user to app settings with strong explanation.
 *          }
 *    });
 *  } </pre><br><br>
 *
 *
 *  - If the device is running Android 5.1 or lower, or your app's target SDK is 22 or lower:
 *    If you list a dangerous permission in your manifest, the user has to grant the permission
 *    when they install the app; if they do not grant the permission, the system does not install the app at all. <br><br>
 *
 *  - If the device is running Android 6.0 or higher, and your app's target SDK is 23 or higher:
 *    The app has to list the permissions in the manifest, and it must request each dangerous permission
 *    it needs while the app is running. The user can grant or deny each permission, and the app can
 *    continue to run with limited capabilities even if the user denies a permission request. <br><br>
 *
 *  For permission specialities: (API level and permission strength) <br>
 *  <a href="https://developer.android.com/reference/android/Manifest.permission.html">Manifest.permission</a> <br>
 *  <a href="https://developer.android.com/training/permissions/requesting.html">Requesting Permissions at Run Time</a> <br><br>
 *
 *
 *  Created by orcan on 13.06.2017.
 */

public final class PermissionHelper {

    private static final String TAG = PermissionHelper.class.getSimpleName();

    private Activity mActivity;
    private List<String> mPermissionsAlreadyGranted;
    private PermissionResultCallback mPermissionResultCallback;
    private ArrayList<String> mPermissionsNeeded;

    public PermissionHelper(Activity activity) {
        mActivity = activity;
    }

    /**
     * This method checks the given permissions and requests them if necessary. <br><br>
     *
     * This method should be called when doing the job whic demanding permission. And then do the job in callback.
     *
     * Callback methods called immediately after the permission check. <br>
     * <b>If permissions already granted before, permissionsGranted() callback called anyway.
     * Do your job which needs permission in permissionsGranted() method. </b>
     *
     * @param requestCode   Request specific code which is created arbitrarily in the code.
     *                      This code returns in callback methods <br><br>
     *
     * @param permissions   Permission strings which will be checked and requested if necessary <br><br>
     *
     * @param callback      The {@link PermissionResultCallback} callback. <br>
     *                      <b> If permissions already granted before, permissionsGranted() callback called anyway. </b>
     */
    public void checkDeviceAndRequestPermissions(int requestCode, @NonNull List<String> permissions, PermissionResultCallback callback) {

        mPermissionsAlreadyGranted = permissions;
        mPermissionResultCallback = callback;

        if (permissions.isEmpty()) {
            Log.w(TAG, "Permission list is empty.");
            mPermissionResultCallback.permissionsGranted(requestCode, permissions);
        } else {
            if (isDeviceSupportsRuntimePermissions()) {
                if (checkAndRequestPermissions(permissions, requestCode)) {
                    Log.i(TAG, "All permissions granted.");
                    mPermissionResultCallback.permissionsGranted(requestCode, permissions);
                } else {
                    Log.d(TAG, "Permissions requested.");
                }
            } else {
                Log.i(TAG, "Device does not support Runtime Permissions. All permissions granted.");
                mPermissionResultCallback.permissionsGranted(requestCode, permissions);
            }
        }
    }

    public void checkDeviceAndRequestPermissions(int requestCode, @NonNull String[] permissions, PermissionResultCallback callback) {
        checkDeviceAndRequestPermissions(requestCode, Arrays.asList(permissions), callback);
    }

    /**
     * This method finds which permission is not granted and requests them.
     */
    private boolean checkAndRequestPermissions(List<String> permissions, int requestCode) {
        mPermissionsNeeded = new ArrayList<>();
        mPermissionsAlreadyGranted = new ArrayList<>();

        for (String permission : permissions) {
            if (! isPermissionGranted(mActivity, permission)) {
                mPermissionsNeeded.add(permission);
            } else {
                mPermissionsAlreadyGranted.add(permission);
            }
        }
        if (! mPermissionsNeeded.isEmpty()) {
            Log.i(TAG, "Requesting permissions: " + mPermissionsNeeded);
            ActivityCompat.requestPermissions(mActivity, mPermissionsNeeded.toArray(new String[mPermissionsNeeded.size()]), requestCode);
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method handles callbacks for permission responses. <br><br>
     *
     * <b> This method must be called within onRequestPermissionsResult(). </b>
     *
     * @param requestCode   is an integer which is given at request method
     * @param permissions   requested permissions
     * @param grantResults  request responses
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(grantResults.length > 0) {
            Map<String, Integer> permissionPairs = new HashMap<>();

            for (int i = 0; i < permissions.length; i++) {
                permissionPairs.put(permissions[i], grantResults[i]);
            }

            ArrayList<String> permissionsGranted = new ArrayList<>();
            ArrayList<String> permissionsDenied = new ArrayList<>();
            ArrayList<String> permissionsNeverAskAgain = new ArrayList<>();

            for (String permission : mPermissionsNeeded) {
                if (permissionPairs.get(permission) != PackageManager.PERMISSION_GRANTED) {
                    if(shouldShowPermissionExplanation(mActivity, permission))
                        permissionsDenied.add(permission);
                    else {
                        permissionsNeverAskAgain.add(permission);
                    }
                } else {
                    permissionsGranted.add(permission);
                }
            }

            permissionsGranted.addAll(mPermissionsAlreadyGranted);

            if (! permissionsGranted.isEmpty()) {
                Log.i(TAG, "Permissions granted: " + permissionsGranted);
                mPermissionResultCallback.permissionsGranted(requestCode, permissionsGranted);
            }

            if (! permissionsDenied.isEmpty()) {
                Log.i(TAG, "Permissions denied: " + permissionsDenied);
                mPermissionResultCallback.permissionsDenied(requestCode, permissionsDenied);
            }

            if (! permissionsNeverAskAgain.isEmpty()) {
                Log.i(TAG, "Permissions never ask again: " + permissionsNeverAskAgain);
                mPermissionResultCallback.permissionsNeverAskAgain(requestCode, permissionsNeverAskAgain);
            }
        }
    }

    /**
     * If the permission denied once and and user did not check "Never ask again" then this method returns true.
     */
    public static boolean shouldShowPermissionExplanation(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    /**
     *  Checks device supports runtime permissions. <br><br>
     *
     *  Runtime permissions came to Android with Marshmallow (6.0)
     */
    public static boolean isDeviceSupportsRuntimePermissions() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     Returns a boolean which represents if given permission is granted.
     @param permission Permission string can be retrieved from {@link Manifest.permission} class.
     */
    public static boolean isPermissionGranted(Context context, String permission) {
        return  isDeviceSupportsRuntimePermissions() &&
                ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     Returns a boolean which represents if given permission is granted.
     @param permissions Multiple permission strings.
     */
    public static boolean isPermissionGranted(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (! isPermissionGranted(context, permission)) {
                return false;
            }
        }

        return true;
    }

    /**
     *  <b> This method opens settings and navigates to application details page. </b> <br><br>
     *
     *  Runtime permissions can not be revoked programmatically. So it must be reset from
     *  application settings manually
     */
    public static void openApplicationSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * Callback for permission status.
     */
    public interface PermissionResultCallback {
        /**
         * This method called whether the permissions granted before (not requested) or permissions newly granted (requested)
         */
        void permissionsGranted(int requestCode, List<String> permissionsGranted);
        void permissionsDenied(int requestCode, List<String> permissionsDenied);
        void permissionsNeverAskAgain(int requestCode, List<String> permissionsNeverAskAgain);
    }
}
