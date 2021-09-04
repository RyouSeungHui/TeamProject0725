package com.example.photosnsproject.Alarm;

public class SendData {


    private String post_id;
    private String user_id;
    private String send_id;
    private String flag;


    public SendData(String post_id, String user_id, String send_id, String flag) {

        this.post_id = post_id;
        this.user_id = user_id;
        this.send_id = send_id;
        this.flag=flag;
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

    public String getSend_id() {
        return send_id;
    }

    public void setSend_id(String send_id) {
        this.send_id = send_id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
