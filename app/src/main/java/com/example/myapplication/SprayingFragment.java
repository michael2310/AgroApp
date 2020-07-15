package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Adapters.SprayingAdapter;

public class SprayingFragment extends Fragment {

    private EditText editTextDose;
    private EditText editTextArea;
    private Button buttonResult;
    private TextView textViewResult;

    public SprayingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_spraying, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextDose = (EditText) view.findViewById(R.id.editTextTextDose);
        editTextArea = (EditText) view.findViewById(R.id.editTextTextArea);
        buttonResult = (Button) view.findViewById(R.id.buttonResult);
        textViewResult = (TextView) view.findViewById(R.id.textViewResult);

        buttonResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String dose = editTextDose.getText().toString();
                String area = editTextArea.getText().toString();
                if(dose.equals("") && area.equals("") ){
                    Toast.makeText(getActivity(), "Wpisz dane", Toast.LENGTH_SHORT).show();
                }
                else{
                    double result = Double.parseDouble(area) * Double.parseDouble(dose);
                    textViewResult.setText("Wynik: " + String.valueOf(result));
                }
//                if(!editTextDose.getText().toString().equals("") && !editTextArea.getText().toString().equals("")){
//                    double dose = Double.parseDouble(editTextDose.getText().toString());
//                    double area = Double.parseDouble(editTextArea.getText().toString());
//                    double result = area * dose;
//                    textViewResult.setText(String.valueOf(result));
//                }
            }
        });

//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.sprayRecycler);
//        RecyclerView.LayoutManager sprayingLayoutManager = new LinearLayoutManager(getActivity());
//        SprayingAdapter sprayingAdapter = new SprayingAdapter();
//        recyclerView.setLayoutManager(sprayingLayoutManager);
//        recyclerView.setAdapter(sprayingAdapter);
//        ((LinearLayoutManager)recyclerView.getLayoutManager()).setStackFromEnd(true);
    }
}