package com.example.ran.happymoments;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

import static android.support.constraint.Constraints.TAG;

class SeriesDetector {

    ArrayList<PhotoSeries> mFoundSeries;
    ArrayList<Photo> mPhotos;


    public SeriesDetector(ArrayList<Photo> photos) {
        mFoundSeries = new ArrayList<>();
        mPhotos = photos;
    }

    public void detect() {

        while(!mPhotos.isEmpty()) {
            Photo currentPhoto = mPhotos.get(0);

            if (currentPhoto.hasExifData()){
                PhotoSeries series = new PhotoSeries();
                series.addPhoto(mPhotos.remove(0));

                Photo nextPhoto;
                Iterator<Photo> iter = mPhotos.iterator();
                while (iter.hasNext()) {
                    nextPhoto = iter.next();

                    if (currentPhoto.getPhotoFeatures().compareFeatures(nextPhoto.getPhotoFeatures())) {
                        series.addPhoto(nextPhoto);
                        iter.remove();
                    }
                }
                mFoundSeries.add(series);
            }
        }



        //just for debug
        for (PhotoSeries series : mFoundSeries) {
            Log.i(TAG ,"Series "+ series.getId());
            for (Photo photo : series.getPhotos()) {
                Log.i(TAG ,photo.getPath());
            }
        }
    }
}
