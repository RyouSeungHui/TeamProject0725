package com.example.photosnsproject;

public class User {

    private String id, password,nick;

    public User(){

    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public User(String id, String password, String nick) {
        this.id = id;
        this.password = password;
        this.nick=nick;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
