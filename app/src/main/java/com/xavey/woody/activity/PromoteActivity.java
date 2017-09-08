package com.xavey.woody.activity;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.xavey.woody.R;
import com.xavey.woody.adapter.PromoteAdapter;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.PromoPosts;
import com.xavey.woody.helper.TokenHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hnin on 8/31/15.
 */
public class PromoteActivity extends BaseActivity{
    public static final String EXTRA_ITEM = "ProfileActivity.EXTRA_ITEM";

    String AuthToken="";

    @InjectView(R.id.llMessagePromote)
    LinearLayout llMessagePromote;

    private String[]navMenuTitles;
    private TypedArray navMenuIcons;
    private PromoteAdapter promoteAdapter;
    private String id;

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promote);

        navMenuTitles =getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles,navMenuIcons);


        try {
            AuthToken = TokenHelper.genAPIToken(this,PromoteActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(AuthToken != ""){
            ButterKnife.inject(this);
            getMellPoint(this,AuthToken);

            loadPromoteItems();

        }

    }

    private void loadPromoteItems(){
        SampleClient.getWoodyApiClient(this).getPromotionItem(AuthToken,new Callback<PromoPosts>() {
            @Override
            public void success(PromoPosts promoPosts, Response response) {
                if(promoPosts !=null && promoPosts.getTotal()>0){
                    ListView pqList = new ListView(getApplicationContext());
                    pqList.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    promoteAdapter = new PromoteAdapter(promoPosts.getPostsets(),getApplicationContext());
                    pqList.setAdapter(promoteAdapter);
                    llMessagePromote.addView(pqList);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                    Toast bread =Toast.makeText(getApplicationContext(),"Network connection error",Toast.LENGTH_LONG);
                    bread.show();
                }
                Log.d("API_Profile", error.toString());

            }
        });
    }
}
