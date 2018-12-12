package com.example.ran.happymoments.generator;


import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.generator.face.Face;
import com.example.ran.happymoments.generator.photo.Person;
import com.example.ran.happymoments.generator.photo.Photo;

import java.util.List;

public class Ranker {

    public Ranker(){ }

    public void rankPhoto(Photo photo){
        float facesScoreAvg = 0;
        for(int i = 0 ; i< photo.getPersonList().size(); i++)
        {
            facesScoreAvg += photo.getFaces().get(i).getFaceScore();
        }
        facesScoreAvg /= photo.getFaces().size();
        photo.setFaceScore(facesScoreAvg);
    }

    //average between eyesOpen and smile -> multiply the result with the importance percentage
    public void rankFace(Face face) {
        float eyesScore, smileScore;
        eyesScore = face.getEyes().getEyesOpenProbability();
        smileScore = face.getSmile().getSmilingProbability();
        double eyesAndSmileScore = ((eyesScore * AppConstants.EYES_WEIGHT) + (smileScore * AppConstants.SMILE_WEIGHT)) / 2;
        double rank = eyesAndSmileScore * face.getFaceImportanceScore();
        face.setFaceScore((float)rank); //casting to float may cause losing data
    }

    public void rankFaceImportance(List<Face> faces){
        float faceArea;
        float maxArea = findMaxFaceArea(faces);
        for(int i = 0; i< faces.size(); i++){
            faceArea = faces.get(i).getHeight() * faces.get(i).getWidth();
            faces.get(i).setFaceImportanceScore(faceArea / maxArea);
        }
    }

    public float findMaxFaceArea(List<Face> faces){
        float faceArea, maxArea = 0;
        for(int i = 0; i < faces.size(); i++){
            faceArea = faces.get(i).getHeight() * faces.get(i).getWidth();
            if(faceArea > maxArea)
                maxArea = faceArea;
        }
        return maxArea;
    }


}
