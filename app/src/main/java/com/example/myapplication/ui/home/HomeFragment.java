package com.example.myapplication.ui.home;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.esri.arcgisruntime.mapping.view.MapView;
import com.example.myapplication.Adapters.WorkersMapAdapter;
import com.example.myapplication.Models.Employee;
import com.example.myapplication.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.Locale;

public class HomeFragment extends Fragment implements OnMapReadyCallback, ChildEventListener {

    private final String TAG = HomeFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;
    WorkersMapAdapter workersMapAdapter;
    SupportMapFragment mapFragment;
    Location location;

    private boolean bound = false;
    private FusedLocationProviderClient client;
    private RecyclerView menuRecycler;
    FirebaseUser user;


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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LatLng startLatLng = null;
        startLatLng = new LatLng(0,0);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(startLatLng);
        markerOptions.visible(false);
        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
               // Marker marker = googleMap.addMarker(markerOptions);
                final Handler handler = new Handler();
                handler.post(new Runnable() {
                    Marker marker = googleMap.addMarker(markerOptions);
                    @Override
                    public void run() {
                        if(location != null) {
                            marker.remove();
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.visible(true);
                            //marker.setPosition(latLng);
                            marker = googleMap.addMarker(markerOptions);
                            //googleMap.addMarker(markerOptions);
                        }
                        handler.postDelayed(this, 5000);
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
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }


    public void trackEmployees() {
        for (int i = 0; i < workersMapAdapter.employeeArrayListMap.size(); i++) {
            Employee employees = workersMapAdapter.employeeArrayListMap.get(i);
            LatLng latLng = new LatLng(employees.getLatitude(), employees.getLongitude());
          //  Marker mark = map.addMarker(new MarkerOptions().position(latLng).title(employees.getName()));
          //  mark.setTag(employees.getName());
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
        //location.setLatitude((Double) dataSnapshot.child("lat").getValue());
        //location.setLongitude((Double) dataSnapshot.child("lgt").getValue());
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


    public void setLocation(Location location){
        this.location = location;
    }

}
