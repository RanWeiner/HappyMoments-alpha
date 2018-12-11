package com.example.ran.happymoments.generator;


import com.example.ran.happymoments.common.RelativePositionVector;
import com.example.ran.happymoments.generator.photo.Person;
import com.example.ran.happymoments.generator.photo.Photo;
import com.example.ran.happymoments.generator.series.PhotoSeries;

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

    private double compareVectors(RelativePositionVector vector, RelativePositionVector vector1) {

        return 0;
    }


}
