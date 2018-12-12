package com.example.ran.happymoments.generator;

import android.content.Context;

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



    public SeriesGenerator(Context context , List<String> imagesPath) {
        mContext = context;
        mInput = imagesPath;
        mAllPhotos = setPhotos(imagesPath);
        mFaceExtractor = new FaceExtractorMobileVision();
        mOutput = new ArrayList<>();
        mRanker = new Ranker();
    }


    public List<String> detect() {

        ///////////////////// [SERIES PART] /////////////////////
        mSameFeaturesSeriesList = generateSeriesByFeatures();
        filterAllOnePhotoSeries();

        ///////////////////// [FACES PART] /////////////////////
        mIdenticalSeriesList = generateSeriesByFaces();

        ///////////////////// [NORMALIZE] /////////////////////
        normalizeVectors();

        ///////////////////// [FACE CORRESPONDENCE] /////////////////////

        FaceMatcher matcher = new FaceMatcher();
        for (PhotoSeries series : mIdenticalSeriesList) {
            matcher.matchPersons(series);
        }

        //now each series has list of photos that contains list of person
        //that each person has a matching person (by id) in another photo


        ///////////////////// [RANK] /////////////////////


        for (PhotoSeries series: mIdenticalSeriesList) {
            Photo p = series.getHighestRankedPhoto();
            mOutput.add(p.getPath());
        }

        return mOutput;
    }


    private void normalizeVectors() {
        double maxDistance;

        for (PhotoSeries series: mIdenticalSeriesList) {

            maxDistance = series.getMaxDistanceToFacesCenter();

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

                if (!faces.isEmpty()) {

                    setTotalFacesCenterInPhoto(photo , faces);

                    for (Face face : faces) {

                        double angle = face.getPosition().calcAngle(photo.getTotalFacesCenter());
                        double dist = face.getPosition().calcEuclidDistance(photo.getTotalFacesCenter());

                        photo.addPerson(new Person(face , new RelativePositionVector(angle , dist)));
                    }

                    addPhotoToMap(sameFacesPhotos, photo, faces.size());
                }
//                if has no faces decide what to do
//                addPhotoToMap(sameFacesPhotos, photo, faces.size());
            }

            for (Map.Entry<Integer, List<Photo>> entry : sameFacesPhotos.entrySet()) {
                PhotoSeries s = new PhotoSeries(entry.getValue());
                s.setFaceMaxDistanceToCenter();
                result.add(s);
            }
        }
        return result;
    }


    private void setTotalFacesCenterInPhoto(Photo photo, List<Face> faces) {
        Position centerGravity;

        photo.setFaces(faces);
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
