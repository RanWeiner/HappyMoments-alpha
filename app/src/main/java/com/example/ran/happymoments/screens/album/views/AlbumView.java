package com.example.ran.happymoments.screens.album.views;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ran.happymoments.R;

public class AlbumView {

    private final View mRootView;



    public AlbumView(LayoutInflater inflater, @Nullable ViewGroup parent){
        mRootView = inflater.inflate(R.layout.activity_detection, parent, false);

    }
}
