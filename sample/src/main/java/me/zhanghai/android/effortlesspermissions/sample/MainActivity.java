/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.effortlesspermissions.sample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.effortlesspermissions.AfterPermissionDenied;
import me.zhanghai.android.effortlesspermissions.EffortlessPermissions;
import me.zhanghai.android.effortlesspermissions.OpenAppDetailsDialogFragment;
import pub.devrel.easypermissions.AfterPermissionGranted;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SAVE_FILE_PERMISSION = 1;
    private static final String[] PERMISSIONS_SAVE_FILE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @BindView(R.id.save)
    Button mSaveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFile();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Dispatch to our library.
        EffortlessPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults,
                this);
    }

    // Call back to the same method so that we'll check and proceed.
    @AfterPermissionGranted(REQUEST_CODE_SAVE_FILE_PERMISSION)
    private void saveFile() {
        if (EffortlessPermissions.hasPermissions(this, PERMISSIONS_SAVE_FILE)) {
            // We've got the permission.
            saveFileWithPermission();
        } else if (EffortlessPermissions.somePermissionPermanentlyDenied(this,
                PERMISSIONS_SAVE_FILE)) {
            // Some permission is permanently denied so we cannot request them normally.
            OpenAppDetailsDialogFragment.show(
                    R.string.save_file_permission_permanently_denied_message,
                    R.string.open_settings, this);
        } else  {
            // Request the permissions.
            EffortlessPermissions.requestPermissions(this,
                    R.string.save_file_permission_request_message,
                    REQUEST_CODE_SAVE_FILE_PERMISSION, PERMISSIONS_SAVE_FILE);
        }
    }

    @AfterPermissionDenied(REQUEST_CODE_SAVE_FILE_PERMISSION)
    private void onSaveFilePermissionDenied() {
        // User denied at least some of the required permissions, report the error.
        Toast.makeText(this, R.string.save_file_permission_denied, Toast.LENGTH_SHORT).show();
    }

    private void saveFileWithPermission() {
        // It's show time!
        Toast.makeText(this, R.string.save_file_show_time, Toast.LENGTH_SHORT).show();
    }
}
