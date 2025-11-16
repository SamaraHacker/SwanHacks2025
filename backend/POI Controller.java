import backend.POI;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

public class Main {
    

    public POI createPoi(long lat, long lon, int type, User author){
        try{
            POI newP = new POI(lat, lon, type);
            author.addPoi(newP);
            newP.addToDb();
        }catch(Error e){
            throw e;
        }
    }



    public boolean deletePoi(POI p, User u){
        QueryDocumentSnapshot doc = searchForPoi(p);
        if(doc){
            u.removePoi(p);
            doc.delete();
            return true;
        }
        return false;
    }
        

    public void poiTimeCheck(){
        ApiFuture<QuerySnapshot> future = db.collection("POI").get();
        try{
            QuerySnapshot querySnapshot = future.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                POI dPoi = doc.getData().get(doc.getId())
                if(dPoi.getStartTime() - Instant.now().getEpochSecond() >= 8640){
                    doc.delete();
                }
            }
        }catch(Error e){
            throw e;
        }
    }

    public QueryDocumentSnapshot searchForPoi(POI p){
        ApiFuture<QuerySnapshot> future = db.collection("POI").get();
        try{
            QuerySnapshot querySnapshot = future.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                POI dPoi = doc.getData().get(doc.getId())
                if(p.getId() == dPoi.getId()){
                    return doc;
                }
            }
        }catch(Error e){
            throw e;
        }
        return null;
    }
    
}
