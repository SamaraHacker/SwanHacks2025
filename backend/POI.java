import java.time.LocalDateTime;

public class POI{

    //Useful vars
    private int id;
    private long lat;
    private long lon;
    private String alertType; 
    private String locationName;
    private LocalDateTime dateTime;

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
        this.dateTime = LocalDateTime.now();
        this.id = idIter;
        idIter+=1;
    }

    //Start getters/setters
    public int getId(){return id;}

    public long getLat(){return lat;}
    public void setLat(long lat){this.lat = lat;}

    public long getLon(){return lon;}
    public void setLon(long lon){this.lon = lon;}

    public String getAlertType(){return this.alertType;}
    public void setAlertType(int type){this.alertType = ALERTS[type];}

    public String getLocationName(){return this.locationName;}
    public void setLocationName(String name){this.locationName = name;}

    public String getDateTimeS(){return dateTime.toString();}
    //End getters/setters

    public String toString(){
        return alertType.toLowerCase() + " at location: " + locationName + " at: " + dateTime.toString() 
        + "\nLatitiude: " + lat + "   Longitude: " + lon;
    }

    public boolean addToDb(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try{
            db.collection("POI").document(locationName).set(this);
            return true;
        }catch(Error e){
            Log.w(TAG, "Error writing document", e);
            return false;
        }
    }
}