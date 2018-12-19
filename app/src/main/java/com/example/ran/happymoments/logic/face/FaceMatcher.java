package com.example.ran.happymoments.logic.face;


import com.example.ran.happymoments.common.RelativePositionVector;
import com.example.ran.happymoments.logic.photo.Person;
import com.example.ran.happymoments.logic.series.PhotoSeries;

import java.util.List;

public class FaceMatcher {


    public FaceMatcher() {

    }

    public void matchPersons(PhotoSeries series) {

        List<Person> basePersons , anyPersons;
        int numOfPersons , numOfPhotos , matchingId;
        double distance , minDistance;

        numOfPhotos = series.getPhotos().size();
        basePersons = series.getPhoto(0).getPersons();
        numOfPersons = basePersons.size();


        for (int photoIdx = 1 ; photoIdx < numOfPhotos ; photoIdx++) {

            anyPersons = series.getPhoto(photoIdx).getPersons();

            for (int i =0 ; i < numOfPersons ; i++) {

                minDistance = compareVectors(anyPersons.get(i).getVector() , basePersons.get(0).getVector());
                matchingId = basePersons.get(0).getId();

                for (int j = 1 ; j < numOfPersons ; j++) {

                    distance = compareVectors(anyPersons.get(i).getVector() , basePersons.get(j).getVector());
                    if (distance < minDistance) {
                        minDistance = distance;
                        matchingId = basePersons.get(j).getId();
                    }
                }
                anyPersons.get(i).setId(matchingId);
            }
        }
    }

    private double compareVectors(RelativePositionVector v1, RelativePositionVector v2) {
        double exponent = 2.0;
        double a, b, distance;

        a = (v2.getDistance() * Math.cos(v2.getAngle())) - (v1.getDistance() * Math.cos(v1.getAngle()));
        b = (v2.getDistance() * Math.sin(v2.getAngle())) - (v1.getDistance() * Math.sin(v1.getAngle()));
        distance = Math.pow(a,exponent) + Math.pow(b, exponent);
        return Math.sqrt(distance);
    }


}
