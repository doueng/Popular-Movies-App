package com.example.douglas.popularmoviesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by douglas on 08/05/2016.
 */
public class GridAdapterFavorites extends ArrayAdapter<String> {

    public GridAdapterFavorites(Context context, ArrayList<String> poster) {
        super(context, 0, poster);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String url = getItem(position);
        String[] part = url.split("http");

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.grid_content, parent, false);
        }
        ImageView gridView = (ImageView) convertView.findViewById(R.id.movie_poster);
        Picasso.with(getContext()).load("http" + part[1]).into(gridView);

        return convertView;
    }
}
