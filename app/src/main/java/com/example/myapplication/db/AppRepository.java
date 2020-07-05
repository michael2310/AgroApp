package com.example.myapplication.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.Models.Fields;
import com.example.myapplication.Models.Owner;
import com.google.android.gms.tasks.Continuation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AppRepository {

    private final static String TAG = "AppRepository";

    private final DatabaseReference fieldsRef;
    private final DatabaseReference fieldsDetailUprawa;
    private final DatabaseReference usersRef;
    private final DatabaseReference farmWorkersRef;
    private final FirebaseAuth auth;

    private static AppRepository instance = null;

    public static AppRepository getInstance() {
        if (instance == null) {
            instance = new AppRepository();
        }
        return instance;
    }

    public boolean isUserLogged() {
        return auth.getCurrentUser() != null;
    }

    private AppRepository() {
        fieldsRef = FirebaseDatabase.getInstance().getReference("fields");
        fieldsDetailUprawa = FirebaseDatabase.getInstance().getReference("fieldsDetailUprawa");
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        farmWorkersRef = FirebaseDatabase.getInstance().getReference().child("FarmWorkers");
        auth = FirebaseAuth.getInstance();
    }

    public DatabaseReference getFarmWorkersRef() {
        return farmWorkersRef;
    }

    public DatabaseReference getUserFarmWorkersRef() {
        return farmWorkersRef.child(auth.getCurrentUser().getUid());
    }

    public DatabaseReference getUserRef() {
        return usersRef.child(auth.getCurrentUser().getUid());
    }

    private DatabaseReference getUserFieldRef() {
        return fieldsRef.child(auth.getCurrentUser().getUid());
    }


    public void addField(String name, String number, double area) {
        if (auth.getCurrentUser() != null) {
            getUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: ");
                    if (dataSnapshot.exists()) {
                        Owner owner = dataSnapshot.getValue(Owner.class);
                        String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                        int newFields = owner.getTotalFields() + 1;
                        double newArea = area + owner.getTotalArea();
                        getUserFieldRef().child(id).setValue(new Fields(name, number, area + "", id));
                        getUserRef().child("totalArea").setValue(newArea)
                                .continueWith((Continuation<Void, Object>) task -> getUserRef().child("totalFields").setValue(newFields));
                        Log.d("postKey", id);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: ");
                }
            });
        }
    }


}
