package com.example.gross.gallery.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gross.gallery.R;
import com.example.gross.gallery.model.ImageData;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import static com.example.gross.gallery.Consts.CURRENT_POSITION;


public class DetailFragment extends Fragment implements View.OnClickListener {

    private ImageData imageData;

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
        imageData = ImageData.imageDataList.get(currentPosition);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);
        PhotoView imageView = view.findViewById(R.id.imageViewDetail);
        imageView.setOnClickListener(this);
        Picasso.with(getActivity()).load(imageData.getUri()).into(imageView);

        return view;
    }

    @Override
    public void onClick(View view) {
        ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (bar != null) {
            if (bar.isShowing())
                bar.hide();
            else
                bar.show();
        }
    }
}
