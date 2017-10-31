package com.example.gross.gallery.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.example.gross.gallery.R;
import com.example.gross.gallery.adapters.MediaAdapter;
import com.example.gross.gallery.model.ImageData;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.example.gross.gallery.Consts.ACTIVITY_START_CAMERA_APP;
import static com.example.gross.gallery.Consts.CAMERA_PERMISSION_RESULT;
import static com.example.gross.gallery.Consts.CURRENT_POSITION;
import static com.example.gross.gallery.Consts.MEDIA_LOADER_ID;
import static com.example.gross.gallery.Consts.READ_EXTERNAL_STORAGE_RESULT;
import static com.example.gross.gallery.Consts.REQUEST_CODE_CURRENT_POSITION;

public class MediaActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView thumbRecyclerView;
    private MediaAdapter mediaAdapter;
    private Cursor mediaCursor;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        thumbRecyclerView = (RecyclerView) findViewById(R.id.thumbRecyclerView);
        GridLayoutManager gridLayoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(this, 2);
        } else {
            gridLayoutManager = new GridLayoutManager(this, 3);
        }

        thumbRecyclerView.setLayoutManager(gridLayoutManager);

        checkReadExternalStoragePermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuCamera){
            Toast.makeText(getApplicationContext(), "Camera", Toast.LENGTH_SHORT).show();
            checkCameraPermission();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_CURRENT_POSITION:
                    thumbRecyclerView.scrollToPosition(data.getIntExtra(CURRENT_POSITION, 0));
                    break;
                case ACTIVITY_START_CAMERA_APP :
                    //add photo to the Media Provider's database
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri contentUri = Uri.fromFile(photoFile);
                    mediaScanIntent.setData(contentUri);
                    this.sendBroadcast(mediaScanIntent);
                    getSupportLoaderManager().restartLoader(MEDIA_LOADER_ID, null, this);
                    break;
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
            case CAMERA_PERMISSION_RESULT:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void takePhoto() {
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = null;
        try {
            photoFile = createImageFile();

        } catch (IOException e) {
            e.printStackTrace();

        }
        String authorities = getApplicationContext().getPackageName() + ".fileprovider";
        Uri imageUri = FileProvider.getUriForFile(this, authorities, photoFile);
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }
    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        return File.createTempFile(imageFileName, ".jpg", storageDirectory);

    }
    private void checkCameraPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String writeExtStorPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            String cameraPermission = Manifest.permission.CAMERA;
            if (ContextCompat.checkSelfPermission(this, cameraPermission) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, writeExtStorPermission) == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            }else {
                if (shouldShowRequestPermissionRationale(cameraPermission)) {
                    Toast.makeText(this, "App need camera permission", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{cameraPermission, writeExtStorPermission}, CAMERA_PERMISSION_RESULT);

            }
        } else{
            takePhoto();
        }
    }
    private void checkReadExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String readExtStorPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
            if (ContextCompat.checkSelfPermission(this, readExtStorPermission) ==
                    PackageManager.PERMISSION_GRANTED) {
                //start cursor loader
                getSupportLoaderManager().initLoader(MEDIA_LOADER_ID, null, this);

            } else {
                if (shouldShowRequestPermissionRationale(readExtStorPermission)) {
                    Toast.makeText(this, "App need read external storage permission", Toast.LENGTH_SHORT).show();
                }
                requestPermissions(new String[]{readExtStorPermission}, READ_EXTERNAL_STORAGE_RESULT);
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
                MediaStore.Images.ImageColumns.DATA };
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
        return new CursorLoader(this, MediaStore.Files.getContentUri("external"),
                projection, selection, null, MediaStore.Files.FileColumns.DATE_ADDED + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        changeCursor(data);
        writeData();
    }

    private void writeData() {
        int titleIndex = mediaCursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE);
        int widthIndex = mediaCursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH);
        int heightIndex = mediaCursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT);
        int dataIndex = mediaCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);

        mediaCursor.moveToPosition(-1);
        ImageData.imageDataList.clear();
        while (mediaCursor.moveToNext()){
            String title = mediaCursor.getString(titleIndex);
            int width = Integer.parseInt(mediaCursor.getString(widthIndex));
            int height = Integer.parseInt(mediaCursor.getString(heightIndex));

            Uri uri = Uri.parse("file://" + mediaCursor.getString(dataIndex));
            ImageData.imageDataList.add(new ImageData(title, width, height, uri));
        }
        mediaAdapter = new MediaAdapter(this);
        thumbRecyclerView.setAdapter(mediaAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        changeCursor(null);
    }

    private Cursor swapCursor(Cursor cursor){
        if (mediaCursor == cursor){
            return null;
        }
        Cursor oldCursor = mediaCursor;
        this.mediaCursor = cursor;

        return oldCursor;
    }

    private void changeCursor(Cursor cursor){
        Cursor oldCursor = swapCursor(cursor);
        if (oldCursor != null){
            oldCursor.close();
        }
    }
}
