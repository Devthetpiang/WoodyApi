package com.xavey.woody.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.xavey.woody.R;
import com.xavey.woody.adapter.ContactAdapter;
import com.xavey.woody.api.model.InternalContact;

import java.util.ArrayList;

/**
 * Created by tinmaungaye on 8/23/15.
 */
public class InviteFragment extends Fragment{
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_LIST = "ARG_LIST";

    private static int mPage;
    private static ArrayList<InternalContact> mInternalContacts;
    ContactAdapter contactAdapter;

    public static InviteFragment newInstance(int page, ArrayList<InternalContact> ics) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(ARG_LIST, ics);
        InviteFragment fragment = new InviteFragment();
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
        mInternalContacts = (ArrayList<InternalContact>)getArguments().getSerializable(ARG_LIST);
        View view = inflater.inflate(R.layout.profile_list_holder, container, false);
        final LinearLayout ll = (LinearLayout)view.findViewById(R.id.llListHolderPr);
        displayContact(ll);
        return view;
    }

    private void displayContact(final LinearLayout ll){
        if (mInternalContacts != null && mInternalContacts.size() > 0) {
            ListView qList = new ListView(getActivity());
            qList.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            contactAdapter = new ContactAdapter(mInternalContacts, getActivity(), getActivity());
            qList.setAdapter(contactAdapter);
            ll.addView(qList);
        } else {
            //showAskButtion();
        }
    }
}
