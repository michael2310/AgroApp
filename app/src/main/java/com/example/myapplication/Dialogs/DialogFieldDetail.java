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
    private EditText editTextChemia;
    private EditText editTextDate;
    private Spinner spinner;
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
                String chemia = editTextChemia.getText().toString();
                String date = editTextDate.getText().toString();
                String category = String.valueOf(spinner.getSelectedItem());
                listener.applyTexts(plant, chemia, date, category);
            }
        });

        editTextPlant = view.findViewById(R.id.editTextPlant);
        editTextChemia = view.findViewById(R.id.editTextChemia);
        editTextDate = view.findViewById(R.id.editTextDate);
        spinner = view.findViewById(R.id.spinner);


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
        void applyTexts(String plant, String chemia, String date, String category);
    }
}
