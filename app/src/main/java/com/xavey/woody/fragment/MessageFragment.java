package com.xavey.woody.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TypeFaceHelper;


/**
 * Created by tinmaungaye on 19/8/15.
 */
public class MessageFragment extends Fragment {
    // Store instance variables
    private String title;
    private String detail;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static MessageFragment newInstance(int page, String title, String Detail) {
        MessageFragment fragmentFirst = new MessageFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        args.putString("someDetail", Detail);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
        detail = getArguments().getString("someDetail");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //ScrollView scroller = new ScrollView(getActivity());
        LinearLayout linear = new LinearLayout(getActivity());
        linear.setOrientation(LinearLayout.VERTICAL);
        TextView tvTitle = new TextView(getActivity());
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, getActivity()
                        .getResources().getDisplayMetrics());
        tvTitle.setPadding(padding, padding, padding, 20);
        tvTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TypeFaceHelper.setM3TypeFace(tvTitle, getActivity());
        if (AppValues.getInstance().getZawGyiDisplay()) {
            tvTitle.setText(Rabbit.uni2zg(title));
        } else {
            tvTitle.setText(title);
        }

        linear.addView(tvTitle);

        TextView tvDetail = new TextView(getActivity());
        tvDetail.setPadding(padding, 10, padding, padding);
        tvDetail.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvDetail.setLineSpacing(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f,  getResources().getDisplayMetrics()), 1.0f);
        TypeFaceHelper.setM3TypeFace(tvDetail, getActivity());
        if (AppValues.getInstance().getZawGyiDisplay()) {
            tvDetail.setText(Rabbit.uni2zg(detail));
        } else {
            tvDetail.setText(detail);
        }
        linear.addView(tvDetail);

        return linear;
    }

    public int getShownIndex() {
        return getArguments().getInt("index", 0);
    }
}
