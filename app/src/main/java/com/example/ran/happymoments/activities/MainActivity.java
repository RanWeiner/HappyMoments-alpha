package com.example.ran.happymoments.activities;


import android.app.Activity;
import android.content.Intent;

import android.content.pm.PackageManager;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ran.happymoments.R;
import com.example.ran.happymoments.common.AppConstants;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

import static android.support.constraint.Constraints.TAG;




public class MainActivity extends AppCompatActivity {

    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final int LIMIT = 20;
    private Button mImportBtn , mAlbumBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
    }


    private void initializeViews() {
        mImportBtn = (Button)findViewById(R.id.import_btn);
        mAlbumBtn = (Button)findViewById(R.id.album_btn);
        setListeners();
    }


    public void  setListeners() {

        mImportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importImages();
            }
        });

        mAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAlbumActivity();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }



    public void importImages() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermissionForExternalStorage()) {
                requestStoragePermission();
            } else {
                chooseImagesFromDeviceGallery();
            }
        }else{
            chooseImagesFromDeviceGallery();
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {

            ArrayList<Image> chosenImages = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
            ArrayList<String> chosenImagesPath = getImagesPath(chosenImages);

            goToDetectionActivity(chosenImagesPath);
        }
    }

    private ArrayList<String> getImagesPath(ArrayList<Image> chosenImages) {

        ArrayList<String> rv = new ArrayList<>();

        for (Image image : chosenImages) {
            rv.add(image.path);
        }
        return rv;
    }




    private void goToDetectionActivity(ArrayList<String> photos) {
        Intent intent = new Intent(MainActivity.this, DetectionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(AppConstants.IMPORTED_IMAGES , photos);

        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void chooseImagesFromDeviceGallery() {
        Intent intent = new Intent(MainActivity.this, AlbumSelectActivity.class);
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
    }


    private void goToAlbumActivity() {
        Intent intent = new Intent(MainActivity.this , AlbumActivity.class);
        startActivity(intent);
        finish();
    }




    private BaseLoaderCallback baseLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG , "OpenCv Loaded Successfully!");
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()){
            Log.i(TAG , "OpenCv lib Not found, using manager!");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0 , this , baseLoaderCallback);
        }else {
            Log.i(TAG , "OpenCv Loaded Successfully!");
            baseLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }



    //permissions

    public boolean checkPermissionForExternalStorage() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    public boolean requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                this.requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION);
        }
        return false;
    }
}


