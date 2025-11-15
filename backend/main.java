import backend.POI;
public static void main(String[] args) {
    FileInputStream serviceAccount = new FileInputStream("./serviceAccountKey.json");

    FirebaseOptions options = new FirebaseOptions.Builder()
    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    .build();
    FirebaseApp.initializeApp(options);
/* 
    FireStore db = FirestoreClient.getFirestore();
*/
    POI test = new POI(0, 0, 1);
    test.saddToDb();
}
