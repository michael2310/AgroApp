package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.res.Configuration;
import android.os.Bundle;



import com.example.myapplication.Adapters.OptimalizationAdapter;
import com.example.myapplication.Models.OptimalizationNames;
import com.example.myapplication.pagers.OptimalizationPagerAdapter;
import com.example.myapplication.R;

public class OptimalizationActivity extends AppCompatActivity {
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimalization);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new OptimalizationPagerAdapter(this));
        viewPager.setUserInputEnabled(false);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.optimalizationRecycler);

        String[] names = new String[OptimalizationNames.names.length];
        for (int i = 0; i< OptimalizationNames.names.length; i++){
            names[i] = OptimalizationNames.names[i].getName();
        }

        OptimalizationAdapter optimalizationAdapter = new OptimalizationAdapter(names);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(optimalizationAdapter);
        } else {
            GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(optimalizationAdapter);
        }

        optimalizationAdapter.setListener(new OptimalizationAdapter.Listener() {
            @Override
            public void onClick(int position) {
                switch (position){
                    case 0:
                       viewPager.setCurrentItem(0, true);
                       break;
                    case 1:
                        viewPager.setCurrentItem(1, true);
                        break;
                }
            }
        });
    }
}
