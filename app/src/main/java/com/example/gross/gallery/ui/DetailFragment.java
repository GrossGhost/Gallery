package com.example.gross.gallery.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gross.gallery.R;
import com.example.gross.gallery.SmartImageView;
import com.example.gross.gallery.adapters.MediaAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;



public class DetailFragment extends Fragment {

    private Uri imageUri;
    private ImageLoader imageLoader;

    public static DetailFragment newInstance(final int content) {
        DetailFragment fragment = new DetailFragment();
        Bundle arguments = new Bundle();
        arguments.putInt("currentPosition", content);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageLoader = ImageLoader.getInstance();

        int currentPosition = getArguments().getInt("currentPosition");
        imageUri = MediaAdapter.uriList.get(currentPosition);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);
        SmartImageView smartImageView = view.findViewById(R.id.imageViewDetail);


        imageLoader.displayImage(imageUri.toString(), smartImageView);

        return view;
    }

}
