package com.example.myapplication.MachinesFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.MachinesAdapter;
import com.example.myapplication.R;
import com.example.myapplication.Models.Machines.Tractors;


/**
 * A simple {@link Fragment} subclass.
 */
public class TractorsFragment extends Fragment {

    public TractorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_tractors, container, false);

       // RecyclerView menuRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_menu, container, false) ;

        RecyclerView menuRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_tractors, container, false);

        String[] names = new String[Tractors.tractors.length];
        for (int i = 0; i<Tractors.tractors.length; i++){
            names[i] = Tractors.tractors[i].getName();

        }

        int[] images = new int[Tractors.tractors.length];
        for (int i = 0; i<Tractors.tractors.length; i++){
            if(Tractors.tractors[i].getResourceId() != 0){
                images[i] = Tractors.tractors[i].getResourceId();
            }
        }

        MachinesAdapter adapter = new MachinesAdapter(names);
        //manager ukladu
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        menuRecycler.setLayoutManager(layoutManager);
        menuRecycler.setAdapter(adapter);

        //return root;
        return menuRecycler;
    }
}
