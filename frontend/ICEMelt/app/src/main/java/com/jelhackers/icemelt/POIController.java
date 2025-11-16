package com.jelhackers.icemelt;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class POIController {

    public FirebaseFirestore db;

    public POIController() {
        db = FirebaseFirestore.getInstance();
    }
    public void createPoi(long lat, long lon, int type, String locationName, String locationDescription){
        try{
            POIObject newP = new POIObject(lat, lon, type, locationName, locationDescription);
            //addToDb(newP);
        }catch(Error e){
            throw e;
        }
    }
//
//    public void addToDb(POIObject p)
//    {
//        db.collection("POI")
//                .document(String.valueOf(p.getId()))
//                .set(p)
//                .addOnSuccessListener(aVoid -> Log.d("POIController","POI added"))
//                .addOnFailureListener(e -> Log.e("POIController","Failed to add POI", e));
//    }
//
//    public void deletePoi(POIObject p){
//        db.collection("POI")
//                .document(String.valueOf(p.getId()))
//                .delete()
//                .addOnSuccessListener(aVoid -> Log.d("POIController","POI deleted"))
//                .addOnFailureListener(e -> Log.e("POIController","Failed to delete POI", e));
//    }

    public ArrayList<POIObject> getAllPoi()
    {
        ArrayList<POIObject> poiList = new ArrayList<>();
        db.collection("POI").get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        poiList.add(doc.toObject(POIObject.class));
                    }
                    // Do something with poiList
                });
        return poiList;
    }

//    public void poiTimeCheck(){
//        ApiFuture<QuerySnapshot> future = db.collection("POI").get();
//        try{
//            QuerySnapshot querySnapshot = future.get();
//            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
//            for (QueryDocumentSnapshot doc : documents) {
//                POI dPoi = doc.getData().get(doc.getId())
//                if(dPoi.getStartTime() - Instant.now().getEpochSecond() >= 8640){
//                    doc.delete();
//                }
//            }
//        }catch(Error e){
//            throw e;
//        }
//    }
//
//    public QueryDocumentSnapshot searchForPoi(POI p){
//        ApiFuture<QuerySnapshot> future = db.collection("POI").get();
//        try{
//            QuerySnapshot querySnapshot = future.get();
//            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
//            for (QueryDocumentSnapshot doc : documents) {
//                POI dPoi = doc.getData().get(doc.getId())
//                if(p.getId() == dPoi.getId()){
//                    return doc;
//                }
//            }
//        }catch(Error e){
//            throw e;
//        }
//        return null;
//    }

}