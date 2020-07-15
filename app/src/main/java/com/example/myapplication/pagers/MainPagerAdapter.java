package com.example.myapplication.pagers;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.myapplication.ui.home.HomeFragment;
import com.example.myapplication.ui.menu.MenuFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    public MainPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @Override
    public Fragment createFragment(int position) {
        return (position == 0) ? new HomeFragment() : new MenuFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
