package com.example.myapplication.Models;

import com.example.myapplication.R;

public class MenuNames {

    private String name;
    private int imageId;

    public MenuNames(String name, int imageId){
        this.name = name;
        this.imageId = imageId;
    }

    public static final MenuNames[] names = {
            new MenuNames("Ewidencja", R.drawable.ic_folder_black_24dp),
            new MenuNames("Optymalizacja", R.drawable.ic_build_black_24dp),
            new MenuNames("Pracownicy",R.drawable.ic_person_black_24dp),
            new MenuNames("Maszyny", R.drawable.ic_directions_bus_black_24dp)
    };

    public String getName() {
        return name;
    }

    public int getImageId(){
        return imageId;
    }
}
