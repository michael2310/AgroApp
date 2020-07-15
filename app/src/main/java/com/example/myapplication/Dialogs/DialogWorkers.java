package com.example.myapplication.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

public class DialogWorkers extends DialogFragment {
    private DialogListener listener;


    private EditText textViewName;
    private EditText textViewMail;
    private EditText textViewInfo;

    private String name;
    private String mail;
    private String info;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_workers, null);

        builder.setView(view).setTitle("Dodaj pracownika").setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String surname = textViewName.getText().toString();
                String lastname = textViewMail.getText().toString();
                String info = textViewInfo.getText().toString();
                    if(!surname.equals("") && !lastname.equals("")) {
                    listener.applyTexts(surname, lastname, info);
                } else {
                    Toast.makeText(getContext(), "Podaj imię i nazwisko", Toast.LENGTH_SHORT);
                }
            }
        });

        textViewName = view.findViewById(R.id.editTextSurname);
        textViewMail = view.findViewById(R.id.editTextLastname);
        textViewInfo = view.findViewById(R.id.editTextInfo);

        textViewName.setText(name);
        textViewMail.setText(mail);
        textViewInfo.setText(info);



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
        void applyTexts(String surname, String lastname, String info);
    }

}
