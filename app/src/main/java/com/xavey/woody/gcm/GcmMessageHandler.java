package com.xavey.woody.gcm;

/**
 * Created by tinmaungaye on 7/23/15.
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.xavey.woody.R;
import com.xavey.woody.activity.EditProfileActivity;
import com.xavey.woody.activity.InviteActivity;
import com.xavey.woody.activity.ResultActivity;
import com.xavey.woody.activity.RewardActivity;
import com.xavey.woody.api.model.Reward;

public class GcmMessageHandler extends IntentService {

    private Handler handler;
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    String post = "";
    String title = "";
    String type = "";
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

         post = extras.getString("post");
         title = extras.getString("title");
         type = extras.getString("type");
        if (!extras.isEmpty()) {
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                    .equals(messageType)) {
                sendNotification();
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification() {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher);

        Intent myintent = new Intent(this, ResultActivity.class);
        myintent.putExtra(ResultActivity.EXTRA_POST, post);

        if(type.toLowerCase().equals(this.getString(R.string.Noti_WebLink).toLowerCase())) {
            myintent = new Intent(Intent.ACTION_VIEW, Uri.parse(post));
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_InviteJoined).toLowerCase())) {
            myintent = new Intent(this, InviteActivity.class);
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_FacebookConnect).toLowerCase())) {
            myintent = new Intent(this, InviteActivity.class);
            myintent.putExtra(InviteActivity.EXTRA_LINK,InviteActivity.EXTRA_LINK_ACTION_FB);
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_FacebookConnect).toLowerCase())) {
            myintent = new Intent(this, InviteActivity.class);
            myintent.putExtra(InviteActivity.EXTRA_LINK,InviteActivity.EXTRA_LINK_ACTION_FB);
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_RewardRedeem).toLowerCase())) {
            myintent = new Intent(this, RewardActivity.class);
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_CompleteProfile).toLowerCase())) {
            myintent = new Intent(this, EditProfileActivity.class);
        }


            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                myintent, PendingIntent.FLAG_UPDATE_CURRENT);

        //SpannableString sb = new SpannableString(title);
        //sb.setSpan(new CustomTypefaceSpan(TypeFaceHelper.getCurrentTypeFace(this.getAssets())), 0, title.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        if(type.toLowerCase().equals(getString(R.string.Noti_OwnPostComment).toLowerCase())){
            title = getString(R.string.Noti_OwnPostComment_Label) + ".";
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_FavPostComment).toLowerCase())){
            title = getString(R.string.Noti_FavPostComment_Label) + ".";
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_ComPostComment).toLowerCase())){
            title = getString(R.string.Noti_ComPostComment_Label) + ".";
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_FollowUserPost).toLowerCase())){
            title = getString(R.string.Noti_FollowUserPost_Label)  + ".";
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_OwnPostVote).toLowerCase())){
            title = getString(R.string.Noti_OwnPostVote_Label) + ".";
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_FavPostVote).toLowerCase())){
            title = getString(R.string.Noti_FavPostVote_Label) + ".";
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_WebLink).toLowerCase())) {
            title = title;
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_InviteJoined).toLowerCase())){
            title = getString(R.string.Noti_InviteJoined_Label) + ".";
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_FacebookInvite).toLowerCase())){
            title = getString(R.string.Noti_FacebookInvite_Label) + ".";
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_FacebookConnect).toLowerCase())){
            title = getString(R.string.Noti_FacebookConnect_Label) + ".";
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_RewardRedeem).toLowerCase())){
            title = getString(R.string.Noti_RewardRedeem_Label) + ".";
        }
        else if(type.toLowerCase().equals(getString(R.string.Noti_CompleteProfile).toLowerCase())){
            title = getString(R.string.Noti_CompleteProfile_Label) + ".";
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher_noti)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(getString(R.string.about_title) + " : " + getString(R.string.about_tag))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(title))
                        .setContentText(title);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
