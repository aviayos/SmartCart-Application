package com.example.orella.smartcart.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class CategoriesPageAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> mFragmentList= new ArrayList<>();
    private final ArrayList<String> mFragmentTitleList= new ArrayList<>();


    public CategoriesPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title, Bundle bun){
        mFragmentTitleList.add(title);
        fragment.setArguments(bun);
        mFragmentList.add(fragment);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
