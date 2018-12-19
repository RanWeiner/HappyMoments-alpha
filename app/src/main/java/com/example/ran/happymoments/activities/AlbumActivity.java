package com.example.ran.happymoments.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ran.happymoments.R;


public class AlbumActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AlbumActivity.this , MainActivity.class);
        startActivity(intent);
        finish();
    }


}
