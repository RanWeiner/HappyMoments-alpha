package com.example.ran.happymoments.common;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ran.happymoments.generator.face.Position;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

public class Utils {

    private Context mContext;


    public Utils(Context context) {
        this.mContext = context;
    }

    // Reading file paths from SDCard
    public ArrayList<String> getFilePaths() {
        ArrayList<String> filePaths = new ArrayList<String>();

        File directory = new File(Environment.getExternalStorageDirectory()
                        + File.separator + AppConstants.HAPPY_MOMENTS_ALBUM);

        // check for directory
        if (directory.isDirectory()) {
            // getting list of file paths
            File[] listFiles = directory.listFiles();

            if (listFiles.length > 0) {

                for (int i = 0; i < listFiles.length; i++) {

                    // get file path
                    String filePath = listFiles[i].getAbsolutePath();

                    // check for supported file extension
                    if (IsSupportedFile(filePath)) {
                        // Add image path to array list
                        filePaths.add(filePath);
                    }
                }
            } else {
                // image directory is empty
                Toast.makeText(mContext, AppConstants.HAPPY_MOMENTS_ALBUM + " is empty.", Toast.LENGTH_LONG).show();
            }

        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setTitle("Error!").setMessage(AppConstants.HAPPY_MOMENTS_ALBUM + " directory path is not valid!");
            alert.setPositiveButton("OK", null);
            alert.show();
        }

        return filePaths;
    }

    // Check supported file extensions
    private boolean IsSupportedFile(String filePath) {
        String extension = filePath.substring((filePath.lastIndexOf(".") + 1),
                filePath.length());

        if (AppConstants.SUPPORTED_FILE_EXTENSIONS.contains(extension.toLowerCase(Locale.getDefault())))
            return true;
        else
            return false;

    }


    //return the angle degree from one point to another point
    public double calcAngleBetweenPoints(double srcX ,double srcY , double targetX , double targetY) {
        double angle  = Math.toDegrees(Math.atan2(targetY - srcY, targetX - srcX));

        if (angle < 0) {
            angle += 360;
        }
        return  angle;
    }

    public double calcEuclidDistance(double x1 , double y1 , double x2 , double y2) {
        double x ,y;

        x = x1 - x2;
        y = y1 - y2;

        return Math.sqrt(x*x + y*y);
    }


    // Getting screen width
    public int getScreenWidth() {
        int columnWidth;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        final Point point = new Point();
        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        columnWidth = point.x;
        return columnWidth;
    }




//https://stackoverflow.com/questions/11983654/android-how-to-add-an-image-to-an-album
    public void saveImageToExternal(String imgName, Bitmap bm) throws IOException {

        final String appDirectoryName = AppConstants.HAPPY_MOMENTS_ALBUM;

        //Create Path to save Image
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/"+ appDirectoryName);

        //if there is no directory exist
        if (!path.isDirectory())
            path.mkdirs();

        File imageFile = new File(path, imgName+".png"); // Imagename.png

        FileOutputStream out = new FileOutputStream(imageFile);
        try{
            bm.compress(Bitmap.CompressFormat.PNG, 100, out); // Compress Image
            out.flush();
            out.close();

            // Tell the media scanner about the new file so that it is
            // immediately available to the user.
            MediaScannerConnection.scanFile(mContext,new String[] { imageFile.getAbsolutePath() }, null,new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                    Log.i("ExternalStorage", "Scanned " + path + ":");
                    Log.i("ExternalStorage", "-> uri=" + uri);
                }
            });
        } catch(Exception e) {
            throw new IOException();
        }
    }

    public ArrayList<String> getImagesPathFromExternal() {
        ArrayList<String> filePaths = new ArrayList<String>();
        final String appDirectoryName = AppConstants.HAPPY_MOMENTS_ALBUM;
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+"/"+ appDirectoryName);
        if (!path.isDirectory()) {
            AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
            alert.setTitle("Error!").setMessage(AppConstants.HAPPY_MOMENTS_ALBUM + " directory path is not valid!");
            alert.setPositiveButton("OK", null);
            alert.show();
            return null;
        }

        File[] listFiles = path.listFiles();
        if (listFiles.length > 0) {

            for (int i = 0; i < listFiles.length; i++) {

                // get file path
                String filePath = listFiles[i].getAbsolutePath();

                // check for supported file extension
                if (IsSupportedFile(filePath)) {
                    // Add image path to array list
                    filePaths.add(filePath);
                }
            }
        } else {
            Toast.makeText(mContext, AppConstants.HAPPY_MOMENTS_ALBUM + " is empty.", Toast.LENGTH_LONG).show();
        }

        return filePaths;
    }


    public boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)){
            return true;
        }
        return false;
    }

    //Resizing image size
    public static Bitmap decodeFile(String filePath, int width, int height) {
        try {

            File f = new File(filePath);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            int scale = 1;
            while (o.outWidth / scale / 2 >= width && o.outHeight / scale / 2 >= height)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }




}
