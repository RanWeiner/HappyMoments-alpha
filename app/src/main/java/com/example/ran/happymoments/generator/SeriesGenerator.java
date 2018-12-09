package com.example.ran.happymoments.generator;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.ran.happymoments.common.Utils;
import com.example.ran.happymoments.generator.face.Face;
import com.example.ran.happymoments.common.Position;
import com.example.ran.happymoments.generator.photo.Photo;
import com.example.ran.happymoments.generator.series.PhotoSeries;

import java.util.ArrayList;
import java.util.List;

public class SeriesGenerator {

    private List<String> mInput;
    private List<String> mOutput;

    private List<PhotoSeries> mAllSeries;
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

//        mAllPhotos = setPhotos(mInput);

        ///////////// [SERIES PART] ///////////////////////////////////
        mAllSeries = generateSeriesByFeatures();

        printSerieses();

        filterAllOnePhotoSeries();
        /////////////////////////////////////////////////////////////


        ///////////// [FACES PART] ///////////////////////////////////

        List <Face> faces;
        Position centerGravity;
        for (int i = 0 ; i < mAllSeries.size() ; i++) {

            //TODO in function !

            for (int j = 0 ; j < mAllSeries.get(i).getPhotos().size() ; j++) {

                faces = mFaceExtractor.detectFaces(mContext , mAllSeries.get(i).getPhoto(j).getPath());
                Log.i("FaceXXXXX" , "series : " + mAllSeries.get(i).getPhoto(j).getPath());
                Log.i("FaceXXXXX" ,"numOfFaces= " + faces.size());

                if (!faces.isEmpty()) {
                    mAllSeries.get(i).getPhoto(j).setFaces(faces);
                    centerGravity = calcFacesCenterGravity(faces);
                    mAllSeries.get(i).getPhoto(j).setFacesCenterGravity(centerGravity);

                    for (Face face : faces) {

                        double angle = face.getPosition().calcAngle(centerGravity);
                        double normalizedAngle = Utils.normalize(angle , 360 ,0);
                        double dist = face.getPosition().calcEuclidDistance(centerGravity);
                        double width =  mAllSeries.get(i).getPhoto(j).getBitmap().getWidth();
                        double height =  mAllSeries.get(i).getPhoto(j).getBitmap().getHeight();
                        double diagonal = Utils.pitagoras(width , height);
                        double normalizedDist = Utils.normalize(dist, diagonal,0);

                        face.setAngleFromGravityCenter(normalizedAngle);
                        face.setDistanceFromGravityCenter(normalizedDist);
                    }
                }
            }
        }



        ///////////////////////////////////////////////////////////////

        //now each photo contains array of faces and center gravity
        printFaces();


        return mOutput;
    }





    private void printSerieses() {
        //just for debug
        for (PhotoSeries series : mAllSeries) {
            Log.i("SeriesXXXX" ,"Series "+ series.getId());
            for (Photo photo : series.getPhotos()) {
                Log.i("SeriesXXXX" ,"Series "+ series.getId() + "photo= "+ photo.getPath());
            }
        }
    }

    public void printFaces() {
        for (PhotoSeries series : mAllSeries) {
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
        for (int i = 0 ; i < mAllSeries.size() ; i++) {
            if (mAllSeries.get(i).getNumOfPhotos() == 1) {
                mOutput.add(mAllSeries.get(i).getPhoto(0).getPath());
                photosToBeRemoved.add(mAllSeries.get(i));
            }
        }
        mAllSeries.removeAll(photosToBeRemoved);
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
