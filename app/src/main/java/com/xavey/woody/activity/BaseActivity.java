package com.xavey.woody.activity;

import android.animation.ValueAnimator;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.squareup.okhttp.internal.Util;
import com.xavey.woody.R;
import com.xavey.woody.adapter.NavDrawerListAdapter;
import com.xavey.woody.adapter.NotiDrawerListAdapter;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.Notification;
import com.xavey.woody.api.model.NotificationResult;
import com.xavey.woody.api.model.RelatedStat;
import com.xavey.woody.api.model.UserRelated;
import com.xavey.woody.helper.DBHelper;
import com.xavey.woody.helper.UtilityHelper;
import com.xavey.woody.interfaces.NavDrawerItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tinmaungaye on 5/7/15.
 */
public class BaseActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ListView mDrawerListR;

    private ActionBarDrawerToggle mDrawerToggle;
    protected RelativeLayout _completeLayout, _activityLayout;
    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private Menu mMenu;
    private int mNotificationsCount=0;
    private static boolean doingNoti = false;
    private static boolean doingMP = false;
    private static Date last_date = new Date();
    Toolbar toolbar;
    TextView tv_mellpoint;

    @Override
    public void setContentView(int layoutResID)
    {
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) mDrawerLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(mDrawerLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_mellpoint = (TextView) findViewById(R.id.tv_mellpoint);
        setSupportActionBar(toolbar);

        tv_mellpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    public void set(String[] navMenuTitles, TypedArray navMenuIcons) {
        mTitle = mDrawerTitle = getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerListR = (ListView) findViewById(R.id.rigth_drawer);

        navDrawerItems = new ArrayList<NavDrawerItem>();

        // adding nav drawer items
        if (navMenuIcons == null) {
            for (int i = 0; i < navMenuTitles.length; i++) {
                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i]));
            }
        } else {
            for (int i = 0; i < navMenuTitles.length; i++) {
                navDrawerItems.add(new NavDrawerItem(navMenuTitles[i],
                        navMenuIcons.getResourceId(i, -1)));
            }
        }

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(),
                navDrawerItems);
        mDrawerList.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        /*
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, // nav menu toggle icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                supportInvalidateOptionsMenu();
            }
        };*/
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    public void updateNotificationsBadge(int count) {
        mNotificationsCount = count;
        invalidateOptionsMenu();
    }

    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }
    @Override
    public void onStop () {
        doingMP = false;
        doingNoti = false;
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.answer, menu);

        SearchManager searchManager =(SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =(SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        if(!this.getClass().getName().toLowerCase().endsWith("answeractivity")){
            menu.setGroupVisible(R.id.answer_menu_group,false);
        }
        mMenu = menu;

        MenuItem item = menu.findItem(R.id.menu_notifications);
        LayerDrawable icon = (LayerDrawable) item.getIcon();

        // Update LayerDrawable's BadgeDrawable
        UtilityHelper.setBadgeCount(this, icon, mNotificationsCount);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(mDrawerListR)) {
                    mDrawerLayout.closeDrawer(mDrawerListR);
                }
                if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                } else {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
                return true;
            case R.id.menu_profile:
                Intent intent = new Intent(this, InviteActivity.class);
                intent.putExtra(InviteActivity.EXTRA_LINK, InviteActivity.EXTRA_LINK_ACTION_FB);
                startActivity(intent);
                return true;
            case R.id.menu_notifications:
                if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                    mDrawerLayout.closeDrawer(mDrawerList);
                }

                if (mDrawerLayout.isDrawerOpen(mDrawerListR)) {
                    mDrawerLayout.closeDrawer(mDrawerListR);
                }
                else{
                    mDrawerLayout.openDrawer(mDrawerListR);
                }
                return true;
            case R.id.menu_search:
                return onSearchRequested();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        startSearch(null, false, appData, false);

        return true;
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    public void getNotification(final Context conx, String AuthToken){
        if(!doingNoti) {
            doingNoti=true;
            SampleClient.getWoodyApiClient(conx).getUserRelatedNoti(AuthToken, new Callback<NotificationResult>() {
                @Override
                public void success(NotificationResult results, Response response) {
                    if (results != null && (results.getMessage().equals("010") || results.getMessage().equals("011"))) {
                        DBHelper dbHelper = new DBHelper(conx);
                        try {
                            last_date=results.getNotification().getLast_requested_on();
                            dbHelper.appendNoti(results.getNotification().getNotices(),results.getNotification().getUser(),0);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    updateNewNotification(conx);
                    doingNoti=false;
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("API_Notification", error.toString());
                    updateNewNotification(conx);
                    doingNoti=false;
                }
            });
        }
    }

    public void getMellPointLocal(final Context conx){
        tv_mellpoint.setText("MP "+ String.valueOf(UtilityHelper.getMellPointLocal(conx)));
    }

    public void getMellPoint(final Context conx, String AuthToken){
        if(!doingMP) {
            doingMP=true;
            SampleClient.getWoodyApiClient(conx).getUserMellPoint(AuthToken, new Callback<UserRelated>() {
                @Override
                public void success(UserRelated results, Response response) {
                    if (results != null && (results.getMessage().equals("010"))) {
                        int TotalPoint = 0;
                        for (RelatedStat rs : results.getRelatedStat()) {
                            switch (rs.getName().toLowerCase()) {
                                case "basic_profile":
                                    if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                        TotalPoint += Integer.parseInt(rs.getPoint());
                                    }
                                    break;
                                case "question_set":
                                    break;
                                case "question":
                                    if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                        TotalPoint += Integer.parseInt(rs.getPoint());
                                    }
                                    break;
                                case "follower":
                                    if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                        TotalPoint += Integer.parseInt(rs.getPoint());
                                    }
                                    break;
                                case "following":
                                    break;
                                case "favourite":
                                    if (rs.getPoint() != null && Integer.parseInt(rs.getPoint()) > 0) {
                                        TotalPoint += Integer.parseInt(rs.getPoint());
                                    }
                                    break;
                                case "user_like":
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

                        mellPointAnimation(UtilityHelper.getMellPointLocal(conx), TotalPoint);
                        UtilityHelper.setMellPointLocal(conx, TotalPoint);
                        UtilityHelper.setMyFB(conx,results.getUser().getFacebook_id());
                        UtilityHelper.setPCompleted(conx,results.getUser().getProfile_completed());
                    }
                    doingMP=false;
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("API_Notification", error.toString());
                    doingMP=false;
                }
            });
        }
    }

    private void mellPointAnimation(int st, int nd) {
        ValueAnimator animator = new ValueAnimator();
        animator.setObjectValues(st, nd);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                tv_mellpoint.setText("   MP " + (int) animation.getAnimatedValue());
            }
        });
        animator.start();
    }

    public void updateNewNotification(Context conx){
        DBHelper dbHelper = new DBHelper(conx);
        int count=0;
        ArrayList<Notification> lN = null;
        try {
            lN = dbHelper.getNoti();
            HashMap<String, Date> hashMap = new HashMap<String, Date>();
            HashMap<String, Notification> hashMapN = new HashMap<String, Notification>();
            for(Notification n: lN){
                //checking with postID
                if(hashMap.containsKey(n.getPost())) {
                    Date old = hashMap.get(n.getPost());
                    if(old.before(n.getCreated_on())){
                        hashMapN.put(n.getPost(), n);
                    }
                }
                else{
                    hashMapN.put(n.getPost(),n);
                }
            }

            ArrayList<Notification> notis = new ArrayList(hashMapN.values());
            Collections.sort(notis);
            for(Notification n: notis){
                if(n.getCreated_on().after(last_date) && !n.getChecked()){
                    count++;
                }
            }

            updateNotificationsBadge(count);
            NotiDrawerListAdapter adapter = new NotiDrawerListAdapter(conx,notis);
            mDrawerListR.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void displayFacebookConnect(){

    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(this, AnswerActivity.class);
                break;
            case 1:
                intent = new Intent(this, AskActivity.class);
                break;
            case 2:
                intent = new Intent(this, RewardActivity.class);
                break;
            case 3:
                intent = new Intent(this, InviteActivity.class);
                break;
            case 4:
                intent = new Intent(this, PartnerActivity.class);
                break;
            case 5:
                intent = new Intent(this, PromoteActivity.class);
                break;
            case 6:
                intent = new Intent(this,SettingActivity.class);
                break;
            case 7:
                intent  = new Intent(this, AboutActivity.class);
                break;
            case 8:
                DBHelper dh = new DBHelper(this);
                FacebookSdk.sdkInitialize(getApplicationContext());
                LoginManager.getInstance().logOut();
                try {
                    dh.deleteUser();
                    dh.deleteAuth();
                    dh.deleteNoti();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                intent = new Intent(this, LoginActivity.class);
                break;
            default:
                intent = new Intent(this, AnswerActivity.class);
                break;
        }
        startActivity(intent);
        finish();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerList.setSelection(position);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

//    @Override
//    public void setTitle(CharSequence title) {
//        mTitle = title;
//        getActionBar().setTitle(mTitle);
//    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}
