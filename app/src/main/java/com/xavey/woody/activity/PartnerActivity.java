package com.xavey.woody.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.adapter.PartnerAdapter;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.User;
import com.xavey.woody.api.model.Users;
import com.xavey.woody.helper.TokenHelper;

import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tinmaungaye on 30/8/15.
 */
public class PartnerActivity extends BaseActivity {

    @InjectView(R.id.gvPartner)
    GridView gvPartner;

    @InjectView(R.id.tVMessagePartner)
    TextView tVMessagePartner;

    @InjectView(R.id.llMessagePartner)
    LinearLayout llMessagePartner;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    String AuthToken="";
    private PartnerAdapter partnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);

        try {
            AuthToken= TokenHelper.genAPIToken(this, PartnerActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(AuthToken!="") {
            ButterKnife.inject(this);
            getMellPoint(this,AuthToken);
            llMessagePartner.setLayoutParams(new FrameLayout.LayoutParams(0, 0));
            loadPartners();
        }
    }

    private void loadPartners(){
        //gvPartner.removeAllViews();
        SampleClient.getWoodyApiClient(this).getUserPartners(this.AuthToken, getResources().getString(R.string.data_users_business), new Callback<Users>() {
            @Override
            public void success(Users users, Response response) {
                if (users != null && users.getTotal() > 0) {
                    Collections.sort(users.getUsers());
                    partnerAdapter = new PartnerAdapter(PartnerActivity.this, R.layout.partner_items, users.getUsers(), AuthToken);
                    gvPartner.setAdapter(partnerAdapter);
                } else {
                    displayMessage(getResources().getString(R.string.api_result_notfound), getResources().getColor(R.color.grey_500));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                    displayMessage(getResources().getString(R.string.api_error_offline_message), getResources().getColor(R.color.red_500));
                } else {
                    displayMessage(getResources().getString(R.string.api_result_servererror), getResources().getColor(R.color.red_500));
                }
                Log.d("API_Profile", error.toString());
            }
        });
    }

    @OnItemClick(R.id.gvPartner)
    public void gvItemClick(AdapterView<?> parent, View v, int position, long id) {
        User item = (User) parent.getItemAtPosition(position);
        //Create intent
        Intent intent = new Intent(PartnerActivity.this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.EXTRA_ITEM, item.get_id());

        //Start details activity
        startActivity(intent);
    }

    public void displayMessage(String text,int color){
        tVMessagePartner.setText(text);
        tVMessagePartner.setTextColor(color);
        llMessagePartner.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT));
    }
}
