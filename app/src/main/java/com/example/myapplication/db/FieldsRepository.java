package com.example.myapplication.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myapplication.models.Fields;
import com.example.myapplication.models.Owner;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FieldsRepository {
    private static final String TAG = "FieldsRepository";
    private final DatabaseReference fieldsRef;
    private final FirebaseAuth auth;
    private static FieldsRepository instance;

    public static FieldsRepository getInstance() {
        if (instance == null) {
            instance = new FieldsRepository();
        }
        return instance;
    }

    private FieldsRepository() {
        fieldsRef = FirebaseDatabase.getInstance().getReference("fields");
        auth = FirebaseAuth.getInstance();
    }


    public DatabaseReference getUserFieldRef() {
        return fieldsRef.child(auth.getCurrentUser().getUid());
    }


    public void addField(String name, String number, double area) {
        if (auth.getCurrentUser() != null) {
            UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: ");
                    if (dataSnapshot.exists()) {
                        Owner owner = dataSnapshot.getValue(Owner.class);
                        String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                        int newFields = owner.getTotalFields() + 1;
                        double newArea = area + owner.getTotalArea();
                        getUserFieldRef().child(id).setValue(new Fields(name, number, area, id));
                        UsersRepository.getInstance().getCurrentUserRef().child("totalArea").setValue(newArea)
                                .continueWith((Continuation<Void, Object>) task -> UsersRepository.getInstance().getCurrentUserRef().child("totalFields").setValue(newFields));
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


    public void removeField(String fieldId) {
        if (auth.getCurrentUser() != null) {
            UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Owner owner = snapshot.getValue(Owner.class);
                        getUserFieldRef().child(fieldId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    double area;

                                    if (snapshot.child("area") != null) {
                                        area = Double.parseDouble(snapshot.child("area").getValue().toString());
                                    } else {
                                        area = 0;
                                    }

                                    getUserFieldRef().child(fieldId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG, "onComplete: ");
                                            CultivationRepository.getInstance().getUserFieldDetail(fieldId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d(TAG, "onComplete: ");
                                                }
                                            });

                                            PlantProtectionRepository.getInstance().getUserFieldDetail(fieldId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d(TAG, "onComplete: ");
                                                }
                                            });

                                            FertilizationRepository.getInstance().getUserFieldDetail(fieldId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.d(TAG, "onComplete: ");
                                                }
                                            });

                                            int newFields = owner.getTotalFields() - 1;
                                            double newArea = owner.getTotalArea() - area;
                                            UsersRepository.getInstance().getCurrentUserRef().child("totalArea").setValue(newArea).continueWith((Continuation<Void, Object>) task1 -> UsersRepository.getInstance().getCurrentUserRef().child("totalFields").setValue(newFields));
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void editField(String name, String number, double area, String fieldId, double oldArea) {
        if (auth.getCurrentUser() != null) {
            UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Owner owner = snapshot.getValue(Owner.class);
                        getUserFieldRef().child(fieldId).setValue(new Fields(name, number, area, fieldId));
                        Owner updated = new Owner(owner.getName(),
                                owner.getEmail(),
                                owner.getId(),
                                owner.getCode(),
                                owner.getLat(),
                                owner.getLng(),
                                owner.getImageUrl(),
                                owner.getAccountType(),
                                owner.getSharing(),
                                owner.getTotalArea() - oldArea + area,
                                owner.getTotalFields()
                        );
                        UsersRepository.getInstance().getCurrentUserRef().setValue(updated);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
