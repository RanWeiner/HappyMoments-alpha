package com.example.ran.happymoments.logic;

import android.content.Context;
import android.support.media.ExifInterface;
import android.util.Log;
import android.widget.Toast;

import com.example.ran.happymoments.logic.face.Face;
import com.example.ran.happymoments.logic.photo.Photo;
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

public class PhotoExtractionManager {
    private Context mContext;
    private List<String> mInputPhotosPath;
    private FaceDetector mFaceDetector;

    //store the result
    private ArrayList<String> mPhotosOutputPath;


    public PhotoExtractionManager(Context context , List<String> input, FaceDetector faceDetector) {
        mContext = context;
        mInputPhotosPath = input;
        mFaceDetector = faceDetector;


        //results
        mPhotosOutputPath = new ArrayList<>();
    }



    //return the list of image path of the best photos in each series
    public ArrayList<String> detect() {

        ArrayList<PhotoSeries> seriesList = generateSeries();

        filterAllOnePhotoSeries(seriesList);

        Log.i("Check" , "size = " + seriesList.size());

        //face correspond + ranking



        return mPhotosOutputPath;
    }


    private ArrayList<PhotoSeries>  generateSeries() {
        ArrayList<PhotoSeries> rv = new ArrayList<>();


        List<Photo> allPhotos = extractDataFromImages();

        // distribute photos by the number of persons in photo
        Map<Integer , List<Photo>> map = new HashMap<>();

        for (Photo photo : allPhotos) {
            addPhotoToMap(map , photo , photo.getNumOfPersons());
        }


        for (Map.Entry<Integer, List<Photo>> entry : map.entrySet()) {

            if (!entry.getValue().isEmpty()) {
                //return the photos that are single in the map - no further calculation needed
                if (entry.getValue().size() == 1) {
                    mPhotosOutputPath.add(entry.getValue().get(0).getPath());
                } else {
                    //compare the features of each photo to distribute to more accurate series
                    rv.addAll(generateSeriesByFeatures(entry.getValue()));
                }
            }
        }
        return rv;
    }



    public List<PhotoSeries> generateSeriesByFeatures(List<Photo> photos) {

        List<PhotoSeries> foundSeries =  new ArrayList<>();;
        boolean hasFoundSeries;
        PhotoSeries photoSeries = new PhotoSeries();

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


    private void filterAllOnePhotoSeries(ArrayList<PhotoSeries> seriesList) {

        ArrayList <PhotoSeries> photosToBeRemoved = new ArrayList<>();

        for (int i = 0; i < seriesList.size() ; i++) {

            if (seriesList.get(i).getNumOfPhotos() == 1) {
                mPhotosOutputPath.add(seriesList.get(i).getPhoto(0).getPath());
                photosToBeRemoved.add(seriesList.get(i));
            }
        }
        seriesList.removeAll(photosToBeRemoved);
    }



    public List<Photo> extractDataFromImages() {
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService es = Executors.newFixedThreadPool(cores + 1);

        List<Photo> rv = new ArrayList<>();
        List<Future<Photo>> results = new ArrayList<>();


        for (int i = 0; i < mInputPhotosPath.size(); i++) {
            Callable<Photo> task = new PhotoExtractionTask(mContext, mInputPhotosPath.get(i), mFaceDetector);
            Future<Photo> current = es.submit(task);
            results.add(current);
        }

        es.shutdown();

        Log.i("Check" , "in main, waiting for all results , " +results.size());

        for (Future<Photo> f : results) {
            try {
            Log.i("Check" , "after =  " + f.get().getPath());

                rv.add(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }


        Log.i("Check" , "in main, waiting for all future results , " + rv.size());
        System.out.println("\n\n----------FINISH------------");
        return rv;
    }
}
