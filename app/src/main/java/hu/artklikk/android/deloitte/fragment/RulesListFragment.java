package hu.artklikk.android.deloitte.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.artklikk.android.deloitte.client.IShapeClient;
import java.net.HttpURLConnection;
import java.net.URL;

import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.model.Rule;
import hu.artklikk.android.deloitte.util.Util;

public class RulesListFragment extends Fragment {

    private ListView listView;

    private RelativeLayout layout;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RulesListFragment() {
    }

    public static RulesListFragment newInstance() {
        Bundle args = new Bundle();

        RulesListFragment fragment = new RulesListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        new RuleTask().execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rules_list, null);
        layout = (RelativeLayout) view.findViewById(R.id.ruleLayout);

        listView = (ListView) view.findViewById(android.R.id.list);

        return view;
    }

    private class RuleTask extends AsyncTask<String, Void, Rule[]> {
        @Override
        protected Rule[] doInBackground(String... params) {
            try {
                String uri = Uri.parse(IShapeClient.BASE_URL + "GetRules")
                        .buildUpon()
                        .build().toString();
                try {
                    URL url = new URL(uri);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.addRequestProperty("Cookie", "ASP.NET_SessionId=" + Util.getSharedPrefence(getActivity(), R.string.preference_key_token));
                    ObjectMapper mapper = new ObjectMapper();

                    Rule[] ruleArray = mapper.readValue(urlConnection.getInputStream(), Rule[].class);

                    return ruleArray;

                } catch (Exception e ) {
                    Log.d(getClass().toString(), e.getLocalizedMessage());

                    return null;
                }
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Rule[] ruleArray) {
            if (null != ruleArray) {
                final String[] array = new String[ruleArray.length];
                for (int i=0; i < ruleArray.length; i++){
                    array[i] = ruleArray[i].getTitle();
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter<>(getActivity(),
                        R.layout.expandable_list_title_item, R.id.textViewTitle, array);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        getActivity().getFragmentManager().beginTransaction().
                                replace(R.id.container, RuleFragment.newInstance(ruleArray[position].getTitle(),
                                        ruleArray[position].getDescription())).addToBackStack("5").commit();
                    }
                });

                listView.setAdapter(arrayAdapter);
            } else {
                try {
                    Toast.makeText(getActivity(), "Some problem in download, please try again later", Toast.LENGTH_LONG).show();
                } catch (NullPointerException e){

                }
            }
        }
    }
}
