package mx.saudade.sunshineapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by Angie on 27/07/2015.
 */
public class ForecastFragment extends Fragment {
    String[] weather = {"Today Sunny", "Yesterday ", "Last week"};
    ArrayAdapter<String> adapter;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        list = (ListView) rootView.findViewById(R.id.listView_forecast);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weather);
        list.setAdapter(adapter);

        return rootView;
    }

}
