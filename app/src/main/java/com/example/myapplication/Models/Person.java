package com.example.myapplication.Models;

import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;

public class Person {
    private String name;
    private String email;
    private String password;
    private String id;
    private String code;
    private String lat;
    private String lng;
    private String imageUrl;
    private String accountType;
    private Boolean sharing;

    //public String uid;

    public Person() {
    }

    ArrayList<Person> personArrayList = new ArrayList<>();



    public Person(String name, String email, String id, String code, String accountType, Boolean sharing) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.code = code;
        this.accountType = accountType;
        this.sharing = sharing;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Boolean getSharing() {
        return sharing;
    }

    public void setSharing(Boolean sharing) {
        this.sharing = sharing;
    }
}
