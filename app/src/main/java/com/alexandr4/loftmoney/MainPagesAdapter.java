package com.alexandr4.loftmoney;

import android.content.ClipData;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainPagesAdapter extends FragmentPagerAdapter {
    private final static int PAGE_INCOME = 0;
    private final static int PAGE_EXPENSE = 1;
    private final static int PAGE_BALANCE = 2;
    private final static int PAGES_COUNT = 3;
    private String[] pagesTitles;

    public MainPagesAdapter(FragmentManager fm, Context context) {

        super(fm);
        pagesTitles = context.getResources().getStringArray(R.array.main_tabs);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case PAGE_INCOME:
                return ItemsFragment.newInstance(ItemActivity.TYPE_INCOME);

            case PAGE_EXPENSE:
                return ItemsFragment.newInstance(ItemActivity.TYPE_EXPENSE);

            case PAGE_BALANCE:
                return ItemsFragment.newInstance(ItemActivity.TYPE_BALANCE);

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return PAGES_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pagesTitles[position];
    }

}
