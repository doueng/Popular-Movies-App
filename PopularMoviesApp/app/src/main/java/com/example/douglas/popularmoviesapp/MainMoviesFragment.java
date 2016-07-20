package com.example.douglas.popularmoviesapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


public class MainMoviesFragment extends Fragment implements AsyncResponse {

    private GridAdapter posterAdapter;
    private GridAdapterFavorites adapterFavorites;
    private GridView gridView;
    private String sortOrder = "popular";

    // newInstance constructor for creating fragment with arguments
    public static MainMoviesFragment newInstance(String sort) {
        MainMoviesFragment mainMoviesFragment = new MainMoviesFragment();
        Bundle args = new Bundle();
        args.putString("sort", sort);
        mainMoviesFragment.setArguments(args);
        return mainMoviesFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void processFinish(MoviesParcelable[] result) {
        ArrayList<MoviesParcelable> parcel = new ArrayList<>(Arrays.asList(result));
        this.posterAdapter = new GridAdapter(getActivity(), parcel);
        gridView.setAdapter(posterAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.gridview_container, container, false);


        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.vpPager);
        FragmentPagerAdapter fragmentPagerAdapter = new PagerAdapterSelectMovie(getFragmentManager());
        viewPager.setAdapter(fragmentPagerAdapter);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setSortOrderAndUpdate("popular");
                        break;
                    case 1:
                        setSortOrderAndUpdate("top_rated");
                        break;
                    case 2:
                        setSortOrderAndUpdate("favorite");
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        gridView = (GridView) rootView.findViewById(R.id.gridView);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long I) {

                Bundle bundle = new Bundle();
                MainMoreInfoFragment fragment = new MainMoreInfoFragment();

                if (sortOrder.equals("favorite")) {
                    String movie = adapterFavorites.getItem(position);
                    String[] part = movie.split("http");
                    bundle.putString("movieId", part[0]);
                    bundle.putString("posterUrl", "http" + part[1]);

                } else {
                    MoviesParcelable movie = posterAdapter.getItem(position);
                    bundle.putParcelable("parcel", movie);

                }

                fragment.setArguments(bundle);

                fragment.setEnterTransition(new Slide().setDuration(100));

                if (getActivity().findViewById(R.id.movies_container_two_pane) != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.more_info_container_two_pane, fragment)
                            .commit();
                }
                else if (getActivity().findViewById(R.id.movies_container_two_pane) == null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.movies_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }

            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setSortOrderAndUpdate(sortOrder);
    }

    public void setSortOrderAndUpdate(String sortOrder) {
        this.sortOrder = sortOrder;
        if (sortOrder.equals("favorite")) {
            updateFavoriteMovies();
        } else {
            updateMovies();
        }
    }

    public void updateMovies() {
        FetchMoviesTask update = new FetchMoviesTask();
        update.delegate = this;
        update.execute(this.sortOrder);
    }

    public void updateFavoriteMovies() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        ArrayList<String> list = new ArrayList<>();
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String[] part = entry.getKey().split("&");
            if (part[0].equals("poster")) {
                list.add(entry.getValue().toString());
            }
        }
        adapterFavorites = new GridAdapterFavorites(getActivity(), list);
        gridView.setAdapter(adapterFavorites);
    }


}



