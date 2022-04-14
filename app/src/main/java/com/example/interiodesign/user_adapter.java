package com.example.interiodesign;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class user_adapter extends FragmentStateAdapter {

    private int[] tabIcons = { R.drawable.outline_crop_original_24,
            R.drawable.outline_video_library_24,R.drawable.outline_favorite_24};

    public user_adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
//    private void setupTabIcons() {
//        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
//        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
//        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
//    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position)
        {
            case 0:
                return new Images();

            case 1:
                return  new Videos();

            case 2:
                return  new Favourites();

        }
        return  new Images();
    }

    @Override
    public int getItemCount() {
        return tabIcons.length;
    }
}



