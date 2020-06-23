package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.Models.Employee;
import com.example.myapplication.Models.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.FirebaseDatabase;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class RegisterActivity extends AppCompatActivity  {
    private static final String TAG = LoginActivity.class.getSimpleName();

    EditText editTextName;
    EditText editTextlogin;
    EditText editTextpassword;
    EditText editTextRepeatPassword;
    Button buttonRegister;
    Spinner spinnerAccountType;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;


    Uri resultUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextlogin = (EditText) findViewById(R.id.editTextEmail);
        editTextpassword = (EditText) findViewById(R.id.editTextPassword);
        editTextRepeatPassword = (EditText) findViewById(R.id.editTextRepeatPassword);
        spinnerAccountType = (Spinner) findViewById(R.id.spinnerAccountType);
        progressDialog = new ProgressDialog(this);

        buttonRegister = (Button) findViewById(R.id.loginButton);
        //buttonRegister.setOnClickListener(this);
    }





    private void userRegistered(String name, String email, String code){
        String emailName = email.split("@")[0];
        FirebaseDatabase.getInstance().getReference("users").child(emailName).setValue(new Person(name, email, firebaseAuth.getCurrentUser().getUid(), code)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: ");
                Intent intent = new Intent(RegisterActivity.this, RegisterInfoActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("code", code);
                startActivity(intent);
                progressDialog.dismiss();
                RegisterActivity.this.finish();;
            }

    });
    }

    private void workerRegistered(String name, String email){
        String emailName = email.split("@")[0];
        String farmCode = "0";
        FirebaseDatabase.getInstance().getReference("workers").child(emailName).setValue(new Employee(name, email, firebaseAuth.getCurrentUser().getUid(), farmCode)).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: ");
                Intent intent = new Intent(RegisterActivity.this, RegisterInfoActivity.class);
                intent.putExtra("name", name);
                startActivity(intent);
                progressDialog.dismiss();
                RegisterActivity.this.finish();;
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private String codeGenerator(){
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault());
        Random random = new Random();
        return String.valueOf(100000 + random.nextInt(900000));
    }

    public void onClickRegister(View view){
            progressDialog.setMessage("Tworzenie konta");
            progressDialog.show();
            String name = editTextName.getText().toString();
            String login = editTextlogin.getText().toString();
            String password = editTextpassword.getText().toString();
            String repeatPassword = editTextRepeatPassword.getText().toString();
            String text = spinnerAccountType.getSelectedItem().toString();

        FirebaseAuthUserCollisionException userCollisionException;

            if (name.trim().length() > 0 && login.trim().length() > 0 && password.trim().length() > 0 && repeatPassword.trim().length() > 0) { // sprawdzenie czy pola login i password nie są puste
                if (password.equals(repeatPassword)) {
                    // jeszcze sprawdzic czy nie ma takiego uzytkownika w bazie
                    firebaseAuth.createUserWithEmailAndPassword(login, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "onComplete: ");
                                if (!task.isSuccessful())
                                {
                                    try
                                    {
                                        throw task.getException();
                                    }
                                    // if user enters wrong email.
                                    catch (FirebaseAuthWeakPasswordException weakPassword)
                                    {
                                        Log.d(TAG, "onComplete: weak_password");
                                        progressDialog.dismiss();
                                        // TODO: take your actions!
                                    }
                                    // if user enters wrong password.
                                    catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                    {
                                        Log.d(TAG, "onComplete: malformed_email");
                                        progressDialog.dismiss();
                                        // TODO: Take your action
                                    }
                                    catch (FirebaseAuthUserCollisionException existEmail)
                                    {
                                        Log.d(TAG, "onComplete: exist_email");

                                        progressDialog.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Ten email już jest", Toast.LENGTH_SHORT).show();

                                    }
                                    catch (Exception e)
                                    {
                                        Log.d(TAG, "onComplete: " + e.getMessage());
                                    }
                                } else {


                                    if (text.equals("Właściciel")) {
                                        String code = codeGenerator();
                                        userRegistered(name, login, code);
                                    } else {
                                        workerRegistered(name, login);
                                    }
                                }

                        }
                    });
                }
            }
        }

        public void imageSelect(View view){
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && requestCode == RESULT_OK && data == null){

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                resultUri = result.getUri();
            } else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
            }
        }
    }
}
