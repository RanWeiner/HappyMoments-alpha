package com.example.ran.happymoments.generator;

import android.content.Context;

import com.example.ran.happymoments.generator.face.Face;

import java.util.List;

interface FaceExtractor {
    List<Face> detectFaces(Context context, String imagePath);
}
