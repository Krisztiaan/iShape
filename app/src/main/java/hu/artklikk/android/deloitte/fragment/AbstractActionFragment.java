package hu.artklikk.android.deloitte.fragment;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Arrays;
import java.util.List;

import hu.artklikk.android.deloitte.model.Endorsement;

/**
 * Created by gyozofule on 15. 11. 24..
 */
public abstract class AbstractActionFragment extends ListFragment {

    private BroadcastReceiver listReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] array = (Object[])intent.getSerializableExtra(ActionsFragment.ENDORSEMENT);
            Endorsement[] enArray = Arrays.copyOf(array, array.length, Endorsement[].class);
            handleResponse(Arrays.asList((enArray)));
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

    abstract void handleResponse(List<Endorsement> endorsementList);
}
