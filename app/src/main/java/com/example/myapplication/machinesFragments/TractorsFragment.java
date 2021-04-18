package com.example.myapplication.machinesFragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.adapters.MachinesAdapter;
import com.example.myapplication.models.Machine;
import com.example.myapplication.R;
import com.example.myapplication.db.MachinesRepository;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import static android.content.ContentValues.TAG;


public class TractorsFragment extends Fragment implements ChildEventListener {

    private MachinesAdapter machinesAdapter;

    public TractorsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tractors, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView tractorsRecycler = (RecyclerView) view.findViewById(R.id.tractorsRecycler);
        machinesAdapter = new MachinesAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        tractorsRecycler.setLayoutManager(layoutManager);
        tractorsRecycler.setAdapter(machinesAdapter);

        machinesAdapter.setListener(new MachinesAdapter.Listener() {
            @Override
            public void onClick(int position) {

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        machinesAdapter.machineArrayList.clear();
        MachinesRepository.getInstance().getUserMachinesRef().addChildEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MachinesRepository.getInstance().getUserMachinesRef().removeEventListener(this);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        Log.d(TAG, "onChildAdded: ");
        if(machinesAdapter != null){
            Machine machine = snapshot.getValue(Machine.class);
            if(machine.getCategory().equals("tractor")) {
                machinesAdapter.machineArrayList.add(machine);
            }
            machinesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
