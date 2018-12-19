package com.example.ran.happymoments.logic.photo;


import com.example.ran.happymoments.common.AppConstants;
import com.example.ran.happymoments.logic.face.Face;

import java.util.List;

public class Ranker {

    public Ranker(){ }

    public double rankPhoto(Photo photo){
        int numOfPersons = photo.getPersons().size();
        double photoRank = 0;

        rankPersons(photo.getPersons());

        for(int i = 0 ; i< numOfPersons ; i++)
            photoRank += photo.getPersons().get(i).getRank();

        photoRank /= numOfPersons;
        photo.setRank(photoRank);
        return photoRank;
    }


    private void rankPersons(List<Person> persons) {
        for (Person person : persons) {
            rankPerson(person);
        }
    }

    private void rankPerson(Person person) {
        double faceRank = calcFaceRank(person.getFace());
        double importance = person.getImportance();

        person.setRank(faceRank * importance);
    }



    //average between eyesOpen and smile -> multiply the result with the importance percentage
    public double calcFaceRank(Face face) {
        float eyesScore, smileScore;

        eyesScore = face.getEyes().getEyesOpenProbability() * 100;
        smileScore = face.getSmile().getSmilingProbability() * 100;
        double eyesAndSmileScore = ((eyesScore * AppConstants.EYES_WEIGHT) + (smileScore * AppConstants.SMILE_WEIGHT));
        return eyesAndSmileScore;
    }


//    //Must be called before calcFaceRank()
//    public void rankFacesImportance(List<Face> faces){
//        float faceArea;
//        float maxArea = findMaxFaceArea(faces);
//        for(int i = 0; i< faces.size(); i++){
//            faceArea = faces.get(i).getHeight() * faces.get(i).getWidth();
//            faces.get(i).setFaceImportanceScore(faceArea / maxArea);
//        }
//    }
//
//    public float findMaxFaceArea(List<Face> faces){
//        float faceArea, maxArea = 0;
//        for(int i = 0; i < faces.size(); i++){
//            faceArea = faces.get(i).getHeight() * faces.get(i).getWidth();
//            if(faceArea > maxArea)
//                maxArea = faceArea;
//        }
//        return maxArea;
//    }


}
