package com.example.ran.happymoments;

import android.media.ExifInterface;
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

    private Date dateTime;
    private PhotoLocation photoLocation;
    private Mat histogram;
    private String orientation;


    public PhotoFeatures(String imagePath,  ExifInterface exifInterface) {
        setDate(imagePath , exifInterface);
        setPhotoLocation(imagePath , exifInterface);
        setOrientation(imagePath , exifInterface);

        Log.d(TAG, "dateTime: " + dateTime);
        Log.d(TAG, "photoLocation: " + photoLocation);
        Log.d(TAG, "orientation: " + orientation);

        setHistogram(imagePath);
    }

    private void setHistogram(String imagePath) {
        Mat img = Imgcodecs.imread(imagePath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        Mat histogram = new Mat();
        MatOfFloat ranges = new MatOfFloat(0f, 256f);
        MatOfInt histSize = new MatOfInt(256);

        Imgproc.calcHist(Arrays.asList(img), new MatOfInt(0), new Mat(), histogram, histSize, ranges);

        this.histogram = histogram;
    }

    private void setOrientation(String imagePath, ExifInterface exifInterface) {
        this.orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
    }



    private void setPhotoLocation(String imagePath, ExifInterface exifInterface) {

        float[] coordinates = {0,0};
        if (exifInterface.getLatLong(coordinates)){
            this.photoLocation = new PhotoLocation(coordinates[0] , coordinates[1]);
        }
        else {
            this.photoLocation = null;
        }
    }

    private void setDate(String imagePath ,ExifInterface exifInterface) {
        this.dateTime = StringToDate(exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
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
            System.out.println(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }




}
