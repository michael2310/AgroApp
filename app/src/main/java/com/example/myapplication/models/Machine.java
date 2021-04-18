package com.example.myapplication.models;

public class Machine {
    String brand;
    String model;
    String category;

    public Machine(){

    }

    public Machine(String brand, String model, String category){
        this.brand = brand;
        this.model = model;
        this.category = category;
    }


    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
