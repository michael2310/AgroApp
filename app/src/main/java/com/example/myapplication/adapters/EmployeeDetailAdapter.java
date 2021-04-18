package com.example.myapplication.adapters;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.models.Owner;
import com.example.myapplication.R;
import com.example.myapplication.db.StorageRepository;
import com.example.myapplication.db.UsersRepository;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeDetailAdapter extends RecyclerView.Adapter<EmployeeDetailAdapter.ViewHolder> {

    private Listener listener;
    Resources res = Resources.getSystem();

    public interface Listener{
        void onClick(int position);
    }

    public ArrayList<Owner> ownerArrayList = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_employee_detail, parent, false);

        return new EmployeeDetailAdapter.ViewHolder(cardView);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CardView cardView = holder.cardView;

        TextView textViewUserName = (TextView) cardView.findViewById(R.id.nameText);
        TextView textViewFarmName = (TextView) cardView.findViewById(R.id.textViewFarmName);

        CircleImageView circleImageView = (CircleImageView) cardView.findViewById(R.id.imageView);

        Button button = (Button) cardView.findViewById(R.id.ButtonAddCode);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    listener.onClick(position);
                }
            }
        });
            StorageRepository.getInstance().getAvatar(circleImageView);

            UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {

                        String name = dataSnapshot.child("name").getValue().toString();
                        textViewUserName.setText(name);

                        if(dataSnapshot.child("farmName").getValue() != null) {
                            String code = dataSnapshot.child("farmName").getValue().toString();
                            textViewFarmName.setText(code);
                        } else {
                            textViewFarmName.setText(cardView.getContext().getResources().getString(R.string.employeeDetailEmpty));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    @Override
    public int getItemCount() {
        return 1;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(@NonNull CardView v) {
            super(v);
            cardView = v;


        }
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }
}