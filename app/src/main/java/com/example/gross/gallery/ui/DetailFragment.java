package com.example.gross.gallery.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gross.gallery.R;
import com.example.gross.gallery.adapters.MediaAdapter;
import com.example.gross.gallery.model.ImageDataObject;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import static com.example.gross.gallery.Consts.CURRENT_POSITION;


public class DetailFragment extends Fragment {

    private PhotoView imageView;
    private ImageDataObject imageData;

    public static DetailFragment newInstance(final int content) {
        DetailFragment fragment = new DetailFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(CURRENT_POSITION, content);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int currentPosition = getArguments().getInt(CURRENT_POSITION);
        imageData = MediaAdapter.imageDataList.get(currentPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);
        imageView = view.findViewById(R.id.imageViewDetail);

        Picasso.with(getActivity()).load(imageData.getUri()).into(imageView);

        return view;
    }

}
