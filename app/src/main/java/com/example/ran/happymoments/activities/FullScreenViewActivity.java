package com.example.ran.happymoments.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.ran.happymoments.R;
import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.adapter.FullScreenImageAdapter;

import java.util.ArrayList;


public class FullScreenViewActivity extends Activity implements FullScreenImageAdapter.OnClickListener {


    private ArrayList<String> mPhotosPath;
    private FullScreenImageAdapter adapter;
    private ViewPager viewPager;
    private int mPosition;
    private ImageButton mShareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_view);
        viewPager = (ViewPager) findViewById(R.id.pager);
        mShareBtn = (ImageButton)findViewById(R.id.share_btn);

        Bundle bundle = getIntent().getExtras();
        mPhotosPath = bundle.getStringArrayList(AppConstants.PHOTOS_PATH);
        mPosition = bundle.getInt(AppConstants.POSITION);
        adapter = new FullScreenImageAdapter(FullScreenViewActivity.this, mPhotosPath , this) ;
//            @Override
//            public void onCloseBtnClick() {
//                goToDetectionActivity();
//            }
//        });

        viewPager.setAdapter(adapter);

        viewPager.setCurrentItem(mPosition);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void goToResultActivity() {
        Intent intent = new Intent(FullScreenViewActivity.this, ResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(AppConstants.OUTPUT_PHOTOS , mPhotosPath);

        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }



    @Override
    public void onCloseBtnClick() {
        finish();
    }

}
