package com.example.myapplication.ui.Workers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.myapplication.Adapters.EmployeeAdapter;
import com.example.myapplication.Adapters.EmployeesAdapter;
import com.example.myapplication.Dialogs.DialogWorkers;
import com.example.myapplication.Models.Employee;
import com.example.myapplication.R;
import com.example.myapplication.db.AppRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorkersActivity extends AppCompatActivity implements ChildEventListener {
    private static final String TAG = WorkersActivity.class.getSimpleName();
    FloatingActionButton fab;
    EmployeesAdapter employeesAdapter;
    EmployeeAdapter employeeAdapter;
    String email;
    String id;
    String circleMemberId;

    Employee employee;
    ArrayList<Employee> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        employeeList = new ArrayList<>();


        fab = (FloatingActionButton) findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tak jak działało do tej pory
                //openDialog();
                if (AppRepository.getInstance().isUserLogged()) {
                    // to sa te metody na dole
                    AppRepository.getInstance().getUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String name = dataSnapshot.child("name").getValue().toString();
                                String code = dataSnapshot.child("code").getValue().toString();
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_TEXT, "Użytkownik " + name + " prosi Cię o dołączenie do jego gospodarstwa. Wpisz w aplikacji AgroApp następujący kod: " + code);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            }
        });

        RecyclerView fieldsRecycler = (RecyclerView) findViewById(R.id.employee_recycler);
        fieldsRecycler.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        fieldsRecycler.setLayoutManager(layoutManager);


        AppRepository.getInstance().getUserFarmWorkersRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                employeeList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        circleMemberId = data.child("circleMemberId").getValue(String.class);

                        AppRepository.getInstance().getUserRef().child(circleMemberId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                employee = dataSnapshot.getValue(Employee.class);
                                employeeList.add(employee);
                                employeeAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        Log.d(TAG, "onDataChange: ");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //adapter
        //employeesAdapter = new EmployeesAdapter(employeeList);
        employeeAdapter = new EmployeeAdapter(employeeList, getApplicationContext());
        fieldsRecycler.setAdapter(employeeAdapter);
        employeeAdapter.notifyDataSetChanged();


        employeeAdapter.setListener(new EmployeeAdapter.Listener() {
            @Override
            public void onClick(int position) {
                String name = employeeList.get(position).getName();
                String mail = employeeList.get(position).getEmail();
                String employeeId = employeeList.get(position).getId();

                Intent intent = new Intent(WorkersActivity.this, EmployeeDetailActivity.class);
                //przekazanie pozycji
                // intent.putExtra(FieldsDetailActivity.EXTRA_FIELD_ID, id);
                intent.putExtra("name", name);
                intent.putExtra("mail", mail);
                intent.putExtra("employeeId", employeeId);
                startActivity(intent);
                //  }
                // });
            }
        });


//        employeesAdapter.setListener(new EmployeesAdapter.Listener() {
//            @Override
//            public void onClick(int position) {
//
//            }
//        });
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


//        if (user != null) {
//            email = user.getEmail();
//            //reference = FirebaseDatabase.getInstance().getReference("workers").child(user.getEmail().split("@")[0]);
//            reference = FirebaseDatabase.getInstance().getReference().child("FarmWorkers").child(user.getUid());
//            // to sa te metody na dole
//            //reference.addChildEventListener(this);
//            id = user.getUid();
//        }

    }

    private void openDialog() {
        //create instance of opendialog
        DialogWorkers dialogWorkers = new DialogWorkers();
        dialogWorkers.show(getSupportFragmentManager(), "Dialog Workers");
    }

//    @Override
//    public void applyTexts(String surname, String lastname, String info) {
//        Log.d(TAG, "onClick: ");
//        Intent serviceIntent = new Intent(WorkersActivity.this, WorkersService.class);
//        serviceIntent.putExtra("userName", email.split("@")[0]);
//        serviceIntent.putExtra("surname", surname);
//        serviceIntent.putExtra("lastname", lastname );
//        serviceIntent.putExtra("info", info);
//        startService(serviceIntent);
//    }

    @Override
    protected void onResume() {
        AppRepository.getInstance().getUserRef().addChildEventListener(this);
        super.onResume();

    }

    //zakonczenie w onpause
    @Override
    protected void onPause() {
        AppRepository.getInstance().getUserRef().removeEventListener(this);
        super.onPause();
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildAdded: ");
//        if(employeeAdapter != null) {
//            Employee employee = dataSnapshot.getValue(Employee.class);
//            employeeList.add(employee);
//            employeeAdapter.notifyDataSetChanged();
//        }
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
