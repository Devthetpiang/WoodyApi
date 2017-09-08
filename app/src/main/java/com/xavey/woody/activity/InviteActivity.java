package com.xavey.woody.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.SmsManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.xavey.woody.R;
import com.xavey.woody.adapter.UserAdapter;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.API_Response;
import com.xavey.woody.api.model.Auth;
import com.xavey.woody.api.model.FBUser;
import com.xavey.woody.api.model.InternalContact;
import com.xavey.woody.api.model.UserReferral;
import com.xavey.woody.api.model.UserReferrals;
import com.xavey.woody.fragment.FriendFragment;
import com.xavey.woody.fragment.InviteDialogFragment;
import com.xavey.woody.fragment.InviteFragment;
import com.xavey.woody.fragment.ReferralFragment;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.DBHelper;
import com.xavey.woody.helper.TokenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.facebook.GraphRequest;
import com.xavey.woody.helper.UtilityHelper;

/**
 * Created by tinmaungaye on 5/10/15.
 */
public class InviteActivity extends BaseActivity implements InviteDialogFragment.OnInviteConfirmedCallback{

    String AuthToken="";
    String query="";

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    UserAdapter userAdapter;
    private ArrayList<InternalContact> mInternalContacts;
    private ArrayList<String> mInternalContactNos;

    private static final String SENT = "SMS_SENT";
    private static final String DELIVERED = "SMS_DELIVERED";
    private static final String EXTRA_NUMBER = "number";
    private static final String EXTRA_CONTACTS = "EXTRA_CONTACTS";
    private static final String EXTRA_CONTACTS_NO = "EXTRA_CONTACTS_NO";
    public static final String EXTRA_LINK = "EXTRA_LINK";
    public static final String EXTRA_LINK_ACTION_FB = "EXTRA_LINK_ACTION_FB";

    CallbackManager callbackManager;

    SmsManager smsMgr;
    IntentFilter filter;

    @InjectView(R.id.vpReferralList)
    ViewPager vpReferralList;

    @InjectView(R.id.tlReferralList)
    TabLayout tlReferralList;

    @InjectView(R.id.llInviteView)
    LinearLayout llInviteView;

    @InjectView(R.id.contact_progress)
    ProgressBar contact_progress;

    @InjectView(R.id.fb_login_button)
    LoginButton fb_login_button;

    FBUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_invite);
        smsMgr = SmsManager.getDefault();
        filter = new IntentFilter(SENT);
        filter.addAction(DELIVERED);

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);

        try {
            AuthToken= TokenHelper.genAPIToken(this, InviteActivity.this);
            String appLinkUrl, previewImageUrl;

            /*appLinkUrl = "https://fb.me/742888042478136";
            previewImageUrl = "http://mellapp.com/image/jumbo_background.jpg";

            if (AppInviteDialog.canShow()) {
                AppInviteContent content = new AppInviteContent.Builder()
                        .setApplinkUrl(appLinkUrl)
                        .setPreviewImageUrl(previewImageUrl)
                        .build();
                AppInviteDialog.show(this, content);
            }*/


        } catch (Exception e) {
            e.printStackTrace();
        }

        if(AuthToken!="") {
            ButterKnife.inject(this);

            getMellPoint(this,AuthToken);
            showProgress(true);

            List<String> permissions = Arrays.asList(this.getResources().getStringArray(R.array.fb_read_permission));
            fb_login_button.setReadPermissions(permissions);
            fb_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                private ProfileTracker mProfileTracker;

                @Override
                public void onSuccess(LoginResult loginResult) {
                    fbUser = new FBUser();
                    fbUser.setFbID(loginResult.getAccessToken().getUserId());
                    fbUser.setFbAuth(loginResult.getAccessToken().getToken());
                    if (Profile.getCurrentProfile() == null) {
                        mProfileTracker = new ProfileTracker() {
                            @Override
                            protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                                // profile2 is the new profile
                                fbUser.setProfile(profile2);
                                mProfileTracker.stopTracking();
                                attemptFBLoginReg(fbUser);
                            }
                        };
                        mProfileTracker.startTracking();
                    } else {
                        fbUser.setProfile(Profile.getCurrentProfile());
                        attemptFBLoginReg(fbUser);
                    }
                }

                @Override
                public void onCancel() {
                    Log.v("facebook - onCancel", "cancelled");
                }

                @Override
                public void onError(FacebookException e) {
                    Log.v("facebook - onError", e.getMessage());
                }
            });

            if(getIntent().getSerializableExtra(EXTRA_CONTACTS)!=null && getIntent().getSerializableExtra(EXTRA_CONTACTS)!=""){
                mInternalContacts=(ArrayList<InternalContact>)getIntent().getSerializableExtra(EXTRA_CONTACTS);
            }

            if(getIntent().getSerializableExtra(EXTRA_CONTACTS_NO)!=null && getIntent().getSerializableExtra(EXTRA_CONTACTS_NO)!=""){
                mInternalContactNos=(ArrayList<String>)getIntent().getSerializableExtra(EXTRA_CONTACTS_NO);
            }

            if(mInternalContacts!=null && mInternalContacts.size()>0){
                syncWithServer();
            }
            else{
                //once this process is done, activity will restart with extra objects
                loadContacts();
            }

            if(getIntent().getStringExtra(EXTRA_LINK)!=null && getIntent().getStringExtra(EXTRA_LINK)!=""
                    &&getIntent().getStringExtra(EXTRA_LINK).equals(EXTRA_LINK_ACTION_FB)){
                if(UtilityHelper.getMyFB(this)!=""){
                    showFacebookInvite();
                }
                else{
                    fb_login_button.performClick();
                }
            }else{
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    public void OnInviteConfirmed(String phoneNo, String message) {
        sendText(phoneNo, message, 0);
        saveInvite(phoneNo);
    }

    public void attemptFBLoginReg(FBUser fbu) {

        DBHelper dh = new DBHelper(this);
        try {
            dh.deleteUser();
            dh.deleteAuth();
            dh.deleteNoti();
        } catch (Exception e) {
            e.printStackTrace();
        }

        showProgress(true);
        try {

            final DBHelper dbH = new DBHelper(this);

            SampleClient.getWoodyApiClient(this).postAuthTokenFB(fbu, new Callback<Auth>() {

                @Override
                public void success(Auth pAuth, Response response) {
                    if (pAuth != null) {
                        Log.d("AuthAPI", pAuth.getAccess_token());
                        try {
                            dbH.createAuth(pAuth);
                            getMellPoint(InviteActivity.this,AuthToken);
                            showFacebookInvite();

                        } catch (Exception e) {
                            Log.d("AuthAPI", e.getStackTrace().toString());
                        }
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                    try {
                        dbH.deleteUser();
                        showProgress(false);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    if(error.getMessage().equals(getResources().getString(R.string.api_error_offline))){
                    }
                    Log.d("AuthAPI", error.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateProfile(final String facebook_id, final String facebook_token){
        SampleClient.getWoodyApiClient(this).postFullProfile(this.AuthToken, null, null, null, null, null, null, null, null, null, facebook_id,facebook_token, new Callback<API_Response>() {//change update process
            @Override
            public void success(API_Response apiRes, Response response) {

            }


            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void syncWithServer(){
        if(mInternalContacts!=null && mInternalContacts.size()>0) {
            //llListHolderSr.removeAllViews();
            Collections.sort(mInternalContacts);
            SampleClient.getWoodyApiClient(this).postSyncMeller(this.AuthToken, mInternalContactNos.toArray(new String[mInternalContactNos.size()]), new Callback<UserReferrals>() {
                @Override
                public void success(UserReferrals users, Response response) {
                    if (users.getFoundUsers() != null && users.getFoundUsers().size() > 0) {
                        for (UserReferral u : users.getFoundUsers()) {
                            if (mInternalContactNos.indexOf(u.getUser().getPhone()) > -1) {

                                //InternalContact cTBD = mInternalContacts.get(mInternalContactNos.indexOf(u.getUser().getPhone()));
                                //String nTBD = mInternalContactNos.get(mInternalContactNos.indexOf(u.getUser().getPhone()));
                                mInternalContacts.remove(mInternalContactNos.indexOf(u.getUser().getPhone()));
                                mInternalContactNos.remove(mInternalContactNos.indexOf(u.getUser().getPhone()));


                            }
                        }
                    }

                    if (users.getReferredUsers() != null && users.getReferredUsers().size() > 0) {

                    }

                    vpReferralList.setAdapter(new InviteFragmentPagerAdapter(getSupportFragmentManager(),
                            InviteActivity.this, AuthToken, mInternalContacts, (ArrayList) users.getReferredUsers()));
                    tlReferralList.setupWithViewPager(vpReferralList);
                    showProgress(false);
                }

                @Override
                public void failure(RetrofitError error) {
                    if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                        displayMessage(getResources().getString(R.string.api_error_offline_message), getResources().getColor(R.color.red_500));
                    } else {
                        displayMessage(getResources().getString(R.string.api_result_servererror), getResources().getColor(R.color.red_500));
                    }
                    Log.d("API_Profile", error.toString());
                    showProgress(false);

                }
            });
        }
        showProgress(false);
    }

    private void showFacebookInvite() {
        FacebookSdk.sdkInitialize(this);

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(getResources().getString(R.string.fb_applink_url))
                    .setPreviewImageUrl(getResources().getString(R.string.fb_applink_image))
                    .build();

            AppInviteDialog appInviteDialog = new AppInviteDialog(this);
            appInviteDialog.registerCallback(callbackManager, new FacebookCallback<AppInviteDialog.Result>() {
                @Override
                public void onSuccess(AppInviteDialog.Result result) {
                    //result only return invited frd count
                    SampleClient.getWoodyApiClient(InviteActivity.this).postInviteCounter(AuthToken, "dummy", new Callback<API_Response>() {
                        @Override
                        public void success(API_Response result, Response response) {
                            reLoadAnswer();
                        }
                        @Override
                        public void failure(RetrofitError error) {
                            if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                                displayMessage(getResources().getString(R.string.api_error_offline_message), getResources().getColor(R.color.red_500));
                            }
                            reLoadAnswer();
                        }
                    });
                }

                @Override
                public void onCancel() {
                }

                @Override
                public void onError(FacebookException e) {
                }
            });
            appInviteDialog.show(content);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (SENT.equals(intent.getAction()))
            {
                String number = intent.getStringExtra("number");

                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        saveInvite(number);
                        break;
                }
            }
            else if (DELIVERED.equals(intent.getAction()))
            {

            }
        }
    };

    private void sendText(String conNumber, String msg, int requestCode)
    {
        Intent sentIntent = new Intent(SENT);
        Intent deliveredIntent = new Intent(DELIVERED);

        sentIntent.putExtra(EXTRA_NUMBER, conNumber);

        PendingIntent sentPI = PendingIntent.getBroadcast(this, requestCode, sentIntent, 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, requestCode, deliveredIntent, 0);

        smsMgr.sendTextMessage(conNumber, null, msg, sentPI, deliveredPI);
    }

    private void saveInvite(String conNumber){
        String[] phones = {conNumber};
        SampleClient.getWoodyApiClient(this).postRefMeller(AuthToken, phones, new Callback<API_Response>() {
            @Override
            public void success(API_Response users, Response response) {
                Log.d("", "");
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("", "");
            }
        });
    }

    private void reLoadAnswer(){
        Intent intent = new Intent(this, AnswerActivity.class);
        startActivity(intent);
    }

    public void displayMessage(String text,int color){
        /*llListHolderSr.removeAllViews();
        TextView tv = new TextView(this);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setText(text);
        tv.setTextColor(color);
        llListHolderSr.addView(tv);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public class InviteFragmentPagerAdapter extends FragmentPagerAdapter {
        private int PAGE_COUNT=2;
        private Context context;
        String apiKey ="";
        ArrayList<InternalContact> mInternalContacts;
        ArrayList<UserReferral> mUserReferrals;
        private String[] tabTitles = AppValues.LAYOUT_REFERRAL_LIST;
        int[] imageResId= AppValues.LAYOUT_REFERRAL_IC_LIST;


        public InviteFragmentPagerAdapter(FragmentManager fm, Context context,String api, ArrayList<InternalContact> ics, ArrayList<UserReferral> urs) {
            super(fm);
            this.context = context;
            apiKey = api;
            mInternalContacts = ics;
            mUserReferrals = urs;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public Fragment getItem(int position) {
            if (position==0) {
                return InviteFragment.newInstance(position,mInternalContacts);
            }
            else if(position==1) {
                return ReferralFragment.newInstance(position,mUserReferrals,apiKey);
            }
            else{
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            // return tabTitles[position];
            Drawable image = context.getResources().getDrawable(imageResId[position]);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            SpannableString sb = new SpannableString("  " + tabTitles[position]);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            llInviteView.setVisibility(show ? View.GONE : View.VISIBLE);
            llInviteView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    llInviteView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            contact_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            contact_progress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    contact_progress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            contact_progress.setVisibility(show ? View.VISIBLE : View.GONE);
            llInviteView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    private void loadContacts() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ContentResolver cr = getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);

                mInternalContacts = new ArrayList<InternalContact>();
                mInternalContactNos = new ArrayList<String>();
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        String photoUri = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.PHOTO_THUMBNAIL_URI));

                        if (Integer.parseInt(cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                phoneNo = phoneNo.replace(" ", "");
                                InternalContact ic = new InternalContact();
                                ic.setName(name);
                                ic.setPhone(phoneNo);
                                ic.setPicture(photoUri);
                                ic.setId(id);
                                mInternalContacts.add(ic);
                                mInternalContactNos.add(phoneNo);
                            }
                            pCur.close();
                        }
                    }
                }

                finish();
                Intent intent = new Intent(InviteActivity.this, InviteActivity.class);

                if(getIntent().getStringExtra(EXTRA_LINK)!=null){
                        intent.putExtra(EXTRA_LINK,EXTRA_LINK_ACTION_FB);
                }
                intent.putExtra(InviteActivity.EXTRA_CONTACTS, mInternalContacts);
                intent.putExtra(InviteActivity.EXTRA_CONTACTS_NO, mInternalContactNos);
                startActivity(intent);
            }
        });
        thread.start();
    }
}
