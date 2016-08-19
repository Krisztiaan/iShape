package hu.artklikk.android.deloitte.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.artklikk.android.deloitte.client.IShapeClient;
import java.net.HttpURLConnection;
import java.net.URL;

import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.model.Endorsement;
import hu.artklikk.android.deloitte.util.Util;

public class ActionsFragment extends Fragment {

    public static String ENDORSEMENT = "endorsement";

    private ViewPager pager;

    private PagerSlidingTabStrip tabsStrip;

    public ActionsFragment() {
    }

    public static ActionsFragment newInstance() {
        ActionsFragment fragment = new ActionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actions, null);

        pager = null;
        tabsStrip = null;

        pager = (ViewPager) view.findViewById(R.id.viewPager);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1){
            pager.setAdapter(new ScreenSlidePagerAdapter(getFragmentManager()));
        } else {
            pager.setAdapter(new ScreenSlidePagerAdapter(getChildFragmentManager()));
        }


        tabsStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        //tabsStrip.setActivateTextColor(getResources().getColor(android.R.color.white));
        //tabsStrip.setDeactivateTextColor(getResources().getColor(R.color.light_blue));
        tabsStrip.setViewPager(pager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        new EndorsementTask().execute();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            ListFragment returnFragment;
            if (0 == position) {
                returnFragment = IncomingEndorsementFragment.newInstance();
            } else {
                returnFragment = OutgoingEndorsementFragment.newInstance();
            }
            return returnFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String returnTitle;
            if (0 == position) {
                returnTitle = "incoming";
            } else {
                returnTitle = "outgoing";
            }
            return returnTitle;
        }
    }

    private class EndorsementTask extends AsyncTask<String, Void, Endorsement[]> {
        @Override
        protected Endorsement[] doInBackground(String... params) {
            try {
                String uri = Uri.parse(IShapeClient.BASE_URL + "GetNominations")
                        .buildUpon()
                        .build().toString();
                try {
                    URL url = new URL(uri);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.addRequestProperty("Cookie", "ASP.NET_SessionId=" + Util.getSharedPrefence(getActivity(), R.string.preference_key_token));
                    ObjectMapper mapper = new ObjectMapper();

                    return mapper.readValue(urlConnection.getInputStream(), Endorsement[].class);

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
        protected void onPostExecute(Endorsement[] endorsementList) {
            if (null != endorsementList) {
                Intent intent = new Intent(ENDORSEMENT);
                intent.putExtra(ENDORSEMENT, endorsementList);
                pager.getAdapter().notifyDataSetChanged();
                getActivity().sendBroadcast(intent);
            } else {
                try {
                    Toast.makeText(getActivity(), "Some problem in download, please try again later", Toast.LENGTH_LONG).show();
                } catch (NullPointerException ignored){

                }
            }
        }
    }
}
