package com.example.ran.happymoments;

import android.media.ExifInterface;

import java.io.IOException;

public class Photo {

    private PhotoFeatures photoFeatures;
    private ExifInterface exifInterface;

    public Photo(){

    }


    public PhotoFeatures getPhotoFeatures() {
        return photoFeatures;
    }

//    public void setPhotoFeatures(PhotoFeatures photoFeatures) {
//        this.photoFeatures = photoFeatures;
//    }

    public ExifInterface getExifInterface() {
        return exifInterface;
    }

    public void setExifInterface(String imagePath) {
        try {
           this.exifInterface =  new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            this.exifInterface = null;
        }
    }
}
