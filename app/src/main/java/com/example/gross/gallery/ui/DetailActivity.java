package com.example.gross.gallery.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.gross.gallery.R;
import com.example.gross.gallery.ZoomOutPageTransformer;
import com.example.gross.gallery.adapters.SwipeAdapter;
import com.example.gross.gallery.model.ImageData;
import com.example.gross.gallery.network.DownloadService;

import java.io.File;

import static com.example.gross.gallery.Consts.CURRENT_POSITION;
import static com.example.gross.gallery.Consts.IMAGE_NAME;
import static com.example.gross.gallery.Consts.IMAGE_URL;
import static com.example.gross.gallery.Consts.WRITE_EXTERNAL_STORAGE_RESULT;

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
                    bar.setTitle(ImageData.imageDataList.get(position).getTitle());
                    bar.setSubtitle(position + 1 + "/" + ImageData.imageDataList.size());
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
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
        if (ImageData.isDataFromGallery) {
            menu.findItem(R.id.menuDetailSave).setVisible(false);
        } else {
            menu.findItem(R.id.menuDetailDelete).setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuDetailDelete) {

            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle("Delete an image");
            ad.setMessage("Are you sure wanna delete selected image?");
            ad.setPositiveButton("Delete", (dialog, arg1) -> {
                //delete image
                int position = viewPager.getCurrentItem();
                File toDeleteFile = new File(Uri.parse(ImageData.imageDataList.get(position).getUri()).getPath());
                if (delete(getApplicationContext(), toDeleteFile)) {
                    ImageData.imageDataList.remove(position);
                    viewPager.setAdapter(new SwipeAdapter(getSupportFragmentManager()));
                    int countImages = ImageData.imageDataList.size();
                    if (countImages < position)
                        viewPager.setCurrentItem(position - 1);
                    else
                        viewPager.setCurrentItem(position);
                }
            });
            ad.setNegativeButton("Cancel", (dialog, arg1) -> {

            });
            ad.setCancelable(true);
            ad.show();
        }

        if (item.getItemId() == R.id.menuDetailSave) {
            if (checkPermission()) {
                startDownload();
            } else {
                requestPermission();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

            return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;

        } else
            return true;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_RESULT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE_RESULT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startDownload();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void startDownload() {

        int position = viewPager.getCurrentItem();

        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(IMAGE_URL, ImageData.imageDataList.get(position).getUri());
        intent.putExtra(IMAGE_NAME, ImageData.imageDataList.get(position).getTitle());
        startService(intent);

    }

    public static boolean delete(final Context context, final File file) {
        final String where = MediaStore.MediaColumns.DATA + "=?";
        final String[] selectionArgs = new String[]{
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
