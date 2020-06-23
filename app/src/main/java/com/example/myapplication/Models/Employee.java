package com.example.myapplication.Models;

import java.util.ArrayList;

public class Employee  {
    String surname;
    String lastname;
    String email;
    String info;
    float Latitude;
    float Longitude;
    String id;
    String farmCode;
    int position;
    //LatLng latLng;

    ArrayList<Employee> employees = new ArrayList<>();


    public Employee(String name, String email, String id, String farmCode){
        this.surname = name;
        this.email = email;
        this.id = id;
        this.farmCode = farmCode;
    }

    public Employee(String surname, String lastname){
        this.surname = surname;
        this.lastname = lastname;
        this.info = info;
    }

//    public Employee(String surname, String lastname, String id, String info){
//        this.surname = surname;
//        this.lastname = lastname;
//        this.id = id;
//        this.info = info;
//    }

    public Employee(String surname, String lastname, String id, String info, int position){
        this.surname = surname;
        this.lastname = lastname;
        this.id = id;
        this.info = info;
        this.position = position;
    }
//    public Employee(String name, double x, double y, LatLng latLng){
//        this.name = name;
//        this.x = x;
//        this.y = y;
//        this.latLng = latLng;
//    }

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


    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
}
