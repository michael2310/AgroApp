package com.example.myapplication.ui.fieldRecords;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.myapplication.adapters.FieldDetailAdapterFertilization;
import com.example.myapplication.dialogs.Details.DialogDetailInfo;
import com.example.myapplication.dialogs.Details.DialogDetailRemove;
import com.example.myapplication.dialogs.Details.DialogEditFertilization;
import com.example.myapplication.models.FieldsDetailFertilization;
import com.example.myapplication.R;
import com.example.myapplication.db.FertilizationRepository;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class FertilizationFragment extends Fragment implements ChildEventListener {

    //private String fieldId;
    private String fieldId;
    FieldDetailAdapterFertilization fieldDetailAdapter;

    public FertilizationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FieldsDetailActivity fieldsDetailActivity = (FieldsDetailActivity) getActivity();
        fieldId = fieldsDetailActivity.fieldId;

        return inflater.inflate(R.layout.fragment_fertilization, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fertilizationRecycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);

        fieldDetailAdapter = new FieldDetailAdapterFertilization();
        recyclerView.setAdapter(fieldDetailAdapter);

        fieldDetailAdapter.setListener(new FieldDetailAdapterFertilization.Listener(){
            @Override
            public void onClick(int position) {
                String plant = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getPlant();
                String chemicals = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getChemicals();
                String dose = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getDose();
                String developmentPhase = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getDevelopmentPhase();
                String date = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getDate();
                String info = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getInfo();
                //id = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getId();
                openDialog(plant, chemicals, dose, developmentPhase, date, info);
            }
        });

        fieldDetailAdapter.setListener1(new FieldDetailAdapterFertilization.Listener() {
            @Override
            public void onClick(int position) {
                String id = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getId();
                openDialogEdit(id, fieldId);
            }
        });


        fieldDetailAdapter.setListener2(new FieldDetailAdapterFertilization.Listener() {
            @Override
            public void onClick(int position) {
                String id = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getId();
                //FertilizationRepository.getInstance().removeFieldDetail(fieldId, id);
                openDialogRemove(fieldId, id);
            }
        });
    }


    private void openDialog(String plant, String chemicals, String dose, String developmentPhase, String date, String info) {
        DialogDetailInfo dialogDetailInfo = new DialogDetailInfo(plant, chemicals, dose, developmentPhase, date, info);
        dialogDetailInfo.show(getChildFragmentManager(), "Dialog Detail");
    }

    private void openDialogEdit(String id, String fieldId){
        DialogEditFertilization dialogEditFertilization = new DialogEditFertilization(id, fieldId);
        dialogEditFertilization.show(getChildFragmentManager(), "Dialog Edit Fertilization");
    }


    private void openDialogRemove(String fieldId, String id){
        DialogDetailRemove dialogDetailRemove = new DialogDetailRemove(fieldId, id);
        dialogDetailRemove.show(getChildFragmentManager(), "Dialog Detail Remove");
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        fieldDetailAdapter.fieldsDetailsArrayList.clear();
        FertilizationRepository.getInstance().getUserFieldDetail(fieldId).addChildEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        if(reference != null) {
//            reference.removeEventListener(this);
//        }
        FertilizationRepository.getInstance().getUserFieldDetail(fieldId).removeEventListener(this);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if(fieldDetailAdapter != null) {
            FieldsDetailFertilization field = dataSnapshot.getValue(FieldsDetailFertilization.class);
            fieldDetailAdapter.fieldsDetailsArrayList.add(field);
            fieldDetailAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        if (fieldDetailAdapter != null) {
            FieldsDetailFertilization field = dataSnapshot.getValue(FieldsDetailFertilization.class);
            replace(field);
            fieldDetailAdapter.notifyDataSetChanged();
        }
    }


    private void replace(FieldsDetailFertilization field) {
        for (FieldsDetailFertilization item : fieldDetailAdapter.fieldsDetailsArrayList) {
            if (field.getId().equals(item.getId())) {
                int index = fieldDetailAdapter.fieldsDetailsArrayList.indexOf(item);
                fieldDetailAdapter.fieldsDetailsArrayList.set(index, field);
                break;
            }
        }
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        if(fieldDetailAdapter != null) {
            String key = dataSnapshot.getKey();
            for(FieldsDetailFertilization field : fieldDetailAdapter.fieldsDetailsArrayList){
                if(field.getId().equals(key)){
                    fieldDetailAdapter.fieldsDetailsArrayList.remove(field);
                }
            }
            fieldDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
