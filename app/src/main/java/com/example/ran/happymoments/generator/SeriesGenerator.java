package com.example.ran.happymoments.generator;

import android.content.Context;

import com.example.ran.happymoments.generator.face.Face;
import com.example.ran.happymoments.generator.face.Position;
import com.example.ran.happymoments.generator.photo.Photo;
import com.example.ran.happymoments.generator.series.PhotoSeries;

import java.util.ArrayList;
import java.util.List;

public class SeriesGenerator {

    List<String> mInput;
    List<String> mOutput;

    List<PhotoSeries> mAllSeries;
    List<Photo> mAllPhotos;

    private Context mContext;
    private FaceExtractor mFaceExtractor;


    public SeriesGenerator(Context context , List<String> imagesPath) {
        mContext = context;
        mInput = imagesPath;
        mAllPhotos = setPhotos(imagesPath);
        mFaceExtractor = new FaceExtractor();
    }


    public List<String> detect() {

//        mAllPhotos = setPhotos(mInput);

        ///////////// [SERIES PART] ///////////////////////////////////
        mAllSeries = generateSeriesByFeatures();
        filterAllOnePhotoSeries();
        /////////////////////////////////////////////////////////////


        ///////////// [FACES PART] ///////////////////////////////////

        List <Face> faces;
        Position centerGravity;
        for (int i = 0 ; i < mAllSeries.size() ; i++) {
            for (int j = 0 ; j < mAllSeries.get(i).getPhotos().size() ; j++) {
                faces = mFaceExtractor.detectFaces(mContext , mAllSeries.get(i).getPhoto(j).getPath());
                if (!faces.isEmpty()) {
                    mAllSeries.get(i).getPhoto(j).setFaces(faces);
                    centerGravity = calcFacesCenterGravity(faces);
                    mAllSeries.get(i).getPhoto(j).setFacesCenterGravity(centerGravity);
                }
            }
        }
        ///////////////////////////////////////////////////////////////

        //now each photo contains array of faces and center gravity

        return null;
    }



    private Position calcFacesCenterGravity(List<Face> faces) {
        double sumX = 0 , sumY = 0;

        for (Face face : faces) {
            sumX += face.getPosition().getX();
            sumY += face.getPosition().getY();
        }
        return new Position(sumX/faces.size() , sumY/faces.size());
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
