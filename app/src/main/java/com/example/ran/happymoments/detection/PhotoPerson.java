package com.example.ran.happymoments.detection;

import com.example.ran.happymoments.detection.face.Face;
import com.example.ran.happymoments.detection.series.Photo;

import java.util.ArrayList;

public class PhotoPerson {
    private ArrayList<Face> facesInSeries;

    public PhotoPerson(){
        facesInSeries = new ArrayList<>();
    }

    public ArrayList<Face> getFacesInSeries(){
        return this.facesInSeries;
    }

    public void addFace(Face face){
        facesInSeries.add(face);
    }


}
