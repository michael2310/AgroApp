package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.myapplication.Adapters.FieldAdapter;
import com.example.myapplication.Adapters.FieldDetailAdapterCultivation;
import com.example.myapplication.Dialogs.DialogDetailRemove;
import com.example.myapplication.Dialogs.DialogField;
import com.example.myapplication.Models.Fields;
import com.example.myapplication.Models.FieldsDetailCultivation;
import com.example.myapplication.Services.FieldDetailRemoveService;
import com.example.myapplication.Services.RecordService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FieldsRecordActivity extends AppCompatActivity implements ChildEventListener, DialogField.DialogListener, DialogDetailRemove.DialogDetailRemoveListener {
    private static final String TAG = FieldsRecordActivity.class.getSimpleName();
    
    FloatingActionButton fab;
    DatabaseReference reference;
    FieldAdapter fieldAdapter;
    String email;
    String id;
    String fieldIdToRemove;
    int positionInfo;
    ArrayList<Fields> list;
    FieldAdapter adapter;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fields_record);


        fab = (FloatingActionButton) findViewById(R.id.fields_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(FieldsRecordActivity.this, AddFieldActivity.class);
                //startActivity(intent);
                openDialog();
            }
        });

        RecyclerView fieldsRecycler = (RecyclerView) findViewById(R.id.fields_recycler);
        fieldsRecycler.setHasFixedSize(true);

        //linear layout
       // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
       // fieldsRecycler.setLayoutManager(linearLayoutManager);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        fieldsRecycler.setLayoutManager(layoutManager);

        //adapter
        fieldAdapter = new FieldAdapter();
        fieldsRecycler.setAdapter(fieldAdapter);

        fieldAdapter.setListener(new FieldAdapter.Listener() {
            @Override
            public void onClick(int position) {
                String id = fieldAdapter.fieldsArrayList.get(position).getFieldId();
                 String userName = fieldAdapter.fieldsArrayList.get(position).getUserName();
                 String name = fieldAdapter.fieldsArrayList.get(position).getName();
                 String number = fieldAdapter.fieldsArrayList.get(position).getNumber();
                 String area = fieldAdapter.fieldsArrayList.get(position).getArea();
               // FirebaseDatabase.getInstance().getReference("fieldsDetatail").child(id).push().setValue(new FieldsDetail(userName, name, number, area)).addOnCompleteListener(new OnCompleteListener<Void>() {
                   // @Override
                   // public void onComplete(@NonNull Task<Void> task) {
                       // Log.d(TAG, "onComplete: ");
                        Toast.makeText(FieldsRecordActivity.this, id, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(FieldsRecordActivity.this, FieldsDetailActivity.class);
                        //przekazanie pozycji
                        intent.putExtra(FieldsDetailActivity.EXTRA_FIELD_ID, id);
                        startActivity(intent);
                  //  }
               // });

                //FirebaseDatabase.getInstance().getReference("fieldsDetail").child()

                //Intent intent = new Intent(FieldsRecordActivity.this, FieldsDetailActivity.class);
                //przekazanie pozycji
                //intent.putExtra(FieldsDetailActivity.EXTRA_FIELD_ID, position);
               // startActivity(intent);
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            reference = FirebaseDatabase.getInstance().getReference("fields").child(user.getEmail().split("@")[0]);
            // to sa te metody na dole
            reference.addChildEventListener(this);
           id = user.getUid();
        }

        fieldAdapter.setListener1(new FieldAdapter.Listener() {
            @Override
            public void onClick(int position) {
                String id = fieldAdapter.fieldsArrayList.get(position).getFieldId();

                Toast.makeText(FieldsRecordActivity.this, id, Toast.LENGTH_SHORT).show();
                positionInfo = position;
                Intent serviceIntent = new Intent(FieldsRecordActivity.this, FieldDetailRemoveService.class);
                serviceIntent.putExtra("id", id);
                serviceIntent.putExtra("userName", email.split("@")[0]);
                //serviceIntent.putExtra("fieldId", id);
                startService(serviceIntent);
               // openDialogRemove(position);
               // removeText();
            }
        });

    }




    private void openDialog() {
        //create instance of opendialog
        DialogField dialogField = new DialogField();
        dialogField.show(getSupportFragmentManager(), "Dialog Field");
    }

    private void openDialogRemove(int position){
        DialogDetailRemove dialogDetailRemove = new DialogDetailRemove(position);
        dialogDetailRemove.show(getSupportFragmentManager(), "Dialog Detail Remove");
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
//zakonczenie w onpause
    @Override
    protected void onPause() {
        if(reference != null){
            reference.removeEventListener(this);
        }
        super.onPause();
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildAdded: ");
        if(fieldAdapter != null){
            Fields field = dataSnapshot.getValue(Fields.class);
            fieldAdapter.fieldsArrayList.add(field);
            fieldAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildChanged: ");
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Log.d(TAG, "onChildRemoved: ");
        if(fieldAdapter != null){
            Fields field = dataSnapshot.getValue(Fields.class);
            fieldAdapter.fieldsArrayList.remove(positionInfo);
            fieldAdapter.notifyDataSetChanged();
        }



    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildMoved: ");
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.d(TAG, "onCancelled: ");
    }



    @Override
    public void applyTexts(String number, String area, String name) {
        Log.d(TAG, "onClick: ");
        Intent serviceIntent = new Intent(FieldsRecordActivity.this, RecordService.class);
        serviceIntent.putExtra("userName", email.split("@")[0]);
        serviceIntent.putExtra("name", name);
        serviceIntent.putExtra("number", number);
        serviceIntent.putExtra("area", area);
        startService(serviceIntent);
    }

    @Override
    public void removeText() {
        Intent serviceIntent = new Intent(FieldsRecordActivity.this, FieldDetailRemoveService.class);
        serviceIntent.putExtra("id", fieldIdToRemove);
        //serviceIntent.putExtra("fieldId", id);
        startService(serviceIntent);
    }

//    public void hash(){
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("admin", id);
//        hashMap.put("data", "aaa");
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        String key = reference.child("fields").push().getKey();
//        hashMap.put("key", key);
//        Task task = reference.child("fields").child(key).setValue(hashMap);
//        task.addOnCompleteListener(new OnCompleteListener() {
//            @Override
//            public void onComplete(@NonNull Task task) {
//                Log.d(TAG, "onComplete: ");
//            }
//        });
//    }

    public void setId(String id){
        this.id = id;
    }
}
