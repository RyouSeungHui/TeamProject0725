package com.example.photosnsproject;

import java.util.ArrayList;

public class PostItem {
    public String location;
    public float lati, longi;
    public ArrayList<String> tag, friend;
    public int intPublic;
    public String contents, getTime;

    public PostItem() {}

    public PostItem(String location, float lati, float longi, ArrayList<String> tag, ArrayList<String> friend, int intPublic, String contents, String getTime) {
        this.location = location;
        this.lati = lati;
        this.longi = longi;
        this.tag = tag;
        this.friend = friend;
        this.intPublic = intPublic;
        this.contents = contents;
        this.getTime= getTime;
    }

    public int getIntPublic() {
        return intPublic;
    }

    public void setIntPublic(int intPublic) {
        this.intPublic = intPublic;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getGetTime() {
        return getTime;
    }

    public void setGetTime(String getTime) {
        this.getTime = getTime;
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

    public ArrayList<String> getTag() { return tag; }

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
