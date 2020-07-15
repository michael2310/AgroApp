package com.example.myapplication.db;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class StorageRepository {

    private final static String TAG = "FieldsDetailRepository";
    private static StorageRepository instance;
    private static StorageReference storageReference;
    private final FirebaseAuth auth;


    public static StorageRepository getInstance(){
        if (instance == null){
            instance = new StorageRepository();
        }
        return instance;
    }

    StorageRepository(){
        auth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("Users_avatars");
    }

    public StorageReference getUserStorage (){
        return storageReference.child(auth.getCurrentUser().getUid() + ".jpg");
    }

    public StorageReference getWorkerStorage(String id){
        return storageReference.child(id + ".jpg");
    }


    public void getAvatar(ImageView view){
        if(auth.getCurrentUser() != null){
            getUserStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if(uri != null) {
                        Glide.with(view.getContext()).load(uri).into(view);
                    }
                }
            });
        }
    }


    public void getWorkersAvatar(ImageView view, String id){
        if(id != null){
            getWorkerStorage(id).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if(uri != null) {
                        Glide.with(view.getContext()).load(uri).into(view);
                    }
                }
            });
        }
    }
}
