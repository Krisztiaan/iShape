package hu.artklikk.android.deloitte.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import hu.artklikk.android.deloitte.R;
import hu.artklikk.android.deloitte.util.Util;

public class RuleFragment extends Fragment {

    private WebView webView;

    private static String TITLE = "title";
    private static String DESC = "desc";

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RuleFragment() {
    }

    public static RuleFragment newInstance(String title, String desc) {
        Bundle args = new Bundle();
        args.putSerializable(TITLE, title);
        args.putSerializable(DESC, desc);
        RuleFragment fragment = new RuleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rule, null);

        Button title = (Button) view.findViewById(R.id.titleButton);
        title.setText(getArguments().getString(TITLE));
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        webView = (WebView) view.findViewById(android.R.id.content);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL("", Util.generateHTMLContent(getArguments().getString(DESC)), "text/html", "UTF-8", "");

        return view;
    }
}
