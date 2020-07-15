package com.example.myapplication.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsersRepository {
    private static final String TAG = "UserRepository";
    private final DatabaseReference usersRef;
    private final FirebaseAuth auth;
    private static UsersRepository instance;
    private boolean b;


    public static UsersRepository getInstance(){
        if(instance == null){
            instance = new UsersRepository();
        }
        return instance;
    }

    public boolean isUserLogged() {
        return auth.getCurrentUser() != null;
    }

    private UsersRepository() {
        usersRef = FirebaseDatabase.getInstance().getReference("users");
        auth = FirebaseAuth.getInstance();
    }

    public DatabaseReference getUsersRef(){
        return usersRef;
    }

    public DatabaseReference getCurrentUserRef() {
        return usersRef.child(auth.getCurrentUser().getUid());
    }

    public DatabaseReference getCurrentUserSharing(){
        return usersRef.child(auth.getCurrentUser().getUid()).child("sharing");
    }



    public void changeSharing(boolean location){
        if(auth.getCurrentUser() != null){
            getCurrentUserSharing().setValue(location).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d(TAG, "onComplete: ");
                }
            });
        }
    }


}
