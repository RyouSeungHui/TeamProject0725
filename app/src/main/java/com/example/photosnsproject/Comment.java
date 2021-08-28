package com.example.photosnsproject;

public class Comment {

    private String userid;
    private String talk;

    public Comment() {}

    public Comment(String userid, String talk) {
        this.userid = userid;
        this.talk = talk;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTalk() {
        return talk;
    }

    public void setTalk(String talk) {
        this.talk = talk;
    }
}
