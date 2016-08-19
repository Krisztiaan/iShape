package hu.artklikk.android.deloitte.fragment;

import android.app.Dialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import hu.artklikk.android.deloitte.client.IShapeClient;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hu.artklikk.android.deloitte.MainActivity;
import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.adapter.NominationListAdapter;
import hu.artklikk.android.deloitte.model.Endorsement;
import hu.artklikk.android.deloitte.util.Util;

/**
 * A fragment representing a list of Items.
 * <p/>
 */
public class OutgoingEndorsementFragment extends AbstractActionFragment {

    private Dialog endorsementDialog;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OutgoingEndorsementFragment() {
    }

    @SuppressWarnings("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            Endorsement endorsement = (Endorsement) inputMessage.getData().get("data");
            new SendEndorsementTask().execute(endorsement);
        }
    };

    public static OutgoingEndorsementFragment newInstance() {
        
        Bundle args = new Bundle();
        
        OutgoingEndorsementFragment fragment = new OutgoingEndorsementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_outgoing_nomination_list, null);
        Button sendNomination = (Button) view.findViewById(R.id.buttonSendNomination);
        sendNomination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endorsementDialog = Util.buildEndorsementDialog(getActivity(), handler);
                endorsementDialog.show();
            }
        });
        return view;
    }

    @Override
    void handleResponse(List<Endorsement> endorsementList) {
        List<Endorsement> sortedEndorsement = new ArrayList<>();
        for (Endorsement endorsement : endorsementList){
            if (endorsement.getConsignorId() == Util.getSharedPrefenceId(getActivity(), R.string.preference_key_id)){
                sortedEndorsement.add(endorsement);
            }
        }

        NominationListAdapter adapter = new NominationListAdapter(sortedEndorsement, getActivity(),
                R.layout.list_item_activity_right, ((MainActivity)getActivity()).userMap, false);
        setListAdapter(adapter);
    }

    private class SendEndorsementTask extends AsyncTask<Endorsement, Void, Integer> {
        @Override
        protected Integer doInBackground(Endorsement... params) {
            try {
                String uri = Uri.parse(IShapeClient.BASE_URL + "SendNomination")
                        .buildUpon()
                        .appendQueryParameter("consignorId", String.valueOf(params[0].getConsignorId()))
                        .appendQueryParameter("consigneeId", String.valueOf(params[0].getConsigneeId()))
                        .appendQueryParameter("time", String.valueOf(params[0].getTime()))
                        .appendQueryParameter("category", params[0].getCategory())
                        .appendQueryParameter("description", String.valueOf(params[0].getDescription()))
                        .appendQueryParameter("point", String.valueOf(params[0].getPoint()))
                        .appendQueryParameter("id", String.valueOf(params[0].getId()))
                        .appendQueryParameter("acceptedFlag", String.valueOf(params[0].isAcceptedFlag()))
                        .build().toString();
                try {
                    URL url = new URL(uri);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.addRequestProperty("Cookie", "ASP.NET_SessionId=" +
                            Util.getSharedPrefence(getActivity(), R.string.preference_key_token));
                    int responseCode = urlConnection.getResponseCode();

                    Log.d(getClass().toString(), "response code: " + responseCode);

                    return responseCode;

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
        protected void onPostExecute(Integer endorsementResponse) {
            if (endorsementResponse == HttpURLConnection.HTTP_OK){
                Toast.makeText(getActivity(), "Endorsement sent ;)", Toast.LENGTH_LONG).show();
                endorsementDialog.cancel();
            } else {
                try {
                    Toast.makeText(getActivity(), "Error in endorsement sending... Please try again", Toast.LENGTH_LONG).show();
                } catch (NullPointerException e){

                }
            }
        }
    }
}
