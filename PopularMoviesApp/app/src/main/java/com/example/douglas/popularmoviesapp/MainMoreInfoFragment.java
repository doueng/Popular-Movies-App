package com.example.douglas.popularmoviesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;


public class MainMoreInfoFragment extends Fragment implements AsyncResponseSingle {

    private MoviesParcelable parcel;
    private String trailerURL;
    private CheckBox checkBox;
    private SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.more_info_fragment, container, false);
        Bundle bundle = getArguments();

        checkBox = (CheckBox) rootView.findViewById(R.id.star);
        checkBox.setVisibility(View.INVISIBLE);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        if (bundle != null) {
            if (bundle.containsKey("parcel")) {

                parcel = bundle.getParcelable("parcel");

                update(parcel.getId() + "");

                checkBox.setChecked(sharedPreferences.getBoolean(parcel.getId() + "", false));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        String id = parcel.getId() + "";
                        if (isChecked) {
                            savePreferences(id, parcel.getUrl(), isChecked);

                        } else {
                            removePreferences(id);
                        }
                    }
                });

            } else if (bundle.containsKey("movieId") && bundle.containsKey("posterUrl")) {

                final String id = bundle.getString("movieId");
                final String posterUrl = bundle.getString("posterUrl");

                update(id);

                checkBox.setChecked(sharedPreferences.getBoolean(id, false));
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            savePreferences(id, posterUrl, isChecked);
                        } else {
                            removePreferences(id);
                        }
                    }
                });
            }
        }
        return rootView;
    }

    private void update(String id) {
        FetchSingleMovie update = new FetchSingleMovie();
        update.delegate = this;
        update.execute(id);
    }

    private void removePreferences(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("poster&" + key);
        editor.apply();
    }

    private void savePreferences(String key, String posterURL, boolean checkStatus) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("poster&" + key, key + posterURL);
        editor.putBoolean(key, checkStatus);
        editor.apply();
    }

    @Override
    public void processFinish(MoviesParcelableSingle result) {



        if (getView() != null) {

            checkBox.setVisibility(View.VISIBLE);

            // TITLE
            ((TextView) getView().findViewById(R.id.title)).setText(result.getTitle());

            // POSTER
            ImageView gridView = (ImageView) getView().findViewById(R.id.more_info_poster);
            Picasso.with(getContext()).load(result.getPosterURL()).into(gridView);

            // RELEASE_DATE
            ((TextView) getView().findViewById(R.id.release_date)).setText
                    (result.getReleaseDate());

            // VOTE_AVERAGE
            DecimalFormat df = new DecimalFormat("#.#");
            String voteAverage = df.format(result.getVoteAverage()) + "/10";
            ((TextView) getView().findViewById(R.id.vote_average)).setText(voteAverage);

            // OVERVIEW
            ((TextView) getView().findViewById(R.id.overview)).setText(result.getOverview());


            // TRAILER URL
            trailerURL = "https://www.youtube.com/watch?v=" + result.getTrailerURL();

            // TRAILER TEXT
            ((TextView) getView().findViewById(R.id.trailer_text)).setText(getString(R.string.trailer));

            // TRAILER IMAGE STAR
            int id = getResources().getIdentifier("@drawable/triangle", null, getContext().getPackageName());
            Drawable res = getResources().getDrawable(id);
            ((ImageView) getView().findViewById(R.id.trailer_image)).setImageDrawable(res);

            // TRAILER LISTENER
            LinearLayout linearLayout = (LinearLayout) getView().findViewById(R.id.trailer);
            linearLayout.setOnClickListener(new AdapterView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse(trailerURL)));
                }
            });

            // REVIEW
            String review = getString(R.string.reviews)
                    + "\n\n" + result.getReview();
            ((TextView) getView().findViewById(R.id.reviews)).setText(review);
            if (result.getReview().equals("")) {
                ((TextView) getView().findViewById(R.id.reviews)).setText(getString(R.string.no_reviews));
            }
        }
    }
}



