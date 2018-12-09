package com.example.ran.happymoments.generator.series;

import com.example.ran.happymoments.common.Position;
import com.example.ran.happymoments.generator.face.Face;

import java.util.ArrayList;
import java.util.List;

public class Person {
    List<Face> faces;



    public Person(){
        faces = new ArrayList<>();
    }

    public void addFace(Face face) {
        this.faces.add(face);
    }

    public List<Face> getFaces() {
        return faces;
    }


}
