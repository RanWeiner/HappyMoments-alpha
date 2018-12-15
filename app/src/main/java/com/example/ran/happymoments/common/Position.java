package com.example.ran.happymoments.common;

public class Position {
    double x , y;

    public Position() {
        this.x = 0;
        this.y = 0;
    }

    public Position(double x , double y) {
        this.x = x;
        this.y = y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }



    //return the angle degree to target point
    public double calcAngle(Position targetPosition) {
        double angle  = Math.toDegrees(Math.atan2(targetPosition.y - this.y, targetPosition.x - this.x));

//        angle = angle + Math.ceil( -angle / 360 ) * 360;
        if (angle < 0) {
            angle += 360;
        }
        return  angle;
    }

    //return the euclidean distance from target
    public double calcEuclidDistance(Position targetPosition) {
        double x ,y;

        x = this.x - targetPosition.x;
        y = this.y - targetPosition.y;

        return Math.sqrt(x*x + y*y);
    }
}
