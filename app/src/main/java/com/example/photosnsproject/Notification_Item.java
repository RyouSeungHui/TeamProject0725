package com.example.photosnsproject;

public class Notification_Item {

    private String send_id;
    private String post_id;
    private String user_id;
    private String alarm_id;
    private String getTime;
    private String type;
    private String content;

    public Notification_Item(){}

    public Notification_Item(String content, String send_id, String post_id, String user_id, String alarm_id, String getTime, String type) {
        this.content=content;
        this.send_id = send_id;
        this.post_id = post_id;
        this.user_id = user_id;
        this.alarm_id = alarm_id;
        this.getTime = getTime;
        this.type = type;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGetTime() {
        return getTime;
    }

    public void setGetTime(String getTime) {
        this.getTime = getTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSend_id() {
        return send_id;
    }

    public void setSend_id(String send_id) {
        this.send_id = send_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }
}
