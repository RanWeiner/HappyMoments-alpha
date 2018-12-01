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
    private GridView gridView;
    private int columnWidth;
    private Button mDetectBtn;
    private FeaturesSeriesGenerator mFeaturesSeriesGenerator;
    private FaceSeriesGenerator mFaceSeriesGenerator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_images);

        utils = new Utils(this);
        gridView = (GridView) findViewById(R.id.grid_view_id);
        mDetectBtn = (Button)findViewById(R.id.detect_btn);

        setListeners();
        setGridView();

        images = (ArrayList<Image>) getIntent().getSerializableExtra("chosenImages");
        imagesPath = (ArrayList<String>) getIntent().getSerializableExtra("chosenImagesPath");
        photos = new ArrayList<>();
        setPhotos(imagesPath);
        mOutputPhotos = new ArrayList<>();

        mFeaturesSeriesGenerator = new FeaturesSeriesGeneratorImpl(photos);
        mFaceSeriesGenerator = new FaceSeriesGeneratorImpl();


        adapter = new GridViewImageAdapter(DetectionActivity.this, imagesPath, columnWidth);
        gridView.setAdapter(adapter);
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
                detectAllSeries();
            }
        });
    }


    private void detectAllSeries() {

        //Features Series Generator
        mPhotoSeriesList = mFeaturesSeriesGenerator.detectSeries();

        //just for debug
        printSerieses();


        //returning series containing only one photo
        ArrayList <Photo> photosToBeRemoved = new ArrayList<>();
        for (int i = 0 ; i < mPhotoSeriesList.size() ; i++) {
            if (mPhotoSeriesList.get(i).getNumOfPhotos() == 1) {
                mOutputPhotos.add(mPhotoSeriesList.get(i).getPhoto(0));
                photosToBeRemoved.add(mPhotoSeriesList.get(i).getPhoto(0));
            }
        }
        mPhotoSeriesList.removeAll(photosToBeRemoved);


        //extracting faces in each photo
        List <Face> faces;
        for (int i = 0 ; i < mPhotoSeriesList.size() ; i++) {
            for (int j = 0 ; j < mPhotoSeriesList.get(i).getNumOfPhotos() ; j++) {

                faces = mFaceSeriesGenerator.detectFaces(DetectionActivity.this , mPhotoSeriesList.get(i).getPhoto(j).getPath());
                if (!faces.isEmpty()) {
                    mPhotoSeriesList.get(i).getPhoto(j).setFaces(faces);
                    faces.clear();
                }
            }
        }
        //now each photo contains array of faces


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
        gridView.setNumColumns(AppConstants.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);

    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetectionActivity.this , MainActivity.class);
        startActivity(intent);
        finish();
    }


}