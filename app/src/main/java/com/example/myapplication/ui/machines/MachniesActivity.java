package com.example.myapplication.ui.machines;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.MachinesFragments.CombineHarvesterFragment;
import com.example.myapplication.MachinesFragments.HandlersFragment;
import com.example.myapplication.MachinesFragments.TractorsFragment;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MachniesActivity extends AppCompatActivity {

    private MachinesViewModel machinesViewModel;
    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machines);

        fab = (FloatingActionButton) findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        // pager.setAdapter(new SectionsPagerAdapter(getChildFragmentManager()));
        pager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }



    private class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public SectionsPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TractorsFragment();
                case 1:
                    return new CombineHarvesterFragment();
                case 2:
                    return new HandlersFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getText(R.string.tractors_tab);
                case 1:
                    return getResources().getText(R.string.harvesters_tab);
                case 2:
                    return getResources().getText(R.string.handlers_tab);
            }

            return null;
        }
    }
}
