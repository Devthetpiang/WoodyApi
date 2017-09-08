package com.xavey.woody.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.TypeFaceHelper;
import com.xavey.woody.helper.UtilityHelper;


/**
 * Created by sithu1986 on 27/8/15.
 */
public class OnboardingFragment1 extends Fragment {

    public boolean checkChanged=false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle s) {

        View v = inflater.inflate(
                R.layout.onboarding_screen_1,
                container,
                false
        );

        RadioGroup radioGroup = (RadioGroup) v.findViewById(R.id.myRadioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkChanged=true;
                if (checkedId == R.id.zawMsg2) {
                    UtilityHelper.setZawGyiDisplay(getActivity(), true);
                } else if (checkedId == R.id.uniMsg2) {
                    UtilityHelper.setZawGyiDisplay(getActivity(),false);
                }
                AppValues.getInstance().setZawGyiDisplay(UtilityHelper.getZawGyiDisplay(getActivity()));
            }
        });


        TextView tvTour1Title = (TextView)v.findViewById(R.id.tvTour1Title);
        TextView tvTour1Msg1 = (TextView)v.findViewById(R.id.tvTour1Msg1);
        RadioButton zawMsg2 = (RadioButton)v.findViewById(R.id.zawMsg2);
        RadioButton uniMsg2 = (RadioButton)v.findViewById(R.id.uniMsg2);

        TypeFaceHelper.setM3TypeFace(tvTour1Title, getActivity());
        TypeFaceHelper.setM3TypeFace(tvTour1Msg1, getActivity());

        TypeFaceHelper.setTypeFace(true, zawMsg2, getActivity());
        TypeFaceHelper.setTypeFace(false, uniMsg2, getActivity());
        zawMsg2.setText(getResources().getString(R.string.tour_1_msg_2_zg));
        uniMsg2.setText(getResources().getString(R.string.tour_1_msg_2));


        if (AppValues.getInstance().getZawGyiDisplay()) {
            tvTour1Title.setText(getResources().getString(R.string.tour_1_title_zg));
            tvTour1Msg1.setText(getResources().getString(R.string.tour_1_msg_1_zg));
        } else {
            tvTour1Title.setText(getResources().getString(R.string.tour_1_title));
            tvTour1Msg1.setText(getResources().getString(R.string.tour_1_msg_1));
        }

        return v;

    }
}
