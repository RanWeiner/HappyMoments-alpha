package com.example.ran.happymoments.generator;

import android.content.Context;
import android.util.Log;

import com.example.ran.happymoments.common.Utils;
import com.example.ran.happymoments.generator.face.Face;
import com.example.ran.happymoments.common.Position;
import com.example.ran.happymoments.generator.photo.Photo;
import com.example.ran.happymoments.generator.series.Person;
import com.example.ran.happymoments.generator.series.PhotoSeries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class SeriesGenerator {

    private List<String> mInput;
    private List<String> mOutput;

    private List<PhotoSeries> mSameFeaturesAllSeries;
    private List<PhotoSeries> mIdenticalAllSeries;

    private List<Photo> mAllPhotos;

    private Context mContext;
    private FaceExtractor mFaceExtractor;


    public SeriesGenerator(Context context , List<String> imagesPath) {
        mContext = context;
        mInput = imagesPath;
        mAllPhotos = setPhotos(imagesPath);
        mFaceExtractor = new FaceExtractorMobileVision();
        mOutput = new ArrayList<>();
    }


    public List<String> detect() {

        ///////////// [SERIES PART] ///////////////////////////////////
        mSameFeaturesAllSeries = generateSeriesByFeatures();
        filterAllOnePhotoSeries();

        ///////////// [FACES PART] ///////////////////////////////////
        mIdenticalAllSeries = generateSeriesByFaces();

        FaceMatcher matcher = new FaceMatcher();
        matcher.matchFacesToPersons(mIdenticalAllSeries);

        Ranker ranker = new Ranker();
        //rank all series and person and calculate the total rank for photo


        for (PhotoSeries series: mIdenticalAllSeries) {
            Photo best = series.getHighestRankedPhoto();
//            mOutput.add(getHighestRankedPhoto());
        }

        return mOutput;
    }


    private List<PhotoSeries> generateSeriesByFaces() {
        List<PhotoSeries> result = new ArrayList<>();

        List<Face> faces;
        Position centerGravity;
        Map<Integer, List<Photo>> sameFacesPhotos = new HashMap<>();

        for (PhotoSeries series : mSameFeaturesAllSeries) {

            sameFacesPhotos.clear();

            for (Photo photo : series.getPhotos()) {
                faces = mFaceExtractor.detectFaces(mContext, photo.getPath());

                if (!faces.isEmpty()) {
                    photo.setFaces(faces);
                    centerGravity = calcFacesCenterGravity(faces);
                    photo.setFacesCenterGravity(centerGravity);
                    addPhotoToMap(sameFacesPhotos, photo, faces.size());
                    for (Face face : faces) {
                        double angle = face.getPosition().calcAngle(centerGravity);
                        double normalizedAngle = Utils.normalize(angle , 360 ,0);
                        double dist = face.getPosition().calcEuclidDistance(centerGravity);
                        double width =  photo.getBitmap().getWidth();
                        double height =  photo.getBitmap().getHeight();
                        double diagonal = Utils.pitagoras(width , height);
                        double normalizedDist = Utils.normalize(dist, diagonal,0);
                        face.setAngleFromGravityCenter(normalizedAngle);
                        face.setDistanceFromGravityCenter(normalizedDist);
                    }
                }
//                if has no faces decide what to do
//                addPhotoToMap(sameFacesPhotos, photo, faces.size());
            }

            if (sameFacesPhotos.size() == 1) {

            }
            for (Map.Entry<Integer, List<Photo>> entry : sameFacesPhotos.entrySet()) {
                PhotoSeries s = new PhotoSeries(entry.getValue());
                s.initPersons(entry.getKey());
                result.add(s);
            }
        }

        return result;
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


    private void printSerieses() {
        //just for debug
        for (PhotoSeries series : mSameFeaturesAllSeries) {
            Log.i("SeriesXXXX" ,"Series "+ series.getId());
            for (Photo photo : series.getPhotos()) {
                Log.i("SeriesXXXX" ,"Series "+ series.getId() + "photo= "+ photo.getPath());
            }
        }
    }

    public void printFaces() {
        for (PhotoSeries series : mSameFeaturesAllSeries) {
            Log.i("FaceXXXXX" ,"SeriesXXXXX "+ series.getId());
            for (Photo photo : series.getPhotos()) {

                Log.i("FaceXXXXX" , "series : " + photo.getPath());
                Log.i("FaceXXXXX" ,"Face found: " + photo.getFaces().size());

                for (Face face : photo.getFaces()){
                    Log.i("FaceXXXXX" ,"Face: Smiling Prob = " + face.isSmiling() + " Eyes open Prob = " + face.areEyesOpen());
                    Log.i("FaceXXXXX" ,"angle from center= " + face.getAngleFromGravityCenter());
                    Log.i("FaceXXXXX" ,"distance from center" + face.getDistanceFromGravityCenter());
                }
            }
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
        for (int i = 0; i < mSameFeaturesAllSeries.size() ; i++) {
            if (mSameFeaturesAllSeries.get(i).getNumOfPhotos() == 1) {
                mOutput.add(mSameFeaturesAllSeries.get(i).getPhoto(0).getPath());
                photosToBeRemoved.add(mSameFeaturesAllSeries.get(i));
            }
        }
        mSameFeaturesAllSeries.removeAll(photosToBeRemoved);
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
