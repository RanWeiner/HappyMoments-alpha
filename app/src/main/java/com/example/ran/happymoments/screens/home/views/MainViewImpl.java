package com.example.ran.happymoments.screens.home.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ran.happymoments.R;

public class MainViewImpl implements MainView {

    private final View mRootView;
    private Button mImportBtn , mAlbumBtn;

    private ButtonActionListener mListener;



    public MainViewImpl(LayoutInflater inflater, @Nullable ViewGroup parent){
        mRootView = inflater.inflate(R.layout.activity_main, parent, false);

        initialize();
        setListeners();
    }

    private void initialize() {
        mImportBtn = (Button)getRootView().findViewById(R.id.import_btn);
        mAlbumBtn = (Button)getRootView().findViewById(R.id.album_btn);
    }


    public void  setListeners() {

        mImportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onImportClicked();
                }
            }
        });

        mAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onAlbumClicked();
                }
            }
        });
    }

    @Override
    public void setListener(ButtonActionListener listener) {
        mListener = listener;
    }


    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Context getContext() {
        return getRootView().getContext();
    }

}
