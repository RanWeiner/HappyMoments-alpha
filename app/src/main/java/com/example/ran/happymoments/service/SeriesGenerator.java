package com.example.ran.happymoments.service;

import android.content.Context;
import android.support.media.ExifInterface;
import android.util.Log;

import com.example.ran.happymoments.logic.face.Face;
import com.example.ran.happymoments.logic.face.FaceMatcher;
import com.example.ran.happymoments.logic.photo.Person;
import com.example.ran.happymoments.logic.photo.Photo;
import com.example.ran.happymoments.logic.photo.Ranker;
import com.example.ran.happymoments.logic.series.PhotoSeries;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class SeriesGenerator {

    private Context mContext;
    private List<String> mInputPhotosPath;
    private FaceDetector mFaceDetector;
    private List<String> mPhotosOutputPath;


    public SeriesGenerator(Context context, List<String> inputPhotosPath) {
        mContext = context;
        mInputPhotosPath = inputPhotosPath;
        mFaceDetector = new MobileVision(context);
        mPhotosOutputPath = new ArrayList<>();
    }


    public List<String> detect() {

        List<PhotoSeries> seriesList = generateAllSeries();

        printSeries(seriesList);

        /////////////////// [FACE CORRESPONDENCE & RANKING] /////////////////////

        for (PhotoSeries series : seriesList) {

            //match all persons in the series of photo by their id's
            FaceMatcher.matchPersons(series);

            //in each photo in each series set every person face importance to value between 0-1
            setImportanceFaces(series);

            //finding the highest ranked photo in series
            int highestRankedPhotoIndex = 0;
            double highestRank = Ranker.rankPhoto(series.getPhoto(highestRankedPhotoIndex));
            double currentRank;
            Log.i("SCORE" , "photo= " +series.getPhoto(0).getPath()+  ", rank= " + highestRank);

            for (int i = 1 ; i < series.getPhotos().size() ; i++) {
                currentRank = Ranker.rankPhoto(series.getPhoto(i));
                Log.i("SCORE" , "photo= " +series.getPhoto(i).getPath()+  ", rank= " + currentRank);
                if (currentRank > highestRank) {
                    highestRank = currentRank;
                    highestRankedPhotoIndex = i;
                }
            }
            mPhotosOutputPath.add(series.getPhoto(highestRankedPhotoIndex).getPath());
        }

        return mPhotosOutputPath;
    }




    private void setImportanceFaces(PhotoSeries series) {

        Map<Integer , Double> personMaxFaceSize = new HashMap<>();
        double currentSize , maxSize;
        int key;

        for (Photo photo : series.getPhotos()) {

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

        for (Photo photo : series.getPhotos()) {
            for (Person person : photo.getPersons()) {
                double importance = person.getFaceSize() / personMaxFaceSize.get(person.getId()) ;
                person.setImportance(importance);
                Log.i("FACEIMPORTANCE" , photo.getPath() + ", person = " + person.getFace().getPosition().toString()+", importance= " + importance);
            }
        }
    }


    private List<PhotoSeries> generateAllSeries() {
        List<PhotoSeries> rv = new ArrayList<>();
        Map <Integer , List<Photo>> map = new HashMap<>();
        List<Face> faces;
        ExifInterface exifInterface = null;
        String orientation = null;

//tryyyyyyyy
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService es = Executors.newFixedThreadPool(cores + 1);
        List<Future<Photo>> result = new ArrayList<>();
//////////////


        for (String path : mInputPhotosPath) {
            try {
                exifInterface = new ExifInterface(path);
                orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            } catch (IOException e) {
                e.printStackTrace();
            }

            faces = mFaceDetector.detectFaces(mContext, path ,orientation);

            if (!faces.isEmpty()) {
                //tryyyyyyyy////////////////////////////
                Callable<Photo> task = new PhotoExtractionTask(path , exifInterface , faces);
                Future<Photo> current = es.submit(task);
                result.add(current);
////////////////////////////////////////

//                Person[] persons = new Person[faces.size()];
//                for (int i = 0 ; i < faces.size() ; i++) {
//                    persons[i] = new Person(i , faces.get(i));
//                }
//
//                Photo photo = new Photo(path ,exifInterface, Arrays.asList(persons));
//                addPhotoToMap(map , photo , persons.length);
            }
        }


        //release the detector
        mFaceDetector.release();

/////////////////////
        es.shutdown();
////////////////////
        for (Future<Photo> f : result) {
            try {
                addPhotoToMap(map ,   f.get() ,f.get().getNumOfPersons());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }


        for (Map.Entry<Integer, List<Photo>> entry : map.entrySet()) {
            if (entry.getValue().size() == 1) {
                mPhotosOutputPath.add(entry.getValue().get(0).getPath());
            } else {
                rv.addAll(generateSeriesByFeatures(entry.getValue()));
            }
        }

        filterAllOnePhotoSeries(rv);
        return rv;
    }


//    private List<PhotoSeries> generateAllSeries() {
//        List<PhotoSeries> rv = new ArrayList<>();
//        Map <Integer , List<Photo>> map = new HashMap<>();
//        List<Face> faces;
//        ExifInterface exifInterface = null;
//        String orientation = null;
//
//        for (String path : mInputPhotosPath) {
//            try {
//                exifInterface = new ExifInterface(path);
//                orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            faces = mFaceDetector.detectFaces(mContext, path ,orientation);
//
//            if (!faces.isEmpty()) {
//                Person[] persons = new Person[faces.size()];
//
//                for (int i = 0 ; i < faces.size() ; i++) {
//                    persons[i] = new Person(i , faces.get(i));
//                }
//
//                Photo photo = new Photo(path ,exifInterface, Arrays.asList(persons));
//                addPhotoToMap(map , photo , persons.length);
//            }
//        }
//
//        //release the detector
//        mFaceDetector.release();
//
//        for (Map.Entry<Integer, List<Photo>> entry : map.entrySet()) {
//            if (entry.getValue().size() == 1) {
//                mPhotosOutputPath.add(entry.getValue().get(0).getPath());
//            } else {
//                rv.addAll(generateSeriesByFeatures(entry.getValue()));
//            }
//        }
//
//        filterAllOnePhotoSeries(rv);
//        return rv;
//    }






    private void printSeries(List<PhotoSeries> seriesList) {
        for (PhotoSeries series : seriesList) {
            Log.i("TESTING" , "Series ID= " + series.getId());

            for (Photo photo : series.getPhotos()) {
                Log.i("TESTING" , "path= " + photo.getPath());
            }
        }
    }



    private void addPhotoToMap(Map<Integer,List<Photo>> map, Photo photo, int key) {
        List<Photo> photos = map.get(key);

        //not exist in map
        if (photos == null) {
            photos = new ArrayList<Photo>();
            photos.add(photo);
            map.put(key, photos);
        } else {
            //exist in map
            photos.add(photo);
        }
    }


    private void filterAllOnePhotoSeries(List<PhotoSeries> seriesList) {

        ArrayList <PhotoSeries> photosToBeRemoved = new ArrayList<>();

        for (int i = 0; i < seriesList.size() ; i++) {

            if (seriesList.get(i).getNumOfPhotos() == 1) {
                mPhotosOutputPath.add(seriesList.get(i).getPhoto(0).getPath());
                photosToBeRemoved.add(seriesList.get(i));
            }
        }
        seriesList.removeAll(photosToBeRemoved);
    }


    public List<PhotoSeries> generateSeriesByFeatures(List<Photo> photos) {

        List<PhotoSeries> foundSeries =  new ArrayList<>();;
        boolean hasFoundSeries;
        PhotoSeries photoSeries = new PhotoSeries();

        if (photos.isEmpty()) {
            return null;
        }

        photoSeries.addPhoto(photos.get(0));
        foundSeries.add(photoSeries);

        for (int i = 1 ; i < photos.size() ; i++){

            hasFoundSeries = false;

            for (int j = 0 ; j < foundSeries.size() ; j++) {

                if (foundSeries.get(j).getPhoto(0).similarTo( photos.get(i))){
                    foundSeries.get(j).addPhoto( photos.get(i));
                    hasFoundSeries = true;
                    break;
                }
            }
            if (!hasFoundSeries){
                PhotoSeries newPhotoSeries = new PhotoSeries();
                newPhotoSeries.addPhoto( photos.get(i));
                foundSeries.add(newPhotoSeries);
            }
        }
        return foundSeries;
    }
}
