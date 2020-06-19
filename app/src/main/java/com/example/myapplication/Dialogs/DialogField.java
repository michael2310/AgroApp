package com.example.myapplication.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

public class DialogField extends DialogFragment {
    private EditText editTextNumber;
    private EditText editTextArea;
    private EditText editTextName;
    private DialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view).setTitle("Dodaj działkę").setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String number = editTextNumber.getText().toString();
                String area = editTextArea.getText().toString();
                String name = editTextName.getText().toString();
                listener.applyTexts(number, area, name);
            }
        });

        editTextNumber = view.findViewById(R.id.editTextNumber);
        editTextArea = view.findViewById(R.id.editTextArea);
        editTextName = view.findViewById(R.id.editTextName);


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
        void applyTexts(String number, String area, String name);
    }

}
