package com.example.ran.happymoments.service;

import android.content.Context;

import com.example.ran.happymoments.logic.face.Face;

import java.util.List;

public interface FaceDetector {

    List<Face> detectFaces(Context context ,String imagePath , String orientation);

    void release();
}
