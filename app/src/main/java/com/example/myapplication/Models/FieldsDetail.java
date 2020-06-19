package com.example.myapplication.Models;

import java.util.ArrayList;

public class FieldsDetail {
    private String userName;
    private String plant;
    private String chemia;
    private String date;
    private String id;
    private String category;
    //private String choosenFragment;



    ArrayList<FieldsDetail> fieldsArrayList = new ArrayList<>();

    public FieldsDetail(){
    }

    public FieldsDetail(String userName, String plant, String chemia, String date, String id, String category ) {
        this.userName = userName;
        this.plant = plant;
        this.chemia = chemia;
        this.date = date;
        this.id = id;
        this.category = category;
    }

    public FieldsDetail(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getChemia() {
        return chemia;
    }

    public void setChemia(String chemia) {
        this.chemia = chemia;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}