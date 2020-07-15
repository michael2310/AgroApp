package com.example.myapplication.pagers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.ui.home.EmployeeHomeFragment;
import com.example.myapplication.ui.menu.EmployeeMenuFragment;

public class EmployeePagerAdapter extends FragmentStateAdapter {

    public EmployeePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return (position == 0) ? new EmployeeHomeFragment() : new EmployeeMenuFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}