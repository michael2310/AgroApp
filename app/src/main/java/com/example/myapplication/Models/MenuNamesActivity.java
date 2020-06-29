package com.example.myapplication.Models;

import com.example.myapplication.R;

public class MenuNamesActivity {

    private String name;
    private int imageId;

    public MenuNamesActivity(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public static final MenuNamesActivity[] names = {
            new MenuNamesActivity("Ewidencja", R.drawable.ic_folder_black_24dp),
            new MenuNamesActivity("Optymalizacja", R.drawable.ic_build_black_24dp),
            new MenuNamesActivity("Pracownicy",R.drawable.ic_person_black_24dp),
            new MenuNamesActivity("Maszyny", R.drawable.ic_directions_bus_black_24dp)
    };

    public String getName() {
        return name;
    }

    public int getImageId(){
        return imageId;
    }
}
