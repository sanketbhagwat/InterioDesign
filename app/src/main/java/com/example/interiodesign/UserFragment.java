package com.example.interiodesign;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class UserFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =121 ;

    public UserFragment() {
        // Required empty public constructor
    }

    private FragmentActivity fr;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    user_adapter userAdapter;
    private int[] tabIcons = { R.drawable.outline_crop_original_24,
            R.drawable.outline_video_library_24,R.drawable.outline_favorite_24};




    private ActivityResultLauncher<String> requestPermissionLauncher =
    registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your
            // app.
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
        }
    });



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_user, container, false);
//
        if (ContextCompat.checkSelfPermission(
                view.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
//            performAction(...);
//            loadImage();
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE);
        }


//

        viewPager2 = view.findViewById(R.id.viewPager2);
//
        tabLayout = view.findViewById(R.id.tabLayout);
        userAdapter=new user_adapter((FragmentActivity) view.getContext());

        viewPager2.setAdapter(userAdapter);
        new TabLayoutMediator(tabLayout,viewPager2,(tab, position) -> tab.setIcon(tabIcons[position])).attach();



        return view;
    }

//

}