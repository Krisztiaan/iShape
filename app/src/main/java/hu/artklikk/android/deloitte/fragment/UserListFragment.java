package hu.artklikk.android.deloitte.fragment;

import android.app.ListFragment;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.RadioButton;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hu.artklikk.android.deloitte.MainActivity;
import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.adapter.UserListAdapter;
import hu.artklikk.android.deloitte.model.User;
import hu.artklikk.android.deloitte.util.Util;

/**
 * A fragment representing a list of Items.
 */
public class UserListFragment extends ListFragment implements TextWatcher{

    private static final String DATA = "data";

    private boolean rankIsAsc = true;
    private boolean nameIsAsc = true;
    private boolean cycleIsAsc = true;
    private boolean yearIsAsc = true;

    private List userList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserListFragment() {
    }

    public static UserListFragment newInstance(boolean isToplist) {
        UserListFragment fragment = new UserListFragment();
        Bundle args = new Bundle();
        args.putBoolean(DATA, isToplist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_list, null);
        userList = ((MainActivity) getActivity()).userList;

        boolean isToplist = getArguments().getBoolean(DATA, false);

        if (isToplist){
            Collections.sort(userList, new Comparator<User>() {
                @Override
                public int compare(User lhs, User rhs) {
                    return ((Integer)lhs.getCycleRank()).compareTo(rhs.getCycleRank());
                }
            });
        } else {
            Collections.sort(userList, new Comparator<User>() {
                @Override
                public int compare(User lhs, User rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
        }


        UserListAdapter adapter = new UserListAdapter(userList, getActivity(), isToplist);
        setListAdapter(adapter);

        view.findViewById(R.id.rank).setVisibility(isToplist ? View.VISIBLE : View.GONE);

        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.searchText);
        textView.addTextChangedListener(this);

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getWindowToken(), 0);
            }
        });

        ArrayAdapter<String> nameAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Util.convertUserNamesToArray(((MainActivity)getActivity()).userList));
        textView.setAdapter(nameAdapter);

        initOrder(view, isToplist);
        return view;
    }


    //TODO need some refactor
    private void initOrder(final View view, boolean isToplist){
        final RadioButton radioButtonRank = (RadioButton) view.findViewById(R.id.rank);
        final RadioButton radioButtonName = (RadioButton) view.findViewById(R.id.name);
        final RadioButton radioButtonPointInCycle = (RadioButton) view.findViewById(R.id.pointInCycle);
        final RadioButton radioButtonPointInYear = (RadioButton) view.findViewById(R.id.pointInYear);

        radioButtonName.setChecked(!isToplist);

        radioButtonRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rankIsAsc){
                    radioButtonRank.setButtonDrawable(R.drawable.radio_button_down);
                    Collections.sort(userList, new Comparator<User>() {
                        @Override
                        public int compare(User lhs, User rhs) {
                            return ((Integer)lhs.getCycleRank()).compareTo(rhs.getCycleRank());
                        }
                    });
                } else {
                    radioButtonRank.setButtonDrawable(R.drawable.radio_button_up);
                    Collections.sort(userList, new Comparator<User>() {
                        @Override
                        public int compare(User lhs, User rhs) {
                            return ((Integer) rhs.getCycleRank()).compareTo(lhs.getCycleRank());
                        }
                    });
                }
                ((ArrayAdapter)getListAdapter()).notifyDataSetChanged();
                rankIsAsc = !rankIsAsc;
            }
        });

        radioButtonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameIsAsc){
                    radioButtonName.setButtonDrawable(R.drawable.radio_button_down);
                    Collections.sort(userList, new Comparator<User>() {
                        @Override
                        public int compare(User lhs, User rhs) {
                            return lhs.getName().compareTo(rhs.getName());
                        }
                    });
                } else {
                    radioButtonName.setButtonDrawable(R.drawable.radio_button_up);
                    Collections.sort(userList, new Comparator<User>() {
                        @Override
                        public int compare(User lhs, User rhs) {
                            return rhs.getName().compareTo(lhs.getName());
                        }
                    });
                }
                ((ArrayAdapter)getListAdapter()).notifyDataSetChanged();
                nameIsAsc = !nameIsAsc;
            }
        });

        radioButtonPointInCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cycleIsAsc){
                    radioButtonPointInCycle.setButtonDrawable(R.drawable.radio_button_down);
                    Collections.sort(userList, new Comparator<User>() {
                        @Override
                        public int compare(User lhs, User rhs) {
                            return ((Integer) rhs.getPointInCycle()).compareTo(lhs.getPointInCycle());
                        }
                    });
                } else {
                    radioButtonPointInCycle.setButtonDrawable(R.drawable.radio_button_up);
                    Collections.sort(userList, new Comparator<User>() {
                        @Override
                        public int compare(User lhs, User rhs) {
                            return ((Integer) lhs.getPointInCycle()).compareTo(rhs.getPointInCycle());
                        }
                    });
                }
                ((ArrayAdapter)getListAdapter()).notifyDataSetChanged();
                cycleIsAsc = !cycleIsAsc;
            }
        });

        radioButtonPointInYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yearIsAsc){
                    radioButtonPointInYear.setButtonDrawable(R.drawable.radio_button_down);
                    Collections.sort(userList, new Comparator<User>() {
                        @Override
                        public int compare(User lhs, User rhs) {
                            return ((Integer) rhs.getPointInYear()).compareTo(lhs.getPointInYear());
                        }
                    });
                } else {
                    radioButtonPointInYear.setButtonDrawable(R.drawable.radio_button_up);
                    Collections.sort(userList, new Comparator<User>() {
                        @Override
                        public int compare(User lhs, User rhs) {
                            return ((Integer) lhs.getPointInYear()).compareTo(rhs.getPointInYear());
                        }
                    });
                }
                ((ArrayAdapter)getListAdapter()).notifyDataSetChanged();
                yearIsAsc = !yearIsAsc;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        ((UserListAdapter)getListAdapter()).getFilter().filter("");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments().getBoolean(DATA, false)) {
            getListView().setDivider(new ColorDrawable(getResources().getColor(R.color.green)));
        } else {
            getListView().setDivider(new ColorDrawable(getResources().getColor(R.color.dark_blue)));
        }

        getListView().setDividerHeight(4);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        getActivity().getFragmentManager().
                beginTransaction().replace(R.id.container, ProfileFragment.newInstance((int) id)).addToBackStack("3").commit();
        ((MainActivity) getActivity()).onSectionAttached(3);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ((UserListAdapter)getListAdapter()).getFilter().filter(s.toString());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
