import backend.POI;
import backend.User;
import backend.User;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Main{
    public static void main(String[] args) {
        FileInputStream serviceAccount = new FileInputStream("./serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build();
        FirebaseApp.initializeApp(options);
    /* 
        FireStore db = FirestoreClient.getFirestore();
    */
        POI test = new POI(0, 0, 0);
        test.addToDb();
        User test2 = new User("Roanje", "Qwerty96", "roanje@iastate.edu");
        test2.addToDb();
    }
}