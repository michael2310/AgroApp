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

import com.example.myapplication.Models.FieldsDetailCultivation;
import com.example.myapplication.R;
import com.example.myapplication.db.CultivationRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DialogEditCultivation extends DialogFragment {

    private EditText editTextPlant;
    private EditText editTextCultivationType;
    private EditText editTextSowingType;
    private EditText editTextDate;
    private EditText editTextInfo;
    private String id;
    private String fieldId;

    private DialogEditCultivation.DialogEditListener listener;

    final Calendar myCalendar = Calendar.getInstance();


    public DialogEditCultivation(String id, String fieldId){
        this.id = id;
        this.fieldId = fieldId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_cultivation_detail, null);

        builder.setView(view).setTitle("Edytuj wpis").setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String plant = editTextPlant.getText().toString();
                String cultivationType = editTextCultivationType.getText().toString();
                String sowingType = editTextSowingType.getText().toString();
                String dateToString = editTextDate.getText().toString();
                String date;
                if(dateToString.equals("")){
                    Date currentTime = Calendar.getInstance().getTime();
                    String myFormat = "dd.MM.yyyy";
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.forLanguageTag(currentTime.toString()));
                    date = simpleDateFormat.toString();
                } else {
                    date = editTextDate.getText().toString();
                }
                String info = editTextInfo.getText().toString();
                String category = "Uprawa";
                listener.changeTextsCultivation(plant, cultivationType, sowingType, date, info, category, id);
            }
        });

        editTextPlant = view.findViewById(R.id.editTextPlant);
        editTextCultivationType = view.findViewById(R.id.editTextCultivationType);
        editTextSowingType = view.findViewById(R.id.editTextSowingType);
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


        CultivationRepository.getInstance().getUserFieldDetail(fieldId).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    FieldsDetailCultivation fieldsDetailCultivation = snapshot.getValue(FieldsDetailCultivation.class);
                    editTextPlant.setText(fieldsDetailCultivation.getPlant());
                    editTextCultivationType.setText(fieldsDetailCultivation.getCultivationType());
                    editTextSowingType.setText(fieldsDetailCultivation.getSowingType());
                    editTextDate.setText(fieldsDetailCultivation.getDate());
                    editTextInfo.setText(fieldsDetailCultivation.getInfo());
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
            listener = (DialogEditCultivation.DialogEditListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListner");
        }
    }

    public interface DialogEditListener {
        void changeTextsCultivation (String plant, String cultivationType, String sowingType, String date, String info, String category, String id);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

        editTextDate.setText(sdf.format(myCalendar.getTime()) + " r.");
    }


}
