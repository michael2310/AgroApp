package com.example.myapplication.ui.menu;

import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.myapplication.Adapters.EmployeeDetailAdapter;
import com.example.myapplication.BluetoothActivity;
import com.example.myapplication.db.UsersRepository;
import com.example.myapplication.ui.Workers.EmployeeFarmCodeActivity;
import com.example.myapplication.ui.LoginAndRegister.LoginActivity;
import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.List;


public class EmployeeMenuFragment extends Fragment implements ChildEventListener {


    private MenuViewModel menuViewModel;
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
        menuViewModel =
                ViewModelProviders.of(this).get(MenuViewModel.class);
        //final TextView textView = root.findViewById(R.id.text_menu);
        menuViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //    textView.setText(s);
            }
        });
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_employee_menu, container, false);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        infoRecycler = (RecyclerView) view.findViewById(R.id.infoRecycler);
        ListView userListview = (ListView) view.findViewById(R.id.userListView);


        ListView machineListView = (ListView) view.findViewById(R.id.machineListView);
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
                                FirebaseAuth.getInstance().signOut();
                                startLoginActivity();
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
        UsersRepository.getInstance().getCurrentUserRef().removeEventListener(this);
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