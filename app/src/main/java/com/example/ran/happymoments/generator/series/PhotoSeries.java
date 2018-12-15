package com.example.ran.happymoments.generator.series;

import com.example.ran.happymoments.generator.photo.Person;
import com.example.ran.happymoments.generator.photo.Photo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public double calcMaxDistanceToFacesCenter() {

        double maxDistance = 0;

        for (Photo photo: photos) {
            if (photo.getMaxFaceDistanceFromCenter() > maxDistance) {
                maxDistance = photo.getMaxFaceDistanceFromCenter();
            }
        }
        return maxDistance;
    }


    public void setPersonsImportance() {

        Map<Integer , Double> maxPersonFaceSize = new HashMap<>();
        double currentSize , maxSize;
        int key;

        for (Photo photo : this.getPhotos()) {

            for (Person person : photo.getPersons()) {
                key = person.getId();
                currentSize = person.getFaceSize();

                if (maxPersonFaceSize.containsKey(key)) {
                    maxSize = maxPersonFaceSize.get(key);

                    if (person.getFaceSize() > maxSize) {
                        maxPersonFaceSize.put(key , currentSize);
                    }
                } else {
                    maxPersonFaceSize.put(key , currentSize);
                }
            }
        }

        //now we have each person max face size in a series
        //need to calculate person importance in each photo

        for (Photo photo : this.getPhotos()) {
            for (Person person : photo.getPersons()) {
                double importance = person.getFaceSize() / maxPersonFaceSize.get(person.getId()) ;
                person.setImportance(importance);
            }
        }
    }
}
