package com.example.ran.happymoments.generator.series;

import com.example.ran.happymoments.generator.photo.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoSeries {

    private static int idGenerator = 0;
    private int id;
    private List<Photo> photos;
    private double maxDistanceToFacesCenter;



    public PhotoSeries() {
        this.id = ++idGenerator;
        photos = new ArrayList<>();
        maxDistanceToFacesCenter = 0;
    }

    public PhotoSeries(List<Photo> photos) {
        this();
        photos.addAll(photos);
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

    public double getMaxDistanceToFacesCenter() {
        return maxDistanceToFacesCenter;
    }

    public void setMaxDistanceToFacesCenter(double maxDistanceToFacesCenter) {
        if (maxDistanceToFacesCenter > this.maxDistanceToFacesCenter) {
            this.maxDistanceToFacesCenter = maxDistanceToFacesCenter;
        }
    }

    public void setFaceMaxDistanceToCenter() {

        for (Photo photo: photos) {
            if (photo.getMaxFaceDistanceFromCenter() > this.maxDistanceToFacesCenter) {
                this.maxDistanceToFacesCenter = photo.getMaxFaceDistanceFromCenter();
            }
        }
    }

    public Photo getHighestRankedPhoto() {
        return null;
    }
}
