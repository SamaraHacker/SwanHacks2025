package com.jelhackers.icemelt.Users;
public class CurrentUser extends User{
    private static CurrentUser instance;
    private String id; // or store FirebaseUser or any info you want

    private CurrentUser() {} // private constructor

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public void setid(String id) {
        this.id = id;
    }

    public String getid() {
        return id;
    }
}