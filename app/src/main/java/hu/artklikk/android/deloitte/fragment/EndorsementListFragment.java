package hu.artklikk.android.deloitte.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.adapter.EndorsementListAdapter;
import hu.artklikk.android.deloitte.model.Endorsement;
import hu.artklikk.android.deloitte.model.User;

/**
 * A fragment representing a list of Items.
 */
public class EndorsementListFragment extends DialogFragment {

    private static final String LIST_ENDORSEMENT_DATA = "endorsementData";

    private static final String MAP_USER_DATA = "userData";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EndorsementListFragment() {
    }

    public static EndorsementListFragment newInstance(List<Endorsement> endorsementList, Map<Integer, User> userMap) {

        Bundle args = new Bundle();
        args.putSerializable(LIST_ENDORSEMENT_DATA,(ArrayList<Endorsement>) endorsementList);
        args.putSerializable(MAP_USER_DATA, (HashMap<Integer, User>) userMap);

        EndorsementListFragment fragment = new EndorsementListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_endorsement_list, null);
        ArrayList<Endorsement> list = (ArrayList<Endorsement>) getArguments().getSerializable(LIST_ENDORSEMENT_DATA);
        HashMap<Integer, User> userMap = (HashMap<Integer, User>) getArguments().getSerializable(MAP_USER_DATA);

        view.findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ListView listView = (ListView) view.findViewById(android.R.id.list);

        listView.setAdapter(new EndorsementListAdapter(list, userMap, getActivity()));

        return view;
    }
}
