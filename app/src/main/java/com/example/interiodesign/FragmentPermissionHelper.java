package com.example.interiodesign;

import android.Manifest;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.FragmentActivity;

public class FragmentPermissionHelper {
    public void startpermissionrequest(FragmentActivity fragmentActivity,FragmentPermissionInterface fragmentPermissionInterface,
                                       String manifest){
        ActivityResultLauncher<String> requestPermissionLauncher =
                fragmentActivity.registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                    fragmentPermissionInterface::onGranted);
        requestPermissionLauncher.launch(manifest);
    }
}
