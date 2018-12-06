package com.example.ran.happymoments.screens.detection.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class GridViewImageAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mImagesPath;
    private int mImageWidth;


    public GridViewImageAdapter(Context context, List<String> photos, int imageWidth) {
        mContext = context;
        mImagesPath = photos;
        mImageWidth = imageWidth;
    }


    @Override
    public int getCount() {
        return this.mImagesPath.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mImagesPath.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }

        // get screen dimensions
        Bitmap image = decodeFile(mImagesPath.get(position), mImageWidth,mImageWidth);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(mImageWidth, mImageWidth));
        imageView.setImageBitmap(image);

        // image view click listener
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext , "Clicked Photo " + position , Toast.LENGTH_SHORT).show();
            }
        });

        return imageView;
    }


    public static Bitmap decodeFile(String filePath, int width, int height) {
        try {
            File f = new File(filePath);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            int scale = 1;
            while (o.outWidth / scale / 2 >= width
                    && o.outHeight / scale / 2 >= height)
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
