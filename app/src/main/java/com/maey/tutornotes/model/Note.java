package com.maey.tutornotes.model;

import com.google.firebase.database.ServerValue;

public class Note {

    private String noteKey;
    private String text;
    private Object timeStamp;
    private String course;

    public Note(String text, String course) {
        this.text = text;
        this.timeStamp = ServerValue.TIMESTAMP;
        this.course = course;
    }

    public Note() {
    }

    public void setNoteKey(String noteKey){
        this.noteKey = noteKey;
    }
    public String getNoteKey() { return noteKey; }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Object timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
