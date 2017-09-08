package com.xavey.woody.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.xavey.woody.BuildConfig;
import com.xavey.woody.R;
import com.xavey.woody.fragment.OnboardingFragment1;
import com.xavey.woody.fragment.OnboardingFragment2;
import com.xavey.woody.fragment.OnboardingFragment3;
import com.xavey.woody.fragment.OnboardingFragment4;
import com.xavey.woody.fragment.OnboardingFragment5;

//import com.xavey.woody.R;

/**
 * Created by sithu1986 on 27/8/15.
 */
public class OnboardingActivity extends FragmentActivity {

    private ViewPager pager;
    private SmartTabLayout indicator;
    private Button skip;
    private Button next;

    private int pages = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_onboarding);

        pager = (ViewPager)findViewById(R.id.pager);
        indicator = (SmartTabLayout)findViewById(R.id.indicator);
        skip = (Button)findViewById(R.id.skip);
        next = (Button)findViewById(R.id.next);

        final FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0 : return new OnboardingFragment1();
                    case 1 : return new OnboardingFragment2();
                    case 2 : return new OnboardingFragment3();
                    case 3 : return new OnboardingFragment4();
                    case 4 : return new OnboardingFragment5();
                    default: return null;
                }
            }

            @Override
            public int getCount() {
                return pages;
            }

            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }
        };

        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishOnboarding();
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() == pages - 1) { // The last screen
                    finishOnboarding();
                } else {
                    pager.setCurrentItem(
                            pager.getCurrentItem() + 1,
                            true
                    );
                }
            }
        });

        indicator.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == pages - 1) {
                    skip.setVisibility(View.GONE);
                    next.setText("Done");
                }
                else {
                    skip.setVisibility(View.VISIBLE);
                    next.setText("Next");
                }

                if(position==1){
                    //assumed user did choose z or u on page 0
                    pager.getAdapter().notifyDataSetChanged();
                }
            }
        });

    }

    private void finishOnboarding() {
        // Get the shared preferences
        SharedPreferences preferences =
                getSharedPreferences("woody_onboard_preferences", MODE_PRIVATE);

        // Set onboarding_complete to true
        preferences.edit()
                .putInt("onboarding_complete", BuildConfig.VERSION_CODE).apply();

        // Launch the main Activity, called MainActivity
        Intent main = new Intent(this, MainActivity.class);
        startActivity(main);

        // Close the OnboardingActivity
        finish();
    }
}
