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
import java.util.List;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

import static android.support.constraint.Constraints.TAG;


public class MainActivity extends AppCompatActivity  {

    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final int LIMIT = 5;


    private Button mAlbumBtn , mImportBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAlbumBtn = (Button)findViewById(R.id.album_btn);
        mImportBtn = (Button)findViewById(R.id.import_btn);

        setListeners();


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
////                        chooseImagesFromDeviceGallery();
////                    }
////                }else{
////                    chooseImagesFromDeviceGallery();
////                }
////            }
////        });
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
//        Utils utils = new Utils(this);
//        utils.createAlbumInGallery();
    }




//after user choose images
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
            ArrayList<String> imagesPath = getImagesPath(images);
            goToDetectActivity(imagesPath);

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

    private ArrayList<String> getImagesPath(ArrayList<Image> images) {
        ArrayList<String> imagesPath = new ArrayList<>();

        for (Image image: images) {
            imagesPath.add(image.path);
        }
        return imagesPath;
    }

    private void goToDetectActivity(ArrayList<String> imagesPath) {
        Intent intent = new Intent(MainActivity.this, DetectionActivity.class);
        intent.putExtra("imagesPath", imagesPath);
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



