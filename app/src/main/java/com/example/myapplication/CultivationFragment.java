package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.FieldDetailAdapterCultivation;
import com.example.myapplication.Dialogs.DialogDetail;
import com.example.myapplication.Dialogs.DialogDetailRemove;
import com.example.myapplication.Models.FieldsDetailCultivation;
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
public class CultivationFragment extends Fragment implements ChildEventListener, DialogDetailRemove.DialogDetailRemoveListener {
    private static final String TAG = CultivationFragment.class.getSimpleName();

    String a;

    DatabaseReference reference;
    String email;
    String id;
    FieldDetailAdapterCultivation fieldDetailAdapter;

    String plant;
    String chemia;
    String date;
    int positionInfo;

    public CultivationFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       FieldsDetailActivity fieldsDetailActivity = (FieldsDetailActivity) getActivity();
       a = fieldsDetailActivity.fieldId;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            reference = FirebaseDatabase.getInstance().getReference("fieldsDetailUprawa").child(a);
            // to sa te metody na dole
           // reference.addChildEventListener(this);
        }
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
                plant = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getPlant();
                date = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getDate();
                id = fieldDetailAdapter.fieldsDetailsArrayList.get(position).getId();
                openDialog();
            }
        });

        fieldDetailAdapter.setListener1(new FieldDetailAdapterCultivation.Listener() {
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
        reference.addChildEventListener(this);
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
        Log.d(TAG, "onChildAdded:");
        if(fieldDetailAdapter != null) {
            FieldsDetailCultivation field = dataSnapshot.getValue(FieldsDetailCultivation.class);
            fieldDetailAdapter.fieldsDetailsArrayList.add(field);
            fieldDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Log.d(TAG, "onChildRemoved: ");
        if(fieldDetailAdapter != null) {
            FieldsDetailCultivation field = dataSnapshot.getValue(FieldsDetailCultivation.class);
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


    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                fieldDetailAdapter.fieldsDetailsArrayList.remove(position);
                fieldDetailAdapter.notifyDataSetChanged();
        }
    });

    @Override
    public void removeText() {

    }
}

