package com.example.ran.happymoments;

import android.media.ExifInterface;

import java.io.IOException;

public class Photo {
    private String mPath;
    private PhotoFeatures photoFeatures;
    private ExifInterface exifInterface;


    public Photo(String path){
        this.mPath = path;
        setExifInterface(path);
        setPhotoFeatures(path);

    }

    public String getPath() {
        return this.mPath;
    }



    private void setPhotoFeatures(String path) {
        if (hasExifData()){
            photoFeatures = new PhotoFeatures(path , exifInterface);
        }
    }

    public boolean hasExifData() {
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
