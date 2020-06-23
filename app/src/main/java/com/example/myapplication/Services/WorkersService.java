package com.example.myapplication.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Models.Employee;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WorkersService extends Service {

    private final String TAG = WorkersService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!intent.hasExtra("userName")){
            return super.onStartCommand(intent, flags, startId);
        }
        String userName = intent.getStringExtra("userName");
        String surname = intent.getStringExtra("surname");
        String lastname = intent.getStringExtra("lastname");
        String info = intent.getStringExtra("info");

        // sprawdzić
        FirebaseDatabase.getInstance().getReference("users").child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");
                if(dataSnapshot.exists()){
                    String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                    // FirebaseDatabase.getInstance().getReference("fields").child(userName).push().setValue(new Fields(userName, name, number, area));
                    FirebaseDatabase.getInstance().getReference("workers").child(userName).child(id).setValue(new Employee(surname, lastname, id, info));
                    Log.d("postKey", id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
