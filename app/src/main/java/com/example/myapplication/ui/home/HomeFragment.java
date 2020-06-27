package com.example.myapplication.ui.home;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.example.myapplication.Adapters.WorkersMapAdapter;
import com.example.myapplication.MainActivity;
import com.example.myapplication.Models.Employee;
import com.example.myapplication.R;
import com.example.myapplication.Services.LocationService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements OnMapReadyCallback, ChildEventListener {

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    private static final int PERMISSIONS_FINE_LOACTION = 99;
    private final String TAG = HomeFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;
    //private MapView mv;
    GoogleMap map;
    GoogleApiClient apiClient;
    MapView mMapView;
    WorkersMapAdapter workersMapAdapter;
    DatabaseReference reference;
    String email;
    String id;
    SupportMapFragment mapFragment;

    Location currentLocation;
    // Google Api for Location services
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationCallback locationCallback;
    LatLng latLngStart;

    private LocationService locationService;
    private boolean bound = false;

    private FusedLocationProviderClient client;
    //Location request
    LocationRequest locationRequest;

    private RecyclerView menuRecycler;

    FirebaseUser user;

    public static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

//    private ServiceConnection serviceConnection = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            LocationService.LocationServiceBinder locationServiceBinder = (LocationService.LocationServiceBinder) iBinder;
//            locationService = locationServiceBinder.getLocationService();
//            if(locationService != null){
//                bound = true;
//            }
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName componentName) {
//            // kod wykonywany kiedy usługa jest odłączana
//            bound = false;
//        }
//    };


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // final TextView textView = root.findViewById(R.id.text_dashboard);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //        textView.setText(s);
            }
        });
        //    getCurrentLocation();
           return root;
    }

    Marker marker;
    LatLng start = new LatLng(17.54, 51.33);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            reference = FirebaseDatabase.getInstance().getReference("users").child(user.getEmail().split("@")[0]);
            // to sa te metody na dole
            //reference.addChildEventListener(this);
            id = user.getUid();
        }

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(start);
                marker = googleMap.addMarker(markerOptions);
                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getCurrentLocation();
                        handler.postDelayed(this, 1000);
                    }
                });
            }
        });





        menuRecycler = view.findViewById(R.id.employee_recycler);
        workersMapAdapter = new WorkersMapAdapter();
        //manager ukladu
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        menuRecycler.setLayoutManager(layoutManager);
        menuRecycler.setAdapter(workersMapAdapter);

//        employeesAdapter.setListener (position -> {
//            map.addMarker(new MarkerOptions().position(latLngs[position]));
        workersMapAdapter.setListener(new WorkersMapAdapter.Listener() {
            @Override
            public void onClick(int position) {
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
//        Intent intent = new Intent(getActivity(), LocationService.class);
//        requireActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        //reference.addChildEventListener(this);
    }


    @Override
    public void onPause() {
        // if (mv != null) {
        //    mv.pause();
        // }
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
//        if (bound) {
//            requireActivity().unbindService(serviceConnection);
//            bound = false;
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // requestLocation();
//        if (mv != null) {
//            mv.resume();
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            Toast.makeText(getContext(), "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(getContext()).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(getContext()).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    mapFragment.getMapAsync(new OnMapReadyCallback() {
                        Marker m;
                        @Override
                        public void onMapReady(GoogleMap googleMap) {
                                marker.remove();
                            LatLng latLng = new LatLng(latitude, longitude);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title("tu jestem");
                            marker = googleMap.addMarker(markerOptions);
                            FirebaseDatabase.getInstance().getReference("users").child(user.getEmail().split("@")[0].toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        String id = FirebaseDatabase.getInstance().getReference().push().getKey();
                                        // FirebaseDatabase.getInstance().getReference("fields").child(userName).push().setValue(new Fields(userName, name, number, area));
                                        FirebaseDatabase.getInstance().getReference("users").child(user.getEmail().split("@")[0].toString()).child("latitude").setValue(latLng.latitude);
                                        FirebaseDatabase.getInstance().getReference("users").child(user.getEmail().split("@")[0].toString()).child("longitude").setValue(latLng.longitude);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    });

                }
            }
        }, Looper.getMainLooper());
    }


    @Override
    public void onDestroy() {
//        if (mv != null) {
//            mv.dispose();
//        }
        super.onDestroy();
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
      //  map = googleMap;
    }

    public void trackEmployees() {
        for (int i = 0; i < workersMapAdapter.employeeArrayListMap.size(); i++) {
            Employee employees = workersMapAdapter.employeeArrayListMap.get(i);
            LatLng latLng = new LatLng(employees.getLatitude(), employees.getLongitude());
            Marker mark = map.addMarker(new MarkerOptions().position(latLng).title(employees.getName()));
            mark.setTag(employees.getName());
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildAdded: ");
        if (workersMapAdapter != null) {
            Employee employee = dataSnapshot.getValue(Employee.class);
            workersMapAdapter.employeeArrayListMap.add(employee);
            workersMapAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }


    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

}
