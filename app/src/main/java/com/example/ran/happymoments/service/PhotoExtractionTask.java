package com.example.ran.happymoments.service;

import android.content.Context;
import android.support.media.ExifInterface;
import android.util.Log;

import com.example.ran.happymoments.logic.face.Face;
import com.example.ran.happymoments.logic.photo.Person;
import com.example.ran.happymoments.logic.photo.Photo;
import com.example.ran.happymoments.logic.photo.PhotoFeatures;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class PhotoExtractionTask implements Callable<Photo> {

    private String mPath;
    private ExifInterface mExif;
    private List<Face> mFaces;


    public PhotoExtractionTask(String path , ExifInterface exif , List<Face> faces) {
        mPath = path;
        mExif = exif;
        mFaces = faces;
    }

    @Override
    public Photo call() throws Exception {

        PhotoFeatures features = new PhotoFeatures(mPath, mExif);
        Person[] persons = new Person[mFaces.size()];

        for (int i = 0 ; i < mFaces.size() ; i++) {
            persons[i] = new Person(i , mFaces.get(i));
        }
        return new Photo(mPath, features , Arrays.asList(persons));
    }



}
