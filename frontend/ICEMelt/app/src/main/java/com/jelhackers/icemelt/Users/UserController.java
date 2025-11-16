package com.jelhackers.icemelt.Users;


import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.jelhackers.icemelt.POIObject;

import java.time.Instant;
import java.util.List;

public class UserController {

    public FirebaseFirestore db;

    public UserController() {
        db = FirebaseFirestore.getInstance();
    }
    public void addUser(String uid, String username, String email, String password, AddUserCallback callback) {
        db.collection("Users")
                .whereEqualTo("username", username)
                .get()
                .addOnSuccessListener(usernameResult -> {
                    if (!usernameResult.isEmpty()) {
                        callback.onFailure("Username already exists");
                        return;
                    }
                    db.collection("Users")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener(emailResult -> {
                                if (!emailResult.isEmpty()) {
                                    callback.onFailure("Email already exists");
                                    return;
                                }
                                // If both are unique → create new user
                                User newUser = new User(username, email, password);

                                db.collection("Users")
                                        .document(uid)
                                        .set(newUser)
                                        .addOnSuccessListener(aVoid ->
                                                callback.onSuccess(newUser))
                                        .addOnFailureListener(e -> Log.e("FIREBASE", "Failed to add user", e));

                            });
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
    public void searchForUser(String uid, SearchUserCallback callback) {

        db.collection("Users")
                .document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        User user = doc.toObject(User.class);
                        callback.onFound(user);
                    } else {
                        callback.onNotFound();
                    }
                })
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void deleteUser(String uid, DeleteUserCallback callback) {

        db.collection("Users")
                .document(uid)
                .delete()
                .addOnSuccessListener(aVoid -> callback.onDeleted())
                .addOnFailureListener(e -> callback.onError(e.getMessage()));
    }

    public void updateUser(String id, String username, String password, String email){
        db.collection("Users").document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        db.collection("Users").document(id)
                                .update("username", username, "password", password, "email", email)
                                .addOnSuccessListener(aVoid -> Log.d("POIController", "Infomration updated"))
                                .addOnFailureListener(e -> Log.e("POIController", "Failed to update information", e));
                    } else {
                        Log.d("POIController", "No such document to increment reportCount");
                    }
                })
                .addOnFailureListener(e -> Log.e("POIController", "Error fetching document", e));
    }

    public void updatePois(String id, List<POIObject>  poisPlaced)
    {
        db.collection("Users").document(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        db.collection("Users").document(id)
                                .update("poisPlaced", poisPlaced)
                                .addOnSuccessListener(aVoid -> Log.d("POIController", "Infomration updated"))
                                .addOnFailureListener(e -> Log.e("POIController", "Failed to update information", e));
                    } else {
                        Log.d("POIController", "No such document to increment reportCount");
                    }
                })
                .addOnFailureListener(e -> Log.e("POIController", "Error fetching document", e));
    }

    public interface AddUserCallback {
        void onSuccess(User user);
        void onFailure(String error);
    }

    public interface SearchUserCallback {
        void onFound(User user);
        void onNotFound();
        void onError(String error);
    }

    public interface DeleteUserCallback {
        void onDeleted();
        void onError(String error);
    }
}