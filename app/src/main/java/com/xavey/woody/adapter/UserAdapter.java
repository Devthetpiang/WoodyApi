package com.xavey.woody.adapter;

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
public class UserAdapter extends BaseAdapter {
    private Context mContext;
    private List<User> mUsers;
    private LayoutInflater mInflater;
    private Picasso mPicasso;

    public UserAdapter(List<User> users, Context context, String authToken) {
        this.mUsers = users;
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        mPicasso= PicassoHelper.getInstance(context, authToken).getPicasso();
    }

    public int getCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    public List<User> getItems() {
        return mUsers;
    }

    public User getItem(int position) {
        return mUsers == null ? null : mUsers.get(position);
    }

    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        final User user = mUsers.get(position);
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.user_items, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        if(user.getPicture()!=null){
            String proPath =mContext.getString(R.string.api_endpoint)+mContext.getString(R.string.api_endpoint_profile)+user.getPicture();
            mPicasso.load(proPath)
                    .error(R.drawable.ic_profile)
                    .placeholder(R.drawable.ic_profile)
                    .transform(new CircleTransform()).into(holder.iVUserUI);
            notifyDataSetChanged();
        }
        else{
            holder.iVUserUI.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_profile));
        }

        holder.tVUserUI.setText("@" + user.getUser_name());
        holder.tVNameUI.setText(user.getFull_name());
        if (AppValues.getInstance().getZawGyiDisplay()) {
            holder.tVNameUI.setText(Rabbit.uni2zg(user.getFull_name()));
        }
        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        loadProfile(user.get_id());
                    }

                }
        );
        holder.tVUserUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadProfile(user.get_id());
            }
        });

        return view;
    }

    private void loadProfile(String id){
        Intent intent = new Intent(mContext, ProfileActivity.class);
        intent.putExtra(ProfileActivity.EXTRA_ITEM, id);
        mContext.startActivity(intent);
    }

    static class ViewHolder{
        @InjectView(R.id.tVUserUI)
        TextView tVUserUI;
        @InjectView(R.id.tVNameUI)
        TextView tVNameUI;
        @InjectView(R.id.iVUserUI)
        ImageView iVUserUI;

        public ViewHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }

    }
}
