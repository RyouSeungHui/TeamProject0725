package com.example.photosnsproject;

public class User {

    private String id, password;
    private String[] friendary= new String[100];



    public User(String id, String password) {
        this.id = id;
        this.password = password;

        for(int i=0; i<100; i++) this.friendary[i]="";
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
