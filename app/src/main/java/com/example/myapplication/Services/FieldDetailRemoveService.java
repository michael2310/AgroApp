package com.example.myapplication.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FieldDetailRemoveService extends Service {
    private static final String TAG = RecordService.class.getSimpleName();

    String id;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!intent.hasExtra("id")){
            return super.onStartCommand(intent, flags, startId);
        }
        String id = intent.getStringExtra("id");
        String fieldId = intent.getStringExtra("fieldId");
        String userName = intent.getStringExtra("userName");
if(fieldId != null) {
    // sprawdzić
    FirebaseDatabase.getInstance().getReference("fieldsDetailUprawa").child(fieldId).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d(TAG, "onDataChange: ");
            if (dataSnapshot.exists()) {
                //String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                // FirebaseDatabase.getInstance().getReference("fields").child(userName).push().setValue(new Fields(userName, name, number, area));
                FirebaseDatabase.getInstance().getReference("fieldsDetailUprawa").child(fieldId).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: ");
                    }
                });
                Log.d("postKey", id);
            }
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(TAG, "onCancelled: ");
        }
    });

    FirebaseDatabase.getInstance().getReference("fieldsDetailOchrona").child(fieldId).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d(TAG, "onDataChange: ");
            if (dataSnapshot.exists()) {
                //String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                // FirebaseDatabase.getInstance().getReference("fields").child(userName).push().setValue(new Fields(userName, name, number, area));
                FirebaseDatabase.getInstance().getReference("fieldsDetailOchrona").child(fieldId).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: ");
                    }
                });
                Log.d("postKey", id);
            }
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(TAG, "onCancelled: ");
        }
    });

    FirebaseDatabase.getInstance().getReference("fieldsDetailNawozenie").child(fieldId).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d(TAG, "onDataChange: ");
            if (dataSnapshot.exists()) {
                //String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                // FirebaseDatabase.getInstance().getReference("fields").child(userName).push().setValue(new Fields(userName, name, number, area));
                FirebaseDatabase.getInstance().getReference("fieldsDetailNawozenie").child(fieldId).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: ");
                    }
                });
                Log.d("postKey", id);
            }
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.d(TAG, "onCancelled: ");
        }
    });

}

else {

    FirebaseDatabase.getInstance().getReference("fields").addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d(TAG, "onDataChange: ");
            if (dataSnapshot.exists()) {
                //String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                // FirebaseDatabase.getInstance().getReference("fields").child(userName).push().setValue(new Fields(userName, name, number, area));
                FirebaseDatabase.getInstance().getReference("fields").child(userName).child(id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "onComplete: ");
                    }
                });
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
