package com.example.myapplication.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.models.FieldsDetailCultivation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CultivationRepository {
    private final static String TAG = "FieldsDetailRepository";
    private static CultivationRepository instance;
    private final DatabaseReference fieldsDetailCultivation;
    private final FirebaseAuth auth;


    public static CultivationRepository getInstance(){
        if (instance == null){
            instance = new CultivationRepository();
        }
        return instance;
    }



    private CultivationRepository(){
        auth = FirebaseAuth.getInstance();
        fieldsDetailCultivation = FirebaseDatabase.getInstance().getReference("fieldsDetailCultivation");
    }


    public DatabaseReference getUserFieldDetail(String fieldId){
        return fieldsDetailCultivation.child(fieldId);
    }


    public void addFieldDetail(String category, String plant, String cultivationType, String sowingType, String date, String info, String fieldId){
        if (auth.getCurrentUser() != null) {
            UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: ");
                    if (dataSnapshot.exists()) {
                        String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                        getUserFieldDetail(fieldId).child(id).setValue(new FieldsDetailCultivation(category, plant, cultivationType, sowingType, date, info, id, fieldId));
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


    public void editFieldDetail(String category, String plant, String cultivationType, String sowingType, String date, String info, String id, String fieldId){
        if (auth.getCurrentUser() != null) {
            UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        getUserFieldDetail(fieldId).child(id).setValue(new FieldsDetailCultivation(category, plant, cultivationType, sowingType, date, info, id, fieldId));
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
