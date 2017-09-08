package com.xavey.woody.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TypeFaceHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class AboutActivity extends BaseActivity {

    @InjectView(R.id.tvVersionAb)
    TextView tvVersionAb;
    @InjectView(R.id.tvTextAb)
    TextView tvTextAb;
    @InjectView(R.id.tvContactAb)
    TextView tvContactAb;

    private String[] navMenuTitles;0

    private TypedArray navMenuIcons;

    String version_name="";
    String version_code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);

        ButterKnife.inject(this);
        getMellPointLocal(this);

        TypeFaceHelper.setM3TypeFace(tvTextAb, this);
        TypeFaceHelper.setM3TypeFace(tvContactAb, this);

        if (!AppValues.getInstance().getZawGyiDisplay()) {
            tvTextAb.setText(getResources().getString(R.string.about_text));
            tvContactAb.setText(getResources().getString(R.string.about_contact));
        }
        else {
            tvTextAb.setText(Rabbit.uni2zg(getResources().getString(R.string.about_text)));
            tvContactAb.setText(Rabbit.uni2zg(getResources().getString(R.string.about_contact)));
        }

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version_name = pInfo.versionName;
            version_code = String.valueOf(pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvVersionAb.setText("Version: " + version_name);
    }

    @OnClick(R.id.tvPP)
    public void OnClickPP(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_legal_link_pp)));
        this.startActivity(browserIntent);
    }

    @OnClick(R.id.tvTOC)
    public void OnClickTOC(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_legal_link_toc)));
        this.startActivity(browserIntent);
    }
}
