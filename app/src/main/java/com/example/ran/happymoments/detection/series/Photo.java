package com.example.ran.happymoments.detection.series;

import android.support.media.ExifInterface;
import android.util.Log;

import com.example.ran.happymoments.detection.face.Face;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class Photo {
    private String mPath;
    private PhotoFeatures photoFeatures;
    private ExifInterface exifInterface;
    private List<Face> faces = null;

    public Photo(String path){
        this.mPath = path;
        setExifInterface();
        setPhotoFeatures();
        setPhotoOrientation();
        faces = new ArrayList<>();
    }

    public void setPhotoOrientation() {
        if(hasExifData()){
            int deg = exifInterface.getRotationDegrees();
                exifInterface.rotate(180);
                Log.d(TAG, "Picture was rotated. deg = " + deg);

        }
    }

    public String getPath() {
        return this.mPath;
    }



    private void setPhotoFeatures() {
        if (hasExifData()){
            photoFeatures = new PhotoFeatures(mPath , this.exifInterface);
        }
    }

    public boolean hasExifData() {
        if (this.exifInterface != null){
            return true;
        }
        return false;
    }



    public boolean similarTo(Photo other){
        return this.getPhotoFeatures().compareFeatures(other.getPhotoFeatures());
    }

    public PhotoFeatures getPhotoFeatures() {
        return photoFeatures;
    }

    public ExifInterface getExifInterface() {
        return exifInterface;
    }

    public void setExifInterface() {
        try {
           this.exifInterface =  new ExifInterface(mPath);
        } catch (IOException e) {
            e.printStackTrace();
            this.exifInterface = null;
        }
    }


    public void setFaces(List<Face> faces) {
        this.faces = faces;
    }

    public List<Face> getFaces(){
        return faces;
    }
}
