package com.example.ran.happymoments.detection;


import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.detection.face.Face;
import com.example.ran.happymoments.detection.face.FaceSeriesGenerator;
import com.example.ran.happymoments.detection.face.FaceSeriesGeneratorImpl;
import com.example.ran.happymoments.detection.series.Photo;
import com.example.ran.happymoments.R;
import com.example.ran.happymoments.common.Utils;
import com.example.ran.happymoments.detection.series.PhotoSeries;
import com.example.ran.happymoments.detection.series.FeaturesSeriesGenerator;
import com.example.ran.happymoments.detection.series.FeaturesSeriesGeneratorImpl;
import com.example.ran.happymoments.common.GridViewImageAdapter;
import com.example.ran.happymoments.MainActivity;

import java.util.ArrayList;
import java.util.List;

import in.myinnos.awesomeimagepicker.models.Image;

import static android.support.constraint.Constraints.TAG;

public class DetectionActivity extends AppCompatActivity {

    private ArrayList<Image> images;
    private ArrayList<String> imagesPath;
    private ArrayList<Photo> photos;
    List<PhotoSeries> mPhotoSeriesList;
    private List<Photo> mOutputPhotos;

    private Utils utils;
    private GridViewImageAdapter adapter;
    private GridView mGridView;
    private int columnWidth;
    private Button mDetectBtn;
    private FeaturesSeriesGenerator mFeaturesSeriesGenerator;
    private FaceSeriesGenerator mFaceSeriesGenerator;
    private ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_images);

        utils = new Utils(this);
        mGridView = (GridView) findViewById(R.id.grid_view_id);
        mDetectBtn = (Button)findViewById(R.id.detect_btn);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        setListeners();
        setGridView();

        images = (ArrayList<Image>) getIntent().getSerializableExtra("chosenImages"); //Change to CONST
        imagesPath = (ArrayList<String>) getIntent().getSerializableExtra("chosenImagesPath"); //Change to CONST
        photos = new ArrayList<>();
        setPhotos(imagesPath);
        mOutputPhotos = new ArrayList<>();

        mFeaturesSeriesGenerator = new FeaturesSeriesGeneratorImpl(photos);
        mFaceSeriesGenerator = new FaceSeriesGeneratorImpl();


//        adapter = new GridViewImageAdapter(DetectionActivity.this, imagesPath, columnWidth);
//        mGridView.setAdapter(adapter);

    }

    private void setPhotos(ArrayList<String> imagesPath) {
        for (int i = 0 ;i < imagesPath.size() ; i++){
            this.photos.add(new Photo(imagesPath.get(i)));
        }
    }


    private void setListeners() {
        mDetectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDetectBtn.setEnabled(false);
                mProgressBar.setVisibility(View.VISIBLE);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        detectSeries();
                        excludeSinglePhotoSeries();
                        detectFaces();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                mDetectBtn.setEnabled(true);
                            }
                        });
                    }
                });

                t.start();


            }
        });
    }


    public void detectSeries() {

        //Features Series Generator
        mPhotoSeriesList = mFeaturesSeriesGenerator.detectSeries();

        //just for debug
        printSerieses();

    }

    public void excludeSinglePhotoSeries(){
        //returning series containing only one photo
        ArrayList <PhotoSeries> photosToBeRemoved = new ArrayList<>();
        for (int i = 0 ; i < mPhotoSeriesList.size() ; i++) {
            if (mPhotoSeriesList.get(i).getNumOfPhotos() == 1) {
                mOutputPhotos.add(mPhotoSeriesList.get(i).getPhoto(0));
                photosToBeRemoved.add(mPhotoSeriesList.get(i));
            }
        }
        mPhotoSeriesList.removeAll(photosToBeRemoved);
    }

    public void detectFaces(){
        //extracting faces in each photo
        List <Face> faces;
        for (int i = 0 ; i < mPhotoSeriesList.size() ; i++) {
            for (int j = 0 ; j < mPhotoSeriesList.get(i).getPhotos().size() ; j++) {

                faces = mFaceSeriesGenerator.detectFaces(getApplicationContext(), mPhotoSeriesList.get(i).getPhoto(j).getPath());
                Log.d(TAG, "detectFaces() found " + faces.size() + " faces");
                if (!faces.isEmpty()) {
                    mPhotoSeriesList.get(i).getPhoto(j).setFaces(faces);
                }
            }
        }

        printFaces();
        //now each photo contains array of faces
    }

    public void printFaces() {
        for (PhotoSeries series : mPhotoSeriesList) {
            Log.i(TAG ,"Series "+ series.getId());
            for (Photo photo : series.getPhotos()) {
                Log.i(TAG ,"Face found: " + photo.getFaces().size());

                for (Face face : photo.getFaces()){
                    Log.i(TAG ,"Face: is Smiling = " + face.isSmiling() + " are  Eyes open = " + face.areEyesOpen());

                }
            }

        }
    }


    public void printSerieses() {
        //just for debug
        for (PhotoSeries series : mPhotoSeriesList) {
            Log.i(TAG ,"Series "+ series.getId());
            for (Photo photo : series.getPhotos()) {
                Log.i(TAG ,"Series "+ series.getId() + "photo= "+ photo.getPath());
            }
        }
    }

    private void setGridView() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstants.GRID_PADDING, r.getDisplayMetrics());

        columnWidth = (int) ((utils.getScreenWidth() - ((AppConstants.NUM_OF_COLUMNS + 1) * padding)) / AppConstants.NUM_OF_COLUMNS);
        mGridView.setNumColumns(AppConstants.NUM_OF_COLUMNS);
        mGridView.setColumnWidth(columnWidth);
        mGridView.setStretchMode(GridView.NO_STRETCH);
        mGridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        mGridView.setHorizontalSpacing((int) padding);
        mGridView.setVerticalSpacing((int) padding);

    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetectionActivity.this , MainActivity.class);
        startActivity(intent);
        finish();
    }


}