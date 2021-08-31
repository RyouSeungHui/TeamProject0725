package com.example.photosnsproject;

public class User {

    private String id, password,nick,token;

    public User(){

    }


    public User(String id, String password, String nick, String token) {
        this.id = id;
        this.password = password;
        this.nick = nick;
        this.token = token;
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
