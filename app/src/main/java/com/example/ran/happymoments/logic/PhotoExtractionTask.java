package com.example.ran.happymoments.logic;

import android.content.Context;
import android.support.media.ExifInterface;
import android.util.Log;

import com.example.ran.happymoments.logic.face.Face;
import com.example.ran.happymoments.logic.photo.Person;
import com.example.ran.happymoments.logic.photo.Photo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class PhotoExtractionTask implements Callable<Photo> {

    private String mImagePath;
    private Context mContext;
    private FaceDetector mFaceDetector;


    public PhotoExtractionTask(Context context, String imagePath, FaceDetector faceDetector) {
        mImagePath = imagePath;
        mContext = context;
        mFaceDetector = faceDetector;
    }

    @Override
    public Photo call() throws Exception {
//        List<Face> faces = null;
//        synchronized (this) {
//            faces = mFaceDetector.detectFaces(mContext , mImagePath);
//        }

        ExifInterface exifInterface = null;
        try {
            exifInterface = new ExifInterface(mImagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
        List<Face> faces = mFaceDetector.detectFaces(mContext, mImagePath ,orientation );

        Log.i("Check" , "FACESXXSIZE, " +faces.size());

        List<Person> persons = new ArrayList<>();
        int pId = 0;

        if (faces.size() > 0) {
            for (Face face : faces) {
                persons.add(new Person(++pId,face));
            }
            return new Photo(mImagePath, exifInterface, persons);
        }
        Log.i("Check" , "in main, returning null... , " +faces.size());
       return null;
    }
}
