package com.example.gross.gallery.adapters;


import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gross.gallery.R;
import com.example.gross.gallery.model.ImageData;
import com.example.gross.gallery.ui.DetailActivity;
import com.squareup.picasso.Picasso;

import static com.example.gross.gallery.Consts.CURRENT_POSITION;
import static com.example.gross.gallery.Consts.REQUEST_CODE_CURRENT_POSITION;


public class MediaAdapter extends RecyclerView.Adapter<MediaAdapter.ViewHolder> {

    private final Activity activity;

    public MediaAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_media, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        Picasso.with(activity)
                .load(ImageData.imageDataList.get(position).getUri())
                .resize(200, 200)
                .into(holder.mediaImageView);

        holder.mediaImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startDetailActivityIntent = new Intent(activity, DetailActivity.class);
                startDetailActivityIntent.putExtra(CURRENT_POSITION, pos);

                activity.startActivityForResult(startDetailActivityIntent, REQUEST_CODE_CURRENT_POSITION);
                activity.overridePendingTransition(R.anim.right_in, R.anim.left_out);
            }
        });
    }


    @Override
    public int getItemCount() {
        return ImageData.imageDataList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mediaImageView;

        ViewHolder(View itemView) {
            super(itemView);
            mediaImageView = itemView.findViewById(R.id.mediaImageView);
        }
    }
}
