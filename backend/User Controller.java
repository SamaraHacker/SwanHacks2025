import backend.POI;
import backend.User;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class Main {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //get mapping


    //post mapping
    public User addUser(String username, String password, String email){
        boolean canAdd = false;
        for(DocumentReference doc : db.collection("User")){
            if(doc.get().getUsername().equals(username) || doc.get().getEmail().equals(email)){
                Log.d(TAG, "User with that info already exists");
                canAdd = true;
            }
        }
        if(canAdd){
            User u = new User(username, password, email);
            u.addToDb();
            return u;
        }
        return null;
    }

    //put mapping


    //del mapping
    public boolean deleteUser(username){
        for(DocumentReference doc : db.collection("User")){
            if(doc.get().getUsername().equals(username)){
                doc.delete();
                Log.d(TAG, "User: " + username + " Deleted");
            }
        }
    }



}

