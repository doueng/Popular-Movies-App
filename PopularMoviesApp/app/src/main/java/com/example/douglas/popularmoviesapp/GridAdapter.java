package com.example.douglas.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class GridAdapter extends ArrayAdapter<MoviesParcelable> {

    public GridAdapter(Context context, ArrayList<MoviesParcelable> poster) {
        super(context, 0, poster);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MoviesParcelable parcel = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_content, parent, false);
        }

        ImageView gridView = (ImageView) convertView.findViewById(R.id.movie_poster);
        Picasso.with(getContext()).load(parcel.getUrl()).into(gridView);

        return convertView;
    }




}






