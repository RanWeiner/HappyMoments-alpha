package com.example.ran.happymoments.logic.face;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.media.ExifInterface;
import android.util.Log;
import android.util.SparseArray;

import com.example.ran.happymoments.common.Position;
import com.example.ran.happymoments.logic.photo.Photo;
import com.google.android.gms.vision.Frame;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FaceExtractorMobileVision  {


    public FaceExtractorMobileVision(){

    }

    public List<Face> detectFaces(Context context, Photo photo) {

        List<Face> foundFaces = new ArrayList<>();
        Bitmap bitmap = null;

        bitmap = rotateBitmapIfRequired(photo);

        com.google.android.gms.vision.face.FaceDetector detector = new com.google.android.gms.vision.face.FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(com.google.android.gms.vision.face.FaceDetector.ALL_LANDMARKS)
                .setClassificationType(com.google.android.gms.vision.face.FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        if (!detector.isOperational()) {
            return null;
        }

        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
        SparseArray<com.google.android.gms.vision.face.Face> faces = detector.detect(frame);

        detector.release();

        for (int i = 0; i < faces.size(); i++) {
            Position facePosition = new Position(faces.valueAt(i).getPosition().x, faces.valueAt(i).getPosition().y);
            float width = faces.valueAt(i).getWidth();
            float height = faces.valueAt(i).getHeight();
            float smilingProbability = faces.valueAt(i).getIsSmilingProbability();
            float leftEyeOpenProbability = faces.valueAt(i).getIsLeftEyeOpenProbability();
            float rightEyeOpenProbability = faces.valueAt(i).getIsRightEyeOpenProbability();

            Smile smile = new Smile(smilingProbability);
            Eyes eyes = new Eyes(leftEyeOpenProbability , rightEyeOpenProbability);

            foundFaces.add(new Face(facePosition, width, height, smile,eyes));
        }

        return foundFaces;
    }



    private Bitmap rotateBitmapIfRequired(Photo photo) {
        Bitmap bitmap = null;
        FileInputStream stream = null;
        String orientationString = photo.getPhotoFeatures().getOrientation();

        try {
            stream = new FileInputStream(photo.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap = BitmapFactory.decodeStream(stream);
        int orientation = orientationString != null ? Integer.parseInt(orientationString) : ExifInterface.ORIENTATION_NORMAL;

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateBitmapByDegrees(bitmap, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateBitmapByDegrees(bitmap, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateBitmapByDegrees(bitmap, 270);
            default:
                return bitmap;
        }
    }


    public Bitmap rotateBitmapByDegrees(Bitmap bmp, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedBitmap =  Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
        bmp.recycle();
        return rotatedBitmap;
    }

}
