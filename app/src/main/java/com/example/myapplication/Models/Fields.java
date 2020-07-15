package com.example.myapplication.Models;

public class Fields {

    private String name;
    private String number;
    private Double area;
    private String fieldId;

    public Fields(){
    }

    public Fields(String name, String number, Double area, String fieldId) {
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

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }
}
