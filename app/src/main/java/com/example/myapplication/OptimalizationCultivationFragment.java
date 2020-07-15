package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.myapplication.Adapters.SprayingAdapter;


public class OptimalizationCultivationFragment extends Fragment {


    public OptimalizationCultivationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_optimalization_cultivation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.sprayRecycler);
        RecyclerView.LayoutManager sprayingLayoutManager = new LinearLayoutManager(getActivity());
        SprayingAdapter sprayingAdapter = new SprayingAdapter();
        recyclerView.setLayoutManager(sprayingLayoutManager);
        recyclerView.setAdapter(sprayingAdapter);
        ((LinearLayoutManager)recyclerView.getLayoutManager()).setStackFromEnd(true);
    }
}