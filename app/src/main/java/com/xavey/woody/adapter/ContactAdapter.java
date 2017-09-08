package com.xavey.woody.adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.api.model.InternalContact;
import com.xavey.woody.fragment.InviteDialogFragment;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TypeFaceHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tinmaungaye on 8/27/15.
 */
public class ContactAdapter extends BaseAdapter {
    private Context mContext;
    private FragmentActivity mFActivity;
    private ArrayList<InternalContact> mContacts;
    private String APIKey;
    private LayoutInflater mInflater;
    private InviteDialogFragment inviteDialog;
    private FragmentManager fm;


    public ContactAdapter(ArrayList<InternalContact> contacts, Context context, FragmentActivity fact) {
        this.mContacts = contacts;
        this.mContext = context;
        this.mFActivity = fact;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mContacts == null ? 0 : mContacts.size();
    }

    public List<InternalContact> getItems() {
        return mContacts;
    }

    public InternalContact getItem(int position) {
        return mContacts == null ? null : mContacts.get(position);
    }

    public long getItemId(int position) {
        return -1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        final InternalContact contact = mContacts.get(position);
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.contact_items, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        if(contact.getPicture()!=null){
            holder.ivContactContI.setImageURI((Uri.parse(contact.getPicture())));
            notifyDataSetChanged();
        }
        else{
            holder.ivContactContI.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_profile));
        }

        holder.tVContactNameContI.setText(contact.getName());
        holder.tVContactNoContI.setText(contact.getPhone());
        TypeFaceHelper.setM3TypeFace(holder.tVContactNameContI, mContext);
        TypeFaceHelper.setM3TypeFace(holder.tVContactNoContI, mContext);

        if(AppValues.getInstance().getZawGyiDisplay()){
            holder.tVContactNameContI.setText(Rabbit.uni2zg(contact.getName()));
        } else {
            holder.tVContactNameContI.setText(contact.getName());
        }

        holder.btnInviteContI.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ShowInviteDailog(contact.getPhone());
            }
        });

        return view;
    }

    public void ShowInviteDailog(String phoneNo) {
        fm = mFActivity.getSupportFragmentManager();
        inviteDialog = new InviteDialogFragment();
        inviteDialog.setPhone(phoneNo);
        inviteDialog.show(fm, "fragment_invite_sms");
    }

    static class ViewHolder{
        @InjectView(R.id.tVContactNoContI)
        TextView tVContactNoContI;
        @InjectView(R.id.tVContactNameContI)
        TextView tVContactNameContI;
        @InjectView(R.id.ivContactContI)
        ImageView ivContactContI;
        @InjectView(R.id.btnInviteContI)
        Button btnInviteContI;

        public ViewHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }

    }
}
