package com.xavey.woody.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;
import com.squareup.picasso.Picasso;
import com.xavey.woody.R;
import com.xavey.woody.adapter.CommentAdapter;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.API_Response;
import com.xavey.woody.api.model.Comment;
import com.xavey.woody.api.model.Comments;
import com.xavey.woody.api.model.Like;
import com.xavey.woody.api.model.Post;
import com.xavey.woody.api.model.PostResult;
import com.xavey.woody.fragment.CommentDialogFragment;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.CircleTransform;
import com.xavey.woody.helper.DBHelper;
import com.xavey.woody.helper.PicassoHelper;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TokenHelper;
import com.xavey.woody.helper.TypeFaceHelper;
import com.xavey.woody.helper.UtilityHelper;
import com.xavey.woody.helper.WValueFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tinmaungaye on 5/6/15.
 */
public class ResultActivity extends BaseActivity implements CommentDialogFragment.OnCommentSettedCallback, OnChartValueSelectedListener {

    String AuthToken="";
    private TextView donutSizeTextView;

    public static final String EXTRA_USER = "ResultActivity.EXTRA_USER";
    public static final String EXTRA_POST = "ResultActivity.EXTRA_POST";
    public static final String EXTRA_ITEM = "DetailActivity.EXTRA_ITEM";
    public static final String EXTRA_KILL_CACHE = "ResultActivity.EXTRA_KILL_CACHE";
    private PieChart mChart;
    private int mTotalCount=0;

    String mItem= "";
    String mPostID="";
    Post mPost =  new Post();
    int pagi=1;
    Comments mComments= new Comments();

    CommentDialogFragment commentDialog;
    FragmentManager fm;

    @InjectView(R.id.btnOkRes)
    Button btnOkRes;

    @InjectView(R.id.tvQuestionRes)
    TextView tvQuestionRes;

    @InjectView(R.id.tvQuestionRes2)
    TextView tvQuestionRes2;

    @InjectView(R.id.iBCommentRs)
    ImageView iBCommentRs;

    @InjectView(R.id.iBLikeRes)
    ImageView iBLikeRes;

    @InjectView(R.id.iBShareRes)
    ImageView iBShareRes;

    @InjectView(R.id.iBCreatedBy)
    ImageView iBCreatedBy;

    @InjectView(R.id.lvCommentsRes)
    ListView lvCommentsRes;

    @InjectView(R.id.iVPostPicRes)
    ImageView iVPostPicRes;

    @InjectView(R.id.flWithImage)
    FrameLayout flWithImage;

    @InjectView(R.id.flWithoutImage)
    FrameLayout flWithoutImage;

    @InjectView(R.id.glButtons)
    GridLayout glButtons;

    private Picasso mPicasso;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    CommentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);

         mItem= getIntent().getStringExtra(EXTRA_ITEM);
         mPostID= getIntent().getStringExtra(EXTRA_POST);
         UtilityHelper.hideSoftKeyboard(ResultActivity.this);
        try {
            AuthToken= TokenHelper.genAPIToken(this, ResultActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(AuthToken!="" && mPostID!="") {
            ButterKnife.inject(this);
            getMellPoint(this, AuthToken);
            iVPostPicRes.setVisibility(View.INVISIBLE);
            getNotification(this, AuthToken);
            //syncOptOutCatgories(this,AuthToken);

            lvCommentsRes.setOnTouchListener(new View.OnTouchListener() {
                // Setting on Touch Listener for handling the touch inside ScrollView
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // Disallow the touch request for parent scroll on touch of child view
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    return false;
                }
            });

            DBHelper dbHelper = new DBHelper(this);
            try {
                dbHelper.updateCheckedNoti(mPostID,new Date());
            } catch (Exception e) {
                e.printStackTrace();
            }

            fm = getSupportFragmentManager();
            commentDialog = new CommentDialogFragment();
            adapter = new CommentAdapter(new ArrayList<Comment>(),this,AuthToken);
            lvCommentsRes.setAdapter(adapter);

            fetchResults();
            loadComments();
        }
    }

    @Override
    public void OnCommentSetted(Boolean isSubmittedSuccess) {
        if(isSubmittedSuccess){
            //load comments again
            getMellPoint(this,AuthToken);
            loadComments();
        }
    }

    protected void fetchResults() {
        SampleClient.getWoodyApiClient(this).getVoteResults(this.AuthToken, mPostID, new Callback<PostResult>() {
            @Override
            public void success(PostResult pResult, Response response) {
                mPost = pResult.getPost();

                if (pResult.getPost().getStatus().toLowerCase()!="archive" && pResult.getVoted() == "") {
                    finish();
                    Intent intent = new Intent(ResultActivity.this, AnswerActivity.class);
                    intent.putExtra(AnswerActivity.EXTRA_POST, mPost.get_id());
                    ActivityCompat.startActivity(ResultActivity.this, intent, null);
                } else {

                    glButtons.setVisibility(View.VISIBLE);
                    btnOkRes.setVisibility(View.VISIBLE);
                    lvCommentsRes.setVisibility(View.VISIBLE);
                    findViewById(R.id.pieChart).setVisibility(View.VISIBLE);
                    //TODO: highlight voted item.... pResult.getVote_item_id()...

                    if (pResult.getLiked() != "") {
                        iBLikeRes.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked));
                        iBLikeRes.setTag(pResult.getLiked());
                    } else {
                        iBLikeRes.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                    }
                }
                mPicasso = PicassoHelper.getInstance(ResultActivity.this, AuthToken).getPicasso();
                if(mPost.getCreated_by().getPicture()!=null){
                    String proPath = ResultActivity.this.getString(R.string.api_endpoint) + ResultActivity.this.getString(R.string.api_endpoint_profile) + mPost.getCreated_by().getPicture();
                    mPicasso.load(proPath)
                            .error(R.drawable.ic_profile)
                            .placeholder(R.drawable.ic_profile)
                            .transform(new CircleTransform()).into(iBCreatedBy);
                }
                TypeFaceHelper.setM3TypeFace(tvQuestionRes, ResultActivity.this);
                TypeFaceHelper.setM3TypeFace(tvQuestionRes2, ResultActivity.this);

                if (AppValues.getInstance().getZawGyiDisplay()) {
                    tvQuestionRes.setText(Rabbit.uni2zg(mPost.getTitle()));
                    tvQuestionRes2.setText(Rabbit.uni2zg(mPost.getTitle()));
                } else {
                    tvQuestionRes.setText(mPost.getTitle());
                    tvQuestionRes2.setText(mPost.getTitle());
                }

                if (mPost.getPicture() != null) {
                    iVPostPicRes.setVisibility(View.VISIBLE);
                    String picPath = ResultActivity.this.getString(R.string.api_endpoint) + ResultActivity.this.getString(R.string.api_endpoint_post) + mPost.getPicture();
                    mPicasso.load(picPath)
                            .error(android.R.drawable.stat_notify_error)
                            .placeholder(android.R.drawable.stat_notify_sync)
                            .into(iVPostPicRes);

                    flWithoutImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                } else {
                    flWithImage.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                }

                renderPieChart();
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

    private void loadComments(){
        SampleClient.getWoodyApiClient(this).getComment(this.AuthToken, mPostID, pagi, new Callback<Comments>() {
            @Override
            public void success(Comments coms, Response response) {
                mComments.setLimit(coms.getLimit());
                mComments.setPage(coms.getPage());
                mComments.setTotal(coms.getTotal());
                if (mComments.getComments().size() == 0) {
                    mComments.getComments().addAll(coms.getComments());
                } else {
                    for (Comment com : coms.getComments()) {
                        if (!mComments.getComments().contains(com)) {
                            mComments.getComments().add(com);
                        }
                    }
                }
                Collections.sort(mComments.getComments());
                adapter.getItems().clear();
                adapter.getItems().addAll(mComments.getComments());
                adapter.notifyDataSetChanged();
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

    @OnClick(R.id.iBShareRes)
    public void ShareResult() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.app_share_msg)+" " +getResources().getString(R.string.app_share_link)+mPost.get_id());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share via"));
    }

    @OnClick(R.id.iBCommentRs)
    public void ShowPostComment() {
        commentDialog.setConx(ResultActivity.this);
        commentDialog.setPost(mPost);
        commentDialog.setAuthToken(this.AuthToken);
        commentDialog.show(fm, "fragment_post_comment");

    }

    @OnItemClick(R.id.lvCommentsRes) void onItemClick(int position) {

    }

    public void renderPieChart(){
        mChart = (PieChart) findViewById(R.id.pieChart);
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setPadding(0,0,0,0);

        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawSliceText(false);

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);

        mChart.setTransparentCircleColor(Color.WHITE);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);

        //mChart.setCenterText("MPAndroidChart\nby Philipp Jahoda");

        setData();

        mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        l.setXEntrySpace(20f);
        l.setYEntrySpace(10f);
        //l.setTypeface(TypeFaceHelper.getUniTypeFace(ResultActivity.this));
    }

    @OnClick(R.id.iBCreatedBy)
    public void CreatedByClicked(){
        if(mPost!=null) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(ProfileActivity.EXTRA_ITEM, mPost.getCreated_by().get_id());
            startActivity(intent);
        }
    }

    @OnClick(R.id.iBLikeRes)
    public void LikeClicked() {
        if(iBLikeRes.getTag()==null){
            //havnt liked yet
            Like l = new Like();
            Post po = new Post();
            po.set_id(mPostID);
            l.setPost(po);
            l.setLiked_by(null);
            l.setLiked_on(null);
            SampleClient.getWoodyApiClient(this).postALike(this.AuthToken, mPostID,l,new Callback<API_Response>() {
                @Override
                public void success(API_Response api_response, Response response) {
                    if(api_response.getMessage().equals("010")){
                        iBLikeRes.setImageDrawable(getResources().getDrawable(R.drawable.ic_liked));
                        iBLikeRes.setTag(api_response.getExtra());
                        getMellPoint(ResultActivity.this,AuthToken);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("API_Result", error.toString());
                    if(error.getMessage().equals(getResources().getString(R.string.api_error_offline))){
                        Toast bread = Toast.makeText(getApplicationContext(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                        bread.show();
                    }
                }
            });
        }
        else{
            SampleClient.getWoodyApiClient(this).deleteALike(this.AuthToken, mPostID, (String)iBLikeRes.getTag(),new Callback<API_Response>() {
                @Override
                public void success(API_Response api_response, Response response) {
                    if(api_response.getMessage().equals("010")){
                        iBLikeRes.setImageDrawable(getResources().getDrawable(R.drawable.ic_like));
                        iBLikeRes.setTag(null);
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("API_Result", error.toString());
                    if(error.getMessage().equals(getResources().getString(R.string.api_error_offline))){
                        Toast bread = Toast.makeText(getApplicationContext(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                        bread.show();
                    }

                }
            });
        }

    }

    @OnClick(R.id.btnOkRes)
    public void ResultOK() {
        finish();
        Intent intent = new Intent(this, AnswerActivity.class);
        startActivity(intent);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void setData() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < mPost.getItems().length; i++) {
            mTotalCount +=mPost.getItems()[i].getVote_count();
            yVals1.add(new Entry((float) mPost.getItems()[i].getVote_count() , i));

            String iTitle = mPost.getItems()[i].getTitle();
            //try to break the line
            if(iTitle.length()>30) {
                int iSpace = iTitle.indexOf(" ", 25);
                if (iSpace > -1) {
                    iTitle = iTitle.substring(0, iSpace) + "...";
                    if (iTitle.length() > 30) {
                        iTitle = iTitle.substring(0, 25) + "...";
                    }
                } else {
                    iTitle = iTitle.substring(0, 25) + "...";
                }
            }
            if(AppValues.getInstance().getZawGyiDisplay()){
                iTitle=Rabbit.uni2zg(iTitle);
            }
            xVals.add(iTitle);
            colors.add(getResources().getColor(UtilityHelper.itemColors[i]));
        }

        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        //dataSet.setValueTypeface(TypeFaceHelper.getUniTypeFace(ResultActivity.this));

        dataSet.setColors(colors);
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new WValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        //data.setValueTypeface(TypeFaceHelper.getUniTypeFace(ResultActivity.this));
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);
        mChart.setCenterText(String.valueOf(mTotalCount) + " Votes");
        mChart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;

        //mChart.setCenterText(String.valueOf(mPost.getItems()[dataSetIndex].getVote_count()) + " Votes");
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
        //mChart.setCenterText(String.valueOf(mTotalCount) + " Votes");

    }

    @OnClick(R.id.iVPostPicRes)
    public void enlargePhoto(){
        if(mPost != null) {
            Dialog d = new Dialog(ResultActivity.this);
            d.requestWindowFeature(Window.FEATURE_NO_TITLE);
            d.setContentView(R.layout.dialog_post_image);// custom layour for dialog.
            ImageView iv = (ImageView) d.findViewById(R.id.ivPostPicDiag);

            if(mPost.getPicture()!=null){
                mPicasso = PicassoHelper.getInstance(ResultActivity.this, AuthToken).getPicasso();
                mPicasso.load(ResultActivity.this.getString(R.string.api_endpoint) + ResultActivity.this.getString(R.string.api_endpoint_post)+mPost.getPicture())
                        .error(android.R.drawable.stat_notify_error)
                        .placeholder(android.R.drawable.stat_notify_sync)
                        .into(iv);
            }

            d.show();
        }
    }
}
