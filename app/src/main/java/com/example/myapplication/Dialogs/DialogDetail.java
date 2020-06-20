package com.example.myapplication.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myapplication.R;

public class DialogDetail extends AppCompatDialogFragment {

    public DialogDetail(String plant, String chemicals, String info){
        this.plant = plant;
        this.chemicals = chemicals;
        this.info = info;
    }

    TextView textViewPlant;
    TextView textViewChemia;
    TextView textViewDate;

    String plant;
    String cultivationType;
    String sowingType;
    String chemicals;
    String info;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_detail, null);

        builder.setView(view).setTitle("Informacje o wpisie").setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        textViewPlant = view.findViewById(R.id.plant);
        textViewChemia = view.findViewById(R.id.chemia);
        textViewDate = view.findViewById(R.id.info);

        textViewPlant.setText("Roślina: " +plant);
        textViewChemia.setText("Środek: " + chemicals);
        textViewDate.setText("Informacje: " + info);



        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


}
