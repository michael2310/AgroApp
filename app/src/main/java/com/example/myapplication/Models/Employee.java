package com.example.myapplication.Models;

import java.util.ArrayList;

public class Employee  {
    private String name;
    private String email;
    private String password;
    private String id;
    private String info;
    private float Latitude;
    private float Longitude;
    private String lat;
    private String lng;
    private String farmCode;
    private String imageUrl;
    private String accountType;

    int position;


    //LatLng latLng;

    ArrayList<Employee> employees = new ArrayList<>();


//    public Employee(String name, String email, String id, String farmCode){
//        this.name = name;
//        this.email = email;
//        this.id = id;
//        this.farmCode = farmCode;
//    }



    public Employee(String name, String email, String id, String accountType){
        this.name = name;
        this.email = email;
        this.id = id;
        this.accountType = accountType;
    }


    public Employee(){
    }



//    public static final Employee[] employees ={
//            new Employee("Pracownik 1", 51.1780, 17.5126, new LatLng(51.1780, 17.5126) ),
//            new Employee("Pracownik 2", 51.1826, 17.5008, new LatLng(51.1826, 17.5008) ),
//            new Employee("Pracownik 3", 51.1685, 17.5007, new LatLng(51.1685, 17.5007) ),
//            new Employee("Pracownik 4", 51.1627, 17.5199, new LatLng(51.1627, 17.5199) ),
//            new Employee("Pracownik 5", 51.1677, 17.5099, new LatLng(51.1677, 17.5099) )
//    };


//    public String getName() {
//        return name;
//    }
//
//    public double getX() {
//        return x;
//    }
//
//    public double getY() {
//        return y;
//    }
//
//    public LatLng getLatLng() {
//        return latLng;
//    }
//
//    public void setLatLng(LatLng latLng) {
//        this.latLng = latLng;
//    }


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

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
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
}
