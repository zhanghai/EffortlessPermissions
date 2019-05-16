# EffortlessPermissions

![Icon](sample/src/main/launcher_icon-web.png)

An Android permission library extending Google's [EasyPermissions](https://github.com/googlesamples/easypermissions) with convenient additions.

> Don't say _very easy_, say **effortless**. —— 128 Words to Use Instead of "Very"

<a href="https://play.google.com/store/apps/details?id=me.zhanghai.android.effortlesspermissions.sample" target="_blank"><img alt="Google Play" height="90" src="https://play.google.com/intl/en_US/badges/images/generic/en_badge_web_generic.png"/></a>

[Sample APK](//github.com/zhanghai/EffortlessPermissions/releases/download/v1.1.0/sample-release.apk)

## Why EffortlessPermissions?

- Used as a drop-in replacement for Google's EasyPermissions and based on its battle-tested implementation.
- Added an `@AfterPermissionDenied` annotation for methods to run automatically after denial.
- Included [consumer ProGuard rules](library/proguard-rules.pro) [missing in EasyPermissions](//github.com/googlesamples/easypermissions/blob/master/easypermissions/proguard-rules.pro) which fixes your release build.
- Added more method overloads which make coding easier.
- Added another `DialogFragment` to open app detail settings which you have more control upon, e.g. dialog title can be hidden now.

In a word, just start with **EffortlessPermissions** instead of _EasyPermissions_.

## Integration

Gradle:

```gradle
compile 'me.zhanghai.android.effortlesspermissions:library:1.1.0'
```

## Usage

Just use `EffortlessPermission` wherever you would use `EasyPermissions` ([documentation](https://github.com/googlesamples/easypermissions#usage)), and explore the improvements listed above!

And here is a fully-working sample implementation, handling permission requesting both normally and after permanent denial:

```java
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SAVE_FILE_PERMISSION = 1;
    private static final String[] PERMISSIONS_SAVE_FILE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    ...

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
        } else {
            // Request the permissions.
            EffortlessPermissions.requestPermissions(this,
                    R.string.save_file_permission_request_message,
                    REQUEST_CODE_SAVE_FILE_PERMISSION, PERMISSIONS_SAVE_FILE);
        }
    }

    @AfterPermissionDenied(REQUEST_CODE_SAVE_FILE_PERMISSION)
    private void onSaveFilePermissionDenied() {
        if (EffortlessPermissions.somePermissionPermanentlyDenied(this, PERMISSIONS_SAVE_FILE)) {
            // Some permission is permanently denied so we cannot request them normally.
            OpenAppDetailsDialogFragment.show(
                    R.string.save_file_permission_permanently_denied_message,
                    R.string.open_settings, this);
        } else {
            // User denied at least some of the required permissions, report the error.
            Toast.makeText(this, R.string.save_file_permission_denied, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveFileWithPermission() {
        // It's show time!
        Toast.makeText(this, R.string.save_file_show_time, Toast.LENGTH_SHORT).show();
    }
}
```

Without `EffortlessPermissions`, you would have to make your activity implement `PermissionCallbacks`, check request code and call permission denied callback manually. You would also need to remember writing the ProGuard rules for every project or you'll end up debugging your release build to find it out. But now, only the truly necessary code is written. Cheers!

## ProGuard

The AAR of this library has already included a consumer ProGuard file to retain the annotations and annotated methods.

## License

    Copyright 2017 Zhang Hai

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
