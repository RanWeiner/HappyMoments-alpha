package com.example.ran.happymoments.screens.detection.controllers;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;

import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.generator.SeriesGenerator;
import com.example.ran.happymoments.generator.series.Photo;
import com.example.ran.happymoments.screens.detection.views.DetectionView;
import com.example.ran.happymoments.screens.detection.views.DetectionViewImpl;
import com.example.ran.happymoments.screens.home.controllers.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class DetectionActivity extends AppCompatActivity implements DetectionView.DetectButtonListener {


    private DetectionView mView;

    private SeriesGenerator mSeriesGenerator;
    private List<String> mInput;
    private List<String> mResults;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mInput = (List<String>) getIntent().getSerializableExtra(AppConstants.IMPORTED_IMAGES_PATH);
        mSeriesGenerator = new SeriesGenerator(getApplicationContext() , mInput);

        mResults = new ArrayList<>();

        mView = new DetectionViewImpl(LayoutInflater.from(this), null);
        mView.setListener(this);
        setContentView(mView.getRootView());
    }


    @Override
    protected void onStart() {
        super.onStart();
        bindPhotos(mInput);
    }


    private void disableUserInteraction() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    private void enableUserInteraction() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    private void bindPhotos(List<String> imagesPath) {
        List<Photo> photos = new ArrayList<>(imagesPath.size());

        for (String path : imagesPath) {
            photos.add(new Photo(path));
        }
        mView.bindPhotos(photos);
    }

    @Override
    public void onDetectBtnClicked() {
        disableUserInteraction();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //tell view that started - disable button if not working and start spinner
                mResults = mSeriesGenerator.detect();
                Log.i("Start" , "STARTED DETECTNG");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enableUserInteraction();
                        Log.i("Finish" , "Finish DETECTNG");
                        //tell view finished - enable button and stop spinner
                    }
                });
            }
        });
        t.start();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetectionActivity.this , MainActivity.class);
        startActivity(intent);
        finish();
    }


    //    private void detectAllSeries() {
//
//        //Features Series Generator
//        mPhotoSeriesList = mFeaturesSeriesGenerator.detectSeries();
//
//        //just for debug
//        printSerieses();
//
//
//        //returning series containing only one photo
//        ArrayList <PhotoSeries> photosToBeRemoved = new ArrayList<>();
//        for (int i = 0 ; i < mPhotoSeriesList.size() ; i++) {
//            if (mPhotoSeriesList.get(i).getNumOfPhotos() == 1) {
//                mOutputPhotos.add(mPhotoSeriesList.get(i).getPhoto(0));
//                photosToBeRemoved.add(mPhotoSeriesList.get(i));
//            }
//        }
//        mPhotoSeriesList.removeAll(photosToBeRemoved);
//
//
//        //extracting faces in each photo
//        List <Face> faces;
//        for (int i = 0 ; i < mPhotoSeriesList.size() ; i++) {
//            for (int j = 0 ; j < mPhotoSeriesList.get(i).getPhotos().size() ; j++) {
//
//                faces = mFaceSeriesGenerator.detectFaces(getApplicationContext(), mPhotoSeriesList.get(i).getPhoto(j).getPath());
//                Log.d(TAG, "detectFaces() found " + faces.size() + " faces");
//                if (!faces.isEmpty()) {
//                    mPhotoSeriesList.get(i).getPhoto(j).setFaces(faces);
//                    //faces.clear();
//                }
//            }
//        }
//
//        printFaces();
//        //now each photo contains array of faces
//
//
//    }
//
//    public void printFaces() {
//        for (PhotoSeries series : mPhotoSeriesList) {
//            Log.i(TAG ,"Series "+ series.getId());
//            for (Photo photo : series.getPhotos()) {
//                Log.i(TAG ,"Face found: " + photo.getFaces().size());
//
//                for (Face face : photo.getFaces()){
//                    Log.i(TAG ,"Face: Smiling Prob = " + face.isSmiling() + " Eyes open Prob = " + face.areEyesOpen());
//
//                }
//            }
//
//        }
//    }
//
//
//    public void printSerieses() {
//        //just for debug
//        for (PhotoSeries series : mPhotoSeriesList) {
//            Log.i(TAG ,"Series "+ series.getId());
//            for (Photo photo : series.getPhotos()) {
//                Log.i(TAG ,"Series "+ series.getId() + "photo= "+ photo.getPath());
//            }
//        }
//    }
//
//
//
//
//
//
//    @Override
//    public void onButtonClicked(final int id) {
//        mView.setEnable(id , false);
//        mView.showProgressBar();
//
//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                detectAllSeries();
//
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mView.hideProgressBar();
//                        mView.setEnable(id , true);
//                    }
//                });
//            }
//        });
//
//        t.start();
//    }

}