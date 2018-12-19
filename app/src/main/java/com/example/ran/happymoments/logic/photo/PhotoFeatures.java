package com.example.ran.happymoments.logic.photo;

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
import java.util.Calendar;

import static android.support.constraint.Constraints.TAG;

public class PhotoFeatures {
    private final double HISTOGRAM_THRESHOLD = 70;
    private final double DISTANCE_THRESHOLD = 0.1;
    private Calendar dateTime;
    private PhotoLocation photoLocation;
    private Mat histogram;
    private String orientation;




    public PhotoFeatures(String imagePath,  ExifInterface exifInterface) {
        setDate(exifInterface);
        setPhotoLocation(exifInterface);
        setOrientation(exifInterface);

        //some of them might be null - checked
        Log.d(TAG, "dateTime: " + dateTime.getTime());
        Log.d(TAG, "photoLocation: " + photoLocation);
        Log.d(TAG, "orientation: " + orientation.toString());
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
            this.dateTime = StringToCalendar(dateString);
        }
    }

    public Calendar getDateTime() {
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

    private Calendar StringToCalendar(String dateString){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        try {
            calendar.setTime(format.parse(dateString));
        } catch (ParseException e) {
            Log.d("Exception:", "Parsing went wrong! " + dateString);
            e.printStackTrace();
        }
        return calendar;
    }



    public boolean compareFeatures(PhotoFeatures otherPhotoFeatures) {
            //check if null
//        if(getDistance(this.getPhotoLocation(), otherPhotoFeatures.getPhotoLocation()) > DISTANCE_THRESHOLD)
//            return false;
//
//            if(!areDateSimilar(this.getDateTime(), otherPhotoFeatures.getDateTime()))
//                return false;

//            if(!orientation.equals(otherPhotoFeatures.getOrientation()))
////                return false;


        if (!compareDateTime(otherPhotoFeatures.dateTime)) {
            return false;
        }

        if (!compareLocations(otherPhotoFeatures.photoLocation )) {
            return false;
        }

        if (!compareHist(otherPhotoFeatures.getHistogram())) {
            return false;
        }

        return true;
    }


    private boolean compareDateTime(Calendar other) {
        if (this.dateTime == null && other == null) {
            return true;
        }

        if (this.dateTime == null || other ==null) {
            return false;
        }
        return areDateSimilar(this.dateTime , other);
    }

    private boolean compareLocations(PhotoLocation other) {
        if (this.photoLocation == null && other == null) {
            return true;
        }

        if (this.photoLocation == null || other ==null) {
            return false;
        }

        double dist = calcDistanceBetweenLocations(this.photoLocation , other);
        if (dist > DISTANCE_THRESHOLD) {
            return false;
        }
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

    public double calcDistanceBetweenLocations(PhotoLocation p1, PhotoLocation p2) {
        double theta = p1.getLongitude() - p2.getLongitude();
        double dist = Math.sin(deg2rad(p1.getLatitude())) * Math.sin(deg2rad(p2.getLatitude())) + Math.cos(deg2rad(p1.getLatitude())) * Math.cos(deg2rad(p2.getLatitude())) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;
        return dist;
        }



//    public double distance(double lat1, double lon1, double lat2, double lon2) {
//        double theta = lon1 - lon2;
//        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;
//        dist = dist * 1.609344;
//        return (dist);
//    }


    public  double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    public  double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

//    public boolean areDateSimilar(Date d1, Date d2)
//    {
//        boolean lessThanMinute;
//        long diff = d1.getTime() - d2.getTime();
//        int diffMin = (int) (diff / (60 * 1000));
//
//        lessThanMinute = diffMin < 1;
//
//        if(d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth() && d1.getDay() == d2.getDay() &&
//                d1.getHours() == d2.getHours() && lessThanMinute)
//            return true;
//
//        return false;
//    }

    public boolean areDateSimilar(Calendar c1, Calendar c2)
    {
        long diff = Math.abs(c1.getTimeInMillis() - c2.getTimeInMillis());
       // long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);


        if(diffHours == 0 && diffMinutes < 1)
            return true;
        return false;
    }
}
