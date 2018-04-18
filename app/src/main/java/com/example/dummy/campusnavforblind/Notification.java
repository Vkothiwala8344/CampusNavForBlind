package com.example.dummy.campusnavforblind;

public class Notification {

    private int id;
    private String title;
    private String shortdesc;

    public Notification(String title, String shortdesc) {

        this.title = title;
        this.shortdesc = shortdesc;

    }

    public String getTitle() {
        return title;
    }

    public String getShortdesc() {
        return shortdesc;
    }
}
