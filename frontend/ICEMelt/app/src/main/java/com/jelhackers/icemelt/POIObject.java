package com.jelhackers.icemelt;

import java.time.Instant;
import com.jelhackers.icemelt.POIController;

public class POIObject {
    POIController pc = new POIController();

    //Useful vars
    private String id;
    private float lat;
    private float lon;
    private int alertType;
    private String locationName;
    private String locationDescription;
    private long startTime;
    private int reportCount;

    //statics
    private final String[] alertTypes = {
            "ICE Spotted", "Shots Fired", "Bomb threats", "Electric Malfunction", "Gas Leak", "Dangerous Wildlife"
    };
    public POIObject(){
        this(0,0,0, "Not Found", "None");
    }
    //constructor
    public POIObject(float lat, float lon, int type, String locationName, String locationDescription){
        this.lat = lat;
        this.lon = lon;
        this.alertType = type;
        this.startTime = Instant.now().getEpochSecond();
        this.locationDescription = locationDescription;
        this.locationName = locationName;
        this.id = locationName;
        this.reportCount = 1;
        pc.poiTimeCheck();
    }

    //Start getters/setters
    public String getId(){return id;}
    public void setId(String id){this.id = id;}

    public float getLat(){return lat;}
    public void setLat(float lat){this.lat = lat;}

    public float getLon(){return lon;}
    public void setLon(float lon){this.lon = lon;}

    public int getAlertType(){return this.alertType;}
    public void setAlertType(int type){
        this.alertType = type;
    }

    public String getLocationName(){return this.locationName;}
    public void setLocationName(String name){this.locationName = name;}

    public String getLocationDescription(){return this.locationDescription;}
    public void setLocationDescription(String name){this.locationDescription = name;}

    public long getStartTime(){return startTime;}

    public int getReportCount() {return reportCount;}
    public void incrementReportCount() {reportCount++;}

    //End getters/setters

    public String toString(){
        String message = alertTypes[alertType] + " at location: " + locationName + "\n";
        long timeInSeconds = Instant.now().getEpochSecond() - this.getStartTime();
        if(timeInSeconds / 60 /3600 < 1){
            message += (timeInSeconds / 60) + " min(s) ago";
        } else {
            message += (timeInSeconds / 60 / 3600) + " hr(s) ago";
        }
        return message;
    }
}
