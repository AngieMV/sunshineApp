package mx.saudade.sunshineapp;

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
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Angie on 27/07/2015.
 */
public class FetchWeatherTask extends AsyncTask<String, Void, Void> {

    private String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    @Override
    protected Void doInBackground(String... params) {

        if (params == null || params.length != 1) {
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String forecastJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast
            //http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) getUrl(params[0]).openConnection();
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
            forecastJsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error " + e.toString(), e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        Log.v(LOG_TAG, forecastJsonStr);
        return null;
    }

    private URL getUrl(String postalCode) throws MalformedURLException {

        String original = "http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7";

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http://api.openweathermap.org/data/2.5/forecast/daily");
        builder.appendQueryParameter("q",postalCode);
        builder.appendQueryParameter("mode","json");
        builder.appendQueryParameter("units", "metric");
        builder.appendQueryParameter("cnt", "7");

        original = builder.toString();
        Log.v(LOG_TAG, "URL " + original);
        return new URL(original);
    }


    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex)
            throws JSONException {

        JSONObject json = new JSONObject(weatherJsonStr);
        JSONArray list = json.getJSONArray("list");
        JSONObject item = list.getJSONObject(dayIndex);
        JSONObject main = item.getJSONObject("main");
        return main.getDouble("temp_max");
    }
}
