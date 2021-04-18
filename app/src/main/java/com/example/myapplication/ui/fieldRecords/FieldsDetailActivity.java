package com.example.myapplication.ui.fieldRecords;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.dialogs.Details.DialogCultivation;
import com.example.myapplication.dialogs.Details.DialogDetailRemove;
import com.example.myapplication.dialogs.Details.DialogEditCultivation;
import com.example.myapplication.dialogs.Details.DialogEditFertilization;
import com.example.myapplication.dialogs.Details.DialogEditPlantProtection;
import com.example.myapplication.dialogs.Details.DialogFertilization;
import com.example.myapplication.dialogs.Details.DialogPlantProtection;
import com.example.myapplication.R;
import com.example.myapplication.db.CultivationRepository;
import com.example.myapplication.db.FertilizationRepository;
import com.example.myapplication.db.PlantProtectionRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class FieldsDetailActivity extends AppCompatActivity implements  DialogCultivation.DialogListener,
        DialogPlantProtection.DialogListener, DialogFertilization.DialogListener, DialogEditCultivation.DialogEditListener, DialogEditPlantProtection.DialogEditListener, DialogEditFertilization.DialogEditListener, DialogDetailRemove.DialogDetailRemoveListener {

    public static final String EXTRA_FIELD_ID = "fieldId";
    public String fieldId;
    private ViewPager pager;
    private FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fields_detail);

        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);


        fab = (FloatingActionButton) findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = pager.getCurrentItem();

                if(number == 0){
                    openDialogCultivation();
                }
                else if(number == 1){
                    openDialogPlantProtection();
                }
                else if (number == 2){
                    openDialogFertilization();
                }
            }
        });
        Intent intent = getIntent();

        fieldId = intent.getStringExtra(EXTRA_FIELD_ID);
    }


    private void openDialogCultivation () {
        DialogCultivation dialogCultivation = new DialogCultivation();
        dialogCultivation.show(getSupportFragmentManager(), "Dialog Field Detail");
    }


    private void openDialogFertilization() {
        DialogFertilization dialogFertilization = new DialogFertilization();
        dialogFertilization.show(getSupportFragmentManager(), "Dialog Field Detail");
    }


    private void openDialogPlantProtection() {
        DialogPlantProtection dialogPlantProtection = new DialogPlantProtection();
        dialogPlantProtection.show(getSupportFragmentManager(), "Dialog Field Detail");
    }


    @Override
    public void applyTextsCultivation(String plant, String cultivationType, String sowingType, String date, String info, String category) {
        CultivationRepository.getInstance().addFieldDetail(category, plant, cultivationType, sowingType, date, info, fieldId);
    }

    @Override
    public void changeTextsCultivation(String plant, String cultivationType, String sowingType, String date, String info, String category, String id) {
        CultivationRepository.getInstance().editFieldDetail(category, plant, cultivationType, sowingType, date, info, id, fieldId);
    }

    @Override
    public void applyTextsPlantProtection(String plant, String chemicals, String dose, String developmentPhase, String date, String category, String info) {
        PlantProtectionRepository.getInstance().addFieldDetail(category, fieldId, plant, chemicals, dose, developmentPhase, date, info);
    }

    @Override
    public void changeTextsPlantProtection(String category,String id, String plant, String chemicals, String dose, String developmentPhase, String date, String info) {
        PlantProtectionRepository.getInstance().editFieldDetail(category, id, fieldId, plant, chemicals, dose, developmentPhase, date, info);
    }

    @Override
    public void applyTextsFertilization(String plant, String chemicals, String dose, String developmentPhase, String date, String category, String info) {
        FertilizationRepository.getInstance().addFieldDetail(category, fieldId, plant, chemicals, dose, developmentPhase, date, info);
    }

    @Override
    public void changeTextsFertilization(String category, String id, String plant, String chemicals, String dose, String developmentPhase, String date, String info) {
        FertilizationRepository.getInstance().editFieldDetail(category, id, fieldId, plant, chemicals, dose, developmentPhase, date, info);
    }


    @Override
    public void removeText(String fieldId, String id) {
        int number = pager.getCurrentItem();

        if(number == 0){
            CultivationRepository.getInstance().removeFieldDetail(fieldId, id);
        }
        else if(number == 1){
            PlantProtectionRepository.getInstance().removeFieldDetail(fieldId, id);
        }
        else if (number == 2){
            FertilizationRepository.getInstance().removeFieldDetail(fieldId, id);
        }

    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new CultivationFragment();
                case 1:
                    return new PlantProtectionFragment();
                case 2:
                    return new FertilizationFragment();
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
            switch (position){
                case 0:
                    return getResources().getText(R.string.cultivating);
                case 1:
                    return getResources().getText(R.string.plant_protection);
                case 2:
                    return getResources().getText(R.string.fertilization);
            }
            return super.getPageTitle(position);
        }

    }

    public void setId(String id){
    }
}
