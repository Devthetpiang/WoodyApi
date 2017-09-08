package com.xavey.woody.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.activity.ResultSetActivity;
import com.xavey.woody.api.model.PostSet;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TypeFaceHelper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tinmaungaye on 8/23/15.
 */
public class QuestionSetAdapter extends BaseAdapter {
    private Context mContext;
    private List<PostSet> mPosts;
    private LayoutInflater mInflater;

    public QuestionSetAdapter(List<PostSet> posts, Context context) {
        this.mPosts = posts;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mPosts == null ? 0 : mPosts.size();
    }

    public List<PostSet> getItems() {
        return mPosts;
    }

    public PostSet getItem(int position) {
        return mPosts == null ? null : mPosts.get(position);
    }

    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        final PostSet postItem = mPosts.get(position);

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.question_items, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        if(AppValues.getInstance().getZawGyiDisplay()) {
            holder.tVTitleQI.setText(Rabbit.uni2zg(postItem.getIntro()));
        }
        else{
            holder.tVTitleQI.setText(postItem.getIntro());
        }

        TypeFaceHelper.setM3TypeFace(holder.tVTitleQI, mContext);
        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        loadResultActivity(postItem.get_id());
                    }

                }
        );

        holder.tVTitleQI.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        loadResultActivity(postItem.get_id());
                    }
                }
        );
        return view;
    }

    private void loadResultActivity(String id){
        Intent intent = new Intent(mContext, ResultSetActivity.class);
        intent.putExtra(ResultSetActivity.EXTRA_POST, id);
        //intent.putExtra(ResultActivity.EXTRA_POST, p);
        mContext.startActivity(intent);
    }

    static class ViewHolder{
        @InjectView(R.id.tVTitleQI)
        TextView tVTitleQI;
        public ViewHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }
    }
}
