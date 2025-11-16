package com.jelhackers.icemelt.Users;

import com.jelhackers.icemelt.POIObject;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    private String password;
    private String email;
    private ArrayList<POIObject> poisPlaced;

    public User(){}
    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
        this.poisPlaced = new ArrayList<POIObject>();
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
    public void setEmail(String email)
    {
        this.email = email;
    }
    public void setPassword(String password)
    {
        this.password = password;
    }

    //getters/setters

    public String getUsername(){return username;}

    public String getPassword(){return password;}

    public String getEmail(){return email;}

    public ArrayList<POIObject> getPoisPlaced(){return poisPlaced;}
    public void addPoi(POIObject p){poisPlaced.add(p);}
    public void removePoi(POIObject p){poisPlaced.remove(p);}
}