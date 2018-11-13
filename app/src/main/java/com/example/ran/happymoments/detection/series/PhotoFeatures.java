package com.example.ran.happymoments.detection.series;

import android.support.media.ExifInterface;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;

public class PhotoFeatures {
    private final double HISTOGRAM_THRESHOLD = 70;
    private Date dateTime;
    private PhotoLocation photoLocation;
    private Mat histogram;
    private String orientation;




    public PhotoFeatures(String imagePath,  ExifInterface exifInterface) {
        setDate(exifInterface);
        setPhotoLocation(exifInterface);
        setOrientation(exifInterface);

        //some of them might be null - checked
        Log.d(TAG, "dateTime: " + dateTime);
        Log.d(TAG, "photoLocation: " + photoLocation);
        Log.d(TAG, "orientation: " + orientation);
        Log.d(TAG, "imagePath: " + imagePath);
        setHistogram(imagePath);
        Log.d(TAG, "Mat: " + histogram.toString());
    }

    private void setHistogram(String imagePath) {
        Mat img = Imgcodecs.imread(imagePath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        Mat histogram = new Mat();
        MatOfFloat ranges = new MatOfFloat(0f, 256f);
        MatOfInt histSize = new MatOfInt(256);
        Imgproc.calcHist(Arrays.asList(img), new MatOfInt(0), new Mat(), histogram, histSize, ranges);
        this.histogram = histogram;
    }

    private void setOrientation(ExifInterface exifInterface) {
        this.orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
    }



    private void setPhotoLocation(ExifInterface exifInterface) {
        float[] coordinates = {0,0};
        if (exifInterface.getLatLong(coordinates)){
            this.photoLocation = new PhotoLocation(coordinates[0] , coordinates[1]);
        }
        else {
            this.photoLocation = null;
        }
    }

    private void setDate(ExifInterface exifInterface) {

        String dateString = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);

        Log.d(TAG, "dateString: " + dateString);

        if (dateString != null) {
            this.dateTime = StringToDate(exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
        }
    }

    public Date getDateTime() {
        return dateTime;
    }

    public PhotoLocation getPhotoLocation() {
        return photoLocation;
    }

    public Mat getHistogram() {
        return histogram;
    }

    public String getOrientation() {
        return orientation;
    }

    private Date StringToDate(String dateString){
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }



    public boolean compareFeatures(PhotoFeatures otherPhotoFeatures) {

        if (!compareHist(otherPhotoFeatures.getHistogram()))
            return false;

        return true;
    }

    public boolean compareHist(Mat otherHist){
        //Computes the correlation between the two histograms.
        double res = Imgproc.compareHist(this.histogram, otherHist, Imgproc.CV_COMP_CORREL);
        Double d = new Double(res * 100);

        if (d >= HISTOGRAM_THRESHOLD)
            return true;
        return false;
    }
}
