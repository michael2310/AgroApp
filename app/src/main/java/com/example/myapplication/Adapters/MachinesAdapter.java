package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

public class MachinesAdapter extends RecyclerView.Adapter<MachinesAdapter.ViewHolder> {

    String [] names;
    int[] resourceId;

    public MachinesAdapter(String[] names){
        this.names = names;
    }

    public MachinesAdapter(String[] names, int[] resourceId){
        this.names = names;
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public MachinesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_machines,parent,false);

        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull MachinesAdapter.ViewHolder holder, int position) {

        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.info_text);
        textView.setText(names[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(@NonNull CardView v) {
            super(v);
            cardView = v;
        }
    }
}
