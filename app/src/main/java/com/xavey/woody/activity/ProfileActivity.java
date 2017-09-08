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

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.squareup.picasso.Picasso;
import com.xavey.woody.R;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.API_Response;
import com.xavey.woody.api.model.RelatedStat;
import com.xavey.woody.api.model.User;
import com.xavey.woody.api.model.UserLike;
import com.xavey.woody.api.model.UserRelated;
import com.xavey.woody.fragment.ProfileListFragment;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.CircleTransform;
import com.xavey.woody.helper.PicassoHelper;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TokenHelper;
import com.xavey.woody.helper.TypeFaceHelper;
import com.xavey.woody.helper.UtilityHelper;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ProfileActivity extends BaseActivity {
    public static final String EXTRA_ITEM = "ProfileActivity.EXTRA_ITEM";
    public static final String EXTRA_KILL_CACHE = "ProfileActivity.EXTRA_KILL_CACHE";

    @InjectView(R.id.tvFullName)
    TextView tvFullName;

    @InjectView(R.id.tvUserName)
    TextView tvUserName;

    @InjectView(R.id.tvJoined)
    TextView tvJoined;

    @InjectView(R.id.tvPoint)
    TextView tvPoint;

    @InjectView(R.id.bLikePr)
    Button bLikePr;

    @InjectView(R.id.iVProfilePr)
    ImageView iVProfilePr;

    @InjectView(R.id.iVEdit)
    Button iVEdit;

    @InjectView(R.id.vpProfileList)
    ViewPager vpProfileList;

    @InjectView(R.id.tlProfileList)
    TabLayout tlProfileList;

    @InjectView(R.id.tvRedeem)
            TextView tvRedeem;

    String AuthToken="";

    private Picasso mPicasso;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private final String follower_page="FOLLOWER";
    private final String following_page="FOLLOWING";
    private final String favourite_page="FAVOURITE";
    private final String questions_page="QUESTION";

    private String UserID ="";
    private String requestID ="me";
    private int interactive_follower=0;
    private User thisUser = null;
    private Map<String,String> related_stat = new HashMap<String,String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
        if(getIntent().getStringExtra(EXTRA_ITEM)!=null){
            requestID= getIntent().getStringExtra(EXTRA_ITEM);
        }
        try {
            UtilityHelper.hideSoftKeyboard(this);
            AuthToken=TokenHelper.genAPIToken(this,ProfileActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(AuthToken!="") {
            ButterKnife.inject(this);
            getMellPoint(this, AuthToken);
            loadProfile();
            TypeFaceHelper.setM3TypeFace(tvRedeem, this);
            if (AppValues.getInstance().getZawGyiDisplay()) {
                tvRedeem.setText(Rabbit.uni2zg(tvRedeem.getText().toString()));
            } else {
                tvRedeem.setText(tvRedeem.getText().toString());
            }

            iVEdit.setVisibility(View.INVISIBLE);

            getNotification(this,AuthToken);
        }
    }


    private void loadProfile(){
        SampleClient.getWoodyApiClient(this).getUserRelatedStat(this.AuthToken, requestID, new Callback<UserRelated>() {
            @Override
            public void success(UserRelated userRelated, Response response) {
                if (userRelated != null) {
                    User user = userRelated.getUser();
                    thisUser = user;
                    if (AppValues.getInstance().getZawGyiDisplay()) {
                        tvFullName.setText(Rabbit.uni2zg(user.getFull_name()));
                    } else {
                        tvFullName.setText(user.getFull_name());
                    }
                    TypeFaceHelper.setM3TypeFace(tvFullName, ProfileActivity.this);
                    tvUserName.setText("@" + user.getUser_name());

                    if (user.getGender() != null && user.getGender() != "" && user.getGender() != "NA") {
                        tvJoined.setText(user.getGender() + ", joined " + UtilityHelper.relativeTimespan(user.getCreated_on()) + "!");
                    } else {
                        tvJoined.setText("Gender? Don't know, joined " + UtilityHelper.relativeTimespan(user.getCreated_on()) + "!");
                    }
                    UserID = user.get_id();

                    int tabCount = 4;
                    if (thisUser.getType() == 100 || thisUser.getType() == 110) {
                        tabCount = 5;
                    }
                    if (user.getPicture() != null) {
                        String picPath = ProfileActivity.this.getString(R.string.api_endpoint) + ProfileActivity.this.getString(R.string.api_endpoint_profile) + user.getPicture();

                        mPicasso = PicassoHelper.getInstance(ProfileActivity.this, AuthToken).getPicasso();
                        if (getIntent().getBooleanExtra(EXTRA_KILL_CACHE, false) != false) {
                            mPicasso.invalidate(picPath);
                        }
                        mPicasso.load(picPath)
                                .error(android.R.drawable.stat_notify_error)
                                .placeholder(android.R.drawable.stat_notify_sync)
                                .transform(new CircleTransform()).into(iVProfilePr);
                    } else {
                        if (user.getGender() != null && user.getGender().length() > 0) {
                            switch (user.getGender().toLowerCase()) {
                                case "male":
                                    iVProfilePr.setImageResource(R.drawable.ic_profile_mal);
                                    break;
                                case "female":
                                    iVProfilePr.setImageResource(R.drawable.ic_profile_fem);
                                    break;
                            }
                        }
                    }

                    int TotalPoint = 0;
                    for (RelatedStat rs : userRelated.getRelatedStat()) {
                        switch (rs.getName().toLowerCase()) {
                            case "basic_profile":
                                if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                    TotalPoint += Integer.parseInt(rs.getPoint());
                                }
                                break;
                            case "question_set":
                                related_stat.put(rs.getName().toLowerCase(), String.valueOf(rs.getValue()));
                            case "question":
                                related_stat.put(rs.getName().toLowerCase(), String.valueOf(rs.getValue()));
                                if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                    TotalPoint += Integer.parseInt(rs.getPoint());
                                }
                                break;
                            case "follower":
                                related_stat.put(rs.getName().toLowerCase(), String.valueOf(rs.getValue()));
                                if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                    TotalPoint += Integer.parseInt(rs.getPoint());
                                }
                                break;
                            case "following":
                                related_stat.put(rs.getName().toLowerCase(), String.valueOf(rs.getValue()));
                                break;
                            case "favourite":
                                related_stat.put(rs.getName().toLowerCase(), String.valueOf(rs.getValue()));
                                if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                    TotalPoint += Integer.parseInt(rs.getPoint());
                                }
                                break;
                            case "user_like":
                                if (rs.getValue() == null) {
                                    //havn't like yet
                                    inActiveFollowButton();
                                } else if (rs.getValue().toLowerCase().equals("self")) {
                                    //self profile don't show follow button
                                    bLikePr.setVisibility(View.INVISIBLE);
                                    bLikePr.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
                                    iVEdit.setVisibility(View.VISIBLE);
                                    tvRedeem.setVisibility(View.VISIBLE);
                                } else if (rs.getValue() != null) {
                                    activeFollowButton(rs.getValue());
                                }
                                break;
                            case "comment":
                                if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                    TotalPoint += Integer.parseInt(rs.getPoint());
                                }
                                break;
                            case "voted":
                                if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                    TotalPoint += Integer.parseInt(rs.getPoint());
                                }
                                break;
                            case "vote_set":
                                if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                    TotalPoint += Integer.parseInt(rs.getPoint());
                                }
                                break;
                            case "referral":
                                if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                    TotalPoint += Integer.parseInt(rs.getPoint());
                                }
                                break;
                            case "invite_counter":
                                if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                    TotalPoint += Integer.parseInt(rs.getPoint());
                                }
                                break;
                            case "reward_enroll":
                                if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                    TotalPoint -= Integer.parseInt(rs.getPoint());
                                }
                                break;
                            case "other":
                                if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                    TotalPoint += Integer.parseInt(rs.getPoint());
                                }
                                break;
                        }
                    }//end of stat loop
                    tvPoint.setText("MP : " + String.valueOf(TotalPoint));

                    vpProfileList.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                            ProfileActivity.this, tabCount, related_stat, AuthToken, UserID));

                    tlProfileList.setupWithViewPager(vpProfileList);
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

    @OnClick(R.id.iVEdit)
    public void editClicked(){
        Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tvRedeem)
    public void redeemClicked(){
        Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iVProfilePr)
    public void enlargePhoto(){
        if(thisUser != null) {
            Dialog d = new Dialog(ProfileActivity.this);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.dialog_profile_image);// custom layour for dialog.
            ImageView iv = (ImageView) d.findViewById(R.id.iVProfileEnlarge);

            if(thisUser.getPicture()!=null){
                mPicasso = PicassoHelper.getInstance(ProfileActivity.this, AuthToken).getPicasso();
                mPicasso.load(ProfileActivity.this.getString(R.string.api_endpoint) +ProfileActivity.this.getString(R.string.api_endpoint_profile)+thisUser.getPicture())
                        .error(android.R.drawable.stat_notify_error)
                        .placeholder(android.R.drawable.stat_notify_sync)
                        .into(iv);
            }
            else if(thisUser.getGender()!=null) {
                switch (thisUser.getGender().toLowerCase()) {
                    case "male":
                        iVProfilePr.setImageResource(R.drawable.ic_profile_mal);
                        break;
                    case "female":
                        iVProfilePr.setImageResource(R.drawable.ic_profile_fem);
                        break;
                }
            }

           d.show();
        }
    }

    /*

    @OnClick(R.id.rLQuestionListPr)
    public void loadQuestions() {
        loadUserQuestion();
    }

    @OnClick(R.id.rLFavouriteL
    istPr)



    @OnClick(R.id.rLFollowerListPr)


    @OnClick(R.id.rLFollowingListPr)
*/

    @OnClick(R.id.bLikePr)
    public void LikeClicked() {
        bLikePr.setEnabled(false);
        if(bLikePr.getTag()==null){
            //havnt liked yet
            UserLike l = new UserLike();
            User ur = new User();
            ur.set_id(UserID);
            l.setUser(ur);
            l.setLiked_by(null);
            l.setLiked_on(null);
            SampleClient.getWoodyApiClient(this).postAUserLike(this.AuthToken, UserID,l,new Callback<API_Response>() {
                @Override
                public void success(API_Response api_response, Response response) {
                    if(api_response.getMessage().equals("010")){
                        activeFollowButton(api_response.getExtra());
                        interactive_follower+=1;
                        bLikePr.setEnabled(true);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("API_Result", error.toString());
                    if(error.getMessage().equals(getResources().getString(R.string.api_error_offline))){
                        Toast bread = Toast.makeText(getApplicationContext(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                        bread.show();
                    }
                    bLikePr.setEnabled(true);
                }
            });
        }
        else{
            SampleClient.getWoodyApiClient(this).deleteAUserLike(this.AuthToken, UserID, (String) bLikePr.getTag(), new Callback<API_Response>() {
                @Override
                public void success(API_Response api_response, Response response) {
                    if (api_response.getMessage().equals("010")) {
                        inActiveFollowButton();
                        interactive_follower -= 1;
                        bLikePr.setEnabled(true);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("API_Result", error.toString());
                    if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                        Toast bread = Toast.makeText(getApplicationContext(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                        bread.show();
                    }
                    bLikePr.setEnabled(true);
                }
            });
        }
    }
    /*

    @OnClick(R.id.ivPostsPr)
    public void PostsList(){
        Intent intent = new Intent(this,AnswerSetActivity.class);
        startActivity(intent);
//        SampleClient.getWoodyApiClient(this).getNewPostsList(this.AuthToken,"yes","noskip",new Callback<API_Response>(){
//
//            @Override
//            public void success(API_Response api_response, Response response) {
//
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//
//            }
//        });

    }

     private void setActiveIcon(String page){
        //change all to default
        ivFavouritePr.setImageResource(R.drawable.ic_favs);
        ivFollowerPr.setImageResource(R.drawable.ic_followers);
        ivFollowingPr.setImageResource(R.drawable.ic_followings);
        ivQuestionPr.setImageResource(R.drawable.ic_questions);

        if(page == favourite_page){
            ivFavouritePr.setImageResource(R.drawable.ic_favs_active);
        }
        else if(page==follower_page){
            ivFollowerPr.setImageResource(R.drawable.ic_followers_active);
        }
        else if(page==following_page){
            ivFollowingPr.setImageResource(R.drawable.ic_followings_active);
        }
        else if(page==questions_page){
            ivQuestionPr.setImageResource(R.drawable.ic_questions_active);
        }
    }

    */

    private void showAskButtion(){
        if(requestID.equals("me")) {
            final Button button = new Button(ProfileActivity.this);
            button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            button.setText(getResources().getString(R.string.title_page_ask));
            button.setBackgroundColor(getResources().getColor(R.color.blue_500));
            //llListHolderPr.addView(button);
            button.setOnClickListener(
                    new Button.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            Intent intent = new Intent(ProfileActivity.this, AskActivity.class);
                            startActivity(intent);
                        }
                    });
        }
    }

    private void activeFollowButton(String tag){
//        bLikePr.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_did, 0, 0, 0);
        bLikePr.setText(getResources().getString(R.string.ic_following));
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            bLikePr.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_button_active));
        }
        else{
            bLikePr.setBackground(getResources().getDrawable(R.drawable.background_button_active));
        }
        bLikePr.setTag(tag);
    }

    private void inActiveFollowButton(){
//        bLikePr.setCompoundDrawablesWithIntrinsicBounds( R.drawable.ic_do, 0, 0, 0);
        bLikePr.setText(getResources().getString(R.string.ic_follow));

        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            bLikePr.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_button_inactive));
        }
        else {
            bLikePr.setBackground(getResources().getDrawable(R.drawable.background_button_inactive));
        }
        bLikePr.setTag(null);
    }

    public void OnCheckChanged(){

    }

     public class SampleFragmentPagerAdapter extends FragmentPagerAdapter {
         private int PAGE_COUNT = 0;
         private Map<String,String> related_count = new HashMap<String,String>();
         private Context context;
         int[] imageResId= AppValues.LAYOUT_PROFILE_LIST_IC_NORMAL;
         String[] pageDisplay = AppValues.LAYOUT_PROFILE_LIST_NORMAL;
         String apiKey ="";
         String userId ="";

        public SampleFragmentPagerAdapter(FragmentManager fm, Context context, int tabCount, Map<String,String> related_stat,String api, String user) {
            super(fm);
            this.context = context;
            PAGE_COUNT=tabCount;
            related_count=related_stat;
            apiKey = api;
            userId = user;

            if(PAGE_COUNT==AppValues.LAYOUT_PROFILE_LIST_IC_PREMIUM.length){
                imageResId=AppValues.LAYOUT_PROFILE_LIST_IC_PREMIUM;
                pageDisplay = AppValues.LAYOUT_PROFILE_LIST_PREMIUM;
            }
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            return ProfileListFragment.newInstance(position,PAGE_COUNT,apiKey,userId);
        }

        /*@Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }*/



// ...

         @Override
         public CharSequence getPageTitle(int position) {
             // Generate title based on item position
             // return tabTitles[position];
             Drawable image = context.getResources().getDrawable(imageResId[position]);
             image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
             SpannableString sb = new SpannableString("  " + related_count.get(pageDisplay[position]));
             ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
             sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
             return sb;
         }
    }
}
