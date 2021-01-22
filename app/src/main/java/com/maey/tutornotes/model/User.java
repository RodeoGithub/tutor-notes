package com.maey.tutornotes.model;

public class User {

    private String name;
    private int userType = -1; //default

    public User() {
        //Required for calls to Datasnapshot.getValue(User.class)
    }

    public User(String name, int userType) {
        this.name = name;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public int getUserType() {
        return userType;
    }
}
