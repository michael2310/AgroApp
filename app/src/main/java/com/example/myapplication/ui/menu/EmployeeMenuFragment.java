package com.example.myapplication.ui.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.myapplication.adapters.EmployeeDetailAdapter;
import com.example.myapplication.bt.BluetoothActivity;
import com.example.myapplication.db.UsersRepository;
import com.example.myapplication.ui.workers.EmployeeFarmCodeActivity;
import com.example.myapplication.ui.loginAndRegister.LoginActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class EmployeeMenuFragment extends Fragment implements ChildEventListener {


    private RecyclerView infoRecycler;
    private EmployeeDetailAdapter infoAdapter;
    private Button buttonEditMachine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_employee_menu, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        infoRecycler = (RecyclerView) view.findViewById(R.id.infoRecycler);
        ListView userListview = (ListView) view.findViewById(R.id.userListView);


        ListView machineListView = (ListView) view.findViewById(R.id.tractorsRecycler);
        ListView userListView = (ListView) view.findViewById(R.id.userListView);


        infoAdapter = new EmployeeDetailAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        infoRecycler.setLayoutManager(linearLayoutManager);
        infoRecycler.setAdapter(infoAdapter);

        infoAdapter.setListener(new EmployeeDetailAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(), EmployeeFarmCodeActivity.class);
                getActivity().startActivity(intent);
            }
        });

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 4:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Czy na pewno chcesz się wylogować?");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                startLoginActivity();
                                getActivity().finish();
                                FirebaseAuth.getInstance().signOut();
                            }
                        });
                        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ////
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                }
            }
        };
        userListview.setOnItemClickListener(itemClickListener);

        buttonEditMachine = (Button) view.findViewById(R.id.buttonEditMachine);
        buttonEditMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), BluetoothActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startLoginActivity(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        UsersRepository.getInstance().getCurrentUserRef().addChildEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            UsersRepository.getInstance().getCurrentUserRef().removeEventListener(this);
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        if(infoAdapter != null) {
            infoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {
        if(infoAdapter != null) {
            infoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}