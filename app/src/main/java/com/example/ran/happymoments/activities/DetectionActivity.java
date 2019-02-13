package com.example.ran.happymoments.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ran.happymoments.R;
import com.example.ran.happymoments.common.AppConstants;


import com.example.ran.happymoments.adapter.RecycleViewImageAdapter;
import com.example.ran.happymoments.service.SeriesGenerator;

import java.util.ArrayList;
import java.util.List;

public class DetectionActivity extends AppCompatActivity {

    private List<String> mInputPhotosPath , mOutputPhotosPath;

    private SeriesGenerator mSeriesGenerator;
//    private PhotoExtractionManager manager;
//    private FaceDetector detector;

    private Button mDetectBtn;
    private ProgressBar mProgressBar;
    private RecycleViewImageAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detection);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle bundle = getIntent().getExtras();
        mInputPhotosPath = bundle.getStringArrayList(AppConstants.IMPORTED_IMAGES);

        setUpImageGrid(mInputPhotosPath);

//        mSeriesGenerator = new SeriesGenerator(getApplicationContext() , mInputPhotosPath);


        mSeriesGenerator = new SeriesGenerator(getApplicationContext() , mInputPhotosPath);
//        detector = new MobileVision();
//        manager = new PhotoExtractionManager(getApplicationContext() ,mInputPhotosPath, detector );

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

                if (isNetworkAvailable()) {
                    detectBtnClicked();
                } else {

                    showNetworkError();

                }
            }
        });

    }

    private void showNetworkError() {

        //go to wifi Settings.ACTION_WIRELESS_SETTINGS);
        //go to page mobile network Settings.ACTION_DATA_ROAMING_SETTINGS));
        //go to both wifi and mobile network Settings.ACTION_SETTINGS


        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent intent=new Intent(Settings.ACTION_SETTINGS);
                        startActivity(intent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(DetectionActivity.this);
        builder.setTitle("No Internet").setMessage("This App requires Internet connections ")
                .setPositiveButton("Connect", dialogClickListener).setNegativeButton("Exit", dialogClickListener).show();
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void setUpImageGrid(List<String> photos){

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







    private void goToFullScreenActivity(int position) {
        Intent intent = new Intent(DetectionActivity.this, FullScreenViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(AppConstants.PHOTOS_PATH , (ArrayList<String>) mInputPhotosPath);
        bundle.putInt(AppConstants.POSITION , position);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void detectBtnClicked() {
        disableUserInteraction();
        long startTime = System.nanoTime();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                mOutputPhotosPath = mSeriesGenerator.detect();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enableUserInteraction();
                        goToResultsActivity();
                    }
                });
            }
        });
        t.start();

        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        Log.i("DETECTION TIME", "DETECTION TIME= " + totalTime);

    }



    private void disableUserInteraction() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Toast.makeText(DetectionActivity.this , "Start Detection...",Toast.LENGTH_LONG).show();
        mProgressBar.setVisibility(View.VISIBLE);
        mDetectBtn.setEnabled(false);
    }


    private void enableUserInteraction() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mProgressBar.setVisibility(View.INVISIBLE);
        mDetectBtn.setEnabled(true);
        Toast.makeText(DetectionActivity.this , "Finished!",Toast.LENGTH_LONG).show();
    }



    private void goToResultsActivity() {
        Intent intent = new Intent(DetectionActivity.this , ResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(AppConstants.OUTPUT_PHOTOS , (ArrayList<String>) mOutputPhotosPath);
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
