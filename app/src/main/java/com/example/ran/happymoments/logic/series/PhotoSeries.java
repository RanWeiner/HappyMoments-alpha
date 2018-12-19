package com.example.ran.happymoments.logic.series;

import com.example.ran.happymoments.logic.photo.Photo;


import java.util.ArrayList;
import java.util.List;

public class PhotoSeries {

    private static int idGenerator = 0;
    private int id;
    private List<Photo> photos;


    public PhotoSeries() {
        this.id = ++idGenerator;
        photos = new ArrayList<>();
    }



    public PhotoSeries(List<Photo> photos) {
        this();
        this.photos.addAll(photos);
    }


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
        return photos.size();
    }

    public List<Photo> getPhotos() {
        return this.photos;
    }

    public void addPhoto(Photo photo) {
        this.photos.add(photo);
    }

    public Photo getPhoto(int index) {
        return this.photos.get(index);
    }

    public void removePhoto(Photo photo) {
        this.photos.remove(photo);
    }

    public double calcMaxDistanceToFacesCenter() {

        double maxDistance = 0;

        for (Photo photo: photos) {
            if (photo.getMaxFaceDistanceFromCenter() > maxDistance) {
                maxDistance = photo.getMaxFaceDistanceFromCenter();
            }
        }
        return maxDistance;
    }
}
