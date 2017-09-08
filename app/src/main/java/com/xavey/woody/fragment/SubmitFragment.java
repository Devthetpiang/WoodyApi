package com.xavey.woody.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.api.model.PostSetHolder;
import com.xavey.woody.helper.TypeFaceHelper;


/**
 * Created by tinmaungaye on 19/8/15.
 */
public class SubmitFragment extends Fragment {
    // Store instance variables
    private int page;
    private PostSetHolder psh;

    // newInstance constructor for creating fragment with arguments
    public static SubmitFragment newInstance(int page, PostSetHolder _psh) {
        SubmitFragment fragmentFirst = new SubmitFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putSerializable("someMsg", _psh);

        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        psh = (PostSetHolder)getArguments().getSerializable("someMsg");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (OnPostSetVottedCallback) activity;
        }
        catch(ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ScrollView scroller = new ScrollView(getActivity());
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        final TextView text = new TextView(getActivity());

        int padding = (int) getActivity().getResources().getDimension(R.dimen.control_far_margin);
        text.setPadding(padding, padding, padding, padding);
        text.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        final Button button = new Button(getActivity());
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setText("Submit");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButton(true, button);
                setMessage(text,button, callback.OnPostSetVotted());
            }
        });
        scroller.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setMessage(text,button,psh.getMessage());
        setButton(false,button);
        linearLayout.addView(text);
        linearLayout.addView(button);

        scroller.addView(linearLayout);

        return scroller;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }


    private OnPostSetVottedCallback callback;

    public interface OnPostSetVottedCallback {
        String OnPostSetVotted();
    }

    private void setMessage(TextView text, Button button,String message){
        text.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f,  getResources().getDisplayMetrics()), 1.0f);
        if(message!=null && message.equals(getActivity().getResources().getString(R.string.message_vote_submitted))){
            text.setTextColor(getActivity().getResources().getColor(R.color.green_500));
        }
        else{
            text.setTextColor(getActivity().getResources().getColor(R.color.red_500));
            setButton(false,button);
        }
        text.setText(message);
        TypeFaceHelper.setM3TypeFace(text, getActivity());
    }

    private void setButton(Boolean loading, Button button){
        TypeFaceHelper.setM3TypeFace(button,getActivity());
        button.setEnabled(!loading);
        if(loading){
            button.setText(getActivity().getResources().getString(R.string.button_label_loading));
        }
        else{
            button.setText(getActivity().getResources().getString(R.string.button_label_submit));
        }

    }
}
