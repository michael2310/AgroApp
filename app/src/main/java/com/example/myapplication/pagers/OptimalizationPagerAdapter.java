package com.example.myapplication.pagers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.OptimalizationCultivationFragment;
import com.example.myapplication.SprayingFragment;
import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.menu.MenuFragment;

public class OptimalizationPagerAdapter extends FragmentStateAdapter {

    public OptimalizationPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        return (position == 0) ? new SprayingFragment() : new OptimalizationCultivationFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}