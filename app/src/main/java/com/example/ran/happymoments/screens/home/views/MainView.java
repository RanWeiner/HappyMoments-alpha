package com.example.ran.happymoments.screens.home.views;

import android.content.Context;
import android.view.View;

public interface MainView {


    interface ButtonActionListener {
        void onImportClicked();
        void onAlbumClicked();
    }

    void setListener(ButtonActionListener listener);

    View getRootView();

    Context getContext();
}
