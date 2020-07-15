package com.example.myapplication.ui.menu;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.AboutAppActivity;
import com.example.myapplication.Adapters.MenuAdapter;
import com.example.myapplication.Adapters.UserDetailAdapter;
import com.example.myapplication.SettingsActivity;
import com.example.myapplication.db.UsersRepository;
import com.example.myapplication.ui.LoginAndRegister.LoginActivity;
import com.example.myapplication.ui.Workers.WorkersActivity;
import com.example.myapplication.Models.MenuNames;
import com.example.myapplication.OptimalizationActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.FieldRecords.FieldsRecordActivity;
import com.example.myapplication.ui.machines.MachniesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class MenuFragment extends Fragment implements ChildEventListener {

    private MenuViewModel menuViewModel;
    private RecyclerView infoRecycler;
    private RecyclerView menuRecycler;

    private UserDetailAdapter infoAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        menuViewModel =
                ViewModelProviders.of(this).get(MenuViewModel.class);
        //final TextView textView = root.findViewById(R.id.text_menu);
        menuViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //    textView.setText(s);
            }
        });

        View root = inflater.inflate(R.layout.fragment_menu, container, false);

        return root;
        //return menuRecycler;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        infoRecycler = (RecyclerView) view.findViewById(R.id.infoRecycler);
        menuRecycler = (RecyclerView) view.findViewById(R.id.machineListView) ;
        ListView userListview = (ListView) view.findViewById(R.id.userListView);


        String[] names = new String[MenuNames.names.length];
        for (int i = 0; i< MenuNames.names.length; i++){
            names[i] = MenuNames.names[i].getName();
        }

        int[] imagesId = new int[MenuNames.names.length];
        for(int i = 0; i< MenuNames.names.length; i ++){
            imagesId[i] = MenuNames.names[i].getImageId();
        }

        //UserDetailAdapter infoAdapter = new UserDetailAdapter();
         infoAdapter = new UserDetailAdapter();
        //manager ukladu
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        infoRecycler.setLayoutManager(linearLayoutManager);
        infoRecycler.setAdapter(infoAdapter);

        MenuAdapter adapter = new MenuAdapter(names, imagesId);
        //manager ukladu
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        menuRecycler.setLayoutManager(layoutManager);
        menuRecycler.setAdapter(adapter);

        adapter.setListener(new MenuAdapter.Listener() {
            @Override
            public void onClick(int position) {
                final Intent intent;
                switch (position){
                    case 0:
                        intent = new Intent(getActivity(), FieldsRecordActivity.class);
                       break;
                    case 1:
                        intent = new Intent(getActivity(), OptimalizationActivity.class);
                        break;
                    case 2:
                        intent = new Intent(getActivity(), WorkersActivity.class);
                        break;
                    default:
                        intent = new Intent(getActivity(), MachniesActivity.class);
                        break;
                }
                getActivity().startActivity(intent);
            }
        });

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(getActivity(), SettingsActivity.class);
                        startActivity(intent);
                        break;

                    case 1:
                        Intent intent1 = new Intent(getActivity(), AboutAppActivity.class);
                        startActivity(intent1);
                        break;
                    case 4:
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Czy na pewno chcesz się wylogować?");
                        builder.setCancelable(true);
                        builder.setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseAuth.getInstance().signOut();
                                startLoginActivity();
                            }
                        });
                        builder.setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ////
                            }
                        });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    break;
                }
            }
        };
        userListview.setOnItemClickListener(itemClickListener);
    }

    private void startLoginActivity(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onResume() {
        super.onResume();
//        if(infoAdapter != null) {
//            infoAdapter.notifyDataSetChanged();
//        }
        UsersRepository.getInstance().getCurrentUserRef().addChildEventListener(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        UsersRepository.getInstance().getCurrentUserRef().removeEventListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
        if(infoAdapter != null) {
            infoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
