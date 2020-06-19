package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.Models.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity  {
    private static final String TAG = LoginActivity.class.getSimpleName();

    EditText editTextName;
    EditText editTextlogin;
    EditText editTextpassword;
    EditText editTextRepeatPassword;
    Button buttonRegister;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextlogin = (EditText) findViewById(R.id.editTextEmail);
        editTextpassword = (EditText) findViewById(R.id.editTextPassword);
        editTextRepeatPassword = (EditText) findViewById(R.id.editTextRepeatPassword);

        buttonRegister = (Button) findViewById(R.id.loginButton);
        //buttonRegister.setOnClickListener(this);
    }





    private void userRegistered(String name, String email){
        String emailName = email.split("@")[0];
        FirebaseDatabase.getInstance().getReference("users").child(emailName).setValue(new Person(name, email, firebaseAuth.getCurrentUser().getUid())).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: ");
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }

    });
    }

    public void onClickRegister(View view){

            String name = editTextName.getText().toString();
            String login = editTextlogin.getText().toString();
            String password = editTextpassword.getText().toString();
            String repeatPassword = editTextRepeatPassword.getText().toString();
            if (name.trim().length() > 0 && login.trim().length() > 0 && password.trim().length() > 0 && repeatPassword.trim().length() > 0) { // sprawdzenie czy pola login i password nie są puste
                if (password.equals(repeatPassword)) {
                    // jeszcze sprawdzic czy nie ma takiego uzytkownika w bazie
                    firebaseAuth.createUserWithEmailAndPassword(login, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "onComplete: ");
                            userRegistered(name, login);
                        }
                    });
                }
            }
        }
}
