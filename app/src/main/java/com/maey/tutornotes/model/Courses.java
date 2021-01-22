package com.maey.tutornotes.model;

import android.util.Log;

import java.util.concurrent.ThreadLocalRandom;

public class Courses {

    //Properties
    private String name;
    private String shift;
    private String code;



    //Constructor
    public Courses() {
    }

    public Courses(String name, String shift, String code) {
        this.name = name;
        this.shift = shift;
        this.code = code;
    }
    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //Methods
    public static String generateCourseCode() {
        RandomString code = new RandomString(5, ThreadLocalRandom.current());
        Log.d("code: ", code.nextString());
        return code.nextString();
    }
}
