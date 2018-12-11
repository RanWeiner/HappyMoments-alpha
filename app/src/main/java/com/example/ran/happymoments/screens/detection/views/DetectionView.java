package com.example.ran.happymoments.screens.detection.views;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import com.example.ran.happymoments.generator.photo.Photo;

import java.util.List;

public interface DetectionView {


    void bindPhotos(List<Photo> photos);


    public interface DetectButtonListener {
        void onDetectBtnClicked();
        }


    void setListener(DetectButtonListener listener);

    View getRootView();

    Context getContext();

    Resources getResources();

    void dismissUserJobInProgress();

    void showUserJobInProgress();

    }
