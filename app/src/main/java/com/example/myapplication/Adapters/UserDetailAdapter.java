package com.example.myapplication.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Models.Fields;
import com.example.myapplication.Models.Owner;
import com.example.myapplication.R;
import com.example.myapplication.db.StorageRepository;
import com.example.myapplication.db.UsersRepository;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailAdapter extends RecyclerView.Adapter<UserDetailAdapter.ViewHolder> {

    CardView cardView;
    TextView textViewUserName;
    TextView textViewCode;
    TextView textViewArea;
    TextView textViewNumberOfFields;
    CircleImageView circleImageView;
    Resources res;

    public Resources getRes() {
        return res;
    }

    private Listener listener;


    public interface Listener{
        void onClick(int position);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public ArrayList<Owner> ownerArrayList = new ArrayList<>();
    public Owner owner1;


    @NonNull
    @Override
    public UserDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_user_detail, parent, false);

        return new UserDetailAdapter.ViewHolder(cardView);


    }

    @Override
    public void onBindViewHolder(@NonNull UserDetailAdapter.ViewHolder holder, int position) {

         cardView = holder.cardView;
         textViewUserName = (TextView) cardView.findViewById(R.id.nameText);
         textViewCode = (TextView) cardView.findViewById(R.id.textViewCodeDisplay);
         textViewArea = (TextView) cardView.findViewById(R.id.textViewAreaDisplay);
         textViewNumberOfFields = (TextView) cardView.findViewById(R.id.textViewNumberOfFieldsDisplay);
         circleImageView = (CircleImageView) cardView.findViewById(R.id.imageView);


            StorageRepository.getInstance().getAvatar(circleImageView);

          //  UsersRepository.getInstance().getCurrentUserRef().addChildEventListener(this);

            UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {

                        if(dataSnapshot.child("name").getValue() != null) {
                            String name = dataSnapshot.child("name").getValue().toString();
                            textViewUserName.setText(name);
                        }

                        if(dataSnapshot.child("totalArea").getValue() != null) {
                            String area = dataSnapshot.child("totalArea").getValue().toString();
                            textViewArea.setText(cardView.getContext().getResources().getString(R.string.userDetailHa, area));
                        }

                        if(dataSnapshot.child("totalFields").getValue() != null) {
                            String fields = dataSnapshot.child("totalFields").getValue().toString();
                            textViewNumberOfFields.setText(fields);
                        }

                        if(dataSnapshot.child("code").getValue() != null) {
                             String code = dataSnapshot.child("code").getValue().toString();
                             textViewCode.setText(code);
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


    public static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }


}
