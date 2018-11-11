package com.example.ran.happymoments;

import android.media.ExifInterface;

import java.io.IOException;

public class Photo {

    private PhotoFeatures photoFeatures;
    private ExifInterface exifInterface;


    public Photo(String path){

        setExifInterface(path);
        setPhotoFeatures(path);
    }

    private void setPhotoFeatures(String path) {
        if (hasExifData()){
            photoFeatures = new PhotoFeatures(path , exifInterface);
        }
    }

    private boolean hasExifData() {
        if (this.exifInterface != null){
            return true;
        }
        return false;
    }




    public PhotoFeatures getPhotoFeatures() {
        return photoFeatures;
    }

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
