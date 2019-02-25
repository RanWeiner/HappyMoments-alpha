package com.example.ran.happymoments.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.example.ran.happymoments.R;

import java.io.File;

public class AlbumActivity extends AppCompatActivity {

    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        gridView = (GridView)findViewById(R.id.grid_view);

        presentAlbum();
    }

    public void presentAlbum() {
        File listFile[];
        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File file = new File(sdDir, "HappyMoments");

        if (file.isDirectory())
            listFile = file.listFiles();

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.withAppendedPath(Uri.fromFile(file), "/AppPics"), "image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AlbumActivity.this , MainActivity.class);
        startActivity(intent);
        finish();
    }


}
