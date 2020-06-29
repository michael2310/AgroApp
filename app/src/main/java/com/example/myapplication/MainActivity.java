package com.example.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;

import com.example.myapplication.Services.LocationService;
import com.example.myapplication.Services.UploadLocationService;
import com.example.myapplication.ui.LoginAndRegister.LoginActivity;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.menu.MenuFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity implements ServiceConnection   {

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new MenuFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction;
    Fragment active = fragment1;
    FirebaseUser user;
    DatabaseReference reference;
    DatabaseReference latitudeReference;
    DatabaseReference longitudeReference;

    private LocationService locationService;
    private boolean bound = false;
    private final int PERMISSION_REQUEST_CODE = 698;
    private final int NOTIFICATION_ID = 423;
    private static final int PERMISSIONS_REQUEST = 100;

    
    HomeFragment homeFragment;
    private boolean isChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);


        fragmentManager.beginTransaction().add(R.id.layout_main, fragment2, "2").setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right).hide(fragment2).commit();
        fragmentManager.beginTransaction().add(R.id.layout_main, fragment1, "1").setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.slide_out_right).commit();

        //homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.layout_main);

        user = FirebaseAuth.getInstance().getCurrentUser();


        if (active == fragment1) {
            displayDistance();
        }
    }


    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigation_home:
                    fragmentManager.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;
                case R.id.navigation_menu:
                    fragmentManager.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, LocationService.class);
                    bindService(intent, this, Context.BIND_AUTO_CREATE);
                } else {
                    ////
                }
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
       // if (FirebaseAuth.getInstance().getCurrentUser() == null){
        if (user == null){
        startLoginActivity();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return false;
    }

    private void startLoginActivity(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(intent);
        MainActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, LocationService.PERMISSION_STRING)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{LocationService.PERMISSION_STRING},
                    PERMISSION_REQUEST_CODE);
        } else {
            Intent intent = new Intent(this, LocationService.class);
            bindService(intent, this, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(bound){
            unbindService(this);
            bound = false;
        }
    }

    private void displayDistance() {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                double distance = 0.0;
                if (bound && locationService != null) {
                    Location location = locationService.getLastLocation();
                    if(location!= null) {
                        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.layout_main);
                        homeFragment.setLocation(location);
                        startUploadLocationService(location, user.getEmail().split("@")[0]);
                    }
                }
                handler.postDelayed(this, 5000);
            }
        });
    }

    private void startUploadLocationService(Location location, String userName) {
        Intent intent = new Intent(MainActivity.this, UploadLocationService.class);
        intent.putExtra("lat", location.getLatitude());
        intent.putExtra("lng", location.getLongitude());
        intent.putExtra("userName", userName);
        startService(intent);
    }

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        LocationService.OdometerBinder odometerBinder =
                (LocationService.OdometerBinder) iBinder;
        locationService = odometerBinder.getOdometer();
        bound = true;

    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        bound = false;
    }

}
