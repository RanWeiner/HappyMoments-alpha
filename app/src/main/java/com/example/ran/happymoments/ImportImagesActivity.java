package com.example.ran.happymoments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;

public class ImportImagesActivity extends AppCompatActivity {

    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final int LIMIT = 5;

    private ImageView imageView;
    private TextView txImageSelects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_images);

        txImageSelects = (TextView) findViewById(R.id.txImageSelects);
        imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!checkPermissionForExternalStorage()) {
                        requestStoragePermission();
                    } else {
                        // opening custom gallery
                        goToDeviceGalleryAlbum();
                    }
                }else{
                    goToDeviceGalleryAlbum();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

            for (int i = 0; i < images.size(); i++) {
                Photo p = new Photo(images.get(i).path);

                Uri uri = Uri.fromFile(new File(images.get(i).path));

                Glide.with(this).load(uri)
                        .placeholder(R.color.colorAccent)
                        .override(400, 400)
                        .crossFade()
                        .centerCrop()
                        .into(imageView);

                txImageSelects.setText(txImageSelects.getText().toString().trim()
                        + "\n" +
                        String.valueOf(i + 1) + ". " + String.valueOf(uri));
            }
        }
    }

    public boolean checkPermissionForExternalStorage() {
        int result = ContextCompat.checkSelfPermission(ImportImagesActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }



    public boolean requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(ImportImagesActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        READ_STORAGE_PERMISSION);
            }
        } else {
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ImportImagesActivity.this , MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToDeviceGalleryAlbum() {
        Intent intent = new Intent(ImportImagesActivity.this, AlbumSelectActivity.class);
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
        finish();
    }
}
