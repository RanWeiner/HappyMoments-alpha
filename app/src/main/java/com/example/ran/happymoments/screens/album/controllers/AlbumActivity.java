package com.example.ran.happymoments.screens.album.controllers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.ran.happymoments.R;
import com.example.ran.happymoments.screens.home.controllers.MainActivity;



public class AlbumActivity extends AppCompatActivity {


    //empty

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