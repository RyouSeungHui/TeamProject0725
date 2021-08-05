package com.example.photosnsproject;

import java.util.ArrayList;

public class PostItem {
    public String location;
    public float lati, longi;
    public ArrayList<String> tag, friend;

    public PostItem(String location, float lati, float longi, ArrayList<String> tag, ArrayList<String> friend) {
        this.location = location;
        this.lati = lati;
        this.longi = longi;
        this.tag = tag;
        this.friend = friend;
    }
}
