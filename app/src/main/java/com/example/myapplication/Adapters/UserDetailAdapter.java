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

import java.util.ArrayList;

public class UserDetailAdapter extends RecyclerView.Adapter<UserDetailAdapter.ViewHolder> {

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
        String email = null;
        Button signOutButton;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            //email = user.getEmail();
            email = user.getEmail();
        }
        CardView cardView = holder.cardView;
        TextView textView = (TextView) cardView.findViewById(R.id.nameText);
        textView.setText(email);
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
