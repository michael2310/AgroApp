package com.example.myapplication.dialogs.Details;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.models.FieldsDetailFertilization;
import com.example.myapplication.R;
import com.example.myapplication.db.FertilizationRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class DialogEditFertilization extends DialogFragment {
    private EditText editTextPlant;
    private EditText editTextChemical;
    private EditText editTextDose;
    private EditText editTextDevelopmentPhase;
    private EditText editTextDate;
    private EditText editTextInfo;
    private String id;
    private String fieldId;

    private DialogEditListener listener;

    final Calendar myCalendar = Calendar.getInstance();


    public DialogEditFertilization(String id, String fieldId){
        this.id = id;
        this.fieldId = fieldId;
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_field_detail, null);

        builder.setView(view).setTitle("Edytuj wpis").setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String plant = editTextPlant.getText().toString();
                String chemicals = editTextChemical.getText().toString();
                String dose = editTextDose.getText().toString();
                String developmentPhase = editTextDevelopmentPhase.getText().toString();
                String date = editTextDate.getText().toString();
                String info = editTextInfo.getText().toString();
                String category = "Ochrona";
                // listener.changeTextsPlantProtection(plant, chemicals, dose, developmentPhase, date, category, info, id);
                listener.changeTextsFertilization(category, id, plant, chemicals, dose, developmentPhase, date, info);
            }
        });

        editTextPlant = view.findViewById(R.id.editTextPlant);
        editTextChemical = view.findViewById(R.id.editTextChemical);
        editTextDose = view.findViewById(R.id.editTextDose);
        editTextDevelopmentPhase = view.findViewById(R.id.editTextDevelopmentPhase);
        editTextDate = view.findViewById(R.id.editTextDate);
        editTextInfo = view.findViewById(R.id.editTextInfo);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(view.getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        FertilizationRepository.getInstance().getUserFieldDetail(fieldId).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    FieldsDetailFertilization fieldsDetailFertilization = snapshot.getValue(FieldsDetailFertilization.class);
                    editTextPlant.setText(fieldsDetailFertilization.getPlant());
                    editTextChemical.setText(fieldsDetailFertilization.getChemicals());
                    editTextDose.setText(fieldsDetailFertilization.getDose());
                    editTextDevelopmentPhase.setText(fieldsDetailFertilization.getDevelopmentPhase());
                    editTextDate.setText(fieldsDetailFertilization.getDate());
                    editTextInfo.setText(fieldsDetailFertilization.getInfo());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogEditListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListner");
        }
    }

    public interface DialogEditListener {
        void changeTextsFertilization(String category,String id, String plant, String chemicals, String dose, String developmentPhase, String date, String info);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLabel() {
        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

        editTextDate.setText(sdf.format(myCalendar.getTime()) + " r.");
    }
}