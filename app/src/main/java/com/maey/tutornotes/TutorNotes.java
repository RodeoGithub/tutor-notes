package com.maey.tutornotes;

import android.app.Application;

public class TutorNotes extends Application {

    private int userType;

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
