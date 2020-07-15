package com.example.myapplication.Models;

public class OptimalizationNames {
    String name;

    public OptimalizationNames(String name){
        this.name = name;
    }


    public static final OptimalizationNames[] names = {
      new OptimalizationNames("Oprysk"),
            new OptimalizationNames("Nawożenie"),
            new OptimalizationNames("Kolejny1"),
            new OptimalizationNames("Kolejny2"),
            new OptimalizationNames("Kolejny3"),
            new OptimalizationNames("Kolejny4")
    };


    public String getName() {
        return name;
    }
}
