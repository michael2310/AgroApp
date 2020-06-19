package com.example.myapplication.Models;

import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;

public class Person {
    public String name;
    public String email;
    public String id;
    //public String uid;

    public Person() {
    }

    ArrayList<Person> personArrayList = new ArrayList<>();

    public Person(String name, String email, String id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }
}
