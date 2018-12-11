package com.example.ran.happymoments.common;

public class RelativePositionVector {

    double angle , distance;

    public RelativePositionVector(double angle , double distance){
        this.angle = angle;
        this.distance = distance;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
