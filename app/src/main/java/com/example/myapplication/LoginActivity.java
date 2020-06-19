package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();


    EditText editTextlogin;
    EditText editTextpassword;
    Button buttonLogin;
    Button buttonRegister;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextlogin = (EditText) findViewById(R.id.editTextLogin);
        editTextpassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (Button) findViewById(R.id.loginButton);
        buttonRegister = (Button) findViewById(R.id.registerButton);

    }




    private void userLoggedIn(String email) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(intent);
    }

    public void onClickLogin(View view) {
        String login = editTextlogin.getText().toString();
        String password = editTextpassword.getText().toString();
        firebaseAuth = FirebaseAuth.getInstance();
        if (login.trim().length() > 0 && password.trim().length() > 0 ) { // sprawdzenie czy pola login i password nie są puste
            firebaseAuth.signInWithEmailAndPassword(login, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "onComplete success: ");
                        Toast.makeText(LoginActivity.this, "Zalogowano pomyślnie", Toast.LENGTH_SHORT).show();
                        userLoggedIn(login);

                    } else {
                        Toast.makeText(LoginActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onComplete fail: ");
                    }
                }
            });
        }
    }
}
