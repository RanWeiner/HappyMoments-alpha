package com.example.ran.happymoments.generator.series;

import com.example.ran.happymoments.generator.photo.Photo;

import com.example.ran.happymoments.detection.PhotoPerson;

import java.util.ArrayList;
import java.util.List;

public class PhotoSeries {

    private static int idGenerator = 0;
    private int id;
    private List<Photo> photos;
    private int numOfPhotos;
    private ArrayList<PhotoPerson> personsInSeries;


    public PhotoSeries() {
        this.id = ++idGenerator;
        photos = new ArrayList<>();
        numOfPhotos = 0;
        personsInSeries = new ArrayList<>();
    }

//    public PhotoSeries(List<Photo> photos) {
//        this();
//        mPhotos.addAll(photos);
//    }


    public int getId() {
        return id;
    }




    public void setId(int id) {
        this.id = id;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public int getNumOfPhotos() {
        return numOfPhotos;
    }

    public void setNumOfPhotos(int numOfPhotos) {
        this.numOfPhotos = numOfPhotos;
    }

    public List<Photo> getPhotos() {
        return this.photos;
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
        numOfPhotos++;
    }

    public Photo getPhoto(int index) {
        return this.photos.get(index);
    }

    public void removePhoto(Photo photo) {
        numOfPhotos--;
        this.photos.remove(photo);
    }
}
