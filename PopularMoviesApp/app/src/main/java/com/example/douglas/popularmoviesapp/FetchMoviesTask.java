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


public class FetchMoviesTask extends AsyncTask<String, Void, MoviesParcelable[]> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    public AsyncResponse delegate = null;

    @Override
    protected MoviesParcelable[] doInBackground(String... params) {

        String sortBy = "popular";

        if (params.length > 0) {
            sortBy = params[0];
        }


        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String moviesJsonStr = null;

        try {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(sortBy)
                    .appendQueryParameter("api_key", BuildConfig.API_KEY);

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


    private MoviesParcelable[] getFilmDataFromJson(String movieDBStr)
            throws JSONException {

        final String MDB_RESULTS = "results";
        final String MDB_ID = "id";
        final String MDB_POSTER = "poster_path";

        JSONObject filmJson = new JSONObject(movieDBStr);
        JSONArray filmArray = filmJson.getJSONArray(MDB_RESULTS);

        MoviesParcelable[] resultFilms = new MoviesParcelable[filmArray.length()];
        for (int i = 0; i < filmArray.length(); i++) {

            int id;
            String posterUrl;

            JSONObject film = filmArray.getJSONObject(i);

            id = film.getInt(MDB_ID);
            posterUrl = "http://image.tmdb.org/t/p/w342" + film.getString(MDB_POSTER);

            resultFilms[i] = new MoviesParcelable(id, posterUrl);

        }

        return resultFilms;
    }

    @Override
    protected void onPostExecute(MoviesParcelable[] result) {
        if (result != null) {
            delegate.processFinish(result);
        }
    }
}