package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class SprayingAdapter extends RecyclerView.Adapter<SprayingAdapter.ViewHolder> {

    private Listener listener;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_spraying, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CardView cardView = holder.cardView;
        EditText editTextDose = (EditText) cardView.findViewById(R.id.editTextTextDose);
        EditText editTextArea = (EditText) cardView.findViewById(R.id.editTextTextArea);
        Button button = (Button) cardView.findViewById(R.id.buttonResult);
        TextView textViewResult = (TextView) cardView.findViewById(R.id.textViewResult);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!editTextDose.getText().toString().equals("") && !editTextArea.getText().toString().equals("")) {
                    double dose = Double.parseDouble(editTextDose.getText().toString());
                    double area = Double.parseDouble(editTextArea.getText().toString());
                    double result = area * dose;
                    textViewResult.setText(String.valueOf(result));
                }else {
                    Toast.makeText(view.getContext(), "Wpisz brakujące dane", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public ViewHolder(@NonNull CardView v) {
            super(v);
            cardView = v;
        }
    }

    public interface Listener {
        void applyTexts(double dose, double area);
    }
}
