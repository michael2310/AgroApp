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

public class DialogCultivation extends DialogFragment {

    private EditText editTextPlant;
    private EditText editTextCultivationType;
    private EditText editTextSowingType;
    private EditText editTextDate;
    private EditText editTextInfo;

    private DialogListener listener;

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
                String date = editTextDate.getText().toString();
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
}
