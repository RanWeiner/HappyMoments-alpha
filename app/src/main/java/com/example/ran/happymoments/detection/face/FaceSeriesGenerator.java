package com.example.ran.happymoments.detection.face;

import android.content.Context;

import com.example.ran.happymoments.detection.series.Photo;
import com.example.ran.happymoments.detection.series.PhotoSeries;

import java.util.List;

public interface FaceSeriesGenerator {

    public List<PhotoSeries> detectSeries(List<Photo> photos);

    public List<Face> detectFaces(Context context , String imagePath);
}
