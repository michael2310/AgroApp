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
import com.example.myapplication.Dialogs.DialogWorkers;
import com.example.myapplication.Models.Employee;
import com.example.myapplication.R;
import com.example.myapplication.db.UsersRepository;
import com.example.myapplication.db.WorkersRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorkersActivity extends AppCompatActivity implements ChildEventListener {
    private static final String TAG = WorkersActivity.class.getSimpleName();
    FloatingActionButton fab;
    EmployeeAdapter employeeAdapter;
    String email;
    String id;
    String farmMemberId;

    Employee employee;
    ArrayList<Employee> employeeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers);

        employeeList = new ArrayList<>();

        //WorkersRepository.getInstance().getUserFarmWorkersRef().addChildEventListener(this);


        fab = (FloatingActionButton) findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tak jak działało do tej pory
                //openDialog();
                if (UsersRepository.getInstance().isUserLogged()) {
                    // to sa te metody na dole
                    UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
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


        WorkersRepository.getInstance().getUserFarmWorkersRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateWorkers(dataSnapshot);
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
            }
        });
    }

    private void updateWorkers(@NonNull DataSnapshot dataSnapshot) {
        employeeList.clear();
        if (dataSnapshot.exists()) {
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                farmMemberId = data.child("FarmMemberId").getValue(String.class);

                UsersRepository.getInstance().getUsersRef().child(farmMemberId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        employee = dataSnapshot.getValue(Employee.class);
                        employeeList.add(employee);
                        employeeAdapter.notifyDataSetChanged();
                        //employeeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Log.d(TAG, "onDataChange: ");
            }
        }
    }

    private void openDialog() {
        //create instance of opendialog
        DialogWorkers dialogWorkers = new DialogWorkers();
        dialogWorkers.show(getSupportFragmentManager(), "Dialog Workers");
    }


    @Override
    protected void onResume() {
        UsersRepository.getInstance().getCurrentUserRef().addChildEventListener(this);
        super.onResume();

    }

    //zakonczenie w onpause
    @Override
    protected void onPause() {
        //UsersRepository.getInstance().getCurrentUserRef().removeEventListener(this);
        WorkersRepository.getInstance().getUserFarmWorkersRef().removeEventListener(this);
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
//        if (employeeAdapter != null) {
//            updateWorkers(dataSnapshot);
//            employeeAdapter.notifyDataSetChanged();
//        }
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//        if(employeeAdapter != null) {
//            String key = dataSnapshot.getKey();
//            for (int i = 0; i < employeeList.size(); i++) {
//                if (employeeList.get(i).getId().equals(key))
//                    employeeList.remove(i);
//            }
//            employeeAdapter.notifyDataSetChanged();
//        }
        if(employeeAdapter != null) {
            updateWorkers(dataSnapshot);
            employeeAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

}
