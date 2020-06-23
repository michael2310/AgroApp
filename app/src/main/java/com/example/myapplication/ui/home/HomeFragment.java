package com.example.myapplication.ui.home;

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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
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

public class HomeFragment extends Fragment implements OnMapReadyCallback, ChildEventListener {

    private final String TAG = HomeFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;
    //private MapView mv;
    GoogleMap map;
    MapView mMapView;
    WorkersMapAdapter workersMapAdapter;
    DatabaseReference reference;
    String email;
    String id;

    private RecyclerView menuRecycler;



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

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            reference = FirebaseDatabase.getInstance().getReference("workers").child(user.getEmail().split("@")[0]);
            // to sa te metody na dole
            //reference.addChildEventListener(this);
            id = user.getUid();
        }



        return root;

        //mv = (MapView) root.findViewById(R.id.map);
        //mv = (MapView) menuRecycler.findViewById(R.id.map);

        //setupMap();

        //return menuRecycler;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        menuRecycler = (RecyclerView) view.findViewById(R.id.employee_recycler) ;



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
//                Employee employees = workersMapAdapter.employeeArrayListMap.get(position);
//                LatLng latLng = new LatLng(employees.getLatitude(), employees.getLongitude());
//                Marker mark = map.addMarker(new MarkerOptions().position(latLng).title(employees.getSurname()));
//                mark.setTag(employees.getSurname());
                //trackEmployees();
            }
        });

//            //ArcGISMap map = new ArcGISMap(Basemap.Type.TOPOGRAPHIC_VECTOR, x[position], y[position], 10);
//            //mv.setMap(map);
//        });

        //trackEmployees();

    }

    @Override
    public void onStart() {
        super.onStart();
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
    public void onResume() {
        super.onResume();
//        if (mv != null) {
//            mv.resume();
//        }
    }

    @Override
    public void onDestroy() {
//        if (mv != null) {
//            mv.dispose();
//        }
        super.onDestroy();
    }

//    private void setupMap() {
//        if (mv != null) {
//            Basemap.Type basemapType = Basemap.Type.TOPOGRAPHIC_VECTOR;
//            double latitude = 51.1698;
//            double longitude = 17.5070;
//            int levelOfDetail = 10;
//            ArcGISMap map = new ArcGISMap(basemapType, latitude, longitude, levelOfDetail);
//            mv.setMap(map);
//        }
//    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
//        Marker[] markers = new Marker[workersMapAdapter.employeeArrayListMap.size()];
//        for(int position = 0; position<workersMapAdapter.employeeArrayListMap.size(); position++){
//            Employee employee = workersMapAdapter.employeeArrayListMap.get(position);
//            LatLng latLng = new LatLng(employee.getLatitude(),employee.getLongitude());
//            LatLng latLng1 = new LatLng(51.19, 17.6);
//            markers[position] = map.addMarker(new MarkerOptions().position(latLng1));
//            //markers.set(position, map.addMarker(new MarkerOptions().position(latLng)));
//        }
//        LatLng Strzalkowa = new LatLng(51.169313, 17.506307);
//        googleMap.addMarker(new MarkerOptions().position(Strzalkowa).title("Strzalkowa"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(Strzalkowa));
//        //googleMap.setMyLocationEnabled(true);
//
//        LatLng latLng = new LatLng(51.169313, 17.506307);
//        Marker mark = map.addMarker(new MarkerOptions().position(latLng));
//
//        reference.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Employee Changedemployees = dataSnapshot.getValue(Employee.class);
//                float newLatitude = Changedemployees.getLatitude();
//                float newLongitude = Changedemployees.getLongitude();
//                for(int position = 0; position < workersMapAdapter.employeeArrayListMap.size(); position++){
//                    Employee employee = workersMapAdapter.employeeArrayListMap.get(position);
//                    if(employee.getId() == Changedemployees.getId()){
//                       // markers[position].setTag(employee.getSurname());
//                        Marker mark1;
//                        mark.setTag(employee.getSurname());
//                        employee.setLatitude(newLatitude);
//                        employee.setLongitude(newLongitude);
//                        mark.setPosition(new LatLng(employee.getLatitude(), employee.getLongitude()));
//
//                    }
//                }
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

//    @Override
//    public void onResume() {
//        reference.addChildEventListener(this);
//        super.onResume();
//
//       // timerHandler.postDelayed(timerRunnable, 0);
//
//    }
//    //zakonczenie w onpause
//    @Override
//    public void onPause() {
//        if(reference != null){
//            reference.removeEventListener(this);
//        }
//        super.onPause();

      //  shouldRun = false;
      //  timerHandler.removeCallbacksAndMessages(timerRunnable);
    //}

//    @Override
//    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//        Log.d(TAG, "onChildAdded: ");
//        if(workersMapAdapter != null) {
//            Employee employee = dataSnapshot.getValue(Employee.class);
//            workersMapAdapter.employeeArrayListMap.add(employee);
//            workersMapAdapter.notifyDataSetChanged();
//        }
//    }

    public void trackEmployees(){
        for(int i = 0; i <workersMapAdapter.employeeArrayListMap.size(); i++){
            Employee employees = workersMapAdapter.employeeArrayListMap.get(i);
            LatLng latLng = new LatLng(employees.getLatitude(), employees.getLongitude());
            Marker mark = map.addMarker(new MarkerOptions().position(latLng).title(employees.getSurname()));
            mark.setTag(employees.getSurname());
        }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: ");
        if(workersMapAdapter != null) {
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
