package com.example.myapplication.Dialogs.Details;

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

import com.example.myapplication.Models.FieldsDetailPlantProtection;
import com.example.myapplication.R;
import com.example.myapplication.db.PlantProtectionRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Locale;

public class DialogEditPlantProtection extends DialogFragment {

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


    public DialogEditPlantProtection(String id, String fieldId){
        this.id = id;
        this.fieldId = fieldId;
    }

    @NonNull
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
                listener.changeTextsPlantProtection(category, id, fieldId, plant, chemicals, dose, developmentPhase, date, info);
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


        PlantProtectionRepository.getInstance().getUserFieldDetail(fieldId).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    FieldsDetailPlantProtection fieldsDetailPlantProtection = snapshot.getValue(FieldsDetailPlantProtection.class);
                    editTextPlant.setText(fieldsDetailPlantProtection.getPlant());
                    editTextChemical.setText(fieldsDetailPlantProtection.getChemicals());
                    editTextDose.setText(fieldsDetailPlantProtection.getDose());
                    editTextDevelopmentPhase.setText(fieldsDetailPlantProtection.getDevelopmentPhase());
                    editTextDate.setText(fieldsDetailPlantProtection.getDate());
                    editTextInfo.setText(fieldsDetailPlantProtection.getInfo());
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
        void changeTextsPlantProtection(String category,String id, String fieldId, String plant, String chemicals, String dose, String developmentPhase, String date, String info);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLabel() {
        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

        editTextDate.setText(sdf.format(myCalendar.getTime()) + " r.");
    }
}
