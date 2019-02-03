package com.example.ran.happymoments.logic;

import android.content.Context;
import android.util.Log;

import com.example.ran.happymoments.common.RelativePositionVector;
import com.example.ran.happymoments.logic.face.FaceMatcher;
import com.example.ran.happymoments.logic.face.Face;
import com.example.ran.happymoments.common.Position;
import com.example.ran.happymoments.logic.face.FaceExtractorMobileVision;
import com.example.ran.happymoments.logic.photo.Person;
import com.example.ran.happymoments.logic.photo.Photo;
import com.example.ran.happymoments.logic.series.PhotoSeries;
import com.example.ran.happymoments.logic.photo.Ranker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class SeriesGenerator {

    private ArrayList<String> mInputPhotosPath;
    private ArrayList<String> mPhotosOutputPath;

    private ArrayList<Photo> mPhotosInput;
    private ArrayList<Photo> mPhotosOutput;

    private ArrayList<PhotoSeries> mSeriesList;
    private Context mContext;
    private FaceExtractorMobileVision mFaceExtractor;

    private Ranker mRanker;
    private FaceMatcher mMatcher;




    public SeriesGenerator(Context context , ArrayList<String> inputPhotosPath) {
        mContext = context;
        mInputPhotosPath = inputPhotosPath;
        mPhotosOutputPath = new ArrayList<>();

        mPhotosInput = setPhotos(mInputPhotosPath);
        mPhotosOutput = new ArrayList<>();

        mFaceExtractor = new FaceExtractorMobileVision();
        mRanker = new Ranker();
        mMatcher = new FaceMatcher();
    }



    public ArrayList<String> detect() {

        ///////////////////// [SERIES PART] /////////////////////

        mSeriesList = generateAllSeries();

        filterAllOnePhotoSeries();

        printSeries(mSeriesList);


        ///////////////////// [FACE CORRESPONDENCE & RANKING] /////////////////////

        for (PhotoSeries series : mSeriesList) {

            //match all persons in the series of photo by their id's
            mMatcher.matchPersons(series);

            //in each photo in each series set every person face importance to value between 0-1
            setImportanceFaces(series);

            //finding the highest ranked photo in series
            int highestRankedPhotoIndex = 0;
            double highestRank = mRanker.rankPhoto(series.getPhoto(highestRankedPhotoIndex));
            double currentRank;
            Log.i("SCORE" , "photo= " +series.getPhoto(0).getPath()+  ", rank= " + highestRank);

            for (int i = 1 ; i < series.getPhotos().size() ; i++) {
                currentRank = mRanker.rankPhoto(series.getPhoto(i));
                Log.i("SCORE" , "photo= " +series.getPhoto(i).getPath()+  ", rank= " + currentRank);
                if (currentRank > highestRank) {
                    highestRank = currentRank;
                    highestRankedPhotoIndex = i;
                }
            }

            Photo p = series.getPhoto(highestRankedPhotoIndex);
            mPhotosOutput.add(p);
        }

        mPhotosOutputPath = setOutputPath();
        return mPhotosOutputPath;
    }


    private ArrayList<String> setOutputPath() {
        ArrayList<String> rv = new ArrayList<>();

        for (Photo p : mPhotosOutput) {
            rv.add(p.getPath());
        }
        return rv;
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


    private ArrayList<PhotoSeries>  generateAllSeries() {
        ArrayList<PhotoSeries> rv = new ArrayList<>();
        Map <Integer , List<Photo>> numOfFacesMap = new HashMap<>();
        List<Face> faces;

        for (Photo photo : mPhotosInput){
            faces = mFaceExtractor.detectFaces(mContext, photo);

            if (!faces.isEmpty()) {
                saveFacesData(photo , faces);
                addPhotoToMap(numOfFacesMap , photo , faces.size());
            }
        }

        for (Map.Entry<Integer, List<Photo>> entry : numOfFacesMap.entrySet()) {
            if (entry.getValue().size() == 1) {
                mPhotosOutput.addAll(entry.getValue());
            } else {
                rv.addAll(generateSeriesByFeatures(entry.getValue()));
            }
        }
        return rv;
    }



    private void saveFacesData(Photo photo, List<Face> faces) {

        if(faces.size() > 1) {
            for (int i = 0 ; i < faces.size() ; i++) {
                photo.addPerson(new Person(i, faces.get(i)));
            }
        } else {
            photo.addPerson(new Person(0, faces.get(0)));
        }
    }


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


    private void filterAllOnePhotoSeries() {

        ArrayList <PhotoSeries> photosToBeRemoved = new ArrayList<>();

        for (int i = 0; i < mSeriesList.size() ; i++) {

            if (mSeriesList.get(i).getPhotos().size() == 1) {
                mPhotosOutput.add(mSeriesList.get(i).getPhoto(0));
                photosToBeRemoved.add(mSeriesList.get(i));
            }
        }
        mSeriesList.removeAll(photosToBeRemoved);
    }


    private ArrayList<Photo> setPhotos(ArrayList<String> imagesPath) {
        ArrayList <Photo> rv = new ArrayList<>();

        for (String path : imagesPath) {
            rv.add( new Photo(path));
        }
        return rv;
    }


    //this function create series list from the initial photos
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
