package com.example.myapplication.dialogs.Details;

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

import com.example.myapplication.adapters.FieldDetailAdapterCultivation;
import com.example.myapplication.R;

public class DialogDetailRemove extends AppCompatDialogFragment {

    private TextView textViewInfo;
    private DialogDetailRemoveListener detailListener;;
    private FieldDetailAdapterCultivation fieldDetailAdapter;
    private String id;
    private String fieldId;

    public DialogDetailRemove(String fieldId, String id){
        this.fieldId = fieldId;
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
                    detailListener.removeText(fieldId, id);
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
            detailListener = (DialogDetailRemoveListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListner");
        }
    }

    public interface DialogDetailRemoveListener{
        void removeText(String fieldId, String id);
    }


}
