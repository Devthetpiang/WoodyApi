package com.xavey.woody.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.Switch;

import com.xavey.woody.R;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.UtilityHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;


public class SettingActivity extends BaseActivity {

    @InjectView(R.id.swZGSt)
    Switch swZGSt;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    String version_name="";
    String version_code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);

       ButterKnife.inject(this);
        getMellPointLocal(this);
       LoadSettingFromPref();
    }

    private void LoadSettingFromPref(){
        swZGSt.setChecked(AppValues.getInstance().getZawGyiDisplay());
    }

    @OnCheckedChanged(R.id.swZGSt)
    void onCheckedChanged(Switch thisSwitch, boolean isChecked) {
        UtilityHelper.setZawGyiDisplay(this,isChecked);
        AppValues.getInstance().setZawGyiDisplay(UtilityHelper.getZawGyiDisplay(this));
    }
}
