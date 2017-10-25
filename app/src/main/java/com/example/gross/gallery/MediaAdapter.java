package com.example.gross.gallery;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {

    private Cursor mediaCursor;
    private final Activity activity;
    static List<Uri> uriList = new ArrayList<>();


    MediaAdapter(Activity activity) {
        this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_media, parent, false);
        getUriListFromMediaStore();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Glide.with(activity).load(uriList.get(position)).override(200,200).into(holder.mediaImageView);

        holder.mediaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startDetailActivityIntent = new Intent(activity, DetailActivity.class);
                startDetailActivityIntent.putExtra("position", position);

                activity.startActivity(startDetailActivityIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return (mediaCursor == null) ? 0 : mediaCursor.getCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mediaImageView;

        ViewHolder(View itemView) {
            super(itemView);

            mediaImageView = itemView.findViewById(R.id.mediaImageView);
        }

    }

    private Cursor swapCursor(Cursor cursor){
        if (mediaCursor == cursor){
            return null;
        }
        Cursor oldCursor = mediaCursor;
        this.mediaCursor = cursor;
        if (cursor != null){
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    void changeCursor(Cursor cursor){
        Cursor oldCursor = swapCursor(cursor);
        if (oldCursor != null){
            oldCursor.close();
        }
    }

    private void getUriListFromMediaStore(){
        int dataIndex = mediaCursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        mediaCursor.moveToPosition(0);
        while (mediaCursor.moveToNext()){
            String dataString = mediaCursor.getString(dataIndex);
            Uri uri = Uri.parse("file://" + dataString);
            uriList.add(uri);
        }

    }

}
