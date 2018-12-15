package com.example.ran.happymoments.generator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;
import android.util.Log;
import android.util.SparseArray;

import com.example.ran.happymoments.generator.face.Eyes;
import com.example.ran.happymoments.generator.face.Face;
import com.example.ran.happymoments.common.Position;
import com.example.ran.happymoments.generator.face.Smile;
import com.google.android.gms.vision.Frame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class FaceExtractorMobileVision implements FaceExtractor {


    public FaceExtractorMobileVision(){

    }

    @Override
    public List<Face> detectFaces(Context context, String imagePath) {

        List<Face> foundFaces = new ArrayList<>();
        Bitmap bitmap = null;
        FileInputStream stream;

        File curFile = new File(imagePath);
        Bitmap rotatedBitmap = null;


        try {
            stream = new FileInputStream(imagePath);
            bitmap = BitmapFactory.decodeStream(stream);
            ExifInterface exif = new ExifInterface(curFile.getPath());
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            int rotationInDegrees = exifToDegrees(rotation);
            Matrix matrix = new Matrix();
            if (rotation != 0f) {matrix.preRotate(rotationInDegrees);}
            rotatedBitmap = Bitmap.createBitmap(bitmap,0,0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);


        }catch(IOException ex){
            Log.e(TAG, "Failed to get Exif data", ex);
        }

//        try {
//            stream = new FileInputStream(imagePath);
//            bitmap = BitmapFactory.decodeStream(stream);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }


        com.google.android.gms.vision.face.FaceDetector detector = new com.google.android.gms.vision.face.FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(com.google.android.gms.vision.face.FaceDetector.ALL_LANDMARKS)
                .setClassificationType(com.google.android.gms.vision.face.FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        if (!detector.isOperational()) {
            Log.w(TAG, "Face detector dependencies are not yet available.");
            return null;
        }

        Frame frame = new Frame.Builder().setBitmap(rotatedBitmap).build();
        SparseArray<com.google.android.gms.vision.face.Face> faces = detector.detect(frame);

        detector.release();

        for (int i = 0; i < faces.size(); i++) {
            Position facePosition = new Position(faces.valueAt(i).getPosition().x, faces.valueAt(i).getPosition().y);
            float width = faces.valueAt(i).getWidth();
            float height = faces.valueAt(i).getHeight();
            float smilingProbability = faces.valueAt(i).getIsSmilingProbability();
            float leftEyeOpenProbability = faces.valueAt(i).getIsLeftEyeOpenProbability();
            float rightEyeOpenProbability = faces.valueAt(i).getIsRightEyeOpenProbability();
            int id = faces.valueAt(i).getId();

            Smile smile = new Smile(smilingProbability);
            Eyes eyes = new Eyes(leftEyeOpenProbability , rightEyeOpenProbability);

            foundFaces.add(new Face(facePosition, width, height, smile,eyes));
        }

        return foundFaces;
    }

    private int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }


}
