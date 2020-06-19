package com.example.myapplication.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Models.Fields;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecordService extends Service {
    private static final String TAG = RecordService.class.getSimpleName();

    String id;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!intent.hasExtra("userName")){
            return super.onStartCommand(intent, flags, startId);
        }
        String userName = intent.getStringExtra("userName");
        String name = intent.getStringExtra("name");
        String number = intent.getStringExtra("number");
        String area = intent.getStringExtra("area");

        // sprawdzić
        FirebaseDatabase.getInstance().getReference("users").child(userName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: ");
                if(dataSnapshot.exists()){
                        String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                        // FirebaseDatabase.getInstance().getReference("fields").child(userName).push().setValue(new Fields(userName, name, number, area));
                        FirebaseDatabase.getInstance().getReference("fields").child(userName).child(id).setValue(new Fields(userName, name, number, area, id));
                        Log.d("postKey", id);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: ");
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
