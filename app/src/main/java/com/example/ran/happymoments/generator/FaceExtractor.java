package com.example.ran.happymoments.generator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.SparseArray;

import com.example.ran.happymoments.generator.face.Face;
import com.example.ran.happymoments.generator.face.Position;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class FaceExtractor {


    public FaceExtractor(){

    }



    public List<Face> detectFaces(Context context, String imagePath) {

        List<Face> foundFaces = new ArrayList<>();
        Bitmap bitmap;
        FileInputStream stream;

        try {
            stream = new FileInputStream(imagePath);
            bitmap = BitmapFactory.decodeStream(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        FaceDetector detector = new FaceDetector.Builder(context)
                .setTrackingEnabled(false)
                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        if (!detector.isOperational()) {
            Log.w(TAG, "Face detector dependencies are not yet available.");
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

            foundFaces.add(new Face(facePosition, width, height, smilingProbability, leftEyeOpenProbability, rightEyeOpenProbability));
        }
        return foundFaces;
    }


}
