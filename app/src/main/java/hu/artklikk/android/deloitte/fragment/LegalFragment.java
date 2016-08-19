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
import android.webkit.WebView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.artklikk.android.deloitte.client.IShapeClient;
import java.net.HttpURLConnection;
import java.net.URL;

import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.model.Legal;
import hu.artklikk.android.deloitte.util.Util;

public class LegalFragment extends Fragment {

    private WebView webView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LegalFragment() {
    }

    public static LegalFragment newInstance() {
        Bundle args = new Bundle();

        LegalFragment fragment = new LegalFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        new LegalTask().execute();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_legal, null);

        webView = (WebView) view.findViewById(android.R.id.content);

        return view;
    }

    private class LegalTask extends AsyncTask<String, Void, Legal> {
        @Override
        protected Legal doInBackground(String... params) {
            try {
                String uri = Uri.parse(IShapeClient.BASE_URL + "GetLegal")
                        .buildUpon()
                        .build().toString();
                try {
                    URL url = new URL(uri);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.addRequestProperty("Cookie", "ASP.NET_SessionId=" + Util.getSharedPrefence(getActivity(), R.string.preference_key_token));
                    ObjectMapper mapper = new ObjectMapper();

                    Legal legal = mapper.readValue(urlConnection.getInputStream(), Legal.class);

                    return legal;

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
        protected void onPostExecute(Legal legal) {
            if (null != legal) {
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadDataWithBaseURL("", Util.generateHTMLContent(legal.getContentData()), "text/html", "UTF-8", "");
            } else {
                try {
                    Toast.makeText(getActivity(), "Some problem in download, please try again later", Toast.LENGTH_LONG).show();
                } catch (NullPointerException e){

                }
            }
        }
    }
}
