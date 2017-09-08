package com.xavey.woody.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.api.model.Category;
import com.xavey.woody.interfaces.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by tinmaungaye on 5/5/15.
 */
public class CategoryDialogItemAdapter extends BaseAdapter {
    private Context mContext;
    private List<Category> mCategories;
    private LayoutInflater mInflater;
    private ArrayList<Category> selectedList=new ArrayList<Category>();
    private int highlighter;
    private OnItemClickListener mListener;

    public CategoryDialogItemAdapter(List<Category> categories, Context context, OnItemClickListener listener) {
        this.mCategories = categories;
        this.mContext = context;
        selectedList=new ArrayList<Category>();
        mListener=listener;
        highlighter=mContext.getResources().getColor(R.color.orange_100);
    }

    public int getCount() {
        return mCategories == null ? 0 : mCategories.size();
    }

    public List<Category> getItems() {
        return mCategories;
    }

    public Category getItem(int position) {
        return mCategories == null ? null : mCategories.get(position);
    }

    public long getItemId(int position) {
        return -1;
    }

    public ArrayList<Category> getSelectedList() {
        return selectedList;
    }

    public void setSelectedList(ArrayList<Category> selectedList) {
        this.selectedList = selectedList;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        final Category catItem = mCategories.get(position);

        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            mInflater = ((Activity) mContext).getLayoutInflater();
            view = mInflater.inflate(R.layout.dialog_category_content, null);
            //view.setLayoutParams(new GridView.LayoutParams(200, 90));
            holder = new ViewHolder(view);
            holder.tVCategoryDia=(TextView) view.findViewById(R.id.tVCategoryDia);
            if(catItem.getIsSelected()){
                view.setBackgroundColor(highlighter);
                selectedList.add(catItem);
            }
            else{
                view.setBackgroundColor(Color.WHITE);
            }
            view.setTag(holder);
        }

        holder.tVCategoryDia.setText(mCategories.get(position).getTitle());
        holder.tVCategoryDia.setTag(mCategories.get(position).get_id());

        view.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                    if (catItem.getIsSelected()) {
                        v.setBackgroundColor(Color.WHITE);
                        catItem.setIsSelected(false);
                        if(selectedList.indexOf(catItem)>-1){
                            selectedList.remove(selectedList.indexOf(catItem));
                        }
                    } else {
                        if(selectedList.size()<3) {
                            v.setBackgroundColor(highlighter);
                            catItem.setIsSelected(true);
                            if(selectedList.indexOf(catItem)<=-1) {
                                selectedList.add(catItem);
                            }
                        }
                    }
                    mListener.onClick(holder.tVCategoryDia,position);
                    }

                }
        );
        return view;
    }

    public static class ViewHolder{

        @InjectView(R.id.tVCategoryDia)
        TextView tVCategoryDia;

        public ViewHolder(View itemView) {
            ButterKnife.inject(this, itemView);
        }
    }
}


