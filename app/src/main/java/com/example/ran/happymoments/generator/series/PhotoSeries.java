package com.example.ran.happymoments.generator.series;

import com.example.ran.happymoments.generator.photo.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoSeries {

    private static int idGenerator = 0;
    private int id;
    private List<Photo> photos;
    private Person[] persons;


    public PhotoSeries() {
        this.id = ++idGenerator;
        photos = new ArrayList<>();
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


    public Person getPerson(int index) {
        return this.persons[index];
    }

    public void initPersons(int numOfPersons) {
        this.persons = new Person[numOfPersons];
    }

    public Person[] getPersons() {
        return this.persons;
    }


    public Photo getHighestRankedPhoto() {
        return photos.get(0);
    }
}
