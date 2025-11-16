package com.jelhackers.icemelt;

import android.location.Location;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class POIController {

    public FirebaseFirestore db;

    public POIController() {
        db = FirebaseFirestore.getInstance();
    }
    public void createPoi(float lat, float lon, int type, String locationName, String locationDescription){
        try{
            //If database contains newP -> add 1 to reportCount
            db.collection("POI")
                    .document(locationName)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // POI exists → increment report count
                            POIObject existingPoi = documentSnapshot.toObject(POIObject.class);
                            if (existingPoi != null) {
                                existingPoi.incrementReportCount();
                                db.collection("POI")
                                        .document(locationName)
                                        .set(existingPoi)
                                        .addOnSuccessListener(aVoid -> Log.d("POIController","Report count updated"))
                                        .addOnFailureListener(e -> Log.e("POIController","Failed to update POI", e));
                            }
                        } else {
                            // POI doesn't exist → create new
                            POIObject newP = new POIObject(lat, lon, type, locationName, locationDescription);
                            db.collection("POI")
                                    .document(locationName)
                                    .set(newP)
                                    .addOnSuccessListener(aVoid -> Log.d("POIController","POI added"))
                                    .addOnFailureListener(e -> Log.e("POIController","Failed to add POI", e));
                        }
                    })
                    .addOnFailureListener(e -> Log.e("POIController", "Error checking POI", e));
        }catch(Error e){
            throw e;
        }
    }

    public void incrementReportCount(String location) {
        // Get a reference to the document
        db.collection("POI").document(location)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Get the current reportCount
                        int currentCount = documentSnapshot.getLong("reportCount").intValue();
                        int newCount = currentCount + 1;

                        // Update only the reportCount field
                        db.collection("POI").document(location)
                                .update("reportCount", newCount, "startTime", Instant.now().getEpochSecond())
                                .addOnSuccessListener(aVoid -> Log.d("POIController", "Report count updated to " + newCount))
                                .addOnFailureListener(e -> Log.e("POIController", "Failed to update report count", e));
                    } else {
                        Log.d("POIController", "No such document to increment reportCount");
                    }
                })
                .addOnFailureListener(e -> Log.e("POIController", "Error fetching document", e));
    }
    public void addToDb(POIObject p)
    {
        db.collection("POI")
                .document(p.getId())
                .set(p)
                .addOnSuccessListener(aVoid -> Log.d("POIController","POI added"))
                .addOnFailureListener(e -> Log.e("POIController","Failed to add POI", e));
    }

    public void deletePoi(String location){
        db.collection("POI")
                .document(String.valueOf(location))
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("POIController","POI deleted"))
                .addOnFailureListener(e -> Log.e("POIController","Failed to delete POI", e));
    }

    public void getPoi(String location, POICallback callback) {
        db.collection("POI")
                .document(location)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        POIObject poi = documentSnapshot.toObject(POIObject.class);
                        Log.d("POIController", "Found POI: " + poi);
                        callback.onResult(poi);
                    } else {
                        Log.d("POIController", "No such document");
                        callback.onResult(null); // indicate not found
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("POIController", "Error getting document", e);
                    callback.onError(e);
                });
    }

    public void getAllPois(POIListener listener){
        ArrayList<POIObject> allPois = new ArrayList<>();

        db.collection("POI").get()
                .addOnSuccessListener(querySnapshot -> {
                    for(QueryDocumentSnapshot doc : querySnapshot) {
                        POIObject poi = doc.toObject(POIObject.class);
                        if (poi != null) {
                            allPois.add(poi);
                        }
                    }
                    listener.onPoisRetrieved(allPois);
                }).addOnFailureListener(e -> {
                    Log.e("POIController", "Error retrieving POIs", e);
                    listener.onFailure(e);
                });
    }


    public void getNearbyPois(double userLat, double userLon, double radiusMeters, POIListener listener) {
        ArrayList<POIObject> nearbyPois = new ArrayList<>();

        db.collection("POI").get()
                .addOnSuccessListener(querySnapshot -> {
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        POIObject poi = doc.toObject(POIObject.class);
                        if (poi != null) {
                            float[] results = new float[1];
                            Location.distanceBetween(userLat, userLon, poi.getLat(), poi.getLon(), results);
                            float distanceInMeters = results[0];

                            if (distanceInMeters <= radiusMeters) {
                                nearbyPois.add(poi);
                            }
                            Log.d("POI_DISTANCE", poi.getLocationName() + " is " + distanceInMeters + " meters away");
                        }
                    }
                    // Return the list via the listener
                    listener.onPoisRetrieved(nearbyPois);
                })
                .addOnFailureListener(e -> {
                    Log.e("POIController", "Error retrieving POIs", e);
                    listener.onFailure(e);
                });
    }

    public interface POICallback {
        void onResult(POIObject poi);
        void onError(Exception e);
    }

    public interface POIListener {
        void onPoisRetrieved(ArrayList<POIObject> pois);
        void onFailure(Exception e);
    }

    public void poiTimeCheck(){
        getAllPois(new POIListener() {
            @Override
            public void onPoisRetrieved(ArrayList<POIObject> pois) {
                for(POIObject p : pois){
                    if(Instant.now().getEpochSecond() - p.getStartTime() > 8640){
                        deletePoi(p.getLocationName());
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("AllPois", "Failed to get nearby POIs", e);
            }
        });

   }
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