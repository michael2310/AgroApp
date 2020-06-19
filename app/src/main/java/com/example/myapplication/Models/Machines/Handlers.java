package com.example.myapplication.Models.Machines;

public class Handlers {

    String name;

    public Handlers (String name){
        this.name = name;
    }

    public static final Handlers [] handlers = {
            new Handlers("Claas"),
            new Handlers("John Deere"),
            new Handlers("Merlo"),
    };

    public String getName() {
        return name;
    }
}
