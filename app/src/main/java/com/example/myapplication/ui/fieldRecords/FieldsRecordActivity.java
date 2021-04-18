package com.example.myapplication.ui.fieldRecords;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapters.FieldAdapter;
import com.example.myapplication.dialogs.DialogField;
import com.example.myapplication.dialogs.DialogFieldEdtit;
import com.example.myapplication.dialogs.DialogRemove;
import com.example.myapplication.models.Fields;
import com.example.myapplication.R;
import com.example.myapplication.db.FieldsRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class FieldsRecordActivity extends AppCompatActivity implements ChildEventListener, DialogField.DialogListener, DialogFieldEdtit.DialogEditListener, DialogRemove.DialogRemoveListener {
    private static final String TAG = FieldsRecordActivity.class.getSimpleName();

    FloatingActionButton fab;
    FieldAdapter fieldAdapter;
    String id;
    int positionInfo;
    ArrayList<Fields> list;
    TextView textView;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fields_record);

        textView = (TextView) findViewById(R.id.textViewEmpty);
        RecyclerView fieldsRecycler = (RecyclerView) findViewById(R.id.fields_recycler);
        fab = (FloatingActionButton) findViewById(R.id.fields_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


        fieldsRecycler.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        fieldsRecycler.setLayoutManager(layoutManager);
        fieldAdapter = new FieldAdapter();
        fieldsRecycler.setAdapter(fieldAdapter);


        fieldAdapter.setListener(new FieldAdapter.Listener() {
            @Override
            public void onClick(int position) {
                String id = fieldAdapter.fieldsArrayList.get(position).getFieldId();
                Toast.makeText(FieldsRecordActivity.this, id, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(FieldsRecordActivity.this, FieldsDetailActivity.class);
                //przekazanie pozycji
                intent.putExtra(FieldsDetailActivity.EXTRA_FIELD_ID, id);
                startActivity(intent);
            }
        });


        fieldAdapter.setListener1(new FieldAdapter.Listener() {
            @Override
            public void onClick(int position) {
                openDialogEdit(position);
            }
        });

        fieldAdapter.setListener2(new FieldAdapter.Listener() {
            @Override
            public void onClick(int position) {
                String id = fieldAdapter.fieldsArrayList.get(position).getFieldId();
                positionInfo = position;
                openDialogRemove(id);
                //FieldsRepository.getInstance().removeField(id);
            }
        });
    }


    private void openDialog() {
        DialogField dialogField = new DialogField();
        dialogField.show(getSupportFragmentManager(), "Dialog Field");
    }


    private void openDialogEdit(int position) {
        String fieldId = fieldAdapter.fieldsArrayList.get(position).getFieldId();
        double oldArea = fieldAdapter.fieldsArrayList.get(position).getArea();
        fieldAdapter.fieldsArrayList.get(position).getArea();
        DialogFieldEdtit dialogFieldEdtit = new DialogFieldEdtit(fieldId, oldArea);
        dialogFieldEdtit.show(getSupportFragmentManager(), "Dialog Field Edit");
    }


    private void openDialogRemove(String id) {
        DialogRemove dialogRemove = new DialogRemove(id);
        dialogRemove.show(getSupportFragmentManager(), "Dialog Detail Remove");
    }


    @Override
    protected void onResume() {
        super.onResume();
        fieldAdapter.fieldsArrayList.clear();
        FieldsRepository.getInstance().getUserFieldRef().addChildEventListener(this);
    }


    //zakonczenie w onpause
    @Override
    protected void onPause() {
        FieldsRepository.getInstance().getUserFieldRef().removeEventListener(this);
        super.onPause();
    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildAdded: ");
        if (fieldAdapter != null) {
            textView.setVisibility(View.INVISIBLE);
            Fields field = dataSnapshot.getValue(Fields.class);
            fieldAdapter.fieldsArrayList.add(field);
            fieldAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildChanged: ");
        if (fieldAdapter != null) {
            Fields field = dataSnapshot.getValue(Fields.class);
            replace(field);
            fieldAdapter.notifyDataSetChanged();
        }
    }

    private void replace(Fields field) {
        for (Fields item : fieldAdapter.fieldsArrayList) {
            if (field.getFieldId().equals(item.getFieldId())) {
                int index = fieldAdapter.fieldsArrayList.indexOf(item);
                fieldAdapter.fieldsArrayList.set(index, field);
                break;
            }
        }
    }


    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Log.d(TAG, "onChildRemoved: ");
            if(fieldAdapter != null) {
                textView.setVisibility(View.INVISIBLE);
                String key = dataSnapshot.getKey();
                 for(Fields field : fieldAdapter.fieldsArrayList){
                     if(field.getFieldId().equals(key)){
                         fieldAdapter.fieldsArrayList.remove(field);
                     }
                 }
                fieldAdapter.notifyDataSetChanged();
            }
    }


    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildMoved: ");
    }


    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.d(TAG, "onCancelled: ");
    }


    @Override
    public void applyTexts(String number, String area, String name) {
        FieldsRepository.getInstance().addField(name, number, Double.parseDouble(area));
    }

    @Override
    public void changeTexts(String number, String area, String name, String fieldId, double oldArea) {
        Log.d(TAG, "onClick: ");
        FieldsRepository.getInstance().editField(name, number, Double.parseDouble(area), fieldId, oldArea);
    }


    @Override
    public void removeText(String id) {
        FieldsRepository.getInstance().removeField(id);
    }


    public void setId(String id) {
        this.id = id;
    }

}
