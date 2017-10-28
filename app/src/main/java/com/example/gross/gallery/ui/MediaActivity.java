package com.example.gross.gallery.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


import com.example.gross.gallery.R;
import com.example.gross.gallery.adapters.MediaAdapter;

import static com.example.gross.gallery.Consts.CURRENT_POSITION;
import static com.example.gross.gallery.Consts.MEDIA_LOADER_ID;
import static com.example.gross.gallery.Consts.READ_EXTERNAL_STORAGE_RESULT;
import static com.example.gross.gallery.Consts.REQUEST_CODE_CURRENT_POSITION;

public class MediaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView thumbRecyclerView;
    private MediaAdapter mediaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thumbRecyclerView = (RecyclerView) findViewById(R.id.thumbRecyclerView);
        thumbRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(this, 2);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 3);
        }

        thumbRecyclerView.setLayoutManager(gridLayoutManager);
        mediaAdapter = new MediaAdapter(this);
        thumbRecyclerView.setAdapter(mediaAdapter);


        checkReadExternalStoragePermission();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CURRENT_POSITION:
                    thumbRecyclerView.scrollToPosition(data.getIntExtra(CURRENT_POSITION, 0));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_RESULT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // call cursor adapter
                    getSupportLoaderManager().initLoader(MEDIA_LOADER_ID, null, this);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void checkReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String readExternalStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE;
            if (ContextCompat.checkSelfPermission(this, readExternalStoragePermission) ==
                    PackageManager.PERMISSION_GRANTED) {
                //start cursor loader
                getSupportLoaderManager().initLoader(MEDIA_LOADER_ID, null, this);
            } else {
                if (shouldShowRequestPermissionRationale(readExternalStoragePermission)) {
                    Toast.makeText(this, "App need read external storage permission", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{readExternalStoragePermission}, READ_EXTERNAL_STORAGE_RESULT);
           }
        } else {
            //start cursor loader
            getSupportLoaderManager().initLoader(MEDIA_LOADER_ID, null, this);
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                MediaStore.Images.ImageColumns.TITLE,
                MediaStore.Images.ImageColumns.WIDTH,
                MediaStore.Images.ImageColumns.HEIGHT,
                MediaStore.Images.ImageColumns.MINI_THUMB_MAGIC,
                MediaStore.Images.ImageColumns.DATA };
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
        return new CursorLoader(this, MediaStore.Files.getContentUri("external"),
                projection, selection, null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mediaAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mediaAdapter.changeCursor(null);
    }
}
