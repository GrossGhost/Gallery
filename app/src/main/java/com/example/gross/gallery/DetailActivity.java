package com.example.gross.gallery;


import android.app.Fragment;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    private int cursorPosition;
    private Fragment prevFragment, nextFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        cursorPosition = getIntent().getIntExtra("position", 0);


        final FragmentManager fm = getFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
        final DetailFragment thisFragment = DetailFragment.newInstance(cursorPosition);
        ft.replace(R.id.fragmentContainer, thisFragment);
        ft.commit();


    }
}
