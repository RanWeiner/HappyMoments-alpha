package com.example.ran.happymoments.generator.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.media.ExifInterface;

import com.example.ran.happymoments.generator.face.Face;
import com.example.ran.happymoments.common.Position;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Photo {
    private String mPath;
    private PhotoFeatures photoFeatures;
    private ExifInterface exifInterface;
    private Position totalFacesCenter;
    private List<Person> mPersonList;
    private List<Face> faces;
    private float faceScore;

    private double maxFaceSize , maxFaceDistanceFromCenter;
    private Bitmap bitmap;



    public Photo(String path){
        this.mPath = path;
        setExifInterface();
        setPhotoFeatures();

        //setPhotoOrientation();

        setBitmap();

        mPersonList = new ArrayList<>();
        faces = new ArrayList<>();
        totalFacesCenter = new Position();

        this.maxFaceSize = 0;
        this.maxFaceDistanceFromCenter = 0;
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


    public Position getTotalFacesCenter() {
        return totalFacesCenter;
    }

    public void setTotalFacesCenter(Position totalFacesCenter) {
        this.totalFacesCenter = totalFacesCenter;
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

    public Bitmap getBitmap() {
        return this.bitmap;
    }


    public List<Person> getPersons() {
        return mPersonList;
    }

    public double getMaxFaceSize() {
        return maxFaceSize;
    }

    public void setMaxFaceSize(double maxFaceSize) {
        if (maxFaceSize > this.maxFaceSize) {
            this.maxFaceSize = maxFaceSize;
        }
    }

    public double getMaxFaceDistanceFromCenter() {
        return maxFaceDistanceFromCenter;
    }

    public void setMaxFaceDistanceFromCenter(double maxFaceDistanceFromCenter) {
        if (maxFaceDistanceFromCenter > this.maxFaceDistanceFromCenter) {
            this.maxFaceDistanceFromCenter = maxFaceDistanceFromCenter;
        }
    }

    public void addPerson(Person person) {
        this.mPersonList.add(person);
    }

    public List<Person> getPersonList() { return mPersonList; }

    public float getFaceScore() {
        return faceScore;
    }

    public void setFaceScore(float faceScore) {
        this.faceScore = faceScore;
    }
}
