package com.example.ran.happymoments.detection.face;

import com.example.ran.happymoments.detection.series.Photo;
import com.example.ran.happymoments.detection.series.PhotoSeries;

import java.util.List;

public interface FaceSeriesGenerator {

    public List<PhotoSeries> detectSeries(List<Photo> photos);

}
