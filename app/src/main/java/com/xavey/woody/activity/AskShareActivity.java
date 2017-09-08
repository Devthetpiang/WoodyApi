package com.xavey.woody.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.widget.ImageView;

import com.xavey.woody.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AskShareActivity extends BaseActivity {
    final String LOG_TAG = "myLogs";

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    public static final String EXTRA_POST = "AskShareActivity.EXTRA_POST";
    private String mPostID="";
    @InjectView(R.id.ivImageAShr)
    ImageView ivImageAShr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_share);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
        mPostID= getIntent().getStringExtra(EXTRA_POST);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.ivImageAShr)
    public void OnShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,getResources().getString(R.string.app_share_msg)+"  "+getResources().getString(R.string.app_share_link)+mPostID);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share via"));
    }

    @OnClick(R.id.btnOkAShr)
    public void DoneOK() {
        // TODO submit data to server...
        finish();
        Intent intent = new Intent(AskShareActivity.this, AnswerActivity.class);
        startActivity(intent);


    }
}
