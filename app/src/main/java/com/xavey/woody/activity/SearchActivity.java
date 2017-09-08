package com.xavey.woody.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.adapter.UserAdapter;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.Users;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.TokenHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tinmaungaye on 5/10/15.
 */
public class SearchActivity extends BaseActivity {
    @InjectView(R.id.llListHolderSr)
    LinearLayout llListHolderSr;

    String AuthToken="";
    String query="";
    private Menu mMenu;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        handleIntent(getIntent());
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);

        try {
            AuthToken= TokenHelper.genAPIToken(this, SearchActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(AuthToken!="") {
            ButterKnife.inject(this);
            getMellPoint(this,AuthToken);
            getNotification(this, AuthToken);
            //syncOptOutCatgories(this,AuthToken);
            loadProfileList();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query = intent.getStringExtra(SearchManager.QUERY);
        }
    }

    private void loadProfileList(){
        if(llListHolderSr!=null) {llListHolderSr.removeAllViews();}
        AppValues.SEARCH_VIEW_QUERY=query;
        SampleClient.getWoodyApiClient(this).getUserSearch(this.AuthToken, query, new Callback<Users>() {
            @Override
            public void success(Users users, Response response) {
                if (users != null && users.getTotal()>0) {
                    ListView qList = new ListView(SearchActivity.this);
                    qList.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    userAdapter = new UserAdapter(users.getUsers(), SearchActivity.this,AuthToken);
                    qList.setAdapter(userAdapter);
                    llListHolderSr.addView(qList);
                }
                else{
                    displayMessage(getResources().getString(R.string.api_result_notfound), getResources().getColor(R.color.grey_500));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if(error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                    displayMessage(getResources().getString(R.string.api_error_offline_message), getResources().getColor(R.color.red_500));
                }
                else {
                    displayMessage(getResources().getString(R.string.api_result_servererror), getResources().getColor(R.color.red_500));
                }
                Log.d("API_Profile", error.toString());
            }
        });
    }

    public void displayMessage(String text,int color){
        if(llListHolderSr!=null) {llListHolderSr.removeAllViews();}
        TextView tv = new TextView(this);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setText(text);
        tv.setTextColor(color);
        llListHolderSr.addView(tv);
    }


}
