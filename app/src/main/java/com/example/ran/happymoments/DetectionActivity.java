package com.example.ran.happymoments;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import in.myinnos.awesomeimagepicker.models.Image;

public class DetectionActivity extends AppCompatActivity {

    private ArrayList<Image> images;
    private ArrayList<Photo> photos;
    private Utils utils;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    private Button mDetectBtn;
    private SeriesDetector mSeriesDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_images);

        mDetectBtn = (Button)findViewById(R.id.detect_btn);
        mDetectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findBestMoments();
            }
        });

        images = (ArrayList<Image>) getIntent().getSerializableExtra("images");
        photos = new ArrayList<>();

        for (int i = 0 ; i < images.size() ; i++){
            imagePaths.add(images.get(i).path);
            photos.add(new Photo(images.get(i).path));
        }

        mSeriesDetector = new SeriesDetector(photos);

        gridView = (GridView) findViewById(R.id.grid_view_id);
        utils = new Utils(this);

        setGridView();
        adapter = new GridViewImageAdapter(DetectionActivity.this, imagePaths, columnWidth);
        gridView.setAdapter(adapter);

    }

    private void findBestMoments() {
        mSeriesDetector.detect();
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
