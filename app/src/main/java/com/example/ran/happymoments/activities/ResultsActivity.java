package com.example.ran.happymoments.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ran.happymoments.R;
import com.example.ran.happymoments.adapter.RecycleViewImageAdapter;
import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.common.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ResultsActivity extends AppCompatActivity {

    private ArrayList<String> mResultsPhotosPath;
    private Button mSaveBtn;
    private RecycleViewImageAdapter adapter;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        Bundle bundle = getIntent().getExtras();
        mResultsPhotosPath = bundle.getStringArrayList(AppConstants.OUTPUT_PHOTOS);

        initializeViews();

    }

    
    private void initializeViews() {
        mSaveBtn = (Button)findViewById(R.id.save_btn_id);
        mProgressBar = (ProgressBar)findViewById(R.id.save_progress_bar);
        setUpImageGrid();
        setListeners();
    }


    private void setUpImageGrid() {

        RecyclerView recyclerView = findViewById(R.id.gallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RecycleViewImageAdapter(getApplicationContext(), mResultsPhotosPath, new RecycleViewImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(ResultsActivity.this , "" + mResultsPhotosPath.get(position) , Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    private void setListeners() {
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableUserInteraction();
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        savePhotosToDevice();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                enableUserInteraction();
                                goToMainActivity();
                            }
                        });
                    }
                });
                t.start();
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(ResultsActivity.this , MainActivity.class);
        startActivity(intent);
        finish();
    }


    private void savePhotosToDevice() {

        Utils utils = new Utils(this);

        try {
            FileInputStream stream;
            Bitmap bitmap;

            for (String path : mResultsPhotosPath) {
                stream = new FileInputStream(path);
                bitmap = BitmapFactory.decodeStream(stream);
//                final Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.image);
                utils.saveImageToExternal(bitmap);
                }
        } catch (IOException e) {
            e.printStackTrace();
            }
    }



    private void disableUserInteraction() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE ,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mProgressBar.setVisibility(View.VISIBLE);
        mSaveBtn.setEnabled(false);
    }


    private void enableUserInteraction() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        mProgressBar.setVisibility(View.INVISIBLE);
        mSaveBtn.setEnabled(true);
        Toast.makeText(ResultsActivity.this , "Saved!",Toast.LENGTH_LONG).show();
    }



}


