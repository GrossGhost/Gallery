package com.example.gross.gallery;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPagerDetail);

        int itemPosition = getIntent().getIntExtra("position", 0);

        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());

        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(itemPosition);

    }
}
