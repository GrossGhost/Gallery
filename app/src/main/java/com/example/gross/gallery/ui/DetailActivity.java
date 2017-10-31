package com.example.gross.gallery.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gross.gallery.R;
import com.example.gross.gallery.ZoomOutPageTransformer;
import com.example.gross.gallery.adapters.SwipeAdapter;
import com.example.gross.gallery.model.ImageData;

import static com.example.gross.gallery.Consts.CURRENT_POSITION;

public class DetailActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private boolean isToolbarVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int itemPosition = getIntent().getIntExtra(CURRENT_POSITION, 0);
        final int imageCount = ImageData.imageDataList.size();

        viewPager = (ViewPager) findViewById(R.id.viewPagerDetail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);

        setSupportActionBar(toolbar);

        SwipeAdapter swipeAdapter = new SwipeAdapter(getSupportFragmentManager());

        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(itemPosition);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ActionBar bar = getSupportActionBar();
                if (bar != null) {
                    bar.setShowHideAnimationEnabled(true);
                    bar.setTitle(ImageData.imageDataList.get(position).getTitle());
                    bar.setSubtitle(position + "/" + imageCount);
                }
            }

            @Override
            public void onPageSelected(int position) {}

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.putExtra(CURRENT_POSITION, viewPager.getCurrentItem());
        setResult(RESULT_OK, intent);
        super.finish();
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuDetailDelete){
            Toast.makeText(getApplicationContext(), "DetailDelete", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.menuDetailSave){
            Toast.makeText(getApplicationContext(), "DetailSave", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
