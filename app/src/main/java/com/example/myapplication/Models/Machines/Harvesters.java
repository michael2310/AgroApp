package com.example.myapplication.Models.Machines;

public class Harvesters {

    String name;

    public Harvesters (String name){
        this.name = name;
    }

    public static final Harvesters [] harvesters = {
            new Harvesters("Claas"),
            new Harvesters("John Deere"),
            new Harvesters("Deutz-Fahr"),
            new Harvesters("Fendt")
    };

    public String getName() {
        return name;
    }
}
