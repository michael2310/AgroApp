package com.example.myapplication.ui.FieldRecords;

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

import com.example.myapplication.Adapters.FieldDetailAdapterCultivation;

import com.example.myapplication.Dialogs.Details.DialogDetailCultivationInfo;
import com.example.myapplication.Dialogs.Details.DialogDetailRemove;
import com.example.myapplication.Dialogs.Details.DialogEditCultivation;
import com.example.myapplication.Models.FieldsDetailCultivation;
import com.example.myapplication.R;
import com.example.myapplication.db.CultivationRepository;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;


public class CultivationFragment extends Fragment implements ChildEventListener{
    private static final String TAG = CultivationFragment.class.getSimpleName();



    FieldDetailAdapterCultivation fieldDetailAdapter;

    private String fieldId;

    public CultivationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       FieldsDetailActivity fieldsDetailActivity = (FieldsDetailActivity) getActivity();
       fieldId = fieldsDetailActivity.fieldId;

        return inflater.inflate(R.layout.fragment_cultivation, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.cultivationRecycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        fieldDetailAdapter = new FieldDetailAdapterCultivation();
        recyclerView.setAdapter(fieldDetailAdapter);

        fieldDetailAdapter.setListener(new FieldDetailAdapterCultivation.Listener() {
            @Override
            public void onClick(int position) {
                String plant = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getPlant();
                String cultivationType = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getCultivationType();
                String sowingType = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getSowingType();
                String date = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getDate();
                String id = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getId();
                String info = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getInfo();
                openDialog(plant, cultivationType, sowingType, date, info);
            }
        });


        fieldDetailAdapter.setListener1(new FieldDetailAdapterCultivation.Listener() {
            @Override
            public void onClick(int position) {
                String id = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getId();
                openDialogEdit(id, fieldId);
            }
        });

        fieldDetailAdapter.setListener2(new FieldDetailAdapterCultivation.Listener() {
            @Override
            public void onClick(int position) {
                String id = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getId();
//                CultivationRepository.getInstance().removeFieldDetail(fieldId, id);
                openDialogRemove(fieldId, id);
            }
        });
    }


    private void openDialog(String plant, String cultivationType, String sowingType, String date, String info) {
        DialogDetailCultivationInfo dialogDetailCultivationInfo = new DialogDetailCultivationInfo(plant, cultivationType, sowingType, date, info);
        dialogDetailCultivationInfo.show(getChildFragmentManager(), "Dialog Detail");
    }

    private void openDialogEdit(String id, String fieldId){
       DialogEditCultivation dialogEditCultivation = new DialogEditCultivation(id, fieldId);
       dialogEditCultivation.show(getChildFragmentManager(), "Dialog Edit Cultivation");
    }


    private void openDialogRemove(String fieldId, String id){
        DialogDetailRemove dialogDetailRemove = new DialogDetailRemove(fieldId, id);
        dialogDetailRemove.show(getChildFragmentManager(), "Dialog Detail Remove");
    }


    @Override
    public void onResume() {
        super.onResume();
        fieldDetailAdapter.fieldsDetailsArrayList.clear();
        CultivationRepository.getInstance().getUserFieldDetail(fieldId).addChildEventListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        CultivationRepository.getInstance().getUserFieldDetail(fieldId).removeEventListener(this);
//        if(reference != null) {
//            reference.removeEventListener(this);
//        }
    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildAdded:");
        if(fieldDetailAdapter != null) {
            FieldsDetailCultivation field = dataSnapshot.getValue(FieldsDetailCultivation.class);
            fieldDetailAdapter.fieldsDetailsArrayList.add(field);
            fieldDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        if (fieldDetailAdapter != null) {
            FieldsDetailCultivation field = dataSnapshot.getValue(FieldsDetailCultivation.class);
            replace(field);
            fieldDetailAdapter.notifyDataSetChanged();
        }
    }


    private void replace(FieldsDetailCultivation field) {
        for (FieldsDetailCultivation item : fieldDetailAdapter.fieldsDetailsArrayList) {
            if (field.getId().equals(item.getId())) {
                int index = fieldDetailAdapter.fieldsDetailsArrayList.indexOf(item);
                fieldDetailAdapter.fieldsDetailsArrayList.set(index, field);
                break;
            }
        }
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Log.d(TAG, "onChildRemoved: ");
        if(fieldDetailAdapter != null) {
                String key = dataSnapshot.getKey();
                for(FieldsDetailCultivation field : fieldDetailAdapter.fieldsDetailsArrayList){
                    if(field.getId().equals(key)){
                        fieldDetailAdapter.fieldsDetailsArrayList.remove(field);
                    }
                }
            fieldDetailAdapter.notifyDataSetChanged();
        }

        //String key = dataSnapshot.getKey();

//            for (int i = 0; i <fieldDetailAdapter.fieldsDetailsArrayList.size(); i ++) {
//                if (fieldDetailAdapter.fieldsDetailsArrayList.get(i).getId().equals(key))
//                    fieldDetailAdapter.fieldsDetailsArrayList.remove(i);
//            }
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }


}

