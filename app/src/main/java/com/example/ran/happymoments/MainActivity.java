package com.example.ran.happymoments;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.net.Uri;
import android.os.Build;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;



public class MainActivity extends AppCompatActivity  {


//    private static final int RQS_LOADIMAGE = 1;
//    private Button btnLoad, btnDetFace;
//    private ImageView imgView;
//    private Bitmap myBitmap;
//    int PICK_IMAGE_MULTIPLE = 1;


    private static final int READ_STORAGE_PERMISSION = 4000;
    private static final int LIMIT = 5;
    private ImageView imageView;
    private TextView txImageSelects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txImageSelects = (TextView) findViewById(R.id.txImageSelects);
        imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!checkPermissionForExternalStorage()) {
                        requestStoragePermission();
                    } else {
                        // opening custom gallery
                        goToGalleryAlbum();
                    }
                }else{
                    goToGalleryAlbum();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images
            ArrayList<Image> images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

            for (int i = 0; i < images.size(); i++) {
                Photo p = new Photo(images.get(i).path);

                Uri uri = Uri.fromFile(new File(images.get(i).path));

                Glide.with(this).load(uri)
                        .placeholder(R.color.colorAccent)
                        .override(400, 400)
                        .crossFade()
                        .centerCrop()
                        .into(imageView);

                txImageSelects.setText(txImageSelects.getText().toString().trim()
                        + "\n" +
                        String.valueOf(i + 1) + ". " + String.valueOf(uri));
            }

        }
    }


    private void goToGalleryAlbum() {
// opening custom gallery
        Intent intent = new Intent(MainActivity.this, AlbumSelectActivity.class);
        intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, LIMIT);
        startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
    }

    public boolean checkPermissionForExternalStorage() {
            int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        }

        public boolean requestStoragePermission() {
            if (ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    this.requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_STORAGE_PERMISSION);
                }
            } else {
            }
            return false;
        }

}

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
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
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                startActivityForResult(intent, RQS_LOADIMAGE);
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





//
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

