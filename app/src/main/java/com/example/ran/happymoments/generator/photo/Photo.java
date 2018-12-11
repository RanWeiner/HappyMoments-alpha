package com.example.ran.happymoments.generator.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.media.ExifInterface;
import android.util.Log;

import com.example.ran.happymoments.generator.face.Face;
import com.example.ran.happymoments.common.Position;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class Photo {
    private String mPath;
    private PhotoFeatures photoFeatures;
    private ExifInterface exifInterface;
    private List<Face> faces;
    private Position facesCenterGravity;

    private Bitmap bitmap;




    public Photo(String path){
        this.mPath = path;
        setExifInterface();
        setPhotoFeatures();

        setPhotoOrientation();

        
        //setBitmap();
        faces = new ArrayList<>();
        facesCenterGravity = new Position();
    }

    public void setPhotoOrientation() {
        if(hasExifData()){
            int deg = exifInterface.getRotationDegrees();
                exifInterface.rotate(180);
                Log.d(TAG, "Picture was rotated. deg = " + deg);

        }
    }


    private void setBitmap() {
        this.bitmap = null;
        FileInputStream stream;
        try {
            stream = new FileInputStream(this.mPath);
            this.bitmap = BitmapFactory.decodeStream(stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public String getPath() {
        return this.mPath;
    }


    public int getNumOfFaces() {
        return this.faces.size();
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


    public Position getFacesCenterGravity() {
        return facesCenterGravity;
    }

    public void setFacesCenterGravity(Position facesCenterGravity) {
        this.facesCenterGravity = facesCenterGravity;
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
        }
    }


    public void setFaces(List<Face> faces) {
        this.faces = faces;
    }

    public List<Face> getFaces(){
        return faces;
    }

//    public Bitmap getBitmap() {
//        return this.bitmap;
//    }
}
