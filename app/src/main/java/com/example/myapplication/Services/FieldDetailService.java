package com.example.myapplication.Services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import com.example.myapplication.Models.FieldsDetail;
import com.example.myapplication.Models.FieldsDetailCultivation;
import com.example.myapplication.Models.FieldsDetailFertilization;
import com.example.myapplication.Models.FieldsDetailPlantProtection;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FieldDetailService extends Service {
    private static final String TAG = RecordService.class.getSimpleName();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (!intent.hasExtra("fieldId")){
           return super.onStartCommand(intent, flags, startId);
        }
        String fieldId = intent.getStringExtra("fieldId");
        String category = intent.getStringExtra("category");
        String userName = intent.getStringExtra("userName");
        String plant = intent.getStringExtra("plant");
        String chemia = intent.getStringExtra("chemia");
        String date = intent.getStringExtra("date");
        //String area = intent.getStringExtra("area");

        ///
        String dose = intent.getStringExtra("dose");
        String developmentPhase = intent.getStringExtra("developmentPhase");

        //
        String cultivationType = intent.getStringExtra("cultivationType");
        String sowingType = intent.getStringExtra("sowingType");
        String info = intent.getStringExtra("info");


if(user != null) {
    //  FirebaseDatabase.getInstance().getReference("fieldsDetail").child(fieldId).push().setValue(new FieldsDetail(userName, plant, chemia, date, fieldId));
    // sprawdzić
    FirebaseDatabase.getInstance().getReference("fields").child(user.getUid()).child(fieldId).addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d(TAG, "onDataChange: ");
            if (dataSnapshot.exists()) {
                String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                // FirebaseDatabase.getInstance().getReference("fields").child(userName).push().setValue(new Fields(userName, name, number, area));
//                    FirebaseDatabase.getInstance().getReference("fieldsDetail").child(fieldId).child(id).setValue(new FieldsDetail(userName, plant, chemia, date, id, category));
                if (category.equals("Uprawa")) {
               //     FirebaseDatabase.getInstance().getReference("fieldsDetailUprawa").child(fieldId).child(id).setValue(new FieldsDetailCultivation(userName, plant, cultivationType, sowingType, date, info, id, category));
                }
                if (category.equals("Ochrona")) {
                 //   FirebaseDatabase.getInstance().getReference("fieldsDetailOchrona").child(fieldId).child(id).setValue(new FieldsDetailPlantProtection(userName, plant, chemia, dose, developmentPhase, date, id, category));
                }
                if (category.equals("Nawozenie")) {
               //     FirebaseDatabase.getInstance().getReference("fieldsDetailNawozenie").child(fieldId).child(id).setValue(new FieldsDetailFertilization(userName, plant, chemia, dose, developmentPhase, date, id, category));
                }

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
