package com.example.myapplication.Dialogs.Details;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.myapplication.R;

public class DialogDetailInfo extends AppCompatDialogFragment {


    private TextView textViewPlant;
    private TextView textViewChemicals;
    private TextView textViewDose;
    private TextView textViewDevelopmentPhase;
    private TextView textViewDate;
    private TextView textViewInfo;


    private String plant;
    private String chemicals;
    private String dose;
    private String developmentPhase;
    private String date;
    private String info;

    public DialogDetailInfo(String plant, String chemicals, String dose, String developmentPhase, String date, String info){
        this.plant = plant;
        this.chemicals = chemicals;
        this.dose = dose;
        this.developmentPhase = developmentPhase;
        this.date = date;
        this.info = info;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_detail_info, null);
        setRetainInstance(true);  //wazne

        builder.setView(view).setTitle("Informacje o wpisie").setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        textViewPlant = view.findViewById(R.id.plant);
        textViewChemicals = view.findViewById(R.id.chemicals);
        textViewDose = view.findViewById(R.id.dose);
        textViewDevelopmentPhase = view.findViewById(R.id.developmentPhase);
        textViewDate = view.findViewById(R.id.date);
        textViewInfo = view.findViewById(R.id.info);

        textViewPlant.setText("Roślina: " +plant);
        textViewChemicals.setText("Środek: " + chemicals);
        textViewDose.setText("Dawka: " + dose);
        textViewDevelopmentPhase.setText("Faza rozwojowa: " + developmentPhase);
        textViewDate.setText("Data: " + date);
        textViewInfo.setText("Informacje: " + info);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
    }

}
