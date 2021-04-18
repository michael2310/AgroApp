package com.example.myapplication.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.models.Machine;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MachinesRepository {

    private static final String TAG = "MachinesRepository";
    private final DatabaseReference machinesRef;
    private final FirebaseAuth auth;
    private static MachinesRepository instance;

    public static MachinesRepository getInstance() {
        if (instance == null) {
            instance = new MachinesRepository();
        }
        return instance;
    }

    private MachinesRepository() {
        machinesRef = FirebaseDatabase.getInstance().getReference("machines");
        auth = FirebaseAuth.getInstance();
    }

    public DatabaseReference getMachinesRef(){
        return machinesRef;
    }

    public DatabaseReference getOwnerMachinesRef(String id){
        return  machinesRef.child(id);
    }

    public DatabaseReference getUserMachinesRef() {
        return machinesRef.child(auth.getCurrentUser().getUid());
    }


    public void addMachine(String brand, String model, String category) {
        if (auth.getCurrentUser() != null) {
            UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: ");
                    if (dataSnapshot.exists()) {
                        String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                        getUserMachinesRef().child(id).setValue(new Machine(brand, model, category));
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
