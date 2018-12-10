package com.example.ran.happymoments.generator;

import com.example.ran.happymoments.generator.face.Face;
import com.example.ran.happymoments.generator.photo.Photo;
import com.example.ran.happymoments.generator.series.Person;
import com.example.ran.happymoments.generator.series.PhotoSeries;

import java.util.ArrayList;
import java.util.List;

public class FaceMatcher {


    public FaceMatcher(){

    }

    public void matchFacesToPersons(List<PhotoSeries> series){

        for (PhotoSeries photoSeries: series) {
            for (Photo photo: photoSeries.getPhotos()) {
                matchAllPersonsToAllFaces(photo.getFaces() , photoSeries.getPersons());
            }
        }
    }


    public void matchAllPersonsToAllFaces(List<Face> faces, Person[] persons) {

        List<Face> facesInPhoto = new ArrayList<>(faces);

        for (int i = 0 ; i < persons.length; i++) {

            if (persons[i].getFaces().isEmpty()) {
                persons[i].addFace(facesInPhoto.get(i));
            } else {
                int matched = matchPersonToFace(persons[i] , facesInPhoto);
                facesInPhoto.remove(matched);
            }
        }
    }


    public int matchPersonToFace(Person person, List<Face> facesInPhoto) {

        double minDistance , currentDistance;
        int matchingFaceIndex = 0;
//        minDistance = compareVectors(person.getFaces().get(0).getVector() , facesInPhoto.get(0).getVector())

//        for (int i = 1 ; i < facesInPhoto.size() ; i++) {
//            currentDistance = compareVectors(person.getFaces().get(0).getVector() , face.getVector())
//            if (currentDistance < minDistance) {
//                minDistance = currentDistance;
//                matchingFaceIndex = i;
//            }
//        }

        person.addFace(facesInPhoto.get(matchingFaceIndex));
        return matchingFaceIndex;
    }


}
