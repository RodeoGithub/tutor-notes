package com.maey.tutornotes.model;

import android.util.Log;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.maey.tutornotes.model.RandomString;

public class Courses {

    //Properties
    private String title;
    private String shift;
    private String code;



    //Constructor
    public Courses() {
    }

    public Courses(String title, String shift, String code) {
        this.title = title;
        this.shift = shift;
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
    public static String addNewCourse() {
        RandomString code = new RandomString(5, ThreadLocalRandom.current());
        Log.d("code: ", code.nextString());
        return code.nextString();
    }
}
