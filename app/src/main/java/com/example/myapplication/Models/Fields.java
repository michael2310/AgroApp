package com.example.myapplication.Models;

import java.util.ArrayList;

public class Fields {

    private String name;
    private String number;
    private String area;
    private String fieldId;



    ArrayList<Fields> fieldsArrayList = new ArrayList<>();

    public Fields(){
    }

    public Fields(String name, String number, String area, String fieldId) {
        this.name = name;
        this.number = number;
        this.area = area;
        this.fieldId = fieldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }
}
