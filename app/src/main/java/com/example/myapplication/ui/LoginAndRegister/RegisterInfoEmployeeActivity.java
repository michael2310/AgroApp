package com.example.myapplication.ui.LoginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterInfoEmployeeActivity extends AppCompatActivity {

    Button okButton;
    String name;
    Uri imageUri;
    TextView textViewName;
    CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info_employee);

        textViewName = (TextView) findViewById(R.id.textViewName);
        circleImageView = (CircleImageView) findViewById(R.id.circleImageViewEmployeeRegister);
        okButton = (Button) findViewById(R.id.okButton);

        Intent intent = getIntent();

        if(intent != null){
            name = intent.getStringExtra("name");
            imageUri = intent.getData();

            textViewName.setText("Użytkownik: " + name);
            circleImageView.setImageURI(imageUri);
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterInfoEmployeeActivity.this, LoginActivity.class);
                startActivity(intent);
                RegisterInfoEmployeeActivity.this.finish();
            }
        });
    }
}