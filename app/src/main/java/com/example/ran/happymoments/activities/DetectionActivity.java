package com.example.ran.happymoments.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ran.happymoments.R;
import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.logic.SeriesGenerator;
import com.example.ran.happymoments.adapter.RecycleViewImageAdapter;

import java.util.ArrayList;

public class DetectionActivity extends AppCompatActivity {

    private ArrayList<String> mInputPhotosPath , mOutputPhotosPath;
    private SeriesGenerator mSeriesGenerator;
    private Button mDetectBtn;
    private ProgressBar mProgressBar;
    private RecycleViewImageAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);

        Bundle bundle = getIntent().getExtras();
        mInputPhotosPath = bundle.getStringArrayList(AppConstants.IMPORTED_IMAGES);

        setUpImageGrid(mInputPhotosPath);

        mSeriesGenerator = new SeriesGenerator(getApplicationContext() , mInputPhotosPath);
        mOutputPhotosPath = new ArrayList<>();
        initializeViews();

    }

    private void initializeViews() {
        mDetectBtn = (Button)findViewById(R.id.detect_btn_id);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);

        setListeners();
    }

    private void setListeners() {
        mDetectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectBtnClicked();
            }
        });

    }




    public void setUpImageGrid(ArrayList<String> photos){

        RecyclerView recyclerView = findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecycleViewImageAdapter(getApplicationContext(), photos, new RecycleViewImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                goToFullScreenActivity(position);
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void dismissUserJobInProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mDetectBtn.setEnabled(true);
        Toast.makeText(DetectionActivity.this , "Finished!",Toast.LENGTH_LONG).show();

    }


    public void showUserJobInProgress() {
        Toast.makeText(DetectionActivity.this , "Start Detection...",Toast.LENGTH_LONG).show();

        mProgressBar.setVisibility(View.VISIBLE);
        mDetectBtn.setEnabled(false);
    }


    private void goToFullScreenActivity(int position) {
        Intent intent = new Intent(DetectionActivity.this, FullScreenViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(AppConstants.PHOTOS_PATH , mInputPhotosPath);
        bundle.putInt(AppConstants.POSITION , position);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void detectBtnClicked() {
        disableUserInteraction();
        showUserJobInProgress();


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                mOutputPhotosPath = mSeriesGenerator.detect();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DetectionActivity.this , "Finished!",Toast.LENGTH_SHORT).show();
                        dismissUserJobInProgress();
                        enableUserInteraction();

                        goToResultsActivity();
                    }
                });
            }
        });
        t.start();


    }



    private void disableUserInteraction() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }


    private void enableUserInteraction() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }



    private void goToResultsActivity() {
        Intent intent = new Intent(DetectionActivity.this , ResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(AppConstants.OUTPUT_PHOTOS , mOutputPhotosPath);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DetectionActivity.this , MainActivity.class);
        startActivity(intent);
        finish();
    }


}
