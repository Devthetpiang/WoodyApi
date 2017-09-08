package com.xavey.woody.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.activity.ResultActivity;
import com.xavey.woody.activity.ResultSetActivity;
import com.xavey.woody.api.model.PostSet;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TypeFaceHelper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by hnin on 8/31/15.
 */
public class PromoteAdapter extends BaseAdapter {
    private Context mcontext;
    private List<PostSet> data;
    private LayoutInflater mInflater;

    public PromoteAdapter( List<PostSet> data,Context context) {
        mcontext = context;
        this.data = data;
        mInflater =LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data ==null ? 0:data.size();
    }

    @Override
    public PostSet getItem(int position) {
        return data==null?null:data.get(position);
    }

    public List<PostSet>getItems(){
        return  data;
    }

    @Override
    public long getItemId(int position) {
        return -1;
    }

    public View getView(int postioin,View convertView,ViewGroup parent){
        ViewHolder holder;
        final PostSet postSetItem= data.get(postioin);
        if(convertView != null){
            holder =(ViewHolder)convertView.getTag();
        }else {
            convertView =mInflater.inflate(R.layout.promote_items,parent,false);
            holder =new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        if(AppValues.getInstance().getZawGyiDisplay()){
            holder.tVPromote.setText(Rabbit.uni2zg(postSetItem.getIntro()));
        }
        else {
            holder.tVPromote.setText(postSetItem.getIntro());
        }
        TypeFaceHelper.setM3TypeFace(holder.tVPromote,mcontext);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadResultSetActivity(postSetItem.get_id());
            }
        });
        holder.tVPromote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadResultSetActivity(postSetItem.get_id());
            }
        });
        return convertView;
    }

    private void loadResultSetActivity(String id){
        Intent intent = new Intent(mcontext, ResultSetActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ResultSetActivity.EXTRA_POST,id);
        mcontext.startActivity(intent);
    }

    private void loadResultActivity(String id){
        Intent intent =new Intent(mcontext,ResultActivity.class);
        intent.putExtra(ResultActivity.EXTRA_POST,id);
        mcontext.startActivity(intent);
    }

    static class ViewHolder{
        @InjectView(R.id.tVPromote)
        TextView tVPromote;

        public ViewHolder(View itemView){
            ButterKnife.inject(this,itemView);
        }
    }
}
