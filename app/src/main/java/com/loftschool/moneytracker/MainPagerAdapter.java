package com.loftschool.moneytracker;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPagerAdapter extends FragmentPagerAdapter {

    private static final int PAGE_EXPENSES = 0;
    private static final int PAGE_INCOMES = 1;
    private static final int PAGE_BALANCE = 2;

    private static final int PAGE_COUNT = 3;

    private String[] titles;

    public MainPagerAdapter(FragmentManager fm, Resources resources) {
        super(fm);
        titles = resources.getStringArray(R.array.tab_titles);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case PAGE_EXPENSES:
                return ItemsFragment.getItemsFragment(Item.TYPE_EXPENSES);
            case PAGE_INCOMES:
                return ItemsFragment.getItemsFragment(Item.TYPE_INCOMES);
            case PAGE_BALANCE:
                return new BalanceFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
