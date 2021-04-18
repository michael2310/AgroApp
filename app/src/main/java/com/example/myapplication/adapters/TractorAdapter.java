package com.example.myapplication.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.models.Machine;
import com.example.myapplication.R;

import java.util.ArrayList;

public class TractorAdapter extends  RecyclerView.Adapter<TractorAdapter.ViewHolder> {


    public ArrayList<Machine> machineArrayList = new ArrayList<>();

    private MachinesAdapter.Listener listener;

    public interface Listener{
        void onClick(int position);
    }

    public void setListener(MachinesAdapter.Listener listener){
        this.listener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_machines,parent,false);

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Machine machine = machineArrayList.get(position);

        CardView cardView = holder.cardView;
        TextView textViewBrand = (TextView) cardView.findViewById(R.id.textViewBrand);
        TextView textViewModel = (TextView) cardView.findViewById(R.id.textViewModel);
        textViewBrand.setText(machine.getBrand());
        textViewModel.setText(machine.getModel());

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return machineArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(@NonNull CardView v) {
            super(v);
            cardView = v;
        }
    }
}