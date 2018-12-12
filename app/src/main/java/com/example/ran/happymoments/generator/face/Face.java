package com.example.ran.happymoments.generator.face;

import com.example.ran.happymoments.common.Position;

public class Face {

    private Smile smile;
    private Eyes eyes;
    private Position position;
    private float width, height;
    private float faceImportanceScore, faceScore;

    //just for debug
    public int id;

    public Face(){

    }


    public Face(int id , Position position, float width, float height , Smile smile , Eyes eyes) {
        this.id = id;
        this.position = position;
        this.width = width;
        this.height = height;
        this.smile = smile;
        this.eyes = eyes;
    }



    public Smile getSmile() {
        return smile;
    }

    public Eyes getEyes() {
        return eyes;
    }
//    public double getDistanceFromGravityCenter() {
//        return distanceFromGravityCenter;
//    }
//
//    public void setDistanceFromTotalFacesCenter(double distanceFromGravityCenter) {
//        this.distanceFromGravityCenter = distanceFromGravityCenter;
//    }
//
//    public double getAngleFromGravityCenter() {
//        return angleFromGravityCenter;
//    }
//
//    public void setAngleFromTotalFacesCenter(double angleFromGravityCenter) {
//        this.angleFromGravityCenter = angleFromGravityCenter;
//    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public float getWidth() {
        return this.width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return this.height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getSize() {
        return this.width * this.height;
    }

    public float getFaceImportanceScore() {
        return faceImportanceScore;
    }

    public void setFaceImportanceScore(float faceImportanceScore) {
        this.faceImportanceScore = faceImportanceScore;
    }

    public float getFaceScore() {
        return faceScore;
    }

    public void setFaceScore(float faceScore) {
        this.faceScore = faceScore;
    }



/////just for debug
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
