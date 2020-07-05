package com.example.myapplication.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.Models.Fields;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RecordService extends Service {
    private static final String TAG = RecordService.class.getSimpleName();

    String id;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


//        if (!intent.hasExtra("userName")){
//            return super.onStartCommand(intent, flags, startId);
//        }
//        if (!intent.hasExtra("number")){
//            return super.onStartCommand(intent, flags, startId);
//        }
        String userName = intent.getStringExtra("userName");
        String name = intent.getStringExtra("name");
        String number = intent.getStringExtra("number");
        String area = intent.getStringExtra("area");
        if (area == null) {
            area = "0";
        }
        double addArea = Double.parseDouble(area);

        if (user != null) {
            // sprawdzić
            String finalArea = area;
            final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
            usersRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: ");
                    if (dataSnapshot.exists()) {
                        String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                        double currentArea = dataSnapshot.child("totalArea").getValue(Double.class);
                        int currentFields = dataSnapshot.child("totalFields").getValue(Integer.class);
                        int newFields = currentFields++;
                        double newArea = addArea + currentArea;
                        // FirebaseDatabase.getInstance().getReference("fields").child(userName).push().setValue(new Fields(userName, name, number, area));
                      //  FirebaseDatabase.getInstance().getReference("fields").child(user.getUid()).child(id).setValue(new Fields(userName, name, number, finalArea, id));
                        usersRef.child(user.getUid()).child("totalArea").setValue(newArea).continueWith((Continuation<Void, Object>) task -> usersRef.child(user.getUid()).child("totalFields").setValue(newFields));
                        //FirebaseDatabase.getInstance().getReference("af").child(user.getUid()).child("totalArea").setValue(newArea);
                        Log.d("postKey", id);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: ");
                }
            });
        }

        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
