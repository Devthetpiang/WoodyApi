package com.xavey.woody.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.xavey.woody.R;
import com.xavey.woody.adapter.QuestionAdapter;
import com.xavey.woody.adapter.QuestionSetAdapter;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.PromoPosts;
import com.xavey.woody.helper.AppValues;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by juno on 8/31/15.
 */
public class PromotionItemFragment extends Fragment{
    public static final String ARG_PAGE="ARG_PAGE";
    public static final String ARG_TYPE="ARG_TYPE";

    private static int mPage;
    private static String mDisplayPage;
    private static String apiKey;

    QuestionAdapter questionAdapter;
    QuestionSetAdapter questionSetAdapter;

    public static PromotionItemFragment newInstance(int page,int pageCount,String api){

        String[]layouts = AppValues.LAYOUT_PROMOTION_POST_LIST;

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE,page);
        args.putString(ARG_TYPE,layouts[page]);
        PromotionItemFragment fragment = new PromotionItemFragment();
        fragment.setArguments(args);
        apiKey =api;
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        mPage =getArguments().getInt(ARG_PAGE);
        mDisplayPage=getArguments().getString(ARG_TYPE);
        View view =inflater.inflate(R.layout.profile_list_holder,container,false);
        final LinearLayout ll =(LinearLayout)view.findViewById(R.id.llListHolderPr);
        switch (mDisplayPage){
            case "post":
                loadPromoPost(ll);
                break;
            case "postset":
                loadPromoPostset(ll);
                break;
        }
        return  view;
    }

    private void loadPromoPost(final LinearLayout ll){
        SampleClient.getWoodyApiClient(getActivity()).getPromotionItem(apiKey,new Callback<PromoPosts>() {
            @Override
            public void success(PromoPosts promoPosts, Response response) {
                if(promoPosts != null){
                    ListView qList = new ListView(getActivity());
                    qList.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
//                    questionAdapter = new QuestionAdapter(promoPosts.getPostsets(),getActivity());
//                    qList.setAdapter(questionAdapter);
//                    ll.addView(qList);
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("API_Profile", error.toString());
                if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                    Toast bread = Toast.makeText(getActivity(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                    bread.show();
                }
            }
        });
    }

    private void loadPromoPostset(final LinearLayout ll){
        SampleClient.getWoodyApiClient(getActivity()).getPromotionItem(apiKey,new Callback<PromoPosts>() {
            @Override
            public void success(PromoPosts promoPosts, Response response) {
                if(promoPosts != null){
                    ListView qSetList = new ListView(getActivity());
                    qSetList.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    questionSetAdapter = new QuestionSetAdapter(promoPosts.getPostsets(),getActivity());
                    qSetList.setAdapter(questionSetAdapter);
                    ll.addView(qSetList);
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Log.d("API_Profile", error.toString());
                if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                    Toast bread = Toast.makeText(getActivity(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                    bread.show();
                }

            }
        });
    }
}
