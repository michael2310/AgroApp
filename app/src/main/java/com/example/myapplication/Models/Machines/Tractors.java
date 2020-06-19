package com.example.myapplication.Models.Machines;

public class Tractors {
    private String name;
    private int resourceId;

    public Tractors (String name, int resourceId){
        this.name = name;
        this.resourceId = resourceId;
    }

    public Tractors (String name){
        this.name = name;
    }



    public static final Tractors [] tractors = {
            new Tractors("Claas"),
        new Tractors("John Deere"),
        new Tractors("Deutz-Fahr"),
        new Tractors("Fendt"),
        new Tractors("Valtra")
    };

    public String getName() {
        return name;
    }

    public int getResourceId(){
        return resourceId;
    }
}
