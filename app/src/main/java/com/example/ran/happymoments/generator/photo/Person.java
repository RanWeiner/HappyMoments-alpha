package com.example.ran.happymoments.generator.photo;

import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.common.RelativePositionVector;
import com.example.ran.happymoments.common.Utils;
import com.example.ran.happymoments.generator.face.Face;

public class Person {


    private static int idGenerator = 0;
    private int id;
    Face face;
    RelativePositionVector vector;



    public Person(Face face, RelativePositionVector vector){
        this.id = ++idGenerator;

        this.face = face;
        this.vector = vector;
    }


    public RelativePositionVector getVector() {
        return vector;
    }

    public void setVector(RelativePositionVector vector) {
        this.vector = vector;
    }

    public Face getFace() {
        return face;
    }

    public void setFace(Face face) {
        this.face = face;
    }

    public double getFaceSize() {
        return this.face.getSize();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void normalizeVector(double maxDistance) {
        double normDist = Utils.normalize(this.vector.getDistance(),maxDistance,0);
        double normAngle = Utils.normalize(this.vector.getAngle(), AppConstants.MAX_ANGLE, 0);
        this.vector.setAngle(normAngle);
        this.vector.setDistance(normDist);
    }
}
