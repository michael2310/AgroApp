package com.example.myapplication.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

public class DialogFieldDetail extends DialogFragment {
    private EditText editTextPlant;
    private EditText editTextChemical;
    private EditText editTextDose;
    private EditText editTextDevelopmentPhase;
    private EditText editTextDate;

    private DialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_field_detail, null);

        builder.setView(view).setTitle("Dodaj wpis").setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String plant = editTextPlant.getText().toString();
                String chemia = editTextChemical.getText().toString();
                String dose = editTextDose.getText().toString();
                String developmentPhase = editTextDevelopmentPhase.getText().toString();
                String date = editTextDate.getText().toString();
                String category = "Ochrona";
                listener.applyTexts(plant, chemia, dose, developmentPhase, date, category);
            }
        });

        editTextPlant = view.findViewById(R.id.editTextPlant);
        editTextChemical = view.findViewById(R.id.editTextChemical);
        editTextDose = view.findViewById(R.id.editTextDose);
        editTextDevelopmentPhase = view.findViewById(R.id.editTextDevelopmentPhase);
        editTextDate = view.findViewById(R.id.editTextDate);



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
        void applyTexts(String plant, String chemia, String dose, String developmentPhase, String date, String category);
    }
}
