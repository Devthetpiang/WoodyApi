package com.xavey.woody.activity;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.xavey.woody.R;
import com.xavey.woody.adapter.CommentAdapter;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.Comments;
import com.xavey.woody.api.model.Post;
import com.xavey.woody.api.model.PostSet;
import com.xavey.woody.api.model.PostSetResult;
import com.xavey.woody.fragment.CommentDialogFragment;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.DBHelper;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TokenHelper;
import com.xavey.woody.helper.TypeFaceHelper;
import com.xavey.woody.helper.UtilityHelper;
import com.xavey.woody.helper.WValueFormatter;

import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tinmaungaye on 20/8/15.
 */
public class ResultSetActivity extends BaseActivity {

    String AuthToken="";
    private TextView donutSizeTextView;

    public static final String EXTRA_USER = "ResultSetActivity.EXTRA_USER";
    public static final String EXTRA_POST = "ResultSetActivity.EXTRA_POST";
    public static final String EXTRA_ITEM = "DetailSetActivity.EXTRA_ITEM";
    public static final String EXTRA_KILL_CACHE = "ResultSetActivity.EXTRA_KILL_CACHE";
    private PieChart mChart;
    private int mTotalCount=0;

    String mItem= "";
    String mPostSetID="";
    PostSet mPostSet =  new PostSet();
    int pagi=1;
    Comments mComments= new Comments();

    CommentDialogFragment commentDialog;
    FragmentManager fm;

    @InjectView(R.id.tvQuestionRes_Set)
    TextView tvQuestionRes_Set;

    @InjectView(R.id.rlMessageOnlySet)
    RelativeLayout rlMessageOnlySet;

    @InjectView(R.id.rlResultChartSet)
    RelativeLayout rlResultChartSet;

    @InjectView(R.id.txtMessageOnlySet)
    TextView txtMessageOnlySet;

    @InjectView(R.id.llPieCharts)
    LinearLayout llPieCharts;

    @InjectView(R.id.btnOkResSet)
    Button btnOkResSet;

    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    CommentAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_set);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);
        set(navMenuTitles, navMenuIcons);

        mItem= getIntent().getStringExtra(EXTRA_ITEM);
        mPostSetID= getIntent().getStringExtra(EXTRA_POST);
        UtilityHelper.hideSoftKeyboard(ResultSetActivity.this);

        try {
            AuthToken= TokenHelper.genAPIToken(this, ResultSetActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(AuthToken!="" && mPostSetID!="") {
            ButterKnife.inject(this);
            getMellPoint(this,AuthToken);
            getNotification(this, AuthToken);

            DBHelper dbHelper = new DBHelper(this);
            try {
                dbHelper.updateCheckedNoti(mPostSetID,new Date());
            } catch (Exception e) {
                e.printStackTrace();
            }
            fm = getSupportFragmentManager();
            fetchResults();
        }
    }

    protected void fetchResults() {
        SampleClient.getWoodyApiClient(this).getVoteSetResults(this.AuthToken, mPostSetID, new Callback<PostSetResult>() {
            @Override
            public void success(PostSetResult pResult, Response response) {
                //mPost = pResult.getPost();
                if (pResult.getPostset().getStatus().toLowerCase() != "archive" && pResult.getVoted().equals("") && pResult.getOwned().equals("")) {
                    finish();
                    Intent intent = new Intent(ResultSetActivity.this, AnswerSetActivity.class);
                    intent.putExtra(AnswerSetActivity.EXTRA_POSTSET, pResult.getPostset().get_id());
                    ActivityCompat.startActivity(ResultSetActivity.this, intent, null);
                } else if (!pResult.getVoted().equals("") && pResult.getOwned().equals("")) {
                    //voted and not owner
                    rlResultChartSet.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    TypeFaceHelper.setM3TypeFace(txtMessageOnlySet, ResultSetActivity.this);
                    String message = getResources().getString(R.string.message_previously_answered);
                    if (AppValues.getInstance().getZawGyiDisplay()) {
                        txtMessageOnlySet.setText(Rabbit.uni2zg(message));
                    } else {
                        txtMessageOnlySet.setText(message);
                    }
                    btnOkResSet.setVisibility(View.VISIBLE);
                } else if (!pResult.getOwned().equals("")) {
                    //owner
                    rlMessageOnlySet.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
                    TypeFaceHelper.setM3TypeFace(tvQuestionRes_Set, ResultSetActivity.this);

                    if (AppValues.getInstance().getZawGyiDisplay()) {
                        tvQuestionRes_Set.setText(Rabbit.uni2zg(pResult.getPostset().getIntro()));
                    } else {
                        tvQuestionRes_Set.setText(pResult.getPostset().getIntro());
                    }

                    llPieCharts.setMinimumHeight((int) getResources().getDimension(R.dimen.pie_segment_container_height) * pResult.getPostset().getPosts().length);
                    for (Post p : pResult.getPostset().getPosts()) {

                        mTotalCount = 0;
                        renderPieChart(p);
                    }
                    btnOkResSet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                btnOkResSet.setVisibility(View.VISIBLE);
                Log.d("API_Result", error.toString());
                if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                    Toast bread = Toast.makeText(getApplicationContext(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                    bread.show();
                }
            }
        });
    }

    public void renderPieChart(Post p){
        mChart = new PieChart(this);
        mChart.setMinimumHeight((int) getResources().getDimension(R.dimen.pie_segment_container_height));
        TextView tv = new TextView(this);
        if (AppValues.getInstance().getZawGyiDisplay()) {
            tv.setText(Rabbit.uni2zg(p.getTitle()));
        } else {
            tv.setText(p.getTitle());
        }
        tv.setPadding(0,20,0,40);
        llPieCharts.addView(tv);
        llPieCharts.addView(mChart);

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
       // mChart.setOnChartValueSelectedListener(this);

        //mChart.setCenterText("MPAndroidChart\nby Philipp Jahoda");

        setData(p);

        mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        l.setXEntrySpace(20f);
        l.setYEntrySpace(10f);
        //l.setTypeface(TypeFaceHelper.getUniTypeFace(ResultSetActivity.this));
    }

    private void setData(Post mPost) {
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
        //dataSet.setValueTypeface(TypeFaceHelper.getUniTypeFace(ResultSetActivity.this));

        dataSet.setColors(colors);
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new WValueFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        //data.setValueTypeface(TypeFaceHelper.getUniTypeFace(ResultSetActivity.this));
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);
        mChart.setCenterText(String.valueOf(mTotalCount) + " Votes");
        mChart.invalidate();
    }

    @OnClick(R.id.btnOkResSet)
    public void ResultOK() {
        finish();
        Intent intent = new Intent(this, PartnerActivity.class);
        startActivity(intent);
    }

}
