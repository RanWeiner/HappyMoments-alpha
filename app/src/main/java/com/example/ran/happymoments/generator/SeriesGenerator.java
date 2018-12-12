package com.example.ran.happymoments.generator;

import android.content.Context;
import android.util.Log;

import com.example.ran.happymoments.common.RelativePositionVector;
import com.example.ran.happymoments.generator.face.Face;
import com.example.ran.happymoments.common.Position;
import com.example.ran.happymoments.generator.photo.Person;
import com.example.ran.happymoments.generator.photo.Photo;
import com.example.ran.happymoments.generator.series.PhotoSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SeriesGenerator {

    private List<String> mInput;
    private List<String> mOutput;

    //same features
    private List<PhotoSeries> mSameFeaturesSeriesList;

    //same faces
    private List<PhotoSeries> mIdenticalSeriesList;

    private List<Photo> mAllPhotos;

    private Context mContext;
    private FaceExtractor mFaceExtractor;

    private Ranker mRanker;
    private FaceMatcher mMatcher;



    public SeriesGenerator(Context context , List<String> imagesPath) {
        mContext = context;
        mInput = imagesPath;
        mAllPhotos = setPhotos(imagesPath);
        mFaceExtractor = new FaceExtractorMobileVision();
        mOutput = new ArrayList<>();
        mRanker = new Ranker();
        mMatcher = new FaceMatcher();
    }


    public List<String> detect() {

        ///////////////////// [SERIES PART] /////////////////////
        mSameFeaturesSeriesList = generateSeriesByFeatures();
        filterAllOnePhotoSeries();

        printSeries(mSameFeaturesSeriesList);
        
        ///////////////////// [FACES PART] /////////////////////
        Log.i("TESTING" , "FACES");
        mIdenticalSeriesList = generateSeriesByFaces();

        printSeries(mIdenticalSeriesList);



        ///////////////////// [NORMALIZE] /////////////////////
        Log.i("TESTING" , "NORMAELIZING");

        for (PhotoSeries series : mIdenticalSeriesList) {
            for (Photo photo : series.getPhotos()) {
                for (Person person : photo.getPersons()) {
                    Log.i("TESTING" , "NOT normalized angle=" + person.getVector().getAngle());
                    Log.i("TESTING" , "NOT normalized distance=" + person.getVector().getDistance());
                }
            }
        }


        normalizeVectors();
        for (PhotoSeries series : mIdenticalSeriesList) {
            for (Photo photo : series.getPhotos()) {
               for (Person person : photo.getPersons()) {
                   Log.i("TESTING" , "normalized angle=" + person.getVector().getAngle());
                   Log.i("TESTING" , "normalized distance=" + person.getVector().getDistance());
               }
            }
        }


        ///////////////////// [FACE CORRESPONDENCE & RANKING] /////////////////////
        Log.i("TESTING" , "MATCHING");
        for (PhotoSeries series : mIdenticalSeriesList) {

            mMatcher.matchPersons(series);

            series.setPersonsImportance();

            double highestRank = mRanker.rankPhoto(series.getPhoto(0));
            double currentRank;
            int highestRankedPhotoIndex = 0;

            for (int i = 1 ; i < series.getPhotos().size() ; i++) {
                currentRank = mRanker.rankPhoto(series.getPhoto(i));
                if (currentRank > highestRank) {
                    highestRank = currentRank;
                    highestRankedPhotoIndex = i;
                }
            }

            Photo p = series.getPhoto(highestRankedPhotoIndex);
            mOutput.add(p.getPath());
        }

        return mOutput;
    }

    private void printSeries(List<PhotoSeries> seriesList) {
        for (PhotoSeries series : seriesList) {
            Log.i("TESTING" , "Series ID= " + series.getId());

            for (Photo photo : series.getPhotos()) {
                Log.i("TESTING" , "path= " + photo.getPath());
            }
        }
    }


    private void normalizeVectors() {
        double maxDistance;

        for (PhotoSeries series: mIdenticalSeriesList) {

            maxDistance = series.getMaxDistanceToFacesCenter();

            Log.i("TESTING" , "maxDistance=" + maxDistance);

            for (Photo photo: series.getPhotos()) {

                for (Person person: photo.getPersons()) {

                    person.normalizeVector(maxDistance);
                }
            }
        }
    }



    private List<PhotoSeries> generateSeriesByFaces() {

        List<PhotoSeries> result = new ArrayList<>();

        List<Face> faces;

        Map<Integer, List<Photo>> sameFacesPhotos = new HashMap<>();

        for (PhotoSeries series : mSameFeaturesSeriesList) {

            sameFacesPhotos.clear();

            for (Photo photo : series.getPhotos()) {

                faces = mFaceExtractor.detectFaces(mContext, photo.getPath());

                Log.i("TESTING" , "faces size= " + faces.size());


                if (!faces.isEmpty()) {

                    setTotalFacesCenterInPhoto(photo , faces);

                    Log.i("TESTING" , "photo center faces= (" + photo.getTotalFacesCenter().getX() + "," +  + photo.getTotalFacesCenter().getY() +")");

                    for (Face face : faces) {

                        double angle = face.getPosition().calcAngle(photo.getTotalFacesCenter());
                        double dist = face.getPosition().calcEuclidDistance(photo.getTotalFacesCenter());

                        Log.i("TESTING" , "face "+ face.getId()+ ": center faces= (" + photo.getTotalFacesCenter().getX() +"," + photo.getTotalFacesCenter().getY() +")");

                        photo.addPerson(new Person(face , new RelativePositionVector(angle , dist)));
                    }

                    addPhotoToMap(sameFacesPhotos, photo, faces.size());
                }
//                if has no faces decide what to do
//                addPhotoToMap(sameFacesPhotos, photo, faces.size());
            }


            Log.i("TESTING" , "running on map");

            if (sameFacesPhotos.size() == 1) {
                result.add(series);
            } else {
                for (Map.Entry<Integer, List<Photo>> entry : sameFacesPhotos.entrySet()) {
                    PhotoSeries s = new PhotoSeries(entry.getValue());
                    s.setFaceMaxDistanceToCenter();
                    result.add(s);
                }
            }
        }
        return result;
    }


    private void setTotalFacesCenterInPhoto(Photo photo, List<Face> faces) {
        Position centerGravity;
        centerGravity = calcFacesCenterGravity(faces);
        photo.setTotalFacesCenter(centerGravity);
    }


    private void addPhotoToMap(Map<Integer,List<Photo>> sameFacesPhotos, Photo photo, int key) {
        List<Photo> photos = sameFacesPhotos.get(key);

        //not exist in map
        if (photos == null) {
            photos = new ArrayList<Photo>();
            photos.add(photo);
            sameFacesPhotos.put(key, photos);
        } else {
            //exist in map
            photos.add(photo);
        }
    }



    private Position calcFacesCenterGravity(List<Face> faces) {
        double sumX = 0 , sumY = 0;
        int size = faces.size();

        for (Face face : faces) {
            sumX += face.getPosition().getX();
            sumY += face.getPosition().getY();
        }
        return new Position(sumX/size , sumY/size);
    }


    //returning series containing only one photo
    private void filterAllOnePhotoSeries() {

        ArrayList <PhotoSeries> photosToBeRemoved = new ArrayList<>();
        for (int i = 0; i < mSameFeaturesSeriesList.size() ; i++) {
            if (mSameFeaturesSeriesList.get(i).getNumOfPhotos() == 1) {
                mOutput.add(mSameFeaturesSeriesList.get(i).getPhoto(0).getPath());
                photosToBeRemoved.add(mSameFeaturesSeriesList.get(i));
            }
        }
        mSameFeaturesSeriesList.removeAll(photosToBeRemoved);
    }



    private List<Photo> setPhotos(List<String> mImagesPath) {

        List<Photo> photos = new ArrayList<>();

        for (String path : mImagesPath) {
            photos.add( new Photo(path));
        }
        return photos;
    }



    //this function create series list from the initial photos
    public List<PhotoSeries> generateSeriesByFeatures() {

        List<PhotoSeries> foundSeries =  new ArrayList<>();;
        boolean hasFoundSeries;
        PhotoSeries photoSeries = new PhotoSeries();

        if (mAllPhotos.isEmpty()) {
            return null;
        }

        photoSeries.addPhoto(mAllPhotos.get(0));
        foundSeries.add(photoSeries);

        for (int i = 1 ; i < mAllPhotos.size() ; i++){

            hasFoundSeries = false;

            for (int j = 0 ; j < foundSeries.size() ; j++) {

                if (foundSeries.get(j).getPhoto(0).similarTo( mAllPhotos.get(i))){
                    foundSeries.get(j).addPhoto( mAllPhotos.get(i));
                    hasFoundSeries = true;
                    break;
                }
            }
            if (!hasFoundSeries){
                PhotoSeries newPhotoSeries = new PhotoSeries();
                newPhotoSeries.addPhoto( mAllPhotos.get(i));
                foundSeries.add(newPhotoSeries);
            }
        }
        return foundSeries;
    }
}
