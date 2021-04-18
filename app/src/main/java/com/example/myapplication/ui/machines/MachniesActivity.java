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

import com.example.myapplication.dialogs.DialogHarvester;
import com.example.myapplication.dialogs.DialogMachine;
import com.example.myapplication.dialogs.DialogOtherMachine;
import com.example.myapplication.dialogs.DialogTractor;
import com.example.myapplication.machinesFragments.CombineHarvesterFragment;
import com.example.myapplication.machinesFragments.OthersMachinesFragment;
import com.example.myapplication.machinesFragments.TractorsFragment;
import com.example.myapplication.R;
import com.example.myapplication.db.MachinesRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class MachniesActivity extends AppCompatActivity implements DialogMachine.DialogListener {

    private final String tractor = "tractor";
    private final String harvester = "harvester";
    private final String others = "others";
    FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machines);

        fab = (FloatingActionButton) findViewById(R.id.addFab);

        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        // pager.setAdapter(new SectionsPagerAdapter(getChildFragmentManager()));
        pager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int item = pager.getCurrentItem();

                switch (item){
                    case 0:
                        openDialogMachine(tractor);
                        //openDialogTractors();
                        break;
                    case 1:
                        openDialogMachine(harvester);
                        //openDialogHarvesters();
                        break;
                    case 2:
                        openDialogMachine(others);
                        //openDialogOthersMachines();
                        break;
                }
            }
        });
    }



    private void openDialogMachine(String category){
        DialogMachine dialogMachine = new DialogMachine(category);
        dialogMachine.show(getSupportFragmentManager(), "Dialog Machine");
    }
    private void openDialogTractors() {
        DialogTractor dialogTractor = new DialogTractor();
        dialogTractor.show(getSupportFragmentManager(), "Dialog Tractor");
        //Toast.makeText(this, "Tractors", Toast.LENGTH_SHORT).show();
    }

    private void openDialogHarvesters() {
        DialogHarvester dialogHarvester = new DialogHarvester();
        dialogHarvester.show(getSupportFragmentManager(), "Dialog Harvester");
        //Toast.makeText(this, "Harvesters", Toast.LENGTH_SHORT).show();

    }

    private void openDialogOthersMachines() {
        DialogOtherMachine dialogOtherMachine = new DialogOtherMachine();
        dialogOtherMachine.show(getSupportFragmentManager(), "Dialog Other Machine");
        //Toast.makeText(this, "Others", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void applyTextsMachine(String brand, String model, String category) {
        MachinesRepository.getInstance().addMachine(brand, model, category);
    }

//    @Override
//    public void applyTextsHarvester(String brand, String model, String category) {
//        MachinesRepository.getInstance().addMachine(brand, model, category);
//    }
//
//    @Override
//    public void applyTextsOthers(String brand, String model, String category) {
//        MachinesRepository.getInstance().addMachine(brand, model, category);
//    }
//
//    @Override
//    public void applyTextsTractor(String brand, String model, String category) {
//        MachinesRepository.getInstance().addMachine(brand, model, category);
//    }


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
                    return new OthersMachinesFragment();
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
                    return getResources().getText(R.string.othersMachinesTab);
            }

            return null;
        }
    }
}
