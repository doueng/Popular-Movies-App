package com.example.douglas.popularmoviesapp;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by douglas on 04/05/2016.
 */
public class FetchSingleMovie extends AsyncTask<String, Void, MoviesParcelableSingle> {

    private final String LOG_TAG = FetchSingleMovie.class.getSimpleName();

    public AsyncResponseSingle delegate = null;

    @Override
    protected MoviesParcelableSingle doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        String movieID = params[0] + "";

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(movieID)
                    .appendQueryParameter("api_key", BuildConfig.API_KEY)
                    .appendQueryParameter("append_to_response", "trailers,reviews");


            String myUrl = builder.build().toString();
            Log.d(LOG_TAG, myUrl);
            URL url = new URL(myUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            moviesJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        try {
            return getFilmDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

    private MoviesParcelableSingle getFilmDataFromJson(String movieDBStr)
            throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_TRAILERS = "trailers";
        final String MDB_SOURCE = "source";
        final String MDB_TYPE = "type";
        final String MDB_YOUTUBE = "youtube";
        final String MDB_REVIEWS = "reviews";
        final String MDB_CONTENT = "content";

        final String MDB_ID = "id";
        final String MDB_POSTER = "poster_path";
        final String MDB_OVERVIEW = "overview";
        final String MDB_RELEASE = "release_date";
        final String MDB_TITLE = "title";
        final String MDB_VOTE_AVERAGE = "vote_average";

        JSONObject filmJson = new JSONObject(movieDBStr);
        JSONObject trailerJson = filmJson.getJSONObject(MDB_TRAILERS);
        JSONArray filmArray = trailerJson.getJSONArray(MDB_YOUTUBE);
        JSONObject reviewJson = filmJson.getJSONObject(MDB_REVIEWS);
        JSONArray reviewArray = reviewJson.getJSONArray(MDB_RESULTS);

        MoviesParcelableSingle[] resultFilms = new MoviesParcelableSingle[filmArray.length()];

        int id = filmJson.getInt(MDB_ID);
        String posterURL = "http://image.tmdb.org/t/p/w342" + filmJson.getString(MDB_POSTER);
        String overview = filmJson.getString(MDB_OVERVIEW);
        String releaseDate = filmJson.getString(MDB_RELEASE);
        String[] parts = releaseDate.split("-");
        releaseDate = parts[0];
        String title = filmJson.getString(MDB_TITLE);
        double voteAverage = filmJson.getDouble(MDB_VOTE_AVERAGE);

        String trailers = "";
        String reviews = "";

        for (int i = 0; i < filmArray.length(); i++) {

            JSONObject film = filmArray.getJSONObject(i);
            if (film.getString(MDB_TYPE).equals("Trailer")) {
                trailers = film.getString(MDB_SOURCE);
                break;
            }
        }

        if (reviewArray.length() > 0) {
            JSONObject review = reviewArray.getJSONObject(0);
            reviews = review.getString(MDB_CONTENT);
        }


        MoviesParcelableSingle result = new MoviesParcelableSingle(trailers, reviews, posterURL, id,
                overview, releaseDate, title, voteAverage);
        return result;
    }

    @Override
    protected void onPostExecute(MoviesParcelableSingle result) {
        if (result != null) {
            delegate.processFinish(result);
        }
    }
}
