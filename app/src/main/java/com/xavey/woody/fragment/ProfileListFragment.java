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
import com.xavey.woody.adapter.UserAdapter;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.Followers;
import com.xavey.woody.api.model.PostSets;
import com.xavey.woody.api.model.Posts;
import com.xavey.woody.api.model.User;
import com.xavey.woody.api.model.UserLike;
import com.xavey.woody.helper.AppValues;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tinmaungaye on 8/23/15.
 */
public class ProfileListFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_TYPE = "ARG_TYPE";

    private static int mPage;
    private static String mDisplayPage;
    private static String apiKey;
    private static String userId;
    QuestionAdapter questionAdapter;
    QuestionSetAdapter questionSetAdapter;
    UserAdapter userAdapter;

    public static ProfileListFragment newInstance(int page, int pageCount, String api, String user) {

        String[] layouts = AppValues.LAYOUT_PROFILE_LIST_NORMAL;
        if(pageCount == AppValues.LAYOUT_PROFILE_LIST_PREMIUM.length)
        {
            layouts = AppValues.LAYOUT_PROFILE_LIST_PREMIUM;
        }

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putString(ARG_TYPE, layouts[page]);
        ProfileListFragment fragment = new ProfileListFragment();
        fragment.setArguments(args);
        apiKey = api;
        userId = user;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPage = getArguments().getInt(ARG_PAGE);
        mDisplayPage = getArguments().getString(ARG_TYPE);
        View view = inflater.inflate(R.layout.profile_list_holder, container, false);
        final LinearLayout ll = (LinearLayout)view.findViewById(R.id.llListHolderPr);
        switch (mDisplayPage) {
            case "question":
                loadUserQuestion(ll);
            break;
            case "question_set":
                loadUserQuestionSet(ll);
                break;
            case "following":
                loadFollowing(ll);
                break;
            case "follower":
                loadFollower(ll);
                break;
            case "favourite":
                loadFavQuestions(ll);
                break;
        }
//        TextView textView = (TextView) view;
//        textView.setText("Fragment #" + mPage);
        return view;
    }

    private void loadUserQuestion(final LinearLayout ll){
            SampleClient.getWoodyApiClient(getActivity()).getUserPost(apiKey, "byuser", userId, new Callback<Posts>() {
                @Override
                public void success(Posts posts, Response response) {
                    try{
                        if (posts != null) {
                            ListView qList = new ListView(getActivity());
                            qList.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            questionAdapter = new QuestionAdapter(posts.getPosts(), getActivity());
                            qList.setAdapter(questionAdapter);
                            ll.addView(qList);
                        } else {
                            //showAskButtion();
                        }
                    }
                    catch (Exception ex){

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    try{
                        Log.d("API_Profile", error.toString());
                        if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                            Toast bread = Toast.makeText(getActivity(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                            bread.show();
                        }

                    }
                    catch (Exception ex){

                    }
                }
            });
    }

    private void loadUserQuestionSet(final LinearLayout ll){
            SampleClient.getWoodyApiClient(getActivity()).getPostSetList(apiKey, userId, new Callback<PostSets>() {
                @Override
                public void success(PostSets posts, Response response) {
                    try{
                        if (posts != null) {
                            ListView qList = new ListView(getActivity());
                            qList.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            questionSetAdapter = new QuestionSetAdapter(posts.getPostSets(), getActivity());
                            qList.setAdapter(questionSetAdapter);
                            ll.addView(qList);
                        } else {
                            //showAskButtion();
                        }

                    }
                    catch (Exception ex){

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    try{
                        Log.d("API_Profile", error.toString());
                        if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                            Toast bread = Toast.makeText(getActivity(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                            bread.show();
                        }

                    }
                    catch (Exception ex){

                    }
                }
            });
    }


    public void loadFollowing(final LinearLayout ll){
            SampleClient.getWoodyApiClient(getActivity()).getUserFollower(apiKey, userId, new Callback<Followers>() {
                @Override
                public void success(Followers followers, Response response) {
                    try{
                        if (followers != null) {
                            ListView qList = new ListView(getActivity());
                            qList.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                            ArrayList<User> uList = new ArrayList<User>();
                            for (UserLike userLike : followers.getUsers()) {
                                uList.add(userLike.getUser());
                            }
                            userAdapter = new UserAdapter(uList, getActivity(), apiKey);
                            qList.setAdapter(userAdapter);
                            ll.addView(qList);
                        }

                    }
                    catch (Exception ex){

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    try{
                        Log.d("API_Profile", error.toString());
                        if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                            Toast bread = Toast.makeText(getActivity(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                            bread.show();
                        }
                    }
                    catch (Exception ex){

                    }
                }
            });
    }

    public void loadFollower(final LinearLayout ll){
            SampleClient.getWoodyApiClient(getActivity()).getUserFollowing(apiKey, userId, new Callback<Followers>() {
                @Override
                public void success(Followers followers, Response response) {
                    try {
                        if (followers != null) {
                            ListView qList = new ListView(getActivity());
                            qList.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                            ArrayList<User> uList = new ArrayList<User>();
                            for (UserLike userLike : followers.getUsers()) {
                                uList.add(userLike.getLiked_by());
                            }
                            userAdapter = new UserAdapter(uList, getActivity(), apiKey);
                            qList.setAdapter(userAdapter);
                            ll.addView(qList);
                        }
                    }
                    catch (Exception ex) {

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    try {
                        Log.d("API_Profile", error.toString());
                        if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                            Toast bread = Toast.makeText(getActivity(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                            bread.show();
                        }
                    }
                    catch (Exception ex){

                    }
                }
            });
    }

    public void loadFavQuestions(final LinearLayout ll) {

            SampleClient.getWoodyApiClient(getActivity()).getUserPost(apiKey, "byliker", userId, new Callback<Posts>() {
                @Override
                public void success(Posts posts, Response response) {
                    try{
                        if (posts != null) {
                            ListView qList = new ListView(getActivity());
                            qList.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            questionAdapter = new QuestionAdapter(posts.getPosts(), getActivity());
                            qList.setAdapter(questionAdapter);
                            ll.addView(qList);
                        } else {

                        }
                    }
                    catch (Exception ex){

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    try{
                        Log.d("API_Profile", error.toString());
                        if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                            Toast bread = Toast.makeText(getActivity(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                            bread.show();
                        }
                    }
                    catch (Exception ex){

                    }
                }
            });

    }
}
