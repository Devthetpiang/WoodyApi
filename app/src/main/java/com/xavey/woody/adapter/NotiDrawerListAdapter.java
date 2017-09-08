package com.xavey.woody.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.activity.EditProfileActivity;
import com.xavey.woody.activity.InviteActivity;
import com.xavey.woody.activity.ResultActivity;
import com.xavey.woody.activity.RewardActivity;
import com.xavey.woody.api.model.Notification;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.DBHelper;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TypeFaceHelper;
import com.xavey.woody.helper.UtilityHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tinmaungaye on 5/7/15.
 */
public class NotiDrawerListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<Notification> navDrawerItems;

        public NotiDrawerListAdapter(Context context, ArrayList<Notification> navDrawerItems){
            this.context = context;
            this.navDrawerItems = navDrawerItems;
        }

        @Override
        public int getCount() {
            return navDrawerItems.size();
        }

        @Override
        public Object getItem(int position) {
            return navDrawerItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.noti_list_item, null);
            }

            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.notiIcon);
            TextView txtTitle = (TextView) convertView.findViewById(R.id.notiTitle);
            TextView txtTime = (TextView) convertView.findViewById(R.id.notiTime);
            String title = navDrawerItems.get(position).getTitle();
            if(title.length()>50){
                title = title.substring(0,50) + "...";
            }

            final Notification n = navDrawerItems.get(position);

            txtTime.setText(UtilityHelper.relativeTimespan(n.getCreated_on()));

            if(n.getType().toLowerCase().equals(context.getString(R.string.Noti_OwnPostComment).toLowerCase())){
                imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_post_comment));
                txtTitle.setText(context.getString(R.string.Noti_OwnPostComment_Label) + " \"" + title +"\"");
            }
            else if(n.getType().toLowerCase().equals(context.getString(R.string.Noti_FavPostComment).toLowerCase())){
                imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_post_comment));
                txtTitle.setText(context.getString(R.string.Noti_FavPostComment_Label) + " \"" + title +"\"");
            }
            else if(n.getType().toLowerCase().equals(context.getString(R.string.Noti_ComPostComment).toLowerCase())){
                imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_post_comment));
                txtTitle.setText(context.getString(R.string.Noti_ComPostComment_Label) + " \"" + title +"\"");
            }
            else if(n.getType().toLowerCase().equals(context.getString(R.string.Noti_FollowUserPost).toLowerCase())){
                imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_group_post));
                txtTitle.setText(context.getString(R.string.Noti_FollowUserPost_Label) +" \"" + title +"\"");
            }
            else if(n.getType().toLowerCase().equals(context.getString(R.string.Noti_OwnPostVote).toLowerCase())){
                imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_post_vote));
                txtTitle.setText(context.getString(R.string.Noti_OwnPostVote_Label) + " \"" + title +"\"");
            }
            else if(n.getType().toLowerCase().equals(context.getString(R.string.Noti_FavPostVote).toLowerCase())){
                imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_post_vote));
                txtTitle.setText(context.getString(R.string.Noti_FavPostVote_Label) + " \"" + title +"\"");
            }
            else if(n.getType().toLowerCase().equals(context.getString(R.string.Noti_WebLink).toLowerCase())){
                imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_post_comment));
                txtTitle.setText(title);
            }
            else if(n.getType().toLowerCase().equals(context.getString(R.string.Noti_InviteJoined).toLowerCase())){
                imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_group_post));
                txtTitle.setText(context.getString(R.string.Noti_InviteJoined_Label));
            }
            else if(n.getType().toLowerCase().equals(context.getString(R.string.Noti_FacebookConnect).toLowerCase())){
                imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_facebook));
                txtTitle.setText(context.getString(R.string.Noti_FacebookConnect_Label));
            }
            else if(n.getType().toLowerCase().equals(context.getString(R.string.Noti_FacebookInvite).toLowerCase())){
                imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_facebook));
                txtTitle.setText(context.getString(R.string.Noti_FacebookInvite_Label));
            }
            else if(n.getType().toLowerCase().equals(context.getString(R.string.Noti_RewardRedeem).toLowerCase())){
                imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_reward));
                txtTitle.setText(context.getString(R.string.Noti_RewardRedeem_Label));
            }
            else if(n.getType().toLowerCase().equals(context.getString(R.string.Noti_CompleteProfile).toLowerCase())){
                imgIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_profile));
                txtTitle.setText(context.getString(R.string.Noti_CompleteProfile_Label));
            }

            TypeFaceHelper.setM3TypeFace(txtTitle, context);
            if (AppValues.getInstance().getZawGyiDisplay()) {
                txtTitle.setText(Rabbit.uni2zg(txtTitle.getText().toString()));
            }

            if(!n.getChecked()){
                convertView.setBackgroundColor(context.getResources().getColor(R.color.amber_50));
            }
            else{
                convertView.setBackgroundColor(context.getResources().getColor(R.color.grey_300));
            }

            convertView.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            loadResult(n.getPost(),n.getCreated_on(),n.getType());
                        }

                    }
            );

            return convertView;
        }

    private void loadResult(String post,Date last_date, String type){
        DBHelper dbHelper  = new DBHelper(context);
        try {
            dbHelper.updateCheckedNoti(post,last_date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(type.toLowerCase().equals(context.getString(R.string.Noti_WebLink).toLowerCase())){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(post));
            context.startActivity(browserIntent);
        }
        else if(type.toLowerCase().equals(context.getString(R.string.Noti_InviteJoined).toLowerCase())){
            Intent intent = new Intent(context, InviteActivity.class);
            context.startActivity(intent);
        }
        else if(type.toLowerCase().equals(context.getString(R.string.Noti_FacebookConnect).toLowerCase())) {
            Intent intent = new Intent(context, InviteActivity.class);
            intent.putExtra(InviteActivity.EXTRA_LINK,InviteActivity.EXTRA_LINK_ACTION_FB);
            context.startActivity(intent);
        }
        else if(type.toLowerCase().equals(context.getString(R.string.Noti_FacebookConnect).toLowerCase())) {
            Intent intent = new Intent(context, InviteActivity.class);
            intent.putExtra(InviteActivity.EXTRA_LINK,InviteActivity.EXTRA_LINK_ACTION_FB);
            context.startActivity(intent);
        }
        else if(type.toLowerCase().equals(context.getString(R.string.Noti_RewardRedeem).toLowerCase())) {
            Intent intent = new Intent(context, RewardActivity.class);
            context.startActivity(intent);
        }
        else if(type.toLowerCase().equals(context.getString(R.string.Noti_CompleteProfile).toLowerCase())) {
            Intent intent = new Intent(context, EditProfileActivity.class);
            context.startActivity(intent);
        }
        else {
            Intent intent = new Intent(context, ResultActivity.class);
            intent.putExtra(ResultActivity.EXTRA_POST, post);
            context.startActivity(intent);
        }
    }

}
