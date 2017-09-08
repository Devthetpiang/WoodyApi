package com.xavey.woody.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xavey.woody.R;
import com.xavey.woody.activity.ProfileActivity;
import com.xavey.woody.api.model.Comment;
import com.xavey.woody.api.model.User;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.CircleTransform;
import com.xavey.woody.helper.PicassoHelper;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TypeFaceHelper;
import com.xavey.woody.helper.UtilityHelper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tinmaungaye on 5/8/15.
 */
public class CommentAdapter extends BaseAdapter {
    private Context mContext;
    private List<Comment> mComments;
    private LayoutInflater mInflater;
    private Picasso mPicasso;

    public CommentAdapter(List<Comment> comments, Context context, String authToken) {
        this.mComments = comments;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mPicasso= PicassoHelper.getInstance(context, authToken).getPicasso();

    }

    public int getCount() {
        return mComments == null ? 0 : mComments.size();
    }

    public List<Comment> getItems() {
        return mComments;
    }

    public Comment getItem(int position) {
        return mComments == null ? null : mComments.get(position);
    }

    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        final User commentor = mComments.get(position).getCommented_by();
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.comment_items, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        if(commentor.getPicture()!=null){
            String proPath =mContext.getString(R.string.api_endpoint)+mContext.getString(R.string.api_endpoint_profile)+commentor.getPicture();
            mPicasso.load(proPath)
                    .error(R.drawable.ic_profile)
                    .placeholder(R.drawable.ic_profile)
                    .transform(new CircleTransform()).into(holder.ivCommentorCI);
            notifyDataSetChanged();
        }
        else{
            holder.ivCommentorCI.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_profile));
        }
        holder.tVCommentorCI.setText(commentor.getUser_name());
        if(AppValues.getInstance().getZawGyiDisplay()){
            holder.tVCommentCI.setText(Rabbit.uni2zg(mComments.get(position).getComment_Text()));
        }else {
            holder.tVCommentCI.setText(mComments.get(position).getComment_Text());
        }
        TypeFaceHelper.setM3TypeFace(holder.tVCommentCI, mContext);

        holder.tVCommentDateCI.setText(UtilityHelper.relativeTimespan(mComments.get(position).getCommented_on()));

        holder.tVCommentorCI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra(ProfileActivity.EXTRA_ITEM, commentor.get_id());
                mContext.startActivity(intent);
            }
        });
        holder.ivCommentorCI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ProfileActivity.class);
                intent.putExtra(ProfileActivity.EXTRA_ITEM, commentor.get_id());
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    static class ViewHolder{
        @InjectView(R.id.tVCommentorCI)
        TextView tVCommentorCI;
        @InjectView(R.id.tVCommentDateCI)
        TextView tVCommentDateCI;
        @InjectView(R.id.tVCommentCI)
        TextView tVCommentCI;
        @InjectView(R.id.ivCommentorCI)
        ImageView ivCommentorCI;
        public ViewHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }

    }
}
