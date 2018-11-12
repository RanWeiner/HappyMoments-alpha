package com.example.ran.happymoments.screens.base;


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
import com.example.ran.happymoments.screens.album.AlbumActivity;
import com.example.ran.happymoments.screens.detection.DetectionActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

import static android.support.constraint.Constraints.TAG;


public class MainActivity extends AppCompatActivity  {

    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final int LIMIT = 5;


    private Button mAlbumBtn , mImportBtn;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAlbumBtn = (Button)findViewById(R.id.album_btn);
        mImportBtn = (Button)findViewById(R.id.import_btn);

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
                        goToGalleryAlbum();
                    }
                }else{
                    goToGalleryAlbum();
                }
            }
        });
//        txImageSelects = (TextView) findViewById(R.id.txImageSelects);
//        imageView = (ImageView) findViewById(R.id.imageView);
//
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= 23) {
////                    if (!checkPermissionForExternalStorage()) {
////                        requestStoragePermission();
////                    } else {
////                        // opening custom gallery
////                        goToGalleryAlbum();
////                    }
////                }else{
////                    goToGalleryAlbum();
////                }
////            }
////        });
    }


    @Override
    protected void onStart() {
        super.onStart();
//        Utils utils = new Utils(this);
//        utils.createAlbumInGallery();
    }



    private void goToAlbumActivity() {
        Intent intent = new Intent(MainActivity.this , AlbumActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

            goToDetectActivity(images);
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

    private void goToDetectActivity(ArrayList<Image> images) {
        Intent intent = new Intent(MainActivity.this, DetectionActivity.class);
        intent.putExtra("images", images);
        startActivity(intent);
        finish();
    }

    private void goToGalleryAlbum() {
        Intent intent = new Intent(MainActivity.this, AlbumSelectActivity.class);
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
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

}



