package com.xavey.woody.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xavey.woody.R;
import com.xavey.woody.adapter.ReferralAdapter;
import com.xavey.woody.api.model.UserReferral;

import java.util.ArrayList;

/**
 * Created by tinmaungaye on 8/23/15.
 */
public class ReferralFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_LIST = "ARG_LIST";
    public static final String ARG_API = "ARG_API";

    private static int mPage;
    private static String mAPI;
    private static ArrayList<UserReferral> mInternalContacts;
    ReferralAdapter contactAdapter;

    public static ReferralFragment newInstance(int page, ArrayList<UserReferral> ics, String api) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_API,api);
        args.putSerializable(ARG_LIST, ics);
        ReferralFragment fragment = new ReferralFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPage = getArguments().getInt(ARG_PAGE);
        mAPI = getArguments().getString(ARG_API);
        mInternalContacts = (ArrayList<UserReferral>)getArguments().getSerializable(ARG_LIST);
        View view = inflater.inflate(R.layout.profile_list_holder, container, false);
        final LinearLayout ll = (LinearLayout)view.findViewById(R.id.llListHolderPr);
        displayContact(ll);
//        TextView textView = (TextView) view;
//        textView.setText("Fragment #" + mPage);
        return view;
    }

    private void displayContact(final LinearLayout ll){
        if (mInternalContacts != null && mInternalContacts.size() > 0) {
            ListView qList = new ListView(getActivity());
            qList.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            contactAdapter = new ReferralAdapter(mInternalContacts, getActivity(),mAPI);
            qList.setAdapter(contactAdapter);
            ll.addView(qList);
        } else {
            //showAskButtion();
        }
    }
}
