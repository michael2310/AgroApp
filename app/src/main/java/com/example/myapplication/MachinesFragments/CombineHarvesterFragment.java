package com.example.myapplication.MachinesFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.Adapters.MachinesAdapter;
import com.example.myapplication.Models.Machines.Harvesters;
import com.example.myapplication.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CombineHarvesterFragment extends Fragment {

    public CombineHarvesterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_combine_harvester, container, false);

        RecyclerView menuRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_combine_harvester, container, false);

        String[] names = new String[Harvesters.harvesters.length];
        for (int i = 0; i<Harvesters.harvesters.length; i++){
            names[i] = Harvesters.harvesters[i].getName();
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
