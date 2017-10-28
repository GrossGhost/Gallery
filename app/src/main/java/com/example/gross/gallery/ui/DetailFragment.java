package com.example.gross.gallery.ui;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gross.gallery.R;
import com.example.gross.gallery.adapters.MediaAdapter;
import com.example.gross.gallery.model.ImageDataObject;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import static com.example.gross.gallery.Consts.CURRENT_POSITION;
import static java.lang.Thread.sleep;


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


        float k = getScaleKoef();
        Picasso.with(getActivity())
                .load(imageData.getUri())
                .resize(Math.round(imageData.getWidth() * 0.1f), Math.round(imageData.getHeith() * 0.1f))
                .into(imageView);
        //Picasso.with(getActivity()).load(imageData.getUri()).resize(imageData.getWidth()/k, imageData.getHeith()/k).into(imageView);
        //imageLoader.displayImage(imageData.getThumb().toString(),imageView);
        //thumbView.setImageURI(imageData.getUri());

        return view;
    }

    private float getScaleKoef() {
        int max = Math.max(imageData.getWidth(), imageData.getHeith());
        return  100 / max;
    }

    @Override
    public void onResume() {
        new LoadFullImage().execute();
        super.onResume();


    }

    private class LoadFullImage extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                sleep(800);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Picasso.with(getActivity()).load(imageData.getUri()).into(imageView);
        }
    }
}
