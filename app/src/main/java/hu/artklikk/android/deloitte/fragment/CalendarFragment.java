package hu.artklikk.android.deloitte.fragment;


import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.imanoweb.calendarview.CalendarListener;
import com.imanoweb.calendarview.CustomCalendarView;

import hu.artklikk.android.deloitte.client.IShapeClient;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.model.Event;
import hu.artklikk.android.deloitte.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());


    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance() {

        Bundle args = new Bundle();

        CalendarFragment fragment = new CalendarFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();

        new EventTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        CustomCalendarView calendarView = (CustomCalendarView) view.findViewById(R.id.caledar);
        calendarView.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                Toast.makeText(getActivity(), "selected date: " + date.toLocaleString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onMonthChanged(Date time) {

            }
        });

        return view;
    }

    private class EventTask extends AsyncTask<String, Void, Event[]> {
        @Override
        protected Event[] doInBackground(String... params) {
            try {
                String uri = Uri.parse(IShapeClient.BASE_URL + "GetEvents")
                        .buildUpon()
                        .build().toString();
                try {
                    URL url = new URL(uri);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.addRequestProperty("Cookie", "ASP.NET_SessionId=" + Util.getSharedPrefence(getActivity(), R.string.preference_key_token));
                    ObjectMapper mapper = new ObjectMapper();

                    Event[] eventArray = mapper.readValue(urlConnection.getInputStream(), Event[].class);

                    return eventArray;

                } catch (Exception e) {
                    Log.d(getClass().toString(), e.getLocalizedMessage());

                    return null;
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Event[] eventArray) {
            if (null != eventArray) {
                Log.d(getClass().toString(), eventArray.toString());
            } else {
                try {
                    Toast.makeText(getActivity(), "Some problem in download, please try again later", Toast.LENGTH_LONG).show();
                } catch (NullPointerException e){

                }            }
        }
    }
}
