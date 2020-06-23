package com.example.myapplication.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.LoginActivity;
import com.example.myapplication.Models.Person;
import com.example.myapplication.R;
import com.example.myapplication.UserDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserDetailAdapter extends RecyclerView.Adapter<UserDetailAdapter.ViewHolder> {

    DatabaseReference reference;
    private Listener listener;
    //interfejs
    public interface Listener{
        void onClick(int position);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public ArrayList<Person> personArrayList = new ArrayList<>();


    @NonNull
    @Override
    public UserDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_user_detail, parent, false);

        return new UserDetailAdapter.ViewHolder(cardView);


    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailAdapter.ViewHolder holder, int position) {


        CardView cardView = holder.cardView;
        TextView textViewUserName = (TextView) cardView.findViewById(R.id.nameText);
        TextView textViewCode = (TextView) cardView.findViewById(R.id.textViewCodeDisplay);
        TextView textViewArea = (TextView) cardView.findViewById(R.id.textViewAreaDisplay);
        TextView textViewNumberOfFields = (TextView) cardView.findViewById(R.id.textViewNumberOfFieldsDisplay);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getEmail().split("@")[0]);
            // to sa te metody na dole
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String code = dataSnapshot.child("code").getValue().toString();
                        textViewUserName.setText(name);
                        textViewCode.setText(code);
                       // textViewArea.setText();
                       // textViewNumberOfFields.setText();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
       // textView.setText(email);
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

}
