package hu.artklikk.android.deloitte.fragment;


import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import hu.artklikk.android.deloitte.client.IShapeClient;
import java.net.HttpURLConnection;
import java.net.URL;

import hu.artklikk.android.deloitte.MainActivity;
import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.model.communication.MentorResponse;
import hu.artklikk.android.deloitte.util.Util;

/**
 * A simple {@link Fragment} subclass.
 */
public class MentorFragment extends Fragment {
    public static String USER_ID = "userId";

    private ImageView imageView;

    private int userId;

    private WebView webViewContent;

    public MentorFragment() {
        // Required empty public constructor
    }

    public static MentorFragment newInstance(int mentorId) {
        Bundle args = new Bundle();
        args.putInt(USER_ID, mentorId);
        MentorFragment fragment = new MentorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mentor, container, false);

        userId = getArguments().getInt(USER_ID, 0);

        imageView = (ImageView) view.findViewById(R.id.profileImage);

        TextView name = (TextView) view.findViewById(R.id.name);
        TextView job = (TextView) view.findViewById(R.id.job);
        TextView starCategory = (TextView) view.findViewById(R.id.starCategory);
        TextView pointInCycle = (TextView) view.findViewById(R.id.pointInCycle);
        TextView pointInYear = (TextView) view.findViewById(R.id.pointInYear);
        TextView rank = (TextView) view.findViewById(R.id.rank);

        view.findViewById(R.id.buttonVisitFarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getFragmentManager().beginTransaction()
                        .replace(R.id.container, ProfileFragment.newInstance(userId)).addToBackStack("3").commit();

                ((MainActivity) getActivity()).onSectionAttached(3);
            }
        });

        name.setText(((MainActivity) getActivity()).userMap.get(userId).getName());
        job.setText(((MainActivity) getActivity()).userMap.get(userId).getJob());
        starCategory.setText(getResources().
                getStringArray(R.array.category_array)[((MainActivity) getActivity()).userMap.get(userId).getStarCategroy()]);

        pointInCycle.setText(String.valueOf(((MainActivity) getActivity()).userMap.get(userId).getPointInCycle()));
        pointInYear.setText(String.valueOf(((MainActivity) getActivity()).userMap.get(userId).getPointInYear()));
        rank.setText(String.valueOf(((MainActivity) getActivity()).userMap.get(userId).getCycleRank()));

        imageView.setImageDrawable(Util.generatePinToLists(((MainActivity) getActivity()).userMap.get(userId)
                , getActivity().getResources(), R.drawable.search_poi));

        webViewContent = (WebView) view
                .findViewById(R.id.content);
        webViewContent.setBackgroundColor(0);

        new MentorTask().execute();

        return view;
    }

    private class MentorTask extends AsyncTask<String, Void, MentorResponse> {
        @Override
        protected MentorResponse doInBackground(String... params) {
            try {
                String uri = Uri.parse(IShapeClient.BASE_URL + "mentor")
                        .buildUpon()
                        .build().toString();
                try {
                    URL url = new URL(uri);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.addRequestProperty("Cookie", "ASP.NET_SessionId=" + Util.getSharedPrefence(getActivity(), R.string.preference_key_token));
                    ObjectMapper mapper = new ObjectMapper();
                    MentorResponse profileResponse = mapper.readValue(urlConnection.getInputStream(), MentorResponse.class);

                    Log.d(getClass().toString(), profileResponse.toString());

                    return profileResponse;

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
        protected void onPostExecute(MentorResponse mentorResponse) {
            try {
                if (null != mentorResponse) {
                    ((MainActivity) getActivity()).userMap.get(userId).setProfileImage(mentorResponse.getProfileImage());
                    imageView.setImageDrawable(Util.generatePinToLists(((MainActivity) getActivity()).userMap.get(userId)
                            , getActivity().getResources(), R.drawable.search_poi));

                    webViewContent.loadDataWithBaseURL("", Util.generateHTMLContent(mentorResponse.getDescription(), true), "text/html", "UTF-8", "");
                } else {
                    Toast.makeText(getActivity(), "Some problem in download, please try again later", Toast.LENGTH_LONG).show();
                }
            } catch (NullPointerException e) {

            }
        }
    }
}
