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
import com.example.myapplication.Dialogs.Details.DialogDetailInfo;
import com.example.myapplication.Dialogs.Details.DialogDetailRemove;
//import com.example.myapplication.Models.FieldsDetail;
import com.example.myapplication.Dialogs.Details.DialogEditPlantProtection;
import com.example.myapplication.Models.FieldsDetailPlantProtection;
import com.example.myapplication.R;
import com.example.myapplication.db.PlantProtectionRepository;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlantProtectionFragment extends Fragment implements ChildEventListener {

   // private String fieldId;
    private String fieldId;


    FieldDetailAdapterPlantProtection fieldDetailAdapter;
    public PlantProtectionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FieldsDetailActivity fieldsDetailActivity = (FieldsDetailActivity) getActivity();
        fieldId = fieldsDetailActivity.fieldId;

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
                String plant = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getPlant();
                String chemicals = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getChemicals();
                String dose = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getDose();
                String developmentPhase = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getDevelopmentPhase();
                String date = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getDate();
                String info = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getInfo();
                //id = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getId();
                openDialog(plant, chemicals, dose, developmentPhase, date, info);
                openDialog(plant, chemicals, dose, developmentPhase, date, info);
            }
        });


        fieldDetailAdapter.setListener1(new FieldDetailAdapterPlantProtection.Listener() {
            @Override
            public void onClick(int position) {
                String id = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getId();
              openDialogEdit(id, fieldId);
            }
        });

        fieldDetailAdapter.setListener2(new FieldDetailAdapterPlantProtection.Listener() {
            @Override
            public void onClick(int position) {
                String id = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getId();
                //PlantProtectionRepository.getInstance().removeFieldDetail(fieldId, id);
                openDialogRemove(fieldId, id);
            }
        });
    }


    private void openDialog(String plant, String chemicals, String dose, String developmentPhase, String date, String info) {
        DialogDetailInfo dialogDetailInfo = new DialogDetailInfo(plant, chemicals, dose, developmentPhase, date, info);
        dialogDetailInfo.show(getChildFragmentManager(), "Dialog Detail");
    }

    private void openDialogEdit(String id, String fieldId){
        DialogEditPlantProtection dialogEditPlantProtection = new DialogEditPlantProtection(id, fieldId);
        dialogEditPlantProtection.show(getChildFragmentManager(), "Dialog Edit Plant Protection");
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
        PlantProtectionRepository.getInstance().getUserFieldDetail(fieldId).addChildEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//        if(reference != null) {
//            reference.removeEventListener(this);
//        }
        PlantProtectionRepository.getInstance().getUserFieldDetail(fieldId).removeEventListener(this);


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
        if (fieldDetailAdapter != null) {
            FieldsDetailPlantProtection field = dataSnapshot.getValue(FieldsDetailPlantProtection.class);
            replace(field);
            fieldDetailAdapter.notifyDataSetChanged();
        }
    }


    private void replace(FieldsDetailPlantProtection field) {
        for (FieldsDetailPlantProtection item : fieldDetailAdapter.fieldsDetailsArrayList) {
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
            for(FieldsDetailPlantProtection field : fieldDetailAdapter.fieldsDetailsArrayList){
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
