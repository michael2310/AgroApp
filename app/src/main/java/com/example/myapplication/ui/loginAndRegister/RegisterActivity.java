package com.example.myapplication.ui.loginAndRegister;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.models.Employee;
import com.example.myapplication.models.Owner;
import com.example.myapplication.R;
import com.example.myapplication.db.StorageRepository;
import com.example.myapplication.db.UsersRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity  {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private EditText editTextName;
    private EditText editTextlogin;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private Button buttonRegister;
    private Spinner spinnerAccountType;
    private CircleImageView circleImageView;
    private ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    private Uri resultUri;
    private boolean wrongCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();
        //storageReference = FirebaseStorage.getInstance().getReference().child("Users_avatars");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextlogin = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextRepeatPassword = (EditText) findViewById(R.id.editTextRepeatPassword);
        spinnerAccountType = (Spinner) findViewById(R.id.spinnerAccountType);
        circleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        progressDialog = new ProgressDialog(this);
        buttonRegister = (Button) findViewById(R.id.registerButton);

        //buttonRegister.setOnClickListener(this);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCrop();
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRegister();
            }
        });
    }

    private void userRegistered(String name, String email, String code, Uri imageUri){
        UsersRepository.getInstance().getCurrentUserRef().setValue(new Owner(
                name,
                email,
                firebaseAuth.getCurrentUser().getUid(),
                code,
                "owner")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: ");
                if(task.isSuccessful()) {
                    if(imageUri != null) {
                        //StorageReference sr = storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
                        StorageRepository.getInstance().getUserStorage().
                                putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                Log.d(TAG, "onComplete: ");
                                if (task.isSuccessful()) {
                                    String imagePath = task.getResult().getUploadSessionUri().toString();
                                    FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("imageUrl").setValue(imagePath).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG, "onComplete: ");
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(RegisterActivity.this, RegisterInfoOwnerActivity.class);
                                                intent.putExtra("name", name);
                                                intent.putExtra("code", code);
                                                intent.putExtra("imageUri", imageUri);
                                                intent.setData(imageUri);
                                                startActivity(intent);
                                                progressDialog.dismiss();
                                                RegisterActivity.this.finish();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }else {
                        Intent intent = new Intent(RegisterActivity.this, RegisterInfoOwnerActivity.class);
                        intent.putExtra("name", name);
                        intent.putExtra("code", code);
                        startActivity(intent);
                        progressDialog.dismiss();
                        RegisterActivity.this.finish();
                    }
                }
            }
        });
    }

    private void workerRegistered(String name, String email, Uri imageUri){
        String emailName = email.split("@")[0];
        String farmCode = "0";
        UsersRepository.getInstance().getCurrentUserRef().setValue(new Employee(name, email, firebaseAuth.getCurrentUser().getUid(), "employee")).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "onComplete: ");
                if(task.isSuccessful()) {
                    if(imageUri != null) {
                        //StorageReference sr = storageReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid() + ".jpg");
                        StorageRepository.getInstance().getUserStorage().
                                putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                Log.d(TAG, "onComplete: ");
                                if (task.isSuccessful()) {
                                    String imagePath = task.getResult().getUploadSessionUri().toString();
                                    FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.getCurrentUser().getUid()).child("imageUrl").setValue(imagePath).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.d(TAG, "onComplete: ");
                                            if (task.isSuccessful()) {
                                                Intent intent = new Intent(RegisterActivity.this, RegisterInfoEmployeeActivity.class);
                                                intent.putExtra("name", name);
                                                intent.putExtra("imageUri", imageUri);
                                                startActivity(intent);
                                                progressDialog.dismiss();
                                                RegisterActivity.this.finish();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }else {
                        Intent intent = new Intent(RegisterActivity.this, RegisterInfoEmployeeActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                        progressDialog.dismiss();
                        RegisterActivity.this.finish();
                    }
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String generateCode(){
        Random random = new Random();
        String code = String.valueOf(100000 + random.nextInt(900000));
        UsersRepository.getInstance().getUsersRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren() ){
                    String usersCode = data.child("code").getValue(String.class);
                    if(usersCode.equals(code)){
                        ///////////////////////////
                        wrongCode = true;
                        ///////////////////////////
                    }else {
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if(wrongCode){

        }
        return code;
    }



    public void onClickRegister(){
        progressDialog.setMessage("Tworzenie konta");
        progressDialog.show();
        String name = editTextName.getText().toString();
        String login = editTextlogin.getText().toString();
        String password = editTextPassword.getText().toString();
        String repeatPassword = editTextRepeatPassword.getText().toString();
        String accountType = spinnerAccountType.getSelectedItem().toString();
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
                            catch (FirebaseAuthWeakPasswordException weakPassword)
                            {
                                Log.d(TAG, "onComplete: weak_password");
                                progressDialog.dismiss();
                                // TODO: take your actions!
                            }
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
                                Toast.makeText(RegisterActivity.this, "Ten email już istnieje", Toast.LENGTH_SHORT).show();

                            }
                            catch (Exception e)
                            {
                                Log.d(TAG, "onComplete: " + e.getMessage());
                            }
                        } else {


                            if (accountType.equals("Właściciel")) {
                                String code = generateCode();
                                userRegistered(name, login, code, resultUri);
                            } else {
                                workerRegistered(name, login, resultUri);
                            }
                        }
                    }
                });
            }
        }
    }

        private void startCrop(){
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(RegisterActivity.this);
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                circleImageView.setImageURI(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}
