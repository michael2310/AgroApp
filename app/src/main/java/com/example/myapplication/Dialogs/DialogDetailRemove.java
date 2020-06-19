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
import androidx.fragment.app.DialogFragment;

import com.example.myapplication.Adapters.FieldDetailAdapter;
import com.example.myapplication.Models.FieldsDetail;
import com.example.myapplication.R;

public class DialogDetailRemove extends AppCompatDialogFragment {

    TextView textViewInfo;
    DialogDetailRemoveListener listener;
    FieldDetailAdapter fieldDetailAdapter;
    String id;
    int position;

    public DialogDetailRemove(int position){
        this.position = position;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_detail_remove, null);

        fieldDetailAdapter = new FieldDetailAdapter();

        builder.setView(view).setTitle("Informacje o wpisie").setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            listener.removeText();
            }
        });

        textViewInfo = view.findViewById(R.id.textViewInfo);
        textViewInfo.setText("Czy na pewno chcesz usunąć wpis?");
        return builder.create();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DialogDetailRemoveListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListner");
        }
    }


    public interface DialogDetailRemoveListener{
        void removeText();
    }
}
