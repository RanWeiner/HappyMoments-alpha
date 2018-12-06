package com.example.ran.happymoments.common;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private ArrayList<String> mFilePaths = new ArrayList<>();
    private Context mContext;

    public GridAdapter(ArrayList<String> mFilePaths, Context mContext){
        this.mFilePaths = mFilePaths;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mFilePaths.size();
    }

    @Override
    public Object getItem(int position) {
        return mFilePaths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ImageView imageView;
        if (view == null) {
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView)view;
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Photo No. " + i, Toast.LENGTH_SHORT).show();
            }
        });

        return imageView;
    }
}
