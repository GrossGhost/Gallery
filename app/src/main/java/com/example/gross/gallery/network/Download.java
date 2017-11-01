package com.example.gross.gallery.network;


import android.os.Parcel;
import android.os.Parcelable;

class Download implements Parcelable {

    Download() {

    }

    private int progress;
    private int currentFileSize;
    private int totalFileSize;

    int getProgress() {
        return progress;
    }

    void setProgress(int progress) {
        this.progress = progress;
    }

    int getCurrentFileSize() {
        return currentFileSize;
    }

    void setCurrentFileSize(int currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    void setTotalFileSize(int totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(progress);
        parcel.writeInt(currentFileSize);
        parcel.writeInt(totalFileSize);
    }

    private Download(Parcel in) {

        progress = in.readInt();
        currentFileSize = in.readInt();
        totalFileSize = in.readInt();
    }

    public static final Parcelable.Creator<Download> CREATOR = new Parcelable.Creator<Download>() {
        public Download createFromParcel(Parcel in) {
            return new Download(in);
        }

        public Download[] newArray(int size) {
            return new Download[size];
        }
    };
}
