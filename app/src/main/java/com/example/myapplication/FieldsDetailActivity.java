package com.example.myapplication;

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

import com.example.myapplication.Dialogs.DialogCultivation;
import com.example.myapplication.Dialogs.DialogDetailRemove;
import com.example.myapplication.Dialogs.DialogFertilization;
import com.example.myapplication.Dialogs.DialogPlantProtection;
import com.example.myapplication.Services.FieldDetailRemoveService;
import com.example.myapplication.Services.FieldDetailService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class FieldsDetailActivity extends AppCompatActivity implements DialogDetailRemove.DialogDetailRemoveListener, DialogCultivation.DialogListener, DialogPlantProtection.DialogListener, DialogFertilization.DialogListener {

    // do odczyywania pozycji na liscie
    public static final String EXTRA_FIELD_ID = "fieldId";


    FloatingActionButton fab;
    DatabaseReference reference;
    String email;
    String fieldId;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fields_detail);


        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
         //viewPager do TabLaoyout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);


        // id = (TextView) findViewById(R.id.id);
        // number = (TextView) findViewById(R.id.textView2);
        fab = (FloatingActionButton) findViewById(R.id.addFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // openDialog();
                //openDialogChooseCategory();
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


/// FIrebase USER

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            email = user.getEmail();
            // to zmienić
          //  reference = FirebaseDatabase.getInstance().getReference("fieldsDetail").child(fieldId);
            // to sa te metody na dole
           // reference.addChildEventListener(this);
        }

    }




    private void openDialogCultivation () {
        //create instance of opendialog
        DialogCultivation dialogCultivation = new DialogCultivation();
        dialogCultivation.show(getSupportFragmentManager(), "Dialog Field Detail");
    }

    private void openDialogFertilization() {
        //create instance of opendialog
        DialogFertilization dialogFertilization = new DialogFertilization();
        dialogFertilization.show(getSupportFragmentManager(), "Dialog Field Detail");
    }

    private void openDialogPlantProtection() {
        //create instance of opendialog
        DialogPlantProtection dialogPlantProtection = new DialogPlantProtection();
        dialogPlantProtection.show(getSupportFragmentManager(), "Dialog Field Detail");
    }



    @Override
    public void removeText() {
        Intent serviceIntent = new Intent(FieldsDetailActivity.this, FieldDetailRemoveService.class);
        serviceIntent.putExtra("id", id);
        serviceIntent.putExtra("fieldId", fieldId);
        startService(serviceIntent);
    }


    @Override
    public void applyTextsPlantProtection(String plant, String chemia, String dose, String developmentPhase, String date, String category) {
        Intent intent = new Intent(FieldsDetailActivity.this, FieldDetailService.class);
        intent.putExtra("fieldId", fieldId);
        intent.putExtra("userName", email.split("@")[0]);
        intent.putExtra("category", category);
        intent.putExtra("plant", plant);
        intent.putExtra("chemia", chemia);
        intent.putExtra("dose", dose);
        intent.putExtra("developmentPhase", developmentPhase);
        intent.putExtra("date", date);
        startService(intent);
    }

    @Override
    public void applyTextsFertilization(String plant, String chemia, String dose, String developmentPhase, String date, String category) {
        Intent intent = new Intent(FieldsDetailActivity.this, FieldDetailService.class);
        intent.putExtra("fieldId", fieldId);
        intent.putExtra("userName", email.split("@")[0]);
        intent.putExtra("category", category);
        intent.putExtra("plant", plant);
        intent.putExtra("chemia", chemia);
        intent.putExtra("dose", dose);
        intent.putExtra("developmentPhase", developmentPhase);
        intent.putExtra("date", date);
        startService(intent);
    }

    @Override
    public void applyTextsCultivation(String plant, String cultivationType, String sowingType, String date, String info, String category) {
        Intent intent = new Intent(FieldsDetailActivity.this, FieldDetailService.class);
        intent.putExtra("fieldId", fieldId);
        intent.putExtra("userName", email.split("@")[0]);
        intent.putExtra("category", category);
        intent.putExtra("plant", plant);
        intent.putExtra("cultivationType", cultivationType);
        intent.putExtra("sowingType", sowingType);
        intent.putExtra("date", date);
        intent.putExtra("info", info);
        startService(intent);
    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {




        public SectionsPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        private String makeFragmentName(int viewId, long id) {
            return "android:switcher:" + viewId + ":" + id;
        }

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
        this.id = id;
    }

}
