package com.example.photosnsproject;

public class User {

    private String id, password,nick,profileuri,profileimagename;

    public User(){

    }

    public String getNick() {
        return nick;
    }

    public String getProfileuri() {
        return profileuri;
    }

    public void setProfileuri(String profileuri) {
        this.profileuri = profileuri;
    }

    public String getProfileimagename() {
        return profileimagename;
    }

    public void setProfileimagename(String profileimagename) {
        this.profileimagename = profileimagename;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public User(String id, String password, String nick, String profileuri, String profileimagename) {
        this.id = id;
        this.password = password;
        this.nick = nick;
        this.profileuri = profileuri;
        this.profileimagename = profileimagename;
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
