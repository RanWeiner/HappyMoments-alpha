package com.example.ran.happymoments.common;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
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

        //v2
        final String myfolder = Environment.getExternalStorageDirectory()+ "/" + appDirectoryName;
        final File f = new File(myfolder);

        //v1
        final File imageRoot = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getAbsolutePath(), appDirectoryName);



        //if there is no directory - create
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



    public void savePicture(Bitmap bm, File f)
    {
        OutputStream fOut = null;
        String strDirectory = Environment.getExternalStorageDirectory().toString();

        try {
            fOut = new FileOutputStream(f);

            /**Compress image**/
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
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
