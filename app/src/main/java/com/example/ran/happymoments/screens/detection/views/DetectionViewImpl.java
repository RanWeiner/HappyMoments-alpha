package com.example.ran.happymoments.screens.detection.views;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.ran.happymoments.R;
import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.common.Utils;
import com.example.ran.happymoments.generator.photo.Photo;

import java.util.ArrayList;
import java.util.List;

public class DetectionViewImpl implements DetectionView  {

    private final View mRootView;

    private Button mDetectBtn;
    private DetectButtonListener mListener;

    private ProgressBar mProgressBar;

//    private GridViewImageAdapter mAdapter;
//    private GridView mGridView;

    private RecycleViewImageAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private int mColumnWidth;
    private List<String> mImagesPath = new ArrayList<>();
    private List<Photo> mPhotos = new ArrayList<>();

    public DetectionViewImpl(LayoutInflater inflater, @Nullable ViewGroup parent){
        mRootView = inflater.inflate(R.layout.activity_detection, parent, false);

        mDetectBtn = (Button)getRootView().findViewById(R.id.detect_btn);
        mRecyclerView = (RecyclerView)getRootView().findViewById(R.id.recycler_view);
        mProgressBar = (ProgressBar)getRootView().findViewById(R.id.progress_bar);
//        mGridView = (GridView) getRootView().findViewById(R.id.grid_view);

        initRecyclerView();

//        initGridLayout();
//        mAdapter = new RecycleViewImageAdapter(mImagesPath , mColumnWidth);
//        mGridView.setAdapter(mAdapter);

        setListeners();
    }


//    private void initGridLayout() {
//        Utils utils = new Utils(getContext());
//        Resources resources = getResources();
//
//        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, AppConstants.GRID_PADDING, resources.getDisplayMetrics());
//
//        mColumnWidth = (int) ((utils.getScreenWidth() - ((AppConstants.NUM_OF_COLUMNS + 1) * padding)) / AppConstants.NUM_OF_COLUMNS);
//
//        mGridView.setNumColumns(AppConstants.NUM_OF_COLUMNS);
//        mGridView.setColumnWidth(mColumnWidth);
//        mGridView.setStretchMode(GridView.NO_STRETCH);
//        mGridView.setPadding((int) padding, (int) padding, (int) padding, (int) padding);
//        mGridView.setHorizontalSpacing((int) padding);
//        mGridView.setVerticalSpacing((int) padding);
//    }

    public void initRecyclerView() {
        setColumnWidth();
        mAdapter = new RecycleViewImageAdapter(mPhotos,mColumnWidth);
        mLayoutManager = new GridLayoutManager(getContext(), AppConstants.NUM_OF_COLUMNS);//Change to CONST
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();
    }

    public void setColumnWidth() {
        Utils utils = new Utils(getContext());
        mColumnWidth = (int) ((utils.getScreenWidth() - ((AppConstants.NUM_OF_COLUMNS + 1) )) / AppConstants.NUM_OF_COLUMNS);
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
        mPhotos.addAll(photos);
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

    @Override
    public void dismissUserJobInProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mDetectBtn.setEnabled(true);
    }

    @Override
    public void showUserJobInProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
        mDetectBtn.setEnabled(false);
    }


}
