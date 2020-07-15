package com.example.myapplication.ui.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.example.myapplication.db.UsersRepository;
import com.example.myapplication.ui.Workers.EmployeeMainActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class CheckUserLoggedInActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_user_logged_in);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        if (UsersRepository.getInstance().isUserLogged()) {
            userLoggedIn();
        } else {
            Intent intent = new Intent(CheckUserLoggedInActivity.this, LoginActivity.class);
            startActivity(intent);
            CheckUserLoggedInActivity.this.finish();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void userLoggedIn() {
        UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("accountType").getValue().toString().equals("owner")) {
                        Intent intent = new Intent(CheckUserLoggedInActivity.this, MainActivity.class);
                        startActivity(intent);
                        //progressBar.
                        CheckUserLoggedInActivity.this.finish();
                        progressBar.setVisibility(View.INVISIBLE); // To hide the ProgressBar
                    } else {
                        Intent intent = new Intent(CheckUserLoggedInActivity.this, EmployeeMainActivity.class);
                        startActivity(intent);
                        //progressDialog.dismiss();
                        CheckUserLoggedInActivity.this.finish();
                        progressBar.setVisibility(View.INVISIBLE); // To hide the ProgressBar
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}