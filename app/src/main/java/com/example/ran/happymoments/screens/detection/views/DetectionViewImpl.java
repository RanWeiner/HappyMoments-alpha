package com.example.ran.happymoments.screens.detection.views;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.example.ran.happymoments.R;
import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.common.Utils;
import com.example.ran.happymoments.generator.series.Photo;

import java.util.ArrayList;
import java.util.List;

public class DetectionViewImpl implements DetectionView  {

    private final View mRootView;

    private Button mDetectBtn;
    private DetectButtonListener mListener;

    private GridViewImageAdapter mAdapter;
    private GridView mGridView;
    private int mColumnWidth;
    private List<String> mImagesPath = new ArrayList<>();

    public DetectionViewImpl(LayoutInflater inflater, @Nullable ViewGroup parent){
        mRootView = inflater.inflate(R.layout.activity_detection, parent, false);

        mDetectBtn = (Button)getRootView().findViewById(R.id.detect_btn);
        mGridView = (GridView) getRootView().findViewById(R.id.grid_view);

        initGridLayout();
        mAdapter = new GridViewImageAdapter(inflater.getContext(), mImagesPath, mColumnWidth);
        mGridView.setAdapter(mAdapter);

        setListeners();
    }


    private void initGridLayout() {
        Utils utils = new Utils(getContext());
        Resources resources = getResources();

        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstants.GRID_PADDING, resources.getDisplayMetrics());

        mColumnWidth = (int) ((utils.getScreenWidth() - ((AppConstants.NUM_OF_COLUMNS + 1) * padding)) / AppConstants.NUM_OF_COLUMNS);

        mGridView.setNumColumns(AppConstants.NUM_OF_COLUMNS);
        mGridView.setColumnWidth(mColumnWidth);
        mGridView.setStretchMode(GridView.NO_STRETCH);
        mGridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
        mGridView.setHorizontalSpacing((int) padding);
        mGridView.setVerticalSpacing((int) padding);
    }


    private void setListeners() {

        mDetectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDetectBtnClicked();
                }
            }
        });
    }


    @Override
    public void bindPhotos(List<Photo> photos) {
        for (Photo photo : photos) {
            mImagesPath.add(photo.getPath());
        }
    }

    @Override
    public void setListener(DetectButtonListener listener) {
        mListener = listener;
    }

    @Override
    public View getRootView() {
        return mRootView;
    }

    @Override
    public Context getContext() {
        return getRootView().getContext();
    }

    @Override
    public Resources getResources(){
        return getRootView().getResources();
    }

}
