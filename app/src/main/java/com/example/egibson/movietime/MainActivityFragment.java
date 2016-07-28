package com.example.egibson.movietime;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ImageAdaptor movieAdaptor;
    private GridView gridView;
    private ArrayList<Movie> moviesList;
    private Movie[] moviesArray = {};

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            moviesList = new ArrayList<Movie>(Arrays.asList(moviesArray));
        }
        else {
            moviesList = savedInstanceState.getParcelableArrayList("movies");
        }
    }

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Movie[] dummyMovies = {};
        //movieAdaptor = new ImageAdaptor(getActivity(),Arrays.asList(dummyMovies));
        movieAdaptor = new ImageAdaptor(getActivity(),moviesList);
        gridView = (GridView) rootView.findViewById(R.id.gridView_images);
        gridView.setAdapter(movieAdaptor);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movieAdaptor.getItem(position);

                Intent launchDetail = new Intent(getActivity(), movie_detail.class)
                        .putExtra("com.example.egibson.movietime.Movie", movie);
                startActivity(launchDetail);
            }
        });

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("movies", moviesList);
        super.onSaveInstanceState(outState);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public class FetchMoviesTask extends AsyncTask<String,Void,Movie[]> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected Movie[] doInBackground(String...params) {


            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                final String base;
                Log.e("PlaceholderFragment", "params[0]="+params[0]);
                if (params[0].equals("0"))
                    base = "http://api.themoviedb.org/3/movie/popular?api_key=0a4e07e521aa70416fb2f0bd1985a5ac";
                else
                    base = "http://api.themoviedb.org/3/movie/top_rated?api_key=0a4e07e521aa70416fb2f0bd1985a5ac";

                Log.v(LOG_TAG, "URL: " + base);
                URL url = new URL(base);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG, "forecastJsonStr: " + movieJsonStr);
                Movie[] movieData = new Movie[20];

                try {
                  movieData = getMovieDataFromJson(movieJsonStr, 20);
                } catch (JSONException e) {
                    Log.e(LOG_TAG,e.getMessage(), e);
                }

                return movieData;

            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error no data fetched", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
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
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            super.onPostExecute(movies);
            Log.e("PlaceholderFragment", "length = " + movies.length);
            Log.e("PlaceholderFragment", "movies = " + movies);


            movieAdaptor = new ImageAdaptor(getActivity(), Arrays.asList(movies));
            gridView.setAdapter(null);
            gridView.setAdapter(movieAdaptor);
            Log.e("PlaceholderFragment", "getCount() = " + movieAdaptor.getCount());
        }

        /**
         * Take the String representing the complete forecast in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private Movie[] getMovieDataFromJson(String movieJsonStr, int numMovies)
                throws JSONException {

            Movie[] movies = new Movie[numMovies];
            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray("results");

            Log.e("PlaceholderFragment","number of movies=" + numMovies);

            for(int i = 0; i < numMovies; i++) {

                // Get the JSON object representing the day
                String image = "http://image.tmdb.org/t/p/w185//"+movieArray.getJSONObject(i).getString("poster_path");
                String title = movieArray.getJSONObject(i).getString("original_title");
                String overview = movieArray.getJSONObject(i).getString("overview");
                String vote_average = movieArray.getJSONObject(i).getString("vote_average");
                String release_date = movieArray.getJSONObject(i).getString("release_date");

                movies[i] = new Movie(image,title,overview,vote_average,release_date);
            }

            return movies;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("PlaceholderFragment", "onStart");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortMethod = prefs.getString(getString(R.string.pref_sort_method_key), getString(R.string.pref_sort_method_default));

        if (isOnline()) {
            FetchMoviesTask fetcher = new FetchMoviesTask();
            fetcher.execute(sortMethod);
        } else {
            Log.e("onStart()", "No Network Connection");
            Toast.makeText(getActivity(), "No Network Connection - Can't fetch movie data", Toast.LENGTH_LONG).show();
        }
    }
}
