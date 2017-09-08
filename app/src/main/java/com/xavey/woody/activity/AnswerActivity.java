package com.xavey.woody.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xavey.woody.R;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.API_Response;
import com.xavey.woody.api.model.Category;
import com.xavey.woody.api.model.Item;
import com.xavey.woody.api.model.Post;
import com.xavey.woody.api.model.Vote;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.PicassoHelper;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TokenHelper;
import com.xavey.woody.helper.TypeFaceHelper;
import com.xavey.woody.helper.UtilityHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AnswerActivity extends BaseActivity {
    public static final String EXTRA_POST = "AnswerActivity.EXTRA_POST";
    public static final String EXTRA_SKIP = "AnswerActivity.EXTRA_SKIP";
    public static final String EXTRA_CHECK_PROFILE = "AnswerActivity.EXTRA_CHECK_PROFILE";

    private String mPostId = "yes";
    private String mSkipId = "noskip";
    @InjectView(R.id.rGAnswerGroupAns)
    RadioGroup rGAnswerGroupAns;

    @InjectView(R.id.tvQuestionAns)
    TextView tvQuestionAns;

    @InjectView(R.id.tvAsker)
    TextView tvAsker;

    @InjectView(R.id.tVCategoryAns)
    TextView tVCategoryAns;

    @InjectView(R.id.tVMessageAns)
    TextView tVMessageAns;

    @InjectView(R.id.btnCreateQAns)
    Button btnCreateQAns;

    @InjectView(R.id.btnOkAns)
    Button btnOkAns;

    @InjectView(R.id.iVPostPicAns)
    ImageView iVPostPicAns;

    private Picasso mPicasso;

    String AuthToken="";

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private Post mPost;

    private Menu mMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);
        try {
            if(getIntent().getStringExtra(EXTRA_POST)!=null && getIntent().getStringExtra(EXTRA_POST)!=""){
                mPostId=getIntent().getStringExtra(EXTRA_POST);
            }
            if(getIntent().getStringExtra(EXTRA_SKIP)!=null && getIntent().getStringExtra(EXTRA_SKIP)!=""){
                mSkipId=getIntent().getStringExtra(EXTRA_SKIP);
            }

            if(getIntent().getBooleanExtra(EXTRA_CHECK_PROFILE,false) && !UtilityHelper.getPCompleted(this)){
                finish();
                Intent intent = new Intent(AnswerActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }else{
                // just avoiding unnecessary loading following codes
                AuthToken=TokenHelper.genAPIToken(this, AnswerActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(AuthToken!="") {
            this.setTitle(R.string.title_activity_answer);
            ButterKnife.inject(this);
            iVPostPicAns.setVisibility(View.INVISIBLE);
            OnLoadReadSetting();
            btnOkAns.setVisibility(View.INVISIBLE);
            btnCreateQAns.setVisibility(View.INVISIBLE);
            loadQnA();
            getMellPoint(this,AuthToken);
            getNotification(this,AuthToken);
            if(AppValues.getOpt_out_categories().size()>0){
                //syncOptOutCatgories(this,AuthToken);
            }
            toBeSubmitted();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_skip_question:
                reLoadSurprise();
                return true;
            case R.id.action_flag_report:
                reportFlag();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void reLoadSurprise(){
        finish();
        Intent intent = new Intent(AnswerActivity.this, AnswerActivity.class);
        intent.putExtra(AnswerActivity.EXTRA_SKIP, mSkipId);
        startActivity(intent);
    }

    public void reportFlag() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        reportYes();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this); builder.setMessage(getResources().getString(R.string.dialog_report_message))
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void reportYes(){
        Vote fV = new Vote();
        Post fP = new Post();
        fP.set_id(mPost.get_id());
        fV.setPost(mPost);
        SampleClient.getWoodyApiClient(this).putAFlag(AuthToken, fV.getPost().get_id(), fV, new Callback<API_Response>() {
            @Override
            public void success(API_Response apiRes, Response response) {
                reLoadSurprise();
            }

            @Override
            public void failure(RetrofitError error) {
                reLoadSurprise();
            }
        });
    }

    private void loadQnA(){
        SampleClient.getWoodyApiClient(this).getSurprise(this.AuthToken, mPostId, mSkipId, new Callback<Post>() {
            @Override
            public void success(Post post, Response response) {
                if (post != null) {
                    mPost = post;
                    btnOkAns.setVisibility(View.VISIBLE);
                    btnCreateQAns.setVisibility(View.INVISIBLE);
                    mSkipId = mPost.get_id();
                    tvAsker.setText(post.getCreated_by().getUser_name() + " posted this question " + UtilityHelper.relativeTimespan(post.getCreated_on()) + ".");
                    int r = 0;

                    for (Item item : post.getItems()) {
                        RadioButton rdbtn = new RadioButton(AnswerActivity.this);
                        rdbtn.setTag(item.get_id());

                        TypeFaceHelper.setM3TypeFace(rdbtn, AnswerActivity.this);
                        if (AppValues.getInstance().getZawGyiDisplay()) {
                            rdbtn.setText(Rabbit.uni2zg(item.getTitle()));
                        } else {
                            rdbtn.setText(item.getTitle());
                        }
                        rGAnswerGroupAns.addView(rdbtn);
                        r++;
                    }

                    RadioButton rdbtn = new RadioButton(AnswerActivity.this);
                    rdbtn.setTag(AppValues.ITEM_VALUE_SKIP);
                    rdbtn.setText("Don't want to answer");
                    rdbtn.setTextColor(Color.GRAY);
                    rGAnswerGroupAns.addView(rdbtn);

                    TypeFaceHelper.setM3TypeFace(tvQuestionAns, AnswerActivity.this);
                    if (AppValues.getInstance().getZawGyiDisplay()) {
                        tvQuestionAns.setText(Rabbit.uni2zg(post.getTitle()));
                    } else {
                        tvQuestionAns.setText(post.getTitle());
                    }

                    tvQuestionAns.setTag(post.get_id());
                    String catList = "";
                    for (Category cat : post.getCategories()) {
                        catList += "#" + cat.getTitle() + "  ";
                    }
                    tVCategoryAns.setText(catList);

                    if (mPost.getPicture() != null) {
                        iVPostPicAns.setVisibility(View.VISIBLE);
                        String picPath = AnswerActivity.this.getString(R.string.api_endpoint) + AnswerActivity.this.getString(R.string.api_endpoint_post) + mPost.getPicture();

                        mPicasso = PicassoHelper.getInstance(AnswerActivity.this, AuthToken).getPicasso();
                        mPicasso.load(picPath)
                                .error(android.R.drawable.stat_notify_error)
                                .placeholder(android.R.drawable.stat_notify_sync)
                                .into(iVPostPicAns);
                    } else {
                        iVPostPicAns.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    }

                } else if (response.getStatus() == 204) {
                    tvAsker.setVisibility(View.INVISIBLE);

                    TypeFaceHelper.setM3TypeFace(tvQuestionAns, AnswerActivity.this);
                    if (AppValues.getInstance().getZawGyiDisplay()) {
                        tvQuestionAns.setText(Rabbit.uni2zg(getResources().getString(R.string.message_noquestion)));
                    } else {
                        tvQuestionAns.setText(R.string.message_noquestion);
                    }
                    //tvQuestionAns.setText(R.string.message_noquestion);
                    mMenu.setGroupVisible(R.id.answer_menu_group, false);
                    btnOkAns.setVisibility(View.INVISIBLE);
                    btnCreateQAns.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("API_Category_Dialog", error.toString());

                if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                    tVMessageAns.setText(getResources().getString(R.string.api_error_offline_message));
                } else {
                    tVMessageAns.setText("Please try again");
                }

            }
        });
    }
    @OnClick(R.id.btnCreateQAns)
    public void SendToAskActivity() {
        finish();
        Intent intent = new Intent(AnswerActivity.this, AskActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.iVPostPicAns)
    public void enlargePhoto(){
        if(mPost != null) {
            Dialog d = new Dialog(AnswerActivity.this);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.dialog_post_image);// custom layour for dialog.
            ImageView iv = (ImageView) d.findViewById(R.id.ivPostPicDiag);

            if(mPost.getPicture()!=null){
                mPicasso = PicassoHelper.getInstance(AnswerActivity.this, AuthToken).getPicasso();
                mPicasso.load(AnswerActivity.this.getString(R.string.api_endpoint) + AnswerActivity.this.getString(R.string.api_endpoint_post)+mPost.getPicture())
                        .error(android.R.drawable.stat_notify_error)
                        .placeholder(android.R.drawable.stat_notify_sync)
                        .into(iv);
            }

            d.show();
        }
    }

    @OnClick(R.id.btnOkAns)
    public void RegistrationOK() {
        // TODO submit data to server...
        submitted();
        int selectedId = rGAnswerGroupAns.getCheckedRadioButtonId();
        if(selectedId>-1) {
            // find the radiobutton by returned id
            RadioButton rbAnswer = (RadioButton) findViewById(selectedId);
            String postID = (String)tvQuestionAns.getTag();
            String itemID = (String)rbAnswer.getTag();

            Vote v = new Vote();
            Post po = new Post();
            Item it = new Item();
            po.set_id(postID);
            it.set_id(itemID);
            v.setItem(it);
            v.setPost(po);
            v.setVoted_by(null);
            v.setVoted_on(null);
            final String strLinkPost = postID;

            if(itemID.equals(AppValues.ITEM_VALUE_SKIP)){
                SampleClient.getWoodyApiClient(this).postASkipVote(AuthToken, v.getPost().get_id(), v, new Callback<API_Response>() {
                    @Override
                    public void success(API_Response apiRes, Response response) {
                        if (apiRes.getMessage().equals(AnswerActivity.this.getString(R.string.reg_code_010_success))) {
                            reLoadSurprise();
                        } else {
                            tVMessageAns.setText("Please check your entry and try again later.");
                            tVMessageAns.setTextColor(getResources().getColor(R.color.red_500));
                        }
                        Log.d("regNewPost", response.toString());
                        toBeSubmitted();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                            tVMessageAns.setText(getResources().getString(R.string.api_error_offline_message));
                        } else {
                            tVMessageAns.setText("Please check your entry and try again later.");
                            tVMessageAns.setTextColor(getResources().getColor(R.color.red_500));
                            Log.d("regNewPost", error.toString());
                        }
                        toBeSubmitted();
                    }
                });
            }
            else {
                SampleClient.getWoodyApiClient(this).postAVote(AuthToken, v.getPost().get_id(), v, new Callback<API_Response>() {
                    @Override
                    public void success(API_Response apiRes, Response response) {
                        if (apiRes.getMessage().equals(AnswerActivity.this.getString(R.string.reg_code_010_success))) {
                            tVMessageAns.setText("Your vote has been submitted.");
                            tVMessageAns.setTextColor(getResources().getColor(R.color.green_400));
                            finish();
                            Intent intent = new Intent(AnswerActivity.this, ResultActivity.class);
                            intent.putExtra(ResultActivity.EXTRA_POST, strLinkPost);
                            startActivity(intent);
                        } else {
                            tVMessageAns.setText("Please check your entry and try again later.");
                            tVMessageAns.setTextColor(getResources().getColor(R.color.red_500));
                        }
                        Log.d("regNewPost", response.toString());
                        toBeSubmitted();
                        //Intent intent = new Intent(AnswerActivity.this, ResultActivity.class);
                        //intent.putExtra(ResultActivity.EXTRA_POST, strLinkPost);
                        //intent.putExtra(ResultActivity.EXTRA_POST, p);
                        //ActivityCompat.startActivity(AnswerActivity.this, intent, null);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                            tVMessageAns.setText(getResources().getString(R.string.api_error_offline_message));
                        } else {
                            tVMessageAns.setText("Please check your entry and try again later.");
                            tVMessageAns.setTextColor(getResources().getColor(R.color.red_500));
                            Log.d("regNewPost", error.toString());
                        }
                        toBeSubmitted();
                    }
                });
            }
        }
        else{
            toBeSubmitted();
        }
    }

    public void OnLoadReadSetting(){
        AppValues.getInstance().setZawGyiDisplay(UtilityHelper.getZawGyiDisplay(this));
    }

    public void submitted(){
        btnOkAns.setText(getResources().getString(R.string.button_label_loading));
        btnOkAns.setEnabled(false);
    }

    public void toBeSubmitted(){
        btnOkAns.setEnabled(true);
        btnOkAns.setText(getResources().getString(R.string.button_label_ans));
    }
}
