package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.Adapters.UserDetailAdapter;
import com.example.myapplication.Models.Person;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserDetailActivity extends AppCompatActivity {

    Button signOutButton;
    TextView userNameTextView;
    String email;
    DatabaseReference reference;
    UserDetailAdapter userDetailAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);



        userDetailAdapter = new UserDetailAdapter();

        RecyclerView userRecycler = (RecyclerView) findViewById(R.id.userRecycler);
        ListView userListview = (ListView) findViewById(R.id.userListView);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            email = user.getEmail();
        }



        UserDetailAdapter adapter = new UserDetailAdapter();
        //manager ukladu
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        userRecycler.setLayoutManager(linearLayoutManager);
        userRecycler.setAdapter(adapter);



        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 4:
                        FirebaseAuth.getInstance().signOut();
                        startLoginActivity();
                }
            }
        };
        userListview.setOnItemClickListener(itemClickListener);
    }

    private void startLoginActivity(){
        Intent intent = new Intent(UserDetailActivity.this, LoginActivity.class);
        UserDetailActivity.this.startActivity(intent);
        UserDetailActivity.this.finish();
    }

}
