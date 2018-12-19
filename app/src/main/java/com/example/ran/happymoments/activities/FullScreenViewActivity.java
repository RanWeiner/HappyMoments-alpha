package com.example.ran.happymoments.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.ran.happymoments.R;
import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.adapter.FullScreenImageAdapter;

import java.util.ArrayList;


public class FullScreenViewActivity extends Activity {


    private ArrayList<String> mPhotosPath;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    private int mPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);
        viewPager = (ViewPager) findViewById(R.id.pager);

        Bundle bundle = getIntent().getExtras();
        mPhotosPath = bundle.getStringArrayList(AppConstants.PHOTOS_PATH);
        mPosition = bundle.getInt(AppConstants.POSITION);



        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, mPhotosPath, new FullScreenImageAdapter.OnClickListener() {
            @Override
            public void onCloseBtnClick() {
                goToDetectionActivity();
            }
        });

        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(mPosition);
    }

    @Override
    public void onBackPressed() {
        goToDetectionActivity();
    }

    private void goToDetectionActivity() {
        Intent intent = new Intent(FullScreenViewActivity.this, DetectionActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(AppConstants.IMPORTED_IMAGES , mPhotosPath);

        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }
}
