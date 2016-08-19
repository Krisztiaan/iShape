package hu.artklikk.android.deloitte.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class IncomingEndorsementFragment extends AbstractActionFragment {

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IncomingEndorsementFragment() {
    }

    private BroadcastReceiver listReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(listReceiver, new IntentFilter(ActionsFragment.ENDORSEMENT));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(listReceiver);
    }

    @Override
    void handleResponse(List<Endorsement> endorsementList) {
        List<Endorsement> sortedEndorsement = new ArrayList<>();
        for (Endorsement endorsement : endorsementList){
            if (endorsement.getConsigneeId() == Util.getSharedPrefenceId(getActivity(), R.string.preference_key_id)){
                sortedEndorsement.add(endorsement);
            }
        }

        NominationListAdapter adapter = new NominationListAdapter(sortedEndorsement, getActivity(),
                R.layout.list_item_activity_left, MainActivity.userMap, true);
        setListAdapter(adapter);
    }

    public static IncomingEndorsementFragment newInstance() {
        
        Bundle args = new Bundle();
        
        IncomingEndorsementFragment fragment = new IncomingEndorsementFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incoming_nomination_list, null);
        return view;
    }
}
