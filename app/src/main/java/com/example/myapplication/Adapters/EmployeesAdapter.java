package com.example.myapplication.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Employee;
import com.example.myapplication.R;

import java.util.ArrayList;

public class EmployeesAdapter extends RecyclerView.Adapter<EmployeesAdapter.ViewHolder> {

    String[] names;
    double[] x;
    double[] y;

    private Listener listener;

    public ArrayList<Employee> employeeArrayList = new ArrayList<>();

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onClick(int position);
    }

    @NonNull
    @Override
    public EmployeesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_workers_menu,parent,false);

        return new EmployeesAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeesAdapter.ViewHolder holder, int position) {
        CardView cardView = holder.cardView;

        Employee employees = employeeArrayList.get(position);

        TextView textViewSurname = (TextView) cardView.findViewById(R.id.textViewSurname);
        textViewSurname.setText(employees.getSurname());

        TextView textViewLastname = (TextView) cardView.findViewById(R.id.textViewLastname);
        textViewLastname.setText(employees.getLastname());

        Button numberButton = (Button) cardView.findViewById(R.id.ButtonName);
       // numberButton.setText(employees.getSurname().charAt(0) + employees.getLastname().charAt(0));
        //if(employees.getSurname() != null && employees.getLastname()!= null) {
            numberButton.setText(String.valueOf(employees.getSurname().charAt(0) + "" + employees.getLastname().charAt(0)));
        //}
        numberButton.setTextSize(20);
        //String initials = String.valueOf(surname.charAt(0)) + String.valueOf(lastname.charAt(0));
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
        return employeeArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(@NonNull CardView v) {
            super(v);
            cardView = v;
        }
    }
}
