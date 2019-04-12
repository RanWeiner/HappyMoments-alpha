package com.example.ran.happymoments.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.ran.happymoments.R;
import com.example.ran.happymoments.adapter.SlidingImagesAdapter;
import com.example.ran.happymoments.common.AppConstants;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

public class ResultActivity_take2 extends AppCompatActivity {

    private ArrayList<String> mResultsPhotosPath;
    private ViewPager mPager;
    private FloatingActionButton mShareBtn;
    private FloatingActionButton mSaveBtn;
    private int currentPage=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_take2);

        Bundle bundle = getIntent().getExtras();
        mResultsPhotosPath = bundle.getStringArrayList(AppConstants.OUTPUT_PHOTOS);
        init();
    }

    private void init() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mSaveBtn = (FloatingActionButton)findViewById(R.id.save_btn);
        mShareBtn = (FloatingActionButton)findViewById(R.id.share_btn);
        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);

        //Set circle indicator radius
        float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);

        mPager.setAdapter(new SlidingImagesAdapter(ResultActivity_take2.this,mResultsPhotosPath));
        indicator.setViewPager(mPager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

        setListeners();

    }

    private void setListeners() {
        mShareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String path = mResultsPhotosPath.get(currentPage);
                final Uri uriToImage = Uri.parse(path);
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uriToImage);
                shareIntent.setType("image/jpeg");
                startActivity(Intent.createChooser(shareIntent, "Share image using"));
            }
        });

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO save photo
            }
        });
    }

    @Override
    public void onBackPressed() {
        goToMainActivity();
    }

    public void goToMainActivity() {
        Intent intent = new Intent(ResultActivity_take2.this , MainActivity.class);
        startActivity(intent);
        finish();
    }
}
