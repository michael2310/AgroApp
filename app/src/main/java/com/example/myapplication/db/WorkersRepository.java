package com.example.myapplication.db;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WorkersRepository {
    private final DatabaseReference farmWorkersRef;
    private final FirebaseAuth auth;
    private static WorkersRepository instance;

    public static WorkersRepository getInstance(){
        if(instance == null){
            instance = new WorkersRepository();
        }
        return instance;
    }

    private WorkersRepository() {
        farmWorkersRef = FirebaseDatabase.getInstance().getReference().child("FarmWorkers");
        auth = FirebaseAuth.getInstance();
    }



    public DatabaseReference getUserFarmWorkersRef() {
        return farmWorkersRef.child(auth.getCurrentUser().getUid());
    }
}
