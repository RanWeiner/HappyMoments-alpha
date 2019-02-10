package com.example.ran.happymoments.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ran.happymoments.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecycleViewImageAdapter extends RecyclerView.Adapter<RecycleViewImageAdapter.ViewHolder> {


    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    private Context mContext;
    private List<String> galleryList;
    private final OnItemClickListener listener;


    public RecycleViewImageAdapter(Context mContext, List<String> galleryList, OnItemClickListener listener) {
        this.mContext = mContext;
        this.galleryList = galleryList;
        this.listener = listener;
    }


    @NonNull
    @Override
    public RecycleViewImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewImageAdapter.ViewHolder holder, int position) {
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Uri uri = Uri.fromFile(new File(galleryList.get(position)));

        Glide.clear(holder.imageView);
        Glide.with(mContext).load(uri).into(holder.imageView);

        holder.bind(position,listener);
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view_id);
        }


        public void bind(final int position, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {

                @Override public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });

        }
    }

}
