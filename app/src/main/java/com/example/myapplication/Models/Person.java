package com.example.myapplication.Models;

import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;

public class Person {
    private String name;
    private String email;
    private String id;
    private String code;

    //public String uid;

    public Person() {
    }

    ArrayList<Person> personArrayList = new ArrayList<>();

    public Person(String name, String email, String id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public Person(String name, String email, String id, String code) {
        this.name = name;
        this.email = email;
        this.id = id;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
