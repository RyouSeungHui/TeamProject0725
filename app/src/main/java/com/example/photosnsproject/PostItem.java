package com.example.photosnsproject;

import java.util.ArrayList;

public class PostItem {
    public String location;
    public float lati, longi;
    public ArrayList<String> tag, friend;

    public PostItem() {}

    public PostItem(String location, float lati, float longi, ArrayList<String> tag, ArrayList<String> friend) {
        this.location = location;
        this.lati = lati;
        this.longi = longi;
        this.tag = tag;
        this.friend = friend;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getLati() {
        return lati;
    }

    public void setLati(float lati) {
        this.lati = lati;
    }

    public float getLongi() {
        return longi;
    }

    public void setLongi(float longi) {
        this.longi = longi;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    public ArrayList<String> getFriend() {
        return friend;
    }

    public void setFriend(ArrayList<String> friend) {
        this.friend = friend;
    }
}
