package com.example.douglas.popularmoviesapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by douglas on 09/07/2016.
 */
public class PagerAdapterSelectMovie extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    public PagerAdapterSelectMovie(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MainMoviesFragment.newInstance("popular");
            case 1:
                return MainMoviesFragment.newInstance("top_rated");
            case 2:
                return MainMoviesFragment.newInstance("favorite");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Popular";
            case 1:
                return "Top rated";
            case 2:
                return "Favorites";
            default:
                return null;
        }
    }
}
