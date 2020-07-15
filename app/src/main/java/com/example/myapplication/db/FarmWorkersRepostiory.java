package com.example.myapplication.db;

import android.app.Activity;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.Models.JoinEmployee;
import com.example.myapplication.Models.Owner;
import com.example.myapplication.ui.Workers.EmployeeFarmCodeActivity;
import com.example.myapplication.ui.Workers.EmployeeMainActivity;
import com.example.myapplication.ui.Workers.WorkersActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class FarmWorkersRepostiory {
    private static FarmWorkersRepostiory instance;
    private final DatabaseReference farmWorkersRef;
    private final FirebaseAuth auth;


    public static FarmWorkersRepostiory getInstance(){
        if(instance == null){
            instance = new FarmWorkersRepostiory();
        }
        return instance;
    }

    private FarmWorkersRepostiory(){
        auth = FirebaseAuth.getInstance();
        farmWorkersRef = FirebaseDatabase.getInstance().getReference("FarmWorkers");
    }

    public DatabaseReference getOwnerId(String ownerId){
        return farmWorkersRef.child(ownerId);
    }

    public void addFarmMember(View view, String pinValue) {
        if (auth.getCurrentUser() != null) {
            Query query = UsersRepository.getInstance().getUsersRef().orderByChild("code").equalTo(pinValue);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Owner owner = null;
                        for (DataSnapshot childDss : dataSnapshot.getChildren()) {
                            owner = childDss.getValue(Owner.class);
                            String ownerId = owner.getId();

                            JoinEmployee joinEmployee = new JoinEmployee(auth.getCurrentUser().getUid());

                            //circleReference1.child(user.getUid()).setValue(joinEmployee).addOnCompleteListener(new OnCompleteListener<Void>() {
                            getOwnerId(ownerId).child(auth.getCurrentUser().getUid()).setValue(joinEmployee);
                            UsersRepository.getInstance().getCurrentUserRef().child("farmName").setValue(owner.getName());
                            UsersRepository.getInstance().getCurrentUserRef().child("farmCode").setValue(owner.getCode()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(view.getContext(), "Dodano pracownika", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(view.getContext(), EmployeeMainActivity.class);
                                        view.getContext().startActivity(intent);
                                        ((Activity)(view.getContext())).finish();
                                    }

                                }
                            });

                        }
                    } else {
                        Toast.makeText(view.getContext(), "Nieprawidłowy kod", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void deleteFarmMemberForOwner(View view, String employeeId) {
        if (auth.getCurrentUser() != null) {
            UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Owner owner = snapshot.getValue(Owner.class);
                    getOwnerId(auth.getCurrentUser().getUid()).child(employeeId).removeValue();
                    UsersRepository.getInstance().getUsersRef().child(employeeId).child("farmName").removeValue();
                    UsersRepository.getInstance().getUsersRef().child(employeeId).child("farmCode").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(view.getContext(), "Usunięto", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(view.getContext(), WorkersActivity.class);
                            view.getContext().startActivity(intent);
                            ((Activity) (view.getContext())).finish();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }


        public void deleteFarmMemberForEmployee (View view){
            if (auth.getCurrentUser() != null) {
                Query query = UsersRepository.getInstance().getUsersRef().orderByChild("code");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Owner owner = null;
                            for (DataSnapshot childDss : dataSnapshot.getChildren()) {
                                owner = childDss.getValue(Owner.class);
                                String ownerId = owner.getId();

                                getOwnerId(ownerId).child(auth.getCurrentUser().getUid()).removeValue();
                                UsersRepository.getInstance().getCurrentUserRef().child("farmName").removeValue();
                                UsersRepository.getInstance().getCurrentUserRef().child("farmCode").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(view.getContext(), "Usunięto", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(view.getContext(), EmployeeMainActivity.class);
                                            view.getContext().startActivity(intent);
                                            ((Activity) (view.getContext())).finish();
                                        }

                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
    }
}
