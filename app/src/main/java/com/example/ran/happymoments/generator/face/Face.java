package com.example.ran.happymoments.generator.face;

import com.example.ran.happymoments.common.Position;

public class Face {

    public final static float SMILING_ACCEPT_PROBABILITY =  0.5f;
    public final static float EYES_OPEN_PROBABILITY =  0.5f;

    //normalized vector to center

    Position position;
    float width, height, smilingProbability, leftEyeOpenProbability, rightEyeOpenProbability;

    double angleFromGravityCenter;
    double distanceFromGravityCenter;



    public double getDistanceFromGravityCenter() {
        return distanceFromGravityCenter;
    }

    public void setDistanceFromGravityCenter(double distanceFromGravityCenter) {
        this.distanceFromGravityCenter = distanceFromGravityCenter;
    }

    public Face(){

    }


    public Face(Position position, float width, float height, float smilingProbability, float leftEyeOpenProbability, float rightEyeOpenProbability) {

        this.position = position;
        this.width = width;
        this.height = height;
        this.smilingProbability = smilingProbability;
        this.leftEyeOpenProbability= leftEyeOpenProbability;
        this.rightEyeOpenProbability = rightEyeOpenProbability;
    }



    public double getAngleFromGravityCenter() {
        return angleFromGravityCenter;
    }

    public void setAngleFromGravityCenter(double angleFromGravityCenter) {
        this.angleFromGravityCenter = angleFromGravityCenter;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }


    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getSmilingProbability() {
        return smilingProbability;
    }

    public void setSmilingProbability(float smilingProbability) {
        this.smilingProbability = smilingProbability;
    }

    public float getLeftEyeOpenProbability() {
        return leftEyeOpenProbability;
    }

    public void setLeftEyeOpenProbability(float leftEyeOpenProbability) {
        this.leftEyeOpenProbability = leftEyeOpenProbability;
    }

    public float getRightEyeOpenProbability() {
        return rightEyeOpenProbability;
    }

    public void setRightEyeOpenProbability(float rightEyeOpenProbability) {
        this.rightEyeOpenProbability = rightEyeOpenProbability;
    }


    public boolean isSmiling(){
        if (smilingProbability > SMILING_ACCEPT_PROBABILITY) {
            return true;
        }
        return false;
    }


    public boolean areEyesOpen(){
        if (leftEyeOpenProbability > EYES_OPEN_PROBABILITY
                && rightEyeOpenProbability > EYES_OPEN_PROBABILITY ) {
            return true;
        }
        return false;
    }
}
