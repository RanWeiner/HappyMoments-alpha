package com.example.ran.happymoments.screens.detection;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.detectors.series.Photo;
import com.example.ran.happymoments.R;
import com.example.ran.happymoments.common.Utils;
import com.example.ran.happymoments.detectors.series.SeriesDetector;
import com.example.ran.happymoments.screens.common.GridViewImageAdapter;
import com.example.ran.happymoments.screens.base.MainActivity;

import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.models.Image;

public class DetectionActivity extends AppCompatActivity {

    private ArrayList<Image> images;
    private ArrayList<String> imagesPath;
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

        utils = new Utils(this);
        gridView = (GridView) findViewById(R.id.grid_view_id);
        mDetectBtn = (Button)findViewById(R.id.detect_btn);

        setListeners();
        setGridView();

        imagesPath = (ArrayList<String>) getIntent().getSerializableExtra("imagesPath");

        adapter = new GridViewImageAdapter(DetectionActivity.this, imagePaths, columnWidth);
        gridView.setAdapter(adapter);
    }


    private void setDetector(SeriesDetector seriesDetector) {
        this.mSeriesDetector = seriesDetector;
    }

    private void setListeners() {

        mDetectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeriesDetector.detectSeries();
            }
        });
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
