package com.example.myapplication.ui.LoginAndRegister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.db.UsersRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterInfoOwnerActivity extends AppCompatActivity {

    String name;
    String code;
    Uri imageUri;
    TextView textViewName;
    TextView textViewCode;
    CircleImageView circleImageView;
    Button buttonOk;
    Button buttonSend;
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

        user = FirebaseAuth.getInstance().getCurrentUser();

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String name = dataSnapshot.child("name").getValue().toString();
                                String code = dataSnapshot.child("code").getValue().toString();
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_TEXT, "Użytkownik " + name + " prosi Cię o dołączenie do jego gospodarstwa. Wpisz w aplikacji AgroApp następujący kod: " + code);
                                startActivity(intent);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
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
}