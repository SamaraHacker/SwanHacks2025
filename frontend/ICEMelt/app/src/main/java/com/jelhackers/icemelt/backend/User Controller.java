import backend.POI;
import backend.User;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Main {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //get mapping

    //post mapping
    public User addUser(String username, String password, String email){
        boolean canAdd = true;

        ApiFuture<QuerySnapshot> future = db.collection("User").get();
        try{
            QuerySnapshot querySnapshot = future.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                if(doc.get().getUsername().equals(username) || doc.get().getEmail().equals(email)){
                    Log.d(TAG, "User with that info already exists");
                    canAdd = false;
                }
            }
        }catch(Error e){
            throw e;
        }
        if(canAdd){
            User u = new User(username, password, email);
            u.addToDb();
            return u;
        }
        return null;
    }


    //del mapping
    public boolean deleteUser(User u){
        try{
            QueryDocumentSnapshot doc = searchForUser(u);
            if(doc){
                doc.delete();
                return true;
            }
        }catch(Error e){
            throw e;
        }
        return false;
    }

    
    public QueryDocumentSnapshot searchForUser(User u){
    ApiFuture<QuerySnapshot> future = db.collection("User").get();
    try{
        QuerySnapshot querySnapshot = future.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        for (QueryDocumentSnapshot doc : documents) {
            User dUser = doc.getData().get(doc.getId())
            if(u.getId() == dUser.getId()){
                return doc;
            }
        }
    }catch(Error e){
        throw e;
    }
    return null;
}

}

