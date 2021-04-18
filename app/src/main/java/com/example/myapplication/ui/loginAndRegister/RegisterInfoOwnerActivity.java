package com.example.myapplication.ui.loginAndRegister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterInfoOwnerActivity extends AppCompatActivity {

    private String name;
    private String code;
    private Uri imageUri;
    private TextView textViewName;
    private TextView textViewCode;
    private CircleImageView circleImageView;
    private Button buttonOk;
    private Button buttonSend;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info_owner);

        textViewName = (TextView) findViewById(R.id.textViewName);
        textViewCode = (TextView) findViewById(R.id.textViewFarm);
        buttonOk = (Button) findViewById(R.id.okButton);
        buttonSend = (Button) findViewById(R.id.sendButton);
        circleImageView = (CircleImageView) findViewById(R.id.circleImageViewOwnerRegister);

        Intent intent = getIntent();

        if(intent != null){
            name = intent.getStringExtra("name");
            code = intent.getStringExtra("code");
            imageUri = intent.getData();

            textViewName.setText("Użytkownik: " + name);
            textViewCode.setText(code);
            circleImageView.setImageURI(imageUri);
        }


        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMessage();
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterInfoOwnerActivity.this, LoginActivity.class);
                startActivity(intent);
                RegisterInfoOwnerActivity.this.finish();
            }
        });
    }

    private void createMessage(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Użytkownik "
                + name
                + " prosi Cię o dołączenie do jego gospodarstwa. Wpisz w aplikacji AgroApp następujący kod: "
                + code);
        startActivity(intent);

    }
}