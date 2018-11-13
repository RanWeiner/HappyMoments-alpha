package com.example.ran.happymoments.detection.series;

import java.util.ArrayList;
import java.util.List;

public class PhotoSeries {

    private static int idGenerator = 0;
    private int id;
    private List<Photo> mPhotos;


    public PhotoSeries() {
        this.id = ++idGenerator;
        mPhotos = new ArrayList<>();
    }

//    public PhotoSeries(List<Photo> photos) {
//        this();
//        mPhotos.addAll(photos);
//    }


    public int getId() {
        return id;
    }


    public List<Photo> getPhotos() {
        return this.mPhotos;
    }

    public void addPhoto(Photo photo) {
        this.mPhotos.add(photo);
    }

    public Photo getPhoto(int index) {
        return this.mPhotos.get(index);
    }

    public void removePhoto(Photo photo) {
        this.mPhotos.remove(photo);
    }
}
