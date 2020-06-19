package com.example.myapplication.ui.menu;

import android.content.Intent;
import android.os.Bundle;
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

import com.example.myapplication.Adapters.MenuAdapter;
import com.example.myapplication.WorkersActivity;
import com.example.myapplication.MenuNamesActivity;
import com.example.myapplication.OptimalizationActivity;
import com.example.myapplication.R;
import com.example.myapplication.FieldsRecordActivity;
import com.example.myapplication.ui.machines.MachniesActivity;
import com.google.firebase.auth.FirebaseAuth;

public class MenuFragment extends Fragment {
    // deklaracje
    FirebaseAuth firebaseAuth;
    private MenuViewModel menuViewModel;
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

        menuRecycler = (RecyclerView) view.findViewById(R.id.menu_recycler) ;

        firebaseAuth = FirebaseAuth.getInstance();

        String[] names = new String[MenuNamesActivity.names.length];
        for (int i = 0; i<MenuNamesActivity.names.length; i++){
            names[i] = MenuNamesActivity.names[i].getName();
        }

        int[] imagesId = new int[MenuNamesActivity.names.length];
        for(int i = 0; i<MenuNamesActivity.names.length; i ++){
            imagesId[i] = MenuNamesActivity.names[i].getImageId();
        }

        MenuAdapter adapter = new MenuAdapter(names, imagesId);
        //manager ukladu
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
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
    }
}
