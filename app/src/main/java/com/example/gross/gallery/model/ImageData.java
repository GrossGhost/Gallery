package com.example.gross.gallery.model;

import java.util.ArrayList;
import java.util.List;


public class ImageData {

    public static List<ImageData> imageDataList = new ArrayList<>();
    public static boolean isDataFromGallery = true;

    private String title;
    private String uri;

    public ImageData(String title, String uri) {
        this.title = title;
        this.uri = uri;
    }

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }
}
