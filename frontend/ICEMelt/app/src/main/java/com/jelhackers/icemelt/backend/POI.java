import java.time.LocalDateTime;
import backend.POI;
import backend.User;
import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class POI{
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference doc;

    //Useful vars
    private int id;
    private long lat;
    private long lon;
    private String alertType; 
    private String locationName;
    private long startTime;

    //statics
    private static String ALERTS[] = {"ICE SPOTTED","",""};
    private static int idIter = 0;

    //empty constructor
    public POI(){
        POI(0,0,0);
    }
    //constructor
    public POI(long lat, long lon, int type){
        this.lat = lat;
        this.lon = lon;
        this.alertType = ALERTS[type];
        this.startTime = Instant.now().getEpochSecond();
        this.id = idIter;
        idIter++;
        this.doc = db.collection("POI").document(locationName);
    }

    //Start getters/setters
    public int getId(){return id;}

    public long getLat(){return lat;}
    public void setLat(long lat){this.lat = lat;}

    public long getLon(){return lon;}
    public void setLon(long lon){this.lon = lon;}

    public String getAlertType(){return this.alertType;}
    public void setAlertType(int type){
        this.alertType = ALERTS[type];
    }

    public String getLocationName(){return this.locationName;}
    public void setLocationName(String name){this.locationName = name;}

    public long getStartTime(){return startTime;}
    //End getters/setters

    public String toString(){
        return alertType.toLowerCase() + " at location: " + locationName 
        + "\nLatitiude: " + lat + "   Longitude: " + lon;
    }

    public boolean addToDb(){
        try{
           doc.set(this);
            return true;
        }catch(Error e){
            Log.w(TAG, "Error writing document", e);
            return false;
        }
    }
}