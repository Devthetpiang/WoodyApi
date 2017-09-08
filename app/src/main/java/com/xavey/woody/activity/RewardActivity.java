package com.xavey.woody.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.xavey.woody.R;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.API_Response;
import com.xavey.woody.api.model.RelatedStat;
import com.xavey.woody.api.model.Reward;
import com.xavey.woody.api.model.Rewards;
import com.xavey.woody.api.model.User;
import com.xavey.woody.api.model.UserLike;
import com.xavey.woody.api.model.UserRelated;
import com.xavey.woody.fragment.BottomSheet;
import com.xavey.woody.fragment.ProfileListFragment;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.CircleTransform;
import com.xavey.woody.helper.PicassoHelper;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TokenHelper;
import com.xavey.woody.helper.TypeFaceHelper;
import com.xavey.woody.helper.UtilityHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class RewardActivity extends BaseActivity {
    public static final String EXTRA_ITEM = "RewardActivity.EXTRA_ITEM";
    public static final String EXTRA_KILL_CACHE = "RewardActivity.EXTRA_KILL_CACHE";

    @InjectView(R.id.iVRewardPr)
    ImageView iVRewardPr;

    @InjectView(R.id.btnEndroll)
    Button btnEndroll;

    @InjectView(R.id.tvRewardTitle)
            TextView tvRewardTitle;

    @InjectView(R.id.tvErrorMessage)
    TextView tvErrorMessage;

    String AuthToken="";

    private Picasso mPicasso;
    private Reward thisReward;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reward);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
        try {
            UtilityHelper.hideSoftKeyboard(this);
            AuthToken=TokenHelper.genAPIToken(this,RewardActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(AuthToken!="") {
            ButterKnife.inject(this);
            getMellPoint(this, AuthToken);
            loadReward();
            getNotification(this, AuthToken);
        }
    }

    private void loadReward(){
        SampleClient.getWoodyApiClient(this).getRewards(this.AuthToken, new Callback<Rewards>() {
            @Override
            public void success(Rewards result, Response response) {
                if (result != null) {
                    int count = result.getRewards() != null ? result.getRewards().size() : 0;

                    if (count > 0) {
                        thisReward = result.getRewards().get(0);
                        tvRewardTitle.setText(result.getRewards().get(0).getDesc());
                        btnEndroll.setText("Enroll [MP:"+ String.valueOf(thisReward.getMpoint())+"]");
                        if (Arrays.asList(result.getEnrolled()).contains(thisReward.get_id())){
                            btnEndroll.setText("Enrolled");
                            btnEndroll.setEnabled(false);
                        }
                        else if(UtilityHelper.getPCompleted(RewardActivity.this)) {
                            btnEndroll.setEnabled(false);
                            tvErrorMessage.setText("Please complete your profile to enroll this reward. Edit here.");
                        }

                            if (thisReward.getPicture() != null) {
                            String picPath = RewardActivity.this.getString(R.string.api_endpoint) + RewardActivity.this.getString(R.string.api_endpoint_reward) + thisReward.getPicture();

                            mPicasso = PicassoHelper.getInstance(RewardActivity.this, AuthToken).getPicasso();
                            if (getIntent().getBooleanExtra(EXTRA_KILL_CACHE, false) != false) {
                                mPicasso.invalidate(picPath);
                            }
                            mPicasso.load(picPath)
                                    .error(android.R.drawable.stat_notify_error)
                                    .placeholder(android.R.drawable.stat_notify_sync)
                                    .into(iVRewardPr);
                        }

                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                    Toast bread = Toast.makeText(getApplicationContext(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                    bread.show();
                }
                Log.d("API_Profile", error.toString());
            }
        });
    }

    @OnClick(R.id.tvErrorMessage)
    public void editProfile(){
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iVRewardPr)
    public void enlargePhoto(){
        if(thisReward != null) {
            Dialog d = new Dialog(RewardActivity.this);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.dialog_reward_image);// custom layour for dialog.
            ImageView iv = (ImageView) d.findViewById(R.id.iVProfileEnlarge);

            if(thisReward.getPicture()!=null){
                mPicasso = PicassoHelper.getInstance(RewardActivity.this, AuthToken).getPicasso();
                mPicasso.load(RewardActivity.this.getString(R.string.api_endpoint) +RewardActivity.this.getString(R.string.api_endpoint_reward)+thisReward.getPicture())
                        .error(android.R.drawable.stat_notify_error)
                        .placeholder(android.R.drawable.stat_notify_sync)
                        .into(iv);
            }

           d.show();
        }
    }

    @OnClick(R.id.btnEndroll)
    public void enroll(){
        SampleClient.getWoodyApiClient(this).postRewardEnroll(this.AuthToken, thisReward, new Callback<API_Response>() {
            @Override
            public void success(API_Response api_response, Response response) {
                if (api_response.getMessage().equals("010")) {
                    btnEndroll.setText("Enrolled");
                    btnEndroll.setEnabled(false);
                }
                if (api_response.getMessage().equals("002")) {
                    Toast bread = Toast.makeText(getApplicationContext(), R.string.api_enroll_notenough_message, Toast.LENGTH_SHORT);
                    bread.show();
                }
                else {
                    Toast bread = Toast.makeText(getApplicationContext(), R.string.api_error_generic_message, Toast.LENGTH_SHORT);
                    bread.show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("API_Result", error.toString());
                if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                    Toast bread = Toast.makeText(getApplicationContext(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                    bread.show();
                }
            }
        });
    }
}
