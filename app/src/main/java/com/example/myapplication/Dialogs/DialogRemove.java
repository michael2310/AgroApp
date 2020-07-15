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

import com.example.myapplication.Adapters.FieldDetailAdapterCultivation;
import com.example.myapplication.R;

public class DialogRemove extends AppCompatDialogFragment {

    private TextView textViewInfo;
    private DialogRemove.DialogRemoveListener listener;
    private String id;

    public DialogRemove(String id){
        this.id = id;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_detail_remove, null);

        builder.setView(view).setTitle("Informacje o wpisie").setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    listener.removeText(id);
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
            listener = (DialogRemove.DialogRemoveListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListner");
        }
    }

    public interface DialogRemoveListener{
        void removeText(String id);
    }

}