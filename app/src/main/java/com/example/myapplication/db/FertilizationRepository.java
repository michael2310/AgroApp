package com.example.myapplication.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.models.FieldsDetailFertilization;
import com.example.myapplication.models.Owner;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FertilizationRepository {

    private final static String TAG = "FieldsDetailRepository";
    private static FertilizationRepository instance;
    private final DatabaseReference fieldsDetailFertilization;
    private final FirebaseAuth auth;



    public static FertilizationRepository getInstance(){
        if (instance == null){
            instance = new FertilizationRepository();
        }
        return instance;
    }



    private FertilizationRepository(){
        auth = FirebaseAuth.getInstance();
        fieldsDetailFertilization = FirebaseDatabase.getInstance().getReference("fieldsDetailFertilization");
    }


    public DatabaseReference getUserFieldDetail(String fieldId){
        return fieldsDetailFertilization.child(fieldId);
    }


    public void addFieldDetail(String category, String fieldId, String plant, String chemicals, String dose, String developmentPhase, String date, String info){
        if (auth.getCurrentUser() != null) {
            UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: ");
                    if (dataSnapshot.exists()) {
                        Owner owner = dataSnapshot.getValue(Owner.class);
                        String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                        getUserFieldDetail(fieldId).child(id).setValue(new FieldsDetailFertilization(category, id, fieldId, plant, chemicals, dose, developmentPhase, date, info));
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

    public void editFieldDetail (String category,String id, String fieldId, String plant, String chemicals, String dose, String developmentPhase, String date, String info){
        if (auth.getCurrentUser() != null) {
            UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        getUserFieldDetail(fieldId).child(id).setValue(new FieldsDetailFertilization(category, id, fieldId, plant, chemicals, dose, developmentPhase, date, info));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void removeFieldDetail (String fieldId, String id){
        if(auth.getCurrentUser() != null){
            getUserFieldDetail(fieldId).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "onComplete: ");
                }
            });
        }
    }
}