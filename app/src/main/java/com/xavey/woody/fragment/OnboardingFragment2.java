package com.xavey.woody.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.TypeFaceHelper;

/**
 * Created by sithu1986 on 27/8/15.
 */
public class OnboardingFragment2 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle s) {

        View v = inflater.inflate(
                R.layout.onboarding_screen_2,
                container,
                false
        );

        TextView tvTitle = (TextView)v.findViewById(R.id.tvTour2Title);
        TextView tvMsg = (TextView)v.findViewById(R.id.tvTour2Msg);

        TypeFaceHelper.setM3TypeFace(tvTitle, getActivity());
        TypeFaceHelper.setM3TypeFace(tvMsg, getActivity());
        if (AppValues.getInstance().getZawGyiDisplay()) {
            tvTitle.setText(getResources().getString(R.string.tour_2_title_zg));
            tvMsg.setText(getResources().getString(R.string.tour_2_msg_zg));
        } else {
            tvTitle.setText(getResources().getString(R.string.tour_2_title));
            tvMsg.setText(getResources().getString(R.string.tour_2_msg));
        }
        return v;
    }
}
