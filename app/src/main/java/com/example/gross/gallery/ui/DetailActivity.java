package com.example.gross.gallery.ui;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gross.gallery.R;
import com.example.gross.gallery.ZoomOutPageTransformer;
import com.example.gross.gallery.adapters.SwipeAdapter;
import com.example.gross.gallery.model.ImageData;

import java.io.File;

import static com.example.gross.gallery.Consts.CURRENT_POSITION;

public class DetailActivity extends AppCompatActivity {

    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int itemPosition = getIntent().getIntExtra(CURRENT_POSITION, 0);

        viewPager = (ViewPager) findViewById(R.id.viewPagerDetail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detail);

        setSupportActionBar(toolbar);

        viewPager.setAdapter(new SwipeAdapter(getSupportFragmentManager()));
        viewPager.setCurrentItem(itemPosition);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ActionBar bar = getSupportActionBar();
                if (bar != null) {
                    bar.setShowHideAnimationEnabled(true);
                    bar.setTitle(ImageData.imageDataList.get(position).getTitle());
                    bar.setSubtitle(position + "/" + ImageData.imageDataList.size());
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

            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Delete an image");
            ad.setMessage("Are you sure wanna delete selected image?");
            ad.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    //delete image
                    int position = viewPager.getCurrentItem();
                    File toDeleteFile = new File(ImageData.imageDataList.get(position).getUri().getPath());
                    if (delete(getApplicationContext(), toDeleteFile)) {
                        ImageData.imageDataList.remove(position);
                        viewPager.setAdapter(new SwipeAdapter(getSupportFragmentManager()));
                        int countImages = ImageData.imageDataList.size();
                        if (countImages < position)
                            viewPager.setCurrentItem(position - 1);
                        else
                            viewPager.setCurrentItem(position);
                    }
                }
            });
            ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {

                }
            });
            ad.setCancelable(true);
            ad.show();
        }
        if (item.getItemId() == R.id.menuDetailSave){
            Toast.makeText(getApplicationContext(), "DetailSave", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    public static boolean delete(final Context context, final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[] {
                file.getAbsolutePath()
        };
        final ContentResolver contentResolver = context.getContentResolver();
        final Uri filesUri = MediaStore.Files.getContentUri("external");

        contentResolver.delete(filesUri, where, selectionArgs);

        if (file.exists()) {

            contentResolver.delete(filesUri, where, selectionArgs);
        }
        return !file.exists();
    }
}
