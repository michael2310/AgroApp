package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.FieldDetailAdapter;
import com.example.myapplication.Models.FieldsDetail;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class FertilizationFragment extends Fragment implements ChildEventListener {

    private String fieldId;
    String a;

    DatabaseReference reference;
    String email;
    String id;
    FieldDetailAdapter fieldDetailAdapter;

    public FertilizationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FieldsDetailActivity fieldsDetailActivity = (FieldsDetailActivity) getActivity();
        a = fieldsDetailActivity.fieldId;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            reference = FirebaseDatabase.getInstance().getReference("fieldsDetailNawozenie").child(a);
            // to sa te metody na dole
            reference.addChildEventListener(this);
            id = user.getUid();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fertilization, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fertilizationRecycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);

        fieldDetailAdapter = new FieldDetailAdapter();
        recyclerView.setAdapter(fieldDetailAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(reference != null) {
            reference.removeEventListener(this);
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if(fieldDetailAdapter != null) {
            FieldsDetail field = dataSnapshot.getValue(FieldsDetail.class);
            fieldDetailAdapter.fieldsDetailsArrayList.add(field);
            fieldDetailAdapter.notifyDataSetChanged();
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
