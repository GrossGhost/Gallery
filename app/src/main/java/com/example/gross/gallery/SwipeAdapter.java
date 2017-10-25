package com.example.gross.gallery;




import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


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
        return MediaAdapter.uriList.size();
    }
}
