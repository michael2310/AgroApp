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

import com.example.myapplication.Adapters.MenuAdapter;
import com.example.myapplication.Adapters.UserDetailAdapter;
import com.example.myapplication.ui.LoginAndRegister.LoginActivity;
import com.example.myapplication.ui.Workers.WorkersActivity;
import com.example.myapplication.Models.MenuNamesActivity;
import com.example.myapplication.ui.OptimalizationActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.FieldRecords.FieldsRecordActivity;
import com.example.myapplication.ui.machines.MachniesActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MenuFragment extends Fragment {
    // deklaracje
    FirebaseAuth firebaseAuth;
    private MenuViewModel menuViewModel;
    private RecyclerView infoRecycler;
    private RecyclerView menuRecycler;


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
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        String[] names = new String[MenuNamesActivity.names.length];
        for (int i = 0; i<MenuNamesActivity.names.length; i++){
            names[i] = MenuNamesActivity.names[i].getName();
        }

        int[] imagesId = new int[MenuNamesActivity.names.length];
        for(int i = 0; i<MenuNamesActivity.names.length; i ++){
            imagesId[i] = MenuNamesActivity.names[i].getImageId();
        }

        UserDetailAdapter infoAdapter = new UserDetailAdapter();
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
}
