package com.example.myapplication.Dialogs;

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
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DialogCultivation extends DialogFragment {

    private EditText editTextPlant;
    private EditText editTextCultivationType;
    private EditText editTextSowingType;
    private EditText editTextDate;
    private EditText editTextInfo;

    private DialogListener listener;

    final Calendar myCalendar = Calendar.getInstance();

    

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_cultivation_detail, null);

        builder.setView(view).setTitle("Dodaj wpis").setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String plant = editTextPlant.getText().toString();
                String cultivationType = editTextCultivationType.getText().toString();
                String sowingType = editTextSowingType.getText().toString();
                String dateToString = editTextDate.getText().toString();
                String date;
                if(dateToString.equals("")){
                    Date currentTime = Calendar.getInstance().getTime();
                    date = currentTime.toString();
                } else {
                    date = editTextDate.getText().toString();
                }
                String info = editTextInfo.getText().toString();
                String category = "Uprawa";
                listener.applyTextsCultivation(plant, cultivationType, sowingType, date, info, category);
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

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListner");
        }
    }

    public interface DialogListener{
        void applyTextsCultivation(String plant, String cultivationType, String sowingType, String date, String info, String category);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateLabel() {
        String myFormat = "dd.MM.yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMANY);

        editTextDate.setText(sdf.format(myCalendar.getTime()) + " r.");
    }
}
