package com.example.myapplication.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapters.WorkersMapAdapter;
import com.example.myapplication.Models.Employee;
import com.example.myapplication.Models.Fields;
import com.example.myapplication.Models.Owner;
import com.example.myapplication.R;
import com.example.myapplication.db.UsersRepository;
import com.example.myapplication.db.WorkersRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedHashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements OnMapReadyCallback, ChildEventListener {

    private final String TAG = HomeFragment.class.getSimpleName();
    private HomeViewModel homeViewModel;
    private WorkersMapAdapter workersMapAdapter;
    private SupportMapFragment mapFragment;
    private Employee employee;
    private final Map<String, Marker> workers = new LinkedHashMap<>();
    private Marker userMarker;
    private GoogleMap googleMap;

    private boolean flag;

    private RecyclerView menuRecycler;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(savedInstanceState != null){
            flag = savedInstanceState.getBoolean("flag");
        }

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(gMap -> {
            googleMap = gMap;
        });

        menuRecycler = view.findViewById(R.id.employee_recycler);
        workersMapAdapter = new WorkersMapAdapter();
        //manager ukladu
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        menuRecycler.setLayoutManager(layoutManager);
        menuRecycler.setAdapter(workersMapAdapter);

        FloatingActionButton button = (FloatingActionButton) view.findViewById(R.id.location_button);

    if(!flag) {
        zoomToUser();
    }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomToUser();
            }
        });


        workersMapAdapter.setListener(new WorkersMapAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Employee employee = workersMapAdapter.employeeArrayListMap.get(position);
                if(workers.get(employee.getId()) != null){
                    if(googleMap != null){
                        LatLng latLng = new LatLng(employee.getLat(), employee.getLng());
                        CameraPosition cameraPosition =
                                new CameraPosition.Builder()
                                        .target(latLng)
                                        .bearing(0)
                                        .tilt(0)
                                        .zoom(15)
                                        .build();
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                    Toast.makeText(getContext(), workersMapAdapter.employeeArrayListMap.get(position).getName().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private final ValueEventListener userObserver = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Owner owner = snapshot.getValue(Owner.class);
            if (googleMap != null) {
                LatLng latLng = new LatLng(owner.getLat(), owner.getLng());
                if (userMarker == null) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.visible(true);
                    userMarker = googleMap.addMarker(markerOptions);
                } else {
                    userMarker.setPosition(latLng);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    private final ValueEventListener workersObserver = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            Employee employee = snapshot.getValue(Employee.class);
            if (employee == null)
                return;
            if (workers.get(employee.getId()) == null) {
                workers.put(employee.getId(), null);
            }

            if (workersMapAdapter != null) {
                replace(employee);
                workersMapAdapter.notifyDataSetChanged();
            }

            if (googleMap != null) {
                LatLng latLng = new LatLng(employee.getLat(), employee.getLng());
                Marker workerData = workers.get(employee.getId());
                if (workerData == null) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.visible(true);
                    markerOptions.title(employee.getName());
                    workerData = googleMap.addMarker(markerOptions);
                    workers.put(employee.getId(), workerData);
                } else {
                    workerData.setPosition(latLng);
                }
                workerData.setTag(employee);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };


    private void zoomToUser() {
        UsersRepository.getInstance().getCurrentUserRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Owner owner = snapshot.getValue(Owner.class);
                    LatLng latLng = new LatLng(owner.getLat(), owner.getLng());
                    CameraPosition cameraPosition =
                            new CameraPosition.Builder()
                                    .target(latLng)
                                    .bearing(0)
                                    .tilt(0)
                                    .zoom(15)
                                    .build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initWindow(GoogleMap gMap) {
        gMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                Employee detailDto = (Employee) marker.getTag();
               // View v = View.inflate(requireActiviy(), R.layout.custom_info_window, null);
                // stworzyc layout
                return null;
            }
        });
    }

    private void startObserveUser() {
        UsersRepository.getInstance().getCurrentUserRef().addValueEventListener(userObserver);
    }

    private void stopObserveUser() {
        UsersRepository.getInstance().getCurrentUserRef().removeEventListener(userObserver);
    }

    private void startObserveWorkers() {
        WorkersRepository.getInstance().getUserFarmWorkersRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String farmMemberId = data.child("FarmMemberId").getValue(String.class);
                        UsersRepository.getInstance().getUsersRef().child(farmMemberId).addValueEventListener(workersObserver);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void stopObserveWorkers() {
        WorkersRepository.getInstance().getUserFarmWorkersRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        String farmMemberId = data.child("FarmMemberId").getValue(String.class);

                        UsersRepository.getInstance().getUsersRef().child(farmMemberId).removeEventListener(workersObserver);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
//        workersMapAdapter.employeeArrayListMap.clear();
//        WorkersRepository.getInstance().getUserFarmWorkersRef().addChildEventListener(this);
//        startObserveUser();
//        startObserveWorkers();
        flag = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        workersMapAdapter.employeeArrayListMap.clear();
        WorkersRepository.getInstance().getUserFarmWorkersRef().addChildEventListener(this);
        startObserveUser();
        startObserveWorkers();
    }

    @Override
    public void onPause() {
        stopObserveUser();
        stopObserveWorkers();
        WorkersRepository.getInstance().getUserFarmWorkersRef().removeEventListener(this);
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        flag = false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildAdded: ");
           String employeeId = dataSnapshot.child("FarmMemberId").getValue().toString();
        if(dataSnapshot.exists()) {
            addEmployeeToRecycler(employeeId);
        }
    }


    //private void addEmployeeToRecycler(Employee employee) {
    private void addEmployeeToRecycler(String employeeId){
        UsersRepository.getInstance().getUsersRef().child(employeeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (workersMapAdapter.employeeArrayListMap != null) {
                        employee = dataSnapshot.getValue(Employee.class);
                        workersMapAdapter.employeeArrayListMap.add(employee);
                        workersMapAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Log.d(TAG, "onChildChanged: ");
        if (workersMapAdapter != null) {
            Employee employee = dataSnapshot.getValue(Employee.class);
            replace(employee);
        }
        workersMapAdapter.notifyDataSetChanged();
    }

    private void replace(Employee employee) {
        for (Employee item : workersMapAdapter.employeeArrayListMap) {
            if (employee.getId().equals(item.getId())) {
                int index = workersMapAdapter.employeeArrayListMap.indexOf(item);
                workersMapAdapter.employeeArrayListMap.set(index, employee);
                break;
            }
        }
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        String employeeId = dataSnapshot.child("FarmMemberId").getValue().toString();
        removeEmployeeFromRecycler(employeeId);
    }

    private void removeEmployeeFromRecycler(String employeeId) {
        UsersRepository.getInstance().getUsersRef().child(employeeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if (workersMapAdapter != null) {
                        String key = dataSnapshot.getKey();
                        for (Employee employee : workersMapAdapter.employeeArrayListMap) {
                            if (employee.getId().equals(key)) {
                                workersMapAdapter.employeeArrayListMap.remove(employee);
                                workersMapAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("flag", flag);
    }
}
