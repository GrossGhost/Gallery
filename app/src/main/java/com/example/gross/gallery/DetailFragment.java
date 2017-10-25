package com.example.gross.gallery;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;


public class DetailFragment extends Fragment {

    private ImageView imageView;
    private int currentPosition;
    private Uri imageUri;

    static DetailFragment newInstance(final int content) {
        DetailFragment fragment = new DetailFragment();
        Bundle arguments = new Bundle();
        arguments.putInt("currentPosition", content);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentPosition = getArguments().getInt("currentPosition");
        imageUri = MediaAdapter.uriList.get(currentPosition);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);
        imageView = view.findViewById(R.id.imageViewDetail);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(imageUri.toString(),imageView);

        return view;
    }

}
