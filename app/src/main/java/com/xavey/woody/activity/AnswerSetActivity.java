package com.xavey.woody.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xavey.woody.R;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.API_Response;
import com.xavey.woody.api.model.Item;
import com.xavey.woody.api.model.Post;
import com.xavey.woody.api.model.PostSet;
import com.xavey.woody.api.model.PostSetHolder;
import com.xavey.woody.api.model.VoteSet;
import com.xavey.woody.api.model.VoteSetPost;
import com.xavey.woody.fragment.MessageFragment;
import com.xavey.woody.fragment.PostChecklistFragment;
import com.xavey.woody.fragment.PostRadioGroupFragment;
import com.xavey.woody.fragment.SubmitFragment;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.TokenHelper;
import com.xavey.woody.helper.UtilityHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnPageChange;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by hnin on 8/11/15.
 */

public class AnswerSetActivity extends BaseActivity implements PostRadioGroupFragment.OnPostVottedCallback,PostChecklistFragment.OnPostVottedCallback, SubmitFragment.OnPostSetVottedCallback{
    public static final String EXTRA_POSTSET = "AnswerSetActivity.EXTRA_POSTSET";

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private String UserID ="";
    private String requestID ="me";

    private String mPostId ="yes";
    private String mSkipId ="noskip";

    Map<String, String> mapValue = new HashMap<String, String>();
    String errMsg="";
    String AuthToken="";
    PostSetHolder mPostSetHolder = new PostSetHolder();
    FragmentPagerAdapter adapterViewPager;


    @InjectView(R.id.vpPager)
    ViewPager vpPager;

    @InjectView(R.id.pbSet)
    ProgressBar pbSet;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voteset);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
        this.setTitle("Answer Set");
        if(getIntent().getStringExtra(EXTRA_POSTSET)!=null){
            mPostId= getIntent().getStringExtra(EXTRA_POSTSET);
        }
        try {
            UtilityHelper.hideSoftKeyboard(this);
            AuthToken= TokenHelper.genAPIToken(this, AnswerSetActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(AuthToken!="") {
            ButterKnife.inject(this);

            pbSet.setMax(0);
            pbSet.setProgress(0);
            getMellPoint(this,AuthToken);

            loadPostList();
            getNotification(this,AuthToken);
            //syncOptOutCatgories(this,AuthToken);
        }
//        FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
//        VoteFragment voteFragment =VoteFragment.newInstance(1,"Testing");
//        ft.replace(R.id.voteFragment,voteFragment);
//        ft.commit();
    }
    private void loadPostList(){
        SampleClient.getWoodyApiClient(this).getPostSet(this.AuthToken,mPostId,new Callback<PostSet>(){
            @Override
            public void success(PostSet pSet, Response response) {
                if(pSet != null){
                    mPostSetHolder.setPostSet(pSet);
                    mPostSetHolder.setPageCount(mPostSetHolder.getPostSet().getPosts().length + 2);
                    pbSet.setMax(mPostSetHolder.getPageCount()); //intro + desc + [posts] + submit + ack
                    pbSet.setProgress(1);
                    adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(),mPostSetHolder);
                    vpPager.setAdapter(adapterViewPager);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("API_Profile", error.toString());
                if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                    Toast bread = Toast.makeText(getApplicationContext(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                    bread.show();
                }
            }
        });
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static PostSetHolder postSetHolder=null;
        public MyPagerAdapter(FragmentManager fragmentManager, PostSetHolder postsetHolder) {
            super(fragmentManager);
            postSetHolder = postsetHolder;
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return postSetHolder.getPageCount();
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            if(postSetHolder!=null && postSetHolder.getPostSet().getPosts().length>0){
                if(position == 0) {
                    return MessageFragment.newInstance(position, postSetHolder.getPostSet().getIntro(), postSetHolder.getPostSet().getDesc());
                }
                else if(position == postSetHolder.getPageCount()-1) {
                    return SubmitFragment.newInstance(position,postSetHolder);
                }
                else if(postSetHolder.getPostSet().getPosts()[position-1].getType()!=null && postSetHolder.getPostSet().getPosts()[position-1].getType().equals(AppValues.POST_TYPE_CHECKLIST)){
                    return PostChecklistFragment.newInstance(position, postSetHolder.getPostSet().getPosts()[position - 1]);
                }
                else{
                    return PostRadioGroupFragment.newInstance(position, postSetHolder.getPostSet().getPosts()[position - 1]);
                }
            }
            else{
                return null;
            }

        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    @OnPageChange(value = R.id.vpPager, callback = OnPageChange.Callback.PAGE_SCROLL_STATE_CHANGED)
    void onPageStateChanged(int state) {
        errMsg="";
    }

    @OnPageChange(value = R.id.vpPager, callback = OnPageChange.Callback.PAGE_SELECTED)
    void onPageSelected(int position) {
        UtilityHelper.hideSoftKeyboard(this);
        if(mPostSetHolder.getPostSet()!=null){
            if(position > 0){
                Fragment  f = adapterViewPager.getItem(position-1);
                if(f!=null){

                }
                //View v = f.getView().findViewWithTag(mPostSetHolder.getPostSet().getPosts()[position].get_id());
                //if(v!=null){

                //}
            }
            pbSet.setProgress(position+1);
        }
    }

    @OnPageChange(value = R.id.vpPager, callback = OnPageChange.Callback.PAGE_SCROLLED)
    void onPageScrolled(int start, float middle, int end) {
    }

    @Override
    public String OnPostSetVotted() {
        final VoteSet vs = new VoteSet();
        Post[] ps = mPostSetHolder.getPostSet().getPosts();
        ArrayList<VoteSetPost> vsps = new ArrayList<VoteSetPost>();
        ArrayList<String> count = new ArrayList<String>();
        for(Post post : ps){
            for(Item item : post.getItems()) {
                if (item.getVote_count() == 1) {
                    VoteSetPost vsp = new VoteSetPost();
                    vsp.setItem_id(item.get_id());
                    vsp.setPost_id(post.get_id());
                    vsps.add(vsp);
                    if(count.indexOf(post.get_id())==-1){
                        count.add(post.get_id());
                    }
                } else {
                    item = null;
                }
            }

            if(post.getExtra()!=null && post.getExtra() && post.getExtra_value()!=null && post.getExtra_value().toString().length()>0){
                VoteSetPost vsp = new VoteSetPost();
                vsp.setExtra(post.getExtra_value());
                vsp.setPost_id(post.get_id());
                vsps.add(vsp);
            }
        }
        if(count.size() >= ps.length) {
            vs.setVote_set_posts(vsps.toArray(new VoteSetPost[vsps.size()]));
            vs.setPost_set_id(mPostSetHolder.getPostSet().get_id());
            //ready to submit...
            SampleClient.getWoodyApiClient(this).postVoteSet(AuthToken, vs.getPost_set_id(), vs, new Callback<API_Response>() {
                @Override
                public void success(API_Response apiRes, Response response) {
                    if (apiRes.getMessage().equals(AnswerSetActivity.this.getString(R.string.reg_code_010_success))) {
                        mPostSetHolder.setMessage(AnswerSetActivity.this.getResources().getString(R.string.message_vote_submitted));
                        finish();
                        Intent intent = new Intent(AnswerSetActivity.this, ResultSetActivity.class);
                        intent.putExtra(ResultSetActivity.EXTRA_POST, vs.getPost_set_id());
                        startActivity(intent);
                    } else {
                        mPostSetHolder.setMessage(AnswerSetActivity.this.getResources().getString(R.string.message_vote_not_submitted));
                    }
                    adapterViewPager.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {
                    if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                        mPostSetHolder.setMessage(AnswerSetActivity.this.getResources().getString(R.string.api_error_offline_message));
                    } else {
                        mPostSetHolder.setMessage(AnswerSetActivity.this.getResources().getString(R.string.message_vote_not_submitted));
                        Log.d("regNewPost", error.toString());
                    }
                    adapterViewPager.notifyDataSetChanged();
                }
            });

            return mPostSetHolder.getPostSet().getAck();
        }
        else{
            return "Error: Please answer all questions.";
        }
    }

    @Override
    public void OnPostVotted(Post p, String i, Boolean postItem, Boolean isChecked) {
        for(Post post : mPostSetHolder.getPostSet().getPosts()){
            if(post.get_id()==p.get_id()){
                if(postItem) {
                    for (Item item : post.getItems()) {
                        if (item.get_id() == i) {
                            if(post.getType().equals(AppValues.POST_TYPE_CHECKLIST) && isChecked){
                                item.setVote_count(1);
                            }
                            else if(post.getType().equals(AppValues.POST_TYPE_CHECKLIST) && !isChecked){
                                item.setVote_count(0);
                            }
                            else {
                                // for option default
                                item.setVote_count(1);
                            }
                        } else {
                            if (post.getType().equals(AppValues.POST_TYPE_RADIOGROUP)) {
                                item.setVote_count(0);
                            }
                        }
                    }
                }
                else{
                    post.setExtra_value(i);
                }
            }
        }
    }
}
