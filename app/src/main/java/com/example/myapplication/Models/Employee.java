package com.example.myapplication.Models;

import java.util.ArrayList;

public class Employee  {
    private String name;
    private String email;
    private String password;
    private String id;
    private String info;
    private double lat;
    private double lng;
    private String farmCode;
    private String farmName;
    private String imageUrl;
    private String accountType;
    private boolean sharing;
    private boolean active;

    int position;


    //LatLng latLng;

    public ArrayList<Employee> employees = new ArrayList<>();


    public Employee(String name, String email, String id, String accountType){
        this.name = name;
        this.email = email;
        this.id = id;
        this.accountType = accountType;
    }


    public Employee(){
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public String getCode() {
        return farmCode;
    }

    public void setCode(String farmcode) {
        this.farmCode = farmcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getFarmCode() {
        return farmCode;
    }

    public void setFarmCode(String farmCode) {
        this.farmCode = farmCode;
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

    public boolean isSharing() {
        return sharing;
    }

    public void setSharing(boolean sharing) {
        this.sharing = sharing;
    }

    public String getFarmName() {
        return farmName;
    }

    public void setFarmName(String farmName) {
        this.farmName = farmName;
    }

    public boolean isActive() {
        return active;
    }
//
//    public void setActive(boolean active) {
//        isActive = active;
//    }
}
