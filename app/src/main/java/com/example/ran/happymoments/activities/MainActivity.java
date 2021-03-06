package com.example.ran.happymoments.activities;


import android.content.ClipData;
import android.content.Intent;

import android.content.pm.PackageManager;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.ran.happymoments.R;
import com.example.ran.happymoments.common.AppConstants;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

import static android.support.constraint.Constraints.TAG;




public class MainActivity extends AppCompatActivity {


//    public static final int PICK_IMAGE_MULT = 3;
//    public String imageEncoded;
//    List<String> imagesEncodedList;



   // private static final int READ_STORAGE_PERMISSION = 4000;

    private Button mImportBtn , mAlbumBtn , mCameraBtn;

    ArrayList<String> chosenImagesPath = new ArrayList<>();

    //try camera
    private String pathToFile;
    public int numPicturesTaken = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();

        checkAndRequestPermissions();

    }

    private void checkAndRequestPermissions() {
        if(Build.VERSION.SDK_INT >= 23) {
            checkAndRequestCameraPermission();
            checkAndRequestStoragePermission();
        }
    }



    private void initializeViews() {
        mImportBtn = (Button)findViewById(R.id.import_btn);
        mAlbumBtn = (Button)findViewById(R.id.album_btn);
        mCameraBtn = (Button) findViewById(R.id.camera_btn);
        setListeners();
    }


    public void  setListeners() {

        mImportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                importImages();
//                chooseImagesFromDeviceGallery_2();
                chooseImagesFromDeviceGallery();
            }
        });

        mAlbumBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAlbumActivity();
            }
        });

        mCameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCamera();
            }
        });
    }



    private void goToCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createPhotoFile();

            if (photoFile != null) {
                pathToFile = photoFile.getAbsolutePath();
                Uri photoURI = FileProvider.getUriForFile(MainActivity.this , "com.example.ran.happymoments.fileprovider",photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent , AppConstants.CAMERA_REQUEST_CODE);
            }
        }
//        startActivityForResult(intent,AppConstants.CAMERA_REQUEST_CODE);
    }

    private File createPhotoFile() {
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = null;

        try {
            image = File.createTempFile(name , ".jpg" , storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }



//    private void checkNewShit(int requestCode, int resultCode, Intent data) {
//
//        try { // When an Image is picked
//            if (requestCode == PICK_IMAGE_MULT && resultCode == RESULT_OK && null != data) {
//                // Get the Image from data
//                String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                imagesEncodedList = new ArrayList<String>();
//                if(data.getData()!=null){
//                    Uri mImageUri=data.getData();
//                    // Get the cursor
//                    Cursor cursor = getContentResolver().query(mImageUri, filePathColumn, null, null, null); // Move to first ro
//                    cursor.moveToFirst();
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                    imageEncoded = cursor.getString(columnIndex);
//                    cursor.close();
//                } else {
//                    if (data.getClipData() != null) {
//                        ClipData mClipData = data.getClipData();
//                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
//                        for (int i = 0; i < mClipData.getItemCount(); i++) {
//                            ClipData.Item item = mClipData.getItemAt(i);
//                            Uri uri = item.getUri(); mArrayUri.add(uri); // Get the cursor
//                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null); // Move to first row
//                            cursor.moveToFirst(); int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                            imageEncoded = cursor.getString(columnIndex);
//                            imagesEncodedList.add(imageEncoded);
//                            cursor.close();
//                        }
//                     Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
//                    }
//                }
//        } else {
//                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
//            }
//    } catch (Exception e) {
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG) .show();
//    }
//    }
//    public void importImages() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (!checkPermissionForExternalStorage()) {
//                checkAndRequestStoragePermission();
//            } else {
//                chooseImagesFromDeviceGallery();
//            }
//        }else{
//            chooseImagesFromDeviceGallery();
//        }
//    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


//        checkNewShit(requestCode , resultCode , data);


        if (resultCode != RESULT_OK ) {
            if (numPicturesTaken > 0) {
                goToDetectionActivity(chosenImagesPath);
            } else {
                return;
            }
        }

        else if (requestCode == ConstantsCustomGallery.REQUEST_CODE && data != null) {

                    ArrayList<Image> chosenImages = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
                    chosenImagesPath = getImagesPath(chosenImages);

                    goToDetectionActivity(chosenImagesPath);


                } else if (requestCode == AppConstants.CAMERA_REQUEST_CODE ) {
                    if(numPicturesTaken < 10) {
//                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(cameraIntent, AppConstants.CAMERA_REQUEST_CODE);
                        numPicturesTaken++;
                        chosenImagesPath.add(pathToFile);
                        goToCamera();

                    } else {
                        Toast.makeText(MainActivity.this , "You can take 10 pictures max each time",Toast.LENGTH_SHORT).show();
                    }
                    Log.i("counter" , "count = " + numPicturesTaken);
                }
//        super.onActivityResult(requestCode, resultCode, data);
    }

    private ArrayList<String> getImagesPath(ArrayList<Image> chosenImages) {

        ArrayList<String> rv = new ArrayList<>();

        for (Image image : chosenImages) {
            rv.add(image.path);
        }
        return rv;
    }




    private void goToDetectionActivity(ArrayList<String> photos) {
        Intent intent = new Intent(MainActivity.this, DetectionActivity_take2.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(AppConstants.IMPORTED_IMAGES , photos);

        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void chooseImagesFromDeviceGallery() {
        Intent intent = new Intent(MainActivity.this, AlbumSelectActivity.class);
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, AppConstants.NUM_IMAGE_CHOSEN_LIMIT);
        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
    }

//    private void chooseImagesFromDeviceGallery_2() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE , true);
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent , "Select Pictures"),PICK_IMAGE_MULT);
//    }

    private void goToAlbumActivity() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    public boolean checkAndRequestStoragePermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                this.requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstants.READ_STORAGE_PERMISSION);
        }
        return false;
    }

    public boolean checkAndRequestCameraPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                this.requestPermissions(new String[]{android.Manifest.permission.CAMERA}, AppConstants.CAMERA_PERMISSION);
        }
        return false;
    }
}



