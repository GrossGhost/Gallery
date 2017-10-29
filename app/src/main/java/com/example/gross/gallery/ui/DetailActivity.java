package com.example.gross.gallery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.gross.gallery.R;
import com.example.gross.gallery.ZoomOutPageTransformer;
import com.example.gross.gallery.adapters.SwipeAdapter;

import static com.example.gross.gallery.Consts.CURRENT_POSITION;

public class DetailActivity extends AppCompatActivity {

    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        viewPager = (ViewPager) findViewById(R.id.viewPagerDetail);

        int itemPosition = getIntent().getIntExtra(CURRENT_POSITION, 0);

        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());

        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(itemPosition);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());


    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(CURRENT_POSITION, viewPager.getCurrentItem());
        setResult(RESULT_OK, intent);
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
