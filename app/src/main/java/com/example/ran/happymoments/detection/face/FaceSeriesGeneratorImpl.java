package com.example.ran.happymoments.detection.face;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ran.happymoments.detection.DetectionActivity;
import com.example.ran.happymoments.detection.series.Photo;
import com.example.ran.happymoments.detection.series.PhotoSeries;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class FaceSeriesGeneratorImpl implements FaceSeriesGenerator {

    List<PhotoSeries> mFoundSeries;

    public FaceSeriesGeneratorImpl() {

    }


    public FaceSeriesGeneratorImpl(List<Photo> photos){
//        if (photos != null && !photos.isEmpty()) {
//            mFoundSeries = new ArrayList<>();
//        }
    }


    @Override
    public List<PhotoSeries> detectSeries(List<Photo> photos) {
        return null;
    }


//    //    /*
////    reference:
////    https://search-codelabs.appspot.com/codelabs/face-detection
////     */
//    private void detectFace(int index){
//        String path = mInputImagesPath.get(index);
//        Bitmap bitmap;
//        FileInputStream stream;
//        mImageView = (ImageView) gridView.getChildAt(index);
//
//        try {
//            stream = new FileInputStream(path);
//            bitmap = BitmapFactory.decodeStream(stream);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        FaceDetector detector = new FaceDetector.Builder(getApplicationContext())
//                .setTrackingEnabled(false)
//                .setLandmarkType(FaceDetector.ALL_LANDMARKS)
//                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
//                .build();
//
//        if (!detector.isOperational()) {
//            Log.w(TAG, "Face detector dependencies are not yet available.");
//            return;
//        }
//
//        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
//        SparseArray<Face> faces = detector.detect(frame);
//
//
//        detector.release();
//
////        //Create a Paint object for drawing with
//        Paint myRectPaint = new Paint();
//        myRectPaint.setStrokeWidth(5);
//        myRectPaint.setColor(Color.GREEN);
//        myRectPaint.setStyle(Paint.Style.STROKE);
//
//        Paint landmarksPaint = new Paint();
//        landmarksPaint.setStrokeWidth(10);
//        landmarksPaint.setColor(Color.RED);
//        landmarksPaint.setStyle(Paint.Style.STROKE);
//
//        Paint smilingPaint = new Paint();
//        smilingPaint.setStrokeWidth(4);
//        smilingPaint.setColor(Color.YELLOW);
//        smilingPaint.setStyle(Paint.Style.STROKE);
//
//        boolean somebodySmiling = false;
////
////        //Create a Canvas object for drawing on
//        Bitmap tempBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);
//        Canvas tempCanvas = new Canvas(tempBitmap);
//        tempCanvas.drawBitmap(bitmap, 0, 0, null);
//
////        //Draw Rectangles on the Faces
//        for(int i=0; i<faces.size(); i++) {
//            Face thisFace = faces.valueAt(i);
//            float x1 = thisFace.getPosition().x;
//            float y1 = thisFace.getPosition().y;
//            float x2 = x1 + thisFace.getWidth();
//            float y2 = y1 + thisFace.getHeight();
//            tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
////
////            //get Landmarks for the first face
//            List<Landmark> landmarks = thisFace.getLandmarks();
//            for(int l=0; l<landmarks.size(); l++){
//                PointF pos = landmarks.get(l).getPosition();
//                tempCanvas.drawPoint(pos.x, pos.y, landmarksPaint);
//            }
////
////            //check if this face is Smiling
//            final float smilingAcceptProbability = 0.5f;
//            float smilingProbability = thisFace.getIsSmilingProbability();
//            if(smilingProbability > smilingAcceptProbability){
//                tempCanvas.drawOval(new RectF(x1, y1, x2, y2), smilingPaint);
//                somebodySmiling = true;
//            }
//        }
//
//        mImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
////
//        if(somebodySmiling){
//            Toast.makeText(DetectionActivity.this,
//                    "Done - somebody is Smiling",
//                    Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(DetectionActivity.this,
//                    "Done - nobody is Smiling",
//                    Toast.LENGTH_LONG).show();
//        }
//    }

}
