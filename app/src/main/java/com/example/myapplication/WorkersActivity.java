package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.Adapters.EmployeesAdapter;
import com.example.myapplication.Dialogs.DialogWorkers;
import com.example.myapplication.Models.Employee;
import com.example.myapplication.Services.WorkersService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class WorkersActivity extends AppCompatActivity implements DialogWorkers.DialogListener, ChildEventListener {
    private static final String TAG = WorkersActivity.class.getSimpleName();
    FloatingActionButton fab;
    DatabaseReference reference;
    EmployeesAdapter employeesAdapter;
    String email;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);

        fab = (FloatingActionButton) findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tak jak działało do tej pory
                //openDialog();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "To jest twój kod");
                startActivity(intent);


            }
        });

        RecyclerView fieldsRecycler = (RecyclerView) findViewById(R.id.employee_recycler);
        fieldsRecycler.setHasFixedSize(true);

        //linear layout
        // LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        // fieldsRecycler.setLayoutManager(linearLayoutManager);
        //StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        fieldsRecycler.setLayoutManager(layoutManager);

        //adapter
        employeesAdapter = new EmployeesAdapter();
        fieldsRecycler.setAdapter(employeesAdapter);


        employeesAdapter.setListener(new EmployeesAdapter.Listener() {
            @Override
            public void onClick(int position) {

            }
        });
//        fieldAdapter.setListener(new FieldAdapter.Listener() {
//            @Override
//            public void onClick(int position) {
//                String id = fieldAdapter.fieldsArrayList.get(position).getFieldId();
//                String userName = fieldAdapter.fieldsArrayList.get(position).getUserName();
//                String name = fieldAdapter.fieldsArrayList.get(position).getName();
//                String number = fieldAdapter.fieldsArrayList.get(position).getNumber();
//                String area = fieldAdapter.fieldsArrayList.get(position).getArea();
//                // FirebaseDatabase.getInstance().getReference("fieldsDetatail").child(id).push().setValue(new FieldsDetail(userName, name, number, area)).addOnCompleteListener(new OnCompleteListener<Void>() {
//                // @Override
//                // public void onComplete(@NonNull Task<Void> task) {
//                // Log.d(TAG, "onComplete: ");
//                Toast.makeText(FieldsRecordActivity.this, id, Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(FieldsRecordActivity.this, FieldsDetailActivity.class);
//                //przekazanie pozycji
//                intent.putExtra(FieldsDetailActivity.EXTRA_FIELD_ID, id);
//                startActivity(intent);
//                //  }
//                // });
//
//                //FirebaseDatabase.getInstance().getReference("fieldsDetail").child()
//
//                //Intent intent = new Intent(FieldsRecordActivity.this, FieldsDetailActivity.class);
//                //przekazanie pozycji
//                //intent.putExtra(FieldsDetailActivity.EXTRA_FIELD_ID, position);
//                // startActivity(intent);
//            }
//        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            reference = FirebaseDatabase.getInstance().getReference("workers").child(user.getEmail().split("@")[0]);
            // to sa te metody na dole
            //reference.addChildEventListener(this);
            id = user.getUid();
        }

    }

    private void openDialog() {
        //create instance of opendialog
        DialogWorkers dialogWorkers = new DialogWorkers();
        dialogWorkers.show(getSupportFragmentManager(), "Dialog Workers");
    }

    @Override
    public void applyTexts(String surname, String lastname, String info) {
        Log.d(TAG, "onClick: ");
        Intent serviceIntent = new Intent(WorkersActivity.this, WorkersService.class);
        serviceIntent.putExtra("userName", email.split("@")[0]);
        serviceIntent.putExtra("surname", surname);
        serviceIntent.putExtra("lastname", lastname );
        serviceIntent.putExtra("info", info);
        startService(serviceIntent);
    }

    @Override
    protected void onResume() {
        reference.addChildEventListener(this);
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
        if(employeesAdapter != null) {
            Employee employee = dataSnapshot.getValue(Employee.class);
            employeesAdapter.employeeArrayList.add(employee);
            employeesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

}
