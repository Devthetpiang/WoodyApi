package com.xavey.woody.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xavey.woody.R;
import com.xavey.woody.activity.ProfileActivity;
import com.xavey.woody.api.model.TaggableFriend;
import com.xavey.woody.api.model.User;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.CircleTransform;
import com.xavey.woody.helper.PicassoHelper;
import com.xavey.woody.helper.Rabbit;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tinmaungaye on 5/8/15.
 */
public class FriendAdapter extends BaseAdapter {
    private Context mContext;
    public List<TaggableFriend> mUsers;
    private LayoutInflater mInflater;
    private Picasso mPicasso;

    public FriendAdapter(List<TaggableFriend> users, Context context) {
        this.mUsers = users;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mPicasso= PicassoHelper.getInstance(context, "").getPicasso();
    }

    public int getCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    public List<TaggableFriend> getItems() {
        return mUsers;
    }

    public TaggableFriend getItem(int position) {
        return mUsers == null ? null : mUsers.get(position);
    }

    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        final TaggableFriend user = mUsers.get(position);
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.friend_items, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        if(user.getPicture()!=null){
            String proPath = user.getPicture().getData().getUrl();
            mPicasso.load(proPath)
                    .error(R.drawable.ic_profile)
                    .placeholder(R.drawable.ic_profile)
                    .transform(new CircleTransform()).into(holder.iVFB);
            notifyDataSetChanged();
        }
        else{
            holder.iVFB.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_profile));
        }

        //holder.tVUserUI.setText("@" + user.getName());
        holder.tVFB.setText(user.getName());
        if (AppValues.getInstance().getZawGyiDisplay()) {
            holder.tVFB.setText(Rabbit.uni2zg(user.getName()));
        }

        holder.cKFB.setChecked(mUsers.get(position).getSelected());

        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        checkMe(holder.cKFB,position);
                    }

                }
        );
        holder.tVFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkMe(holder.cKFB,position);
            }
        });
        holder.iVFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkMe(holder.cKFB,position);
            }
        });

        return view;
    }

    private void checkMe(CheckBox cb,int pos){
        this.mUsers.get(pos).setSelected(!cb.isChecked());
        cb.setChecked(!cb.isChecked());
    }

    static class ViewHolder{
        @InjectView(R.id.tVFB)
        TextView tVFB;

        @InjectView(R.id.cKFB)
        CheckBox cKFB;

        @InjectView(R.id.iVFB)
        ImageView iVFB;

        public ViewHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }

    }
}
