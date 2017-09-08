package com.xavey.woody.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TypeFaceHelper;
import com.xavey.woody.interfaces.NavDrawerItem;

import java.util.ArrayList;

/**
 * Created by tinmaungaye on 5/7/15.
 */
public class NavDrawerListAdapter extends BaseAdapter {

        private Context context;
        private ArrayList<NavDrawerItem> navDrawerItems;

        public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems){
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
                convertView = mInflater.inflate(R.layout.drawer_list_item, null);
            }

            ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);

            TextView txtTitle = (TextView) convertView.findViewById(R.id.title);

            if(AppValues.getInstance().getZawGyiDisplay()){
                txtTitle.setText(Rabbit.uni2zg(navDrawerItems.get(position).getTitle()));
            }else {
                txtTitle.setText(navDrawerItems.get(position).getTitle());
            }
            TypeFaceHelper.setM3TypeFace(txtTitle, context);

            imgIcon.setImageResource(navDrawerItems.get(position).getIcon());

            return convertView;
        }

    }
