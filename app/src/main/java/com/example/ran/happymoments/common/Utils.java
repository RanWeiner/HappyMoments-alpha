package com.example.ran.happymoments.common;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
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



    public void createAlbumInGallery(){

        final String appDirectoryName = AppConstants.HAPPY_MOMENTS_ALBUM;

        final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath(), appDirectoryName);

        if (!imageRoot.isDirectory())
            imageRoot.mkdirs();

        final File imageFile = new File(imageRoot , "image1.jpg");



        if (!imageFile.exists()){
            if (!imageFile.mkdir()){
                Log.d(TAG, imageRoot + " cannot be created.");
            }
            else {
                Log.d(TAG, imageRoot + " can be created.");
            }
        }
        Log.d(TAG, imageRoot + " already exist.");
    }

    public File getPublicAlbumStorageDir(String albumName){
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),albumName);
        if (!file.mkdirs()){
            Log.d(TAG, "Directory not created.");
        }
        return file;
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

}
