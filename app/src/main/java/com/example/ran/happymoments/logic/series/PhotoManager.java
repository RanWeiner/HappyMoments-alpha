package com.example.ran.happymoments.logic.series;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhotoManager {

    private static int numOfCores = Runtime.getRuntime().availableProcessors();
    private static ExecutorService executorService = Executors.newFixedThreadPool(numOfCores ); //numofcores + 1


    public void stop(){
        executorService.shutdown();
    }

    public void getAllPhotos() {

    }

}
