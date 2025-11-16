package com.jelhackers.icemelt;

import java.time.Instant;

public class POIObject {

    //Useful vars
    //private int id;
    private double lat;
    private double lon;
    private String alertType;
    private String locationName;
    private String locationDescription;
    private long startTime;

    //statics
    private final String[] alertTypes = {
            "ICE SPOTTED"
    };
    private static int idIter = 0;

    public POIObject(){
        this(0,0,0, "Not Found", "None");
    }
    //constructor
    public POIObject(double lat, double lon, int type, String locationName, String locationDescription){
        this.lat = lat;
        this.lon = lon;
        this.alertType = alertTypes[type];
        this.startTime = Instant.now().getEpochSecond();
        this.locationDescription = locationDescription;
        this.locationName = locationName;
        //this.id = idIter;
        idIter++;
        //this.doc = db.collection("POI").document(locationName);
    }

    //Start getters/setters
    //public int getId(){return id;}

    public double getLat(){return lat;}
    public void setLat(long lat){this.lat = lat;}

    public double getLon(){return lon;}
    public void setLon(long lon){this.lon = lon;}

    public String getAlertType(){return this.alertType;}
    public void setAlertType(int type){
        this.alertType = alertTypes[type];
    }

    public String getLocationName(){return this.locationName;}
    public void setLocationName(String name){this.locationName = name;}

    public String getLocationDescription(){return this.locationName;}
    public void setLocationDescription(String name){this.locationName = name;}

    public long getStartTime(){return startTime;}
    //End getters/setters

    public String toString(){
        return alertType.toLowerCase() + " at location: " + locationName
                + "\nLatitiude: " + lat + "   Longitude: " + lon;
    }
}
