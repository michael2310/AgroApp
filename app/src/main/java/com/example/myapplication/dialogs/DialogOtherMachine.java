package com.example.myapplication.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

public class DialogOtherMachine extends DialogFragment {
    private EditText editTextBrand;
    private EditText editTextModel;

    private DialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_machine, null);

        builder.setView(view).setTitle("Dodaj maszynę").setNegativeButton("Anuluj", (dialog, which) -> {

        }).setPositiveButton("Ok", (dialog, which) -> {
            String brand = editTextBrand.getText().toString();
            String model = editTextModel.getText().toString();
            String category = "Others";
            listener.applyTextsOthers(brand, model, category);
        });

        editTextBrand = view.findViewById(R.id.editTextBrand);
        editTextModel = view.findViewById(R.id.editTextModel);


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

    public interface DialogListener {
        void applyTextsOthers(String brand, String model, String category);
    }
}