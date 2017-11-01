package com.example.gross.gallery.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.gross.gallery.model.ImageData;
import com.example.gross.gallery.ui.DetailFragment;


public class SwipeAdapter extends FragmentStatePagerAdapter {


    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return DetailFragment.newInstance(position);
    }

    @Override
    public int getCount() {

        return ImageData.imageDataList.size();
    }

}
