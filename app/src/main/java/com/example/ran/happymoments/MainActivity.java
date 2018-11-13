package com.example.ran.happymoments;


import android.app.Activity;
import android.content.Intent;

import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.ran.happymoments.album.AlbumActivity;
import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.common.Utils;
import com.example.ran.happymoments.detection.DetectionActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

import static android.support.constraint.Constraints.TAG;


public class MainActivity extends AppCompatActivity  {

    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final int LIMIT = 5;
    private Button mAlbumBtn , mImportBtn;

    //test

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAlbumBtn = (Button)findViewById(R.id.album_btn);
        mImportBtn = (Button)findViewById(R.id.import_btn);

        setListeners();
    }

    private void setListeners() {

        mAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAlbumActivity();
            }
        });

        mImportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        Utils utils = new Utils(this);

        final Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.image);
        try {
            utils.saveImageToExternal("image_name.jpg", bm);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //after user choose images
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> chosenImages = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
            ArrayList<String> chosenImagesPath = getImagesPath(chosenImages);

            goToDetectActivity(chosenImages,chosenImagesPath );

//            for (int i = 0; i < images.size(); i++) {
//                Photo p = new Photo(images.get(i).path);

//                Uri uri = Uri.fromFile(new File(images.get(i).path));
//
//                Glide.with(this).load(uri)
//                        .placeholder(R.color.colorAccent)
//                        .override(400, 400)
//                        .crossFade()
//                        .centerCrop()
//                        .into(imageView);
//
//                txImageSelects.setText(txImageSelects.getText().toString().trim()
//                        + "\n" +
//                        String.valueOf(i + 1) + ". " + String.valueOf(uri));
//            }
        }
    }

    private ArrayList<String> getImagesPath(ArrayList<Image> chosenImages) {
        ArrayList<String> paths = new ArrayList<>();

        for (int i = 0 ; i < chosenImages.size() ; i++) {
            paths.add(chosenImages.get(i).path);
        }
        return  paths;
    }


    private void goToDetectActivity(ArrayList<Image> chosenImages , ArrayList<String> chosenImagesPath) {
        Intent intent = new Intent(MainActivity.this, DetectionActivity.class);
        intent.putExtra("chosenImages", chosenImages);
        intent.putExtra("chosenImagesPath", chosenImagesPath);
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_STORAGE_PERMISSION);
                }
            } else {
            }
            return false;
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

}



