package com.example.myapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.models.Employee;
import com.example.myapplication.R;
import com.example.myapplication.db.StorageRepository;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    ArrayList<Employee> employeeList;
    Context context;

    private EmployeeAdapter.Listener listener;

    public void setListener(EmployeeAdapter.Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        void onClick(int position);
    }

    public EmployeeAdapter(ArrayList<Employee> employeeList, Context context){
        this.employeeList = employeeList;
        this.context = context;
    }

    @NonNull
    @Override
    public EmployeeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_workers_menu,parent,false);
        return new EmployeeAdapter.ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.ViewHolder holder, int position) {

        CardView cardView = holder.cardView;

        Employee employee = employeeList.get(position);

        TextView textViewSurname = (TextView) cardView.findViewById(R.id.textViewSurname);
        textViewSurname.setText(employee.getName());

        TextView textViewLastname = (TextView) cardView.findViewById(R.id.textViewLastname);
        textViewLastname.setText(employee.getEmail());


        CircleImageView circleImageView = (CircleImageView) cardView.findViewById(R.id.circleImageViewWorkers);
        StorageRepository.getInstance().getWorkersAvatar(circleImageView, employee.getId());

        ImageView imageView = (ImageView) cardView.findViewById(R.id.activeDot);
        if(employee.isActive()){
            imageView.setBackgroundResource(R.drawable.ic_online);
        }else {
            imageView.setBackgroundResource(R.drawable.ic_offline);
        }

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
        return employeeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

private CardView cardView;

        public ViewHolder(@NonNull CardView v) {
            super(v);
            cardView = v;
        }
    }
}

