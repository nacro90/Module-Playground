package com.avacodelab.moduleplayground;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.avacodelab.moduleplayground.module.PermissionHelper;

import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends AppCompatActivity implements PermissionHelper.PermissionResultCallback {

    private static final int PERMISSION_REQUEST_CODE = 2555;

    private PermissionHelper mPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        final CheckBox cameraCheckBox = (CheckBox) findViewById(R.id.camera_checkBox);
        final CheckBox fineLocationCheckBox = (CheckBox) findViewById(R.id.fineLocation_checkBox);
        final CheckBox readContactsCheckBox = (CheckBox) findViewById(R.id.readContacts_checkBox);
        final CheckBox nfcCheckBox = (CheckBox) findViewById(R.id.nfc_checkBox);
        final CheckBox vibrateCheckBox = (CheckBox) findViewById(R.id.vibrate_checkBox);

        findViewById(R.id.request_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPermissionHelper = new PermissionHelper(PermissionActivity.this);

                List<String> permissions = new ArrayList<>();

                if (cameraCheckBox.isChecked()) {
                    permissions.add(Manifest.permission.CAMERA);
                }
                if (fineLocationCheckBox.isChecked()) {
                    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if (readContactsCheckBox.isChecked()) {
                    permissions.add(Manifest.permission.READ_CONTACTS);
                }
                if (nfcCheckBox.isChecked()) {
                    permissions.add(Manifest.permission.NFC);
                }
                if (vibrateCheckBox.isChecked()) {
                    permissions.add(Manifest.permission.VIBRATE);
                }

                //////////////////////////  The ultimate method

                mPermissionHelper.checkDeviceAndRequestPermissions(PERMISSION_REQUEST_CODE, permissions, PermissionActivity.this);

                //////////////////////////
            }
        });

        findViewById(R.id.appSettings_Button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionHelper.openApplicationSettings(PermissionActivity.this);
            }
        });
    }

    /**
     * Permission statuses can be changed via the app settings. In case of this situation, check the
     * permission status in onStart() method in activity and handle it.
     */
    @Override
    protected void onStart() {
        super.onStart();

        final CheckBox cameraCheckBox = (CheckBox) findViewById(R.id.camera_checkBox);
        final CheckBox fineLocationCheckBox = (CheckBox) findViewById(R.id.fineLocation_checkBox);
        final CheckBox readContactsCheckBox = (CheckBox) findViewById(R.id.readContacts_checkBox);
        final CheckBox nfcCheckBox = (CheckBox) findViewById(R.id.nfc_checkBox);
        final CheckBox vibrateCheckBox = (CheckBox) findViewById(R.id.vibrate_checkBox);

        initializeCheckBox(cameraCheckBox, Manifest.permission.CAMERA);
        initializeCheckBox(fineLocationCheckBox, Manifest.permission.ACCESS_FINE_LOCATION);
        initializeCheckBox(readContactsCheckBox, Manifest.permission.READ_CONTACTS);
        initializeCheckBox(nfcCheckBox, Manifest.permission.NFC);
        initializeCheckBox(vibrateCheckBox, Manifest.permission.VIBRATE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // PermissionHelper.onRequestPermissionsResult() method must be called in Activity.onRequestPermissionsResult()
        mPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * This method always called if there are any granted permissions. Including the permissions those are granted before!
     */
    @Override
    public void permissionsGranted(int requestCode, List<String> permissionsGranted) {

        Toast.makeText(this, "Permissions granted: " + permissionsGranted, Toast.LENGTH_LONG).show();

        if (requestCode == PERMISSION_REQUEST_CODE) {

            if (permissionsGranted.contains(Manifest.permission.CAMERA)) {
                setCheckBoxPermissionGranted((CheckBox) findViewById(R.id.camera_checkBox), true);
            }
            if (permissionsGranted.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                setCheckBoxPermissionGranted((CheckBox) findViewById(R.id.fineLocation_checkBox), true);
            }
            if (permissionsGranted.contains(Manifest.permission.READ_CONTACTS)) {
                setCheckBoxPermissionGranted((CheckBox) findViewById(R.id.readContacts_checkBox), true);
            }
            if (permissionsGranted.contains(Manifest.permission.NFC)) {
                setCheckBoxPermissionGranted((CheckBox) findViewById(R.id.nfc_checkBox), true);
            }
            if (permissionsGranted.contains(Manifest.permission.VIBRATE)) {
                setCheckBoxPermissionGranted((CheckBox) findViewById(R.id.vibrate_checkBox), true);
            }

        }
    }

    @Override
    public void permissionsDenied(int requestCode, List<String> permissionsDenied) {

        Toast.makeText(this, "Permissions denied: " + permissionsDenied, Toast.LENGTH_LONG).show();

        if (requestCode == PERMISSION_REQUEST_CODE) {

            if (permissionsDenied.contains(Manifest.permission.CAMERA)) {
                setCheckBoxPermissionDeniedAtLeastOnce((CheckBox) findViewById(R.id.camera_checkBox), true);
            }
            if (permissionsDenied.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                setCheckBoxPermissionDeniedAtLeastOnce((CheckBox) findViewById(R.id.fineLocation_checkBox), true);
            }
            if (permissionsDenied.contains(Manifest.permission.READ_CONTACTS)) {
                setCheckBoxPermissionDeniedAtLeastOnce((CheckBox) findViewById(R.id.readContacts_checkBox), true);
            }
            if (permissionsDenied.contains(Manifest.permission.NFC)) {
                setCheckBoxPermissionDeniedAtLeastOnce((CheckBox) findViewById(R.id.nfc_checkBox), true);
            }
            if (permissionsDenied.contains(Manifest.permission.VIBRATE)) {
                setCheckBoxPermissionDeniedAtLeastOnce((CheckBox) findViewById(R.id.vibrate_checkBox), true);
            }

        }
    }

    @Override
    public void permissionsNeverAskAgain(int requestCode, List<String> permissionsNeverAskAgain) {

        Toast.makeText(this, "Permissions denied permanently: " + permissionsNeverAskAgain, Toast.LENGTH_LONG).show();

        if (requestCode == PERMISSION_REQUEST_CODE) {

            if (permissionsNeverAskAgain.contains(Manifest.permission.CAMERA)) {
                setCheckBoxPermissionNeverAskAgain((CheckBox) findViewById(R.id.camera_checkBox), true);
            }
            if (permissionsNeverAskAgain.contains(Manifest.permission.ACCESS_FINE_LOCATION)) {
                setCheckBoxPermissionNeverAskAgain((CheckBox) findViewById(R.id.fineLocation_checkBox), true);
            }
            if (permissionsNeverAskAgain.contains(Manifest.permission.READ_CONTACTS)) {
                setCheckBoxPermissionNeverAskAgain((CheckBox) findViewById(R.id.readContacts_checkBox), true);
            }
            if (permissionsNeverAskAgain.contains(Manifest.permission.NFC)) {
                setCheckBoxPermissionNeverAskAgain((CheckBox) findViewById(R.id.nfc_checkBox), true);
            }
            if (permissionsNeverAskAgain.contains(Manifest.permission.VIBRATE)) {
                setCheckBoxPermissionNeverAskAgain((CheckBox) findViewById(R.id.vibrate_checkBox), true);
            }

        }
    }

    private void initializeCheckBox(CheckBox checkBox, String permission) {
        if (PermissionHelper.isPermissionGranted(this, permission)) {
            setCheckBoxPermissionGranted(checkBox, false);
        } else if (PermissionHelper.shouldShowPermissionExplanation(this, permission)){
            setCheckBoxPermissionDeniedAtLeastOnce(checkBox, false);
        }
    }

    private void setCheckBoxPermissionGranted(CheckBox checkBox, boolean changeColor) {
        checkBox.setEnabled(false);
        checkBox.setChecked(true);
        String[] split = checkBox.getText().toString().split("-");
        checkBox.setText(split[0] + "-(ALLOWED)");
        if (changeColor) {
            checkBox.setTextColor(Color.BLUE);
        }
    }

    private void setCheckBoxPermissionDeniedAtLeastOnce(CheckBox checkBox, boolean changeColor) {
        checkBox.setEnabled(true);
        checkBox.setChecked(false);
        String[] split = checkBox.getText().toString().split("-");
        checkBox.setText(split[0] + "-(DENIED)");
        if (changeColor) {
            checkBox.setTextColor(ContextCompat.getColor(this, R.color.textColorDark));
        }
    }

    private void setCheckBoxPermissionNeverAskAgain(CheckBox checkBox, boolean changeColor) {
        checkBox.setEnabled(true);
        checkBox.setChecked(false);
        String[] split = checkBox.getText().toString().split("-");
        checkBox.setText(split[0] + "-(DENIED PERMANENTLY)");
        if (changeColor) {
            checkBox.setTextColor(Color.RED);
        }
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, PermissionActivity.class);
        context.startActivity(starter);
    }
}
