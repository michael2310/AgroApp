package com.example.myapplication.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.myapplication.Models.Owner;
import com.example.myapplication.R;
import com.example.myapplication.db.AppRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailAdapter extends RecyclerView.Adapter<UserDetailAdapter.ViewHolder> {

    DatabaseReference reference;
    StorageReference storageReference;
    private Listener listener;
    //interfejs
    public interface Listener{
        void onClick(int position);
    }

    public void setListener(Listener listener){
        this.listener = listener;
    }

    public ArrayList<Owner> ownerArrayList = new ArrayList<>();


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
        CircleImageView circleImageView = (CircleImageView) cardView.findViewById(R.id.imageView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            try {
                reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                //           storageReference = FirebaseStorage.getInstance().getReference().child("Users_avatars"));
                storageReference = FirebaseStorage.getInstance().getReference().child("Users_avatars").child(user.getUid() + ".jpg");
            }catch (Exception e){

            }

         storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
             @Override
             public void onSuccess(Uri uri) {
                 if(uri != null) {
                     Glide.with(cardView.getContext()).load(uri.toString()).into(circleImageView);
                 }
             }
         });


            AppRepository.getInstance().getUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null) {
                        String name = dataSnapshot.child("name").getValue().toString();
                        String area = dataSnapshot.child("totalArea").getValue().toString();
                        String fields = dataSnapshot.child("totalFields").getValue().toString();
                      //  String uri = dataSnapshot.child("imageUrl").getValue().toString();

                        if(dataSnapshot.child("code").getValue() != null) {
                             String code = dataSnapshot.child("code").getValue().toString();
                             textViewCode.setText(code);
                        }

                        textViewUserName.setText(name);
                        textViewArea.setText(area + " ha");
                        textViewNumberOfFields.setText(fields);
                       // Glide.with(cardView.getContext()).load(storageReference).into(circleImageView);
                        //textViewCode.setText(code);
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
