//package com.example.ran.happymoments.logic;
//
//
//import com.example.ran.happymoments.logic.photo.Photo;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//public class PhotoDataExtractionService {
//    private static int cores = Runtime.getRuntime().availableProcessors();
//    private static ExecutorService executorService = Executors.newFixedThreadPool(cores + 1);
//
//    public void stop(){
//        executorService.shutdown();
//    }
//
//    public void detectAllFaces(ResultCallback resultCallback){
//        executorService.execute(() -> {
//
//            Future<Photo> futurePhoto = executorService.submit(extractData());
//
//            try {
//                photo = futurePhoto;
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//
//            List<Photo> result = new ArrayList<>();
//            result.add(photo);
//
//            resultCallback.onResult(result);
//        });
//    }
//
//    private Callable<Photo> extractData(Photo photo) {
//        return () -> {
//         System.out.print("extractData");
//
//         return photo;
//        };
//    }
//
//}
