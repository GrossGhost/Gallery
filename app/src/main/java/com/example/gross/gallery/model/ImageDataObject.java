package com.example.gross.gallery.model;


import android.net.Uri;

public class ImageDataObject {

    private String title;
    private int width;
    private int height;
    private Uri thumb;
    private Uri uri;

    public ImageDataObject(String title, int width, int height, Uri thumb, Uri uri){
        this.title = title;
        this.width = width;
        this.height = height;
        this.thumb = thumb;
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

    public Uri getThumb() {
        return thumb;
    }
    public Uri getUri() {
        return uri;
    }
}
