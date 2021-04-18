package com.example.myapplication.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.R;
import com.example.myapplication.services.LocationService;
import com.example.myapplication.db.UsersRepository;
import com.example.myapplication.ui.loginAndRegister.LoginActivity;
import com.example.myapplication.pagers.MainPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSION_REQUEST_CODE = 698;
    private ViewPager2 viewPager;
    private boolean b = false;
    private boolean isClosed = true;
    private Switch sw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            isClosed = savedInstanceState.getBoolean("isClosed");
            b = savedInstanceState.getBoolean("switch");
        }
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainPagerAdapter(this));
        viewPager.setUserInputEnabled(false);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0, true);
                    return true;
                case R.id.navigation_menu:
                    viewPager.setCurrentItem(1, true);
                    return true;
            }
            return false;
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem itemSwitch = menu.findItem(R.id.action_location);
        itemSwitch.setActionView(R.layout.switch_menu);

        sw = (Switch) menu.findItem(R.id.action_location).getActionView().findViewById(R.id.location_switch);

        if(isClosed) {
            UsersRepository.getInstance().getCurrentUserSharing().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    b = (boolean) snapshot.getValue();
                    sw.setChecked(b);
                    isClosed = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            sw.setChecked(b);
        }

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    UsersRepository.getInstance().changeSharing(b);
                    Toast.makeText(getApplicationContext(), "Lokalizacja włączona", Toast.LENGTH_SHORT).show();
                    startLocationService();
                } else {
                    UsersRepository.getInstance().changeSharing(b);
                    Toast.makeText(getApplicationContext(), "Lokalizacja wyłączona", Toast.LENGTH_SHORT).show();
                    stopLocationService();
                }
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return false;
    }



    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            startLoginActivity();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
            b = sw.isChecked();
    }


    private void startLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(intent);
        MainActivity.this.finish();
    }





    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationService();
                } else {
                    ////
                    Toast.makeText(this, "Brak uprawnień", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void stopLocationService() {
        Intent intent = new Intent(this, LocationService.class);
        stopService(intent);
    }

    private void startLocationService() {
        if (ContextCompat.checkSelfPermission(this, LocationService.PERMISSION_STRING)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{LocationService.PERMISSION_STRING},
                    PERMISSION_REQUEST_CODE);
        } else {
            Intent intent = new Intent(this, LocationService.class);
            startService(intent);
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isClosed", isClosed);
        outState.putBoolean("switch", b);
    }
}
