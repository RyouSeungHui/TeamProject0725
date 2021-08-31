package com.example.photosnsproject.Notifications;

public class SendData {

    private String text;
    private String post_id;
    private String user_id;

    public SendData(String text, String post_id, String user_id) {
        this.text = text;
        this.post_id = post_id;
        this.user_id = user_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
}
