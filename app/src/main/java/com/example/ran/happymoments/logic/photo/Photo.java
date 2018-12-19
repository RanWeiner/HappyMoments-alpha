package com.example.ran.happymoments.logic.photo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.media.ExifInterface;

import com.example.ran.happymoments.common.Position;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Photo {
    private String mPath;
    private PhotoFeatures photoFeatures;
    private ExifInterface exifInterface;
    private Position totalFacesCenter;
    private List<Person> persons;
    private double rank;
    private double maxFaceDistanceFromCenter;

//    private Bitmap bitmap;



    public Photo(String path){
        this.mPath = path;
        setExifInterface();
        setPhotoFeatures();

        this.persons = new ArrayList<>();
        this.totalFacesCenter = new Position();
        this.maxFaceDistanceFromCenter = 0;
        this.rank = 0;
    }


//    private void setBitmap() {
//        this.bitmap = null;
//        FileInputStream stream;
//        try {
//            stream = new FileInputStream(this.mPath);
//            this.bitmap = BitmapFactory.decodeStream(stream);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }


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



    public List<Person> getPersons() {
        return persons;
    }


    public double getMaxFaceDistanceFromCenter() {
        return maxFaceDistanceFromCenter;
    }



    public void addPerson(Person person) {
        double personDistance = person.getVector().getDistance();

        if (personDistance > maxFaceDistanceFromCenter) {
            this.maxFaceDistanceFromCenter = personDistance;
        }
        this.persons.add(person);
    }



    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }


}
