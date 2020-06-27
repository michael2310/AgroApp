package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();


    EditText editTextlogin;
    EditText editTextpassword;
    Button buttonLogin;
    Button buttonRegister;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextlogin = (EditText) findViewById(R.id.editTextLogin);
        editTextpassword = (EditText) findViewById(R.id.editTextPassword);

        buttonLogin = (Button) findViewById(R.id.loginButton);
        buttonRegister = (Button) findViewById(R.id.registerButton);

        progressDialog = new ProgressDialog(this);


    }




    private void userLoggedIn(String email) {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        progressDialog.dismiss();
        LoginActivity.this.finish();
    }

    private void userLoggedIn2(DatabaseReference reference) {

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if (user != null) {
//            reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getEmail().split("@")[0]);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot != null) {
                        if(dataSnapshot.child("accountType").getValue().toString().equals("owner")){
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            LoginActivity.this.finish();
                        }else {
                            Intent intent = new Intent(LoginActivity.this, EmployeeMainActivity.class);
                            startActivity(intent);
                            progressDialog.dismiss();
                            LoginActivity.this.finish();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    public void onClickRegister(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(intent);
    }

    public void onClickLogin(View view) {
        progressDialog.setMessage("Logowanie...");
        progressDialog.show();
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
                        FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
                        // if (FirebaseAuth.getInstance().getCurrentUser() == null){
                        if (user1 != null){
                            progressDialog.setMessage("Logowanie...");
                            progressDialog.show();
                            reference1 = FirebaseDatabase.getInstance().getReference().child("users").child(user1.getEmail().split("@")[0]);
                            userLoggedIn2(reference1);
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onComplete fail: ");
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // if (FirebaseAuth.getInstance().getCurrentUser() == null){
        if (user != null){
            progressDialog.setMessage("Logowanie...");
            progressDialog.show();
            reference = FirebaseDatabase.getInstance().getReference().child("users").child(user.getEmail().split("@")[0]);
            userLoggedIn2(reference);
        }
    }
}
