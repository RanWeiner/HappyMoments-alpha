package com.example.ran.happymoments.screens.detection.views;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ran.happymoments.R;
import com.example.ran.happymoments.generator.photo.Photo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class RecycleViewImageAdapter extends RecyclerView.Adapter<RecycleViewImageAdapter.ViewHolder> {

    private ArrayList<Photo> mPhotos;
    private int mColWidth;


    public RecycleViewImageAdapter(ArrayList<Photo> mPhotos,int mColWidth){
        this.mPhotos = mPhotos;
        this.mColWidth = mColWidth;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_photo_grid,parent,false);
        ViewHolder imageViewHolder = new ViewHolder(view);
        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = setImageFromPath(position);
        holder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.img.setImageBitmap(bitmap);
        holder.img.setOnClickListener(new OnImageClickListener(position));
    }


    @Override
    public int getItemCount() { return mPhotos.size(); }



    public String getPhotoName(int position){
        String photoPath = mPhotos.get(position).getPath();
        return photoPath.substring(photoPath.lastIndexOf("/")+1);
    }

    public Bitmap setImageFromPath(int position){
        String imgPath = mPhotos.get(position).getPath();
        Bitmap myBitmap = decodeFile(imgPath, mColWidth, mColWidth);
        return myBitmap;
    }

    public static Bitmap decodeFile(String filePath, int WIDTH, int HEIGHT) {
        try {

            File f = new File(filePath);

            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_WIDTH = WIDTH;
            final int REQUIRED_HEIGHT = HEIGHT;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_WIDTH && o.outHeight / scale / 2 >= REQUIRED_HEIGHT)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        private ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView)itemView.findViewById(R.id.img);
        }
    }

    class OnImageClickListener implements View.OnClickListener {

        int _postion;

        // constructor
        public OnImageClickListener(int position) {
            this._postion = position;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Img "+ getPhotoName(_postion), Toast.LENGTH_SHORT).show();
        }

    }
}

