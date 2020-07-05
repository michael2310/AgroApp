package com.example.myapplication.ui.FieldRecords;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.myapplication.Adapters.FieldDetailAdapterPlantProtection;
import com.example.myapplication.Dialogs.DialogDetail;
import com.example.myapplication.Dialogs.DialogDetailRemove;
//import com.example.myapplication.Models.FieldsDetail;
import com.example.myapplication.Models.FieldsDetailPlantProtection;
import com.example.myapplication.R;
import com.example.myapplication.ui.FieldRecords.FieldsDetailActivity;
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
public class PlantProtectionFragment extends Fragment implements ChildEventListener, DialogDetailRemove.DialogDetailRemoveListener {

    private String fieldId;
    String a;

    DatabaseReference reference;
    String email;
    String id;

    String plant;
    String chemia;
    String date;
    int positionInfo;

    FieldDetailAdapterPlantProtection fieldDetailAdapter;
    public PlantProtectionFragment() {
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
            reference = FirebaseDatabase.getInstance().getReference("fieldsDetailPlantProtection").child(a);
            reference.addChildEventListener(this);
            id = user.getUid();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_plant_protection, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.plantProtectionRecycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);

        fieldDetailAdapter = new FieldDetailAdapterPlantProtection();
        recyclerView.setAdapter(fieldDetailAdapter);

        fieldDetailAdapter.setListener(new FieldDetailAdapterPlantProtection.Listener() {
            @Override
            public void onClick(int position) {
                plant = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getPlant();
                // chemia = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getChemia();
                date = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getDate();
                id = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getId();
                openDialog();
            }
        });


        fieldDetailAdapter.setListener1(new FieldDetailAdapterPlantProtection.Listener() {
            @Override
            public void onClick(int position) {
                id = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getId();
                FieldsDetailActivity fieldsDetailActivity = (FieldsDetailActivity) getActivity();
                fieldsDetailActivity.setId(id);
                positionInfo = position;
//                fieldDetailAdapter.fieldsDetailsArrayList.remove(position);
//                fieldDetailAdapter.notifyItemRemoved(position);
                openDialogRemove(position);
            }
        });
    }


    private void openDialog() {
        DialogDetail dialogDetail = new DialogDetail(plant, chemia, date);
        dialogDetail.show(getChildFragmentManager(), "Dialog Detail");
    }


    private void openDialogRemove(int position){
        DialogDetailRemove dialogDetailRemove = new DialogDetailRemove(position);
        dialogDetailRemove.show(getChildFragmentManager(), "Dialog Detail Remove");
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
            FieldsDetailPlantProtection field = dataSnapshot.getValue(FieldsDetailPlantProtection.class);
            fieldDetailAdapter.fieldsDetailsArrayList.add(field);
            fieldDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        if(fieldDetailAdapter != null) {
            FieldsDetailPlantProtection field = dataSnapshot.getValue(FieldsDetailPlantProtection.class);
            fieldDetailAdapter.fieldsDetailsArrayList.remove(positionInfo);
            fieldDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }


    @Override
    public void removeText() {

    }
}
