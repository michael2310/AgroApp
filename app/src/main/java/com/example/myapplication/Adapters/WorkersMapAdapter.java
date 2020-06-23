package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Employee;
import com.example.myapplication.R;

import java.util.ArrayList;

public class WorkersMapAdapter extends RecyclerView.Adapter<WorkersMapAdapter.ViewHolder> {
    String[] names;
    double[] x;
    double[] y;
    String[] id;

    private Listener listener;

    public ArrayList<Employee> employeeArrayListMap = new ArrayList<>();

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onClick(int position);
    }

    @NonNull
    @Override
    public WorkersMapAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_employees,parent,false);

        return new WorkersMapAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkersMapAdapter.ViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        Employee employees = employeeArrayListMap.get(position);

        TextView textView = (TextView) cardView.findViewById(R.id.info_text);
        textView.setText(employees.getSurname());

        //listener przy tworzeniu viewHoldera
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return employeeArrayListMap.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(@NonNull CardView v) {
            super(v);
            cardView = v;
        }
    }
}
