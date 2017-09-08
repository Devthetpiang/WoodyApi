package com.xavey.woody.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xavey.woody.R;
import com.xavey.woody.api.model.User;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.CircleTransform;
import com.xavey.woody.helper.PicassoHelper;
import com.xavey.woody.helper.Rabbit;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tinmaungaye on 8/30/15.
 */
public class PartnerAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private List<User> data;
    private Picasso mPicasso;

    public PartnerAdapter(Context context, int layoutResourceId, List<User> data, String authToken) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        mPicasso= PicassoHelper.getInstance(context, authToken).getPicasso();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder(row);
            /*holder.tvPartner = (TextView) row.findViewById(R.id.text);
            holder.ivPartner = (ImageView) row.findViewById(R.id.image);*/
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        User item = data.get(position);
        holder.tvPartner.setText("@"+item.getUser_name());
        if (AppValues.getInstance().getZawGyiDisplay()) {
            holder.tvPartnerFull.setText(Rabbit.uni2zg(item.getFull_name()));
        } else {
            holder.tvPartnerFull.setText(item.getFull_name());
        }
        if(item.getPicture()!=null){
            String proPath =context.getString(R.string.api_endpoint)+context.getString(R.string.api_endpoint_profile)+item.getPicture();
            mPicasso.load(proPath)
                    .error(R.drawable.ic_profile)
                    .placeholder(R.drawable.ic_profile)
                    .transform(new CircleTransform()).into(holder.ivPartner);
            //notifyDataSetChanged();
        }
        else{
            holder.ivPartner.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_profile));
        }
        return row;
    }

    static class ViewHolder {
        @InjectView(R.id.tvPartner)
        TextView tvPartner;
        @InjectView(R.id.tvPartnerFull)
        TextView tvPartnerFull;
        @InjectView(R.id.ivPartner)
        ImageView ivPartner;

        public ViewHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }

    }
}
