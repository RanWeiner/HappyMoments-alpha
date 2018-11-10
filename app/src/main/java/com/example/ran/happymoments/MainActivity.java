package com.example.ran.happymoments;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ran.happymoments.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.Landmark;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends AppCompatActivity {

//    private static final int RQS_LOADIMAGE = 1;
//    private Button btnLoad, btnDetFace;
//    private ImageView imgView;
//    private Bitmap myBitmap;


    private Button btn;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    List<String> imagesEncodedList;
    private GridView gvGallery;
    private GalleryAdapter galleryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btn = findViewById(R.id.btn);
        gvGallery = (GridView) findViewById(R.id.gv);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });

    }
//        btnLoad = (Button)findViewById(R.id.btnLoad);
//        btnDetFace = (Button)findViewById(R.id.btnDetectFace);
//        imgView = (ImageView)findViewById(R.id.imgview);
//
//        btnLoad.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//
//                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_MULTIPLE);
////                intent.addCategory(Intent.CATEGORY_OPENABLE);
////                startActivityForResult(intent, RQS_LOADIMAGE);
//            }
//        });
//
//        btnDetFace.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                if(myBitmap == null){
//                    Toast.makeText(MainActivity.this,
//                            "myBitmap == null",
//                            Toast.LENGTH_LONG).show();
//                }else{
//                    detectFace();
//                }
//            }
//        });
//    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                imagesEncodedList = new ArrayList<String>();
                if (data.getData() != null) {

                    Uri mImageUri = data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imageEncoded = cursor.getString(columnIndex);
                    cursor.close();

                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(mImageUri);
                    galleryAdapter = new GalleryAdapter(getApplicationContext(), mArrayUri);
                    gvGallery.setAdapter(galleryAdapter);
                    gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                            .getLayoutParams();
                    mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded = cursor.getString(columnIndex);
                            imagesEncodedList.add(imageEncoded);
                            cursor.close();

                            galleryAdapter = new GalleryAdapter(getApplicationContext(), mArrayUri);
                            gvGallery.setAdapter(galleryAdapter);
                            gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
                            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                                    .getLayoutParams();
                            mlp.setMargins(0, gvGallery.getHorizontalSpacing(), 0, 0);

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}















//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == RQS_LOADIMAGE
//                && resultCode == RESULT_OK){
//
//            if(myBitmap != null){
//                myBitmap.recycle();
//            }
//
//            try {
//                InputStream inputStream =
//                        getContentResolver().openInputStream(data.getData());
//                myBitmap = BitmapFactory.decodeStream(inputStream);
//                inputStream.close();
//                imgView.setImageBitmap(myBitmap);
//
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == PICK_IMAGE_MULTIPLE &&  resultCode == RESULT_OK && null != data.getClipData()) {
//
//
//
//
//                if(data.getClipData() != null) {
//                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
//                    for(int i = 0; i < count; i++)
//                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
//                    //do something with the image (save it to some directory or whatever you need to do with it here)
//                }
//            } else if(data.getData() != null) {
//                String imagePath = data.getData().getPath();
//                //do something with the image (save it to some directory or whatever you need to do with it here)
//            }
//        }
//    }
//}
//
//
//    /*
//    reference:
//    https://search-codelabs.appspot.com/codelabs/face-detection
//     */
//    private void detectFace(){
//
//        //Create a Paint object for drawing with
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
//
//        //Create a Canvas object for drawing on
//        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth(), myBitmap.getHeight(), Bitmap.Config.RGB_565);
//        Canvas tempCanvas = new Canvas(tempBitmap);
//        tempCanvas.drawBitmap(myBitmap, 0, 0, null);
//
//        //Detect the Faces
//
//        //!!!
//        //Cannot resolve method setTrackingEnabled(boolean)
//        //FaceDetector faceDetector = new FaceDetector.Builder(getApplicationContext()).build();
//        //faceDetector.setTrackingEnabled(false);
//
//        FaceDetector faceDetector =
//                new FaceDetector.Builder(getApplicationContext())
//                        .setTrackingEnabled(false)
//                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
//                        .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
//                        .build();
//
//        Frame frame = new Frame.Builder().setBitmap(myBitmap).build();
//        SparseArray<Face> faces = faceDetector.detect(frame);
//
//        //Draw Rectangles on the Faces
//        for(int i=0; i<faces.size(); i++) {
//            Face thisFace = faces.valueAt(i);
//            float x1 = thisFace.getPosition().x;
//            float y1 = thisFace.getPosition().y;
//            float x2 = x1 + thisFace.getWidth();
//            float y2 = y1 + thisFace.getHeight();
//            tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
//
//            //get Landmarks for the first face
//            List<Landmark> landmarks = thisFace.getLandmarks();
//            for(int l=0; l<landmarks.size(); l++){
//                PointF pos = landmarks.get(l).getPosition();
//                tempCanvas.drawPoint(pos.x, pos.y, landmarksPaint);
//            }
//
//            //check if this face is Smiling
//            final float smilingAcceptProbability = 0.5f;
//            float smilingProbability = thisFace.getIsSmilingProbability();
//            if(smilingProbability > smilingAcceptProbability){
//                tempCanvas.drawOval(new RectF(x1, y1, x2, y2), smilingPaint);
//                somebodySmiling = true;
//            }
//        }
//
//        imgView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
//
//        if(somebodySmiling){
//            Toast.makeText(MainActivity.this,
//                    "Done - somebody is Smiling",
//                    Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(MainActivity.this,
//                    "Done - nobody is Smiling",
//                    Toast.LENGTH_LONG).show();
//        }
//
//    }
//}
