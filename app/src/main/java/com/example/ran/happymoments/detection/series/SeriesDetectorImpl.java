package com.example.ran.happymoments.detection.series;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class SeriesDetectorImpl implements SeriesDetector{

    List<PhotoSeries> mFoundSeries;
    Photo[] mPhotos;


    public SeriesDetectorImpl(List<Photo> photos) {
        mFoundSeries = new ArrayList<>();
        mPhotos = photos.toArray(new Photo[0]);
    }


    @Override
    public List<PhotoSeries> detectSeries() {

        boolean hasFoundSeries;
        PhotoSeries photoSeries = new PhotoSeries();

        if (mPhotos.length == 0) {
            return null;
        }

        photoSeries.addPhoto(mPhotos[0]);
        mFoundSeries.add(photoSeries);

        for (int i = 1 ; i < mPhotos.length ; i++){

            hasFoundSeries = false;

            for (int j = 0 ; j < mFoundSeries.size() ; j++) {

                if (mFoundSeries.get(j).getPhoto(0).similarTo( mPhotos[i])){
                    mFoundSeries.get(j).addPhoto(mPhotos[i]);
                    hasFoundSeries = true;
                    break;
                }
            }
            if (!hasFoundSeries){
                PhotoSeries newPhotoSeries = new PhotoSeries();
                newPhotoSeries.addPhoto(mPhotos[i]);
                mFoundSeries.add(newPhotoSeries);
            }
        }
        return mFoundSeries;
    }

}
