package com.example.ran.happymoments.service;

import android.util.Log;

import com.example.ran.happymoments.logic.face.FaceMatcher;
import com.example.ran.happymoments.logic.photo.Person;
import com.example.ran.happymoments.logic.photo.Photo;
import com.example.ran.happymoments.logic.photo.Ranker;
import com.example.ran.happymoments.logic.series.PhotoSeries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;



public class SeriesTask implements Callable<String> {
    private PhotoSeries mPhotoSeries;

    public SeriesTask(PhotoSeries series){
        mPhotoSeries = series;
    }



    private void setImportanceFaces() {

        Map<Integer , Double> personMaxFaceSize = new HashMap<>();
        double currentSize , maxSize;
        int key;

        for (Photo photo : mPhotoSeries.getPhotos()) {

            for (Person person : photo.getPersons()) {
                key = person.getId();
                currentSize = person.getFaceSize();

                if (personMaxFaceSize.containsKey(key)) {
                    maxSize = personMaxFaceSize.get(key);

                    if (person.getFaceSize() > maxSize) {
                        personMaxFaceSize.put(key , currentSize);
                    }
                } else {
                    personMaxFaceSize.put(key , currentSize);
                }
            }
        }

        //now we have each person max face size in a series
        //need to calculate person importance in each photo

        for (Photo photo : mPhotoSeries.getPhotos()) {
            for (Person person : photo.getPersons()) {
                double importance = person.getFaceSize() / personMaxFaceSize.get(person.getId()) ;
                person.setImportance(importance);
                Log.i("FACEIMPORTANCE" , photo.getPath() + ", person = " + person.getFace().getPosition().toString()+", importance= " + importance);
            }
        }
    }



    @Override
    public String call() throws Exception {

        //match all persons in the series of photo by their id's
        FaceMatcher.matchPersons(mPhotoSeries);

        //in each photo in each series set every person face importance to value between 0-1
        setImportanceFaces();

//finding the highest ranked photo in series
        int highestRankedPhotoIndex = 0;
        double highestRank = Ranker.rankPhoto(mPhotoSeries.getPhoto(highestRankedPhotoIndex));
        double currentRank;
        Log.i("SCORE" , "photo= " +mPhotoSeries.getPhoto(0).getPath()+  ", rank= " + highestRank);

        for (int i = 1 ; i < mPhotoSeries.getNumOfPhotos() ; i++) {
            currentRank = Ranker.rankPhoto(mPhotoSeries.getPhoto(i));
            Log.i("SCORE" , "photo= " +mPhotoSeries.getPhoto(i).getPath()+  ", rank= " + currentRank);
            if (currentRank > highestRank) {
                highestRank = currentRank;
                highestRankedPhotoIndex = i;
            }
        }

        return mPhotoSeries.getPhoto(highestRankedPhotoIndex).getPath();
    }
}
