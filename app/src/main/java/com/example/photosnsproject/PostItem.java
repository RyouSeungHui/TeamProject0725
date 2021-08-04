package com.example.photosnsproject;

import java.util.ArrayList;

public class PostItem {
    public String location;
    public ArrayList<String> tag, friend;

    public PostItem(String location, ArrayList<String> tag, ArrayList<String> friend) {
        this.location = location;
        this.tag = tag;
        this.friend = friend;
    }
}
