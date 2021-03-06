package mx.saudade.sunshineapp;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Angie on 27/07/2015.
 */
public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> adapter;

    private ListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWeather() {
        FetchWeatherTask task = new FetchWeatherTask(this);
        task.execute(getLocationPreference());
    }

    public void updateAdapter(final String[] weather) {
        if (weather == null || weather.length == 0) {
            return;
        }

        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weather);
        list = (ListView) getView().findViewById(R.id.listView_forecast);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String forecast = adapter.getItem(position);
                startDetailActivity(forecast);
            }
        });
    }

    private void startDetailActivity(String forecast) {
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_TEXT, forecast);
        intent.setClass(getActivity(), DetailActivity.class);

        getActivity().startActivity(intent);
    }

    private String getLocationPreference() {
        return PreferenceManager
                .getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.pref_location_key)
                        , getString(R.string.pref_location_default));
    }

    private String getUnitsPreference() {
        return null;
    }

}
