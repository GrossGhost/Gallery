package com.example.gross.gallery;


import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.IOException;

public class DetailFragment extends Fragment {

    private static final String BUNDLE_CONTENT = "bundle_content";
    private static final double MAX_SIZE = 4000.0;
    private ImageView imageView;
    private Bitmap imageBitmap;

    public static DetailFragment newInstance(final int content) {
        final DetailFragment fragment = new DetailFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt(BUNDLE_CONTENT, content);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(BUNDLE_CONTENT)) {

            Uri imageUri = MediaAdapter.uriList.get(getArguments().getInt(BUNDLE_CONTENT));
            try {

                imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException("Must be created through newInstance(...)");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, null);
        imageView = view.findViewById(R.id.imageViewDetail);

        prepareBitmapImage();

        imageView.setImageBitmap(imageBitmap);

        return view;

    }

    private void prepareBitmapImage() {

        if (imageBitmap.getWidth() > MAX_SIZE){
            double k = imageBitmap.getWidth()/MAX_SIZE;
            imageBitmap = getResizedBitmap(imageBitmap,
                    (int) (imageBitmap.getWidth()/k),
                    (int) (imageBitmap.getHeight()/k) );
        }

        if (imageBitmap.getHeight() > MAX_SIZE){
            double k = imageBitmap.getHeight()/MAX_SIZE;
            Log.d("prepareBitmap",  k + "  ");
            imageBitmap = getResizedBitmap(imageBitmap,
                    (int) (imageBitmap.getWidth()/k),
                    (int) (imageBitmap.getHeight()/k) );
            Log.d("prepareBitmap", (int) (imageBitmap.getHeight()/k) + "  " + imageBitmap.getHeight()/k );
        }
        Log.d("prepareBitmap", imageBitmap.getWidth() + "  " + imageBitmap.getHeight() );
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        //bm.recycle();
        return resizedBitmap;
    }

}
