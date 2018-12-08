package com.example.ran.happymoments.screens.detection.controllers;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.generator.SeriesGenerator;
import com.example.ran.happymoments.generator.photo.Photo;
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

    List<Photo> photos;

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
//        disableUserInteraction();

        Toast.makeText(DetectionActivity.this , "started...",Toast.LENGTH_SHORT).show();

        mResults = mSeriesGenerator.detect();

//        Thread t = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                //tell view that started - disable button if not working and start spinner
//                mResults = mSeriesGenerator.detect();
//                Log.i("Start" , "STARTED DETECTNG");
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
////                        enableUserInteraction();
//                        Toast.makeText(DetectionActivity.this , "Finished!",Toast.LENGTH_SHORT).show();
//                        Log.i("Finish" , "Finish DETECTNG");
//                        //tell view finished - enable button and stop spinner
//                    }
//                });
//            }
//        });
//        t.start();


        Toast.makeText(DetectionActivity.this , "finished!",Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetectionActivity.this , MainActivity.class);
        startActivity(intent);
        finish();
    }


}