package com.example.ran.happymoments;

import java.util.ArrayList;

public class PhotoSeries {

    private static int idGenerator = 0;
    private int id;
    private ArrayList<Photo> mPhotos;


    public PhotoSeries() {
        this.id = ++idGenerator;
        mPhotos = new ArrayList<>();
    }


    public int getId() {
        return id;
    }


    public ArrayList<Photo> getPhotos() {
        return this.mPhotos;
    }

    public void addPhoto(Photo photo) {
        this.mPhotos.add(photo);
    }

    public void removePhoto(Photo photo) {
        this.mPhotos.remove(photo);
    }
}
