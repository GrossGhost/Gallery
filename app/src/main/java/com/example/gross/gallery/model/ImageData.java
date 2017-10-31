package com.example.gross.gallery.model;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;


public class ImageData {

    public static List<ImageData> imageDataList = new ArrayList<>();

    private String title;
    private int width;
    private int height;
    private Uri uri;

    public ImageData(String title, int width, int height, Uri uri){
        this.title = title;
        this.width = width;
        this.height = height;
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeith() {
        return height;
    }

    public Uri getUri() {
        return uri;
    }
}
