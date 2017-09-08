package com.xavey.woody.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.model.GameRequestContent;
import com.google.android.gms.games.request.GameRequest;
import com.google.android.gms.games.request.GameRequestEntity;
import com.google.gson.Gson;
import com.xavey.woody.R;
import com.xavey.woody.adapter.FriendAdapter;
import com.xavey.woody.api.model.InternalContact;
import com.xavey.woody.api.model.TaggableFriend;
import com.xavey.woody.api.model.TaggableFriends;
import com.xavey.woody.helper.UtilityHelper;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;

import retrofit.client.Response;

/**
 * Created by tinmaungaye on 8/23/15.
 */
public class FriendFragment extends Fragment{
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String ARG_LIST = "ARG_LIST";

    private static int mPage;
    FriendAdapter friendAdapter;
    public static List<TaggableFriend> friendListFacebook = new ArrayList<TaggableFriend>();
    Button btnInvite;

    public static FriendFragment newInstance(int page, ArrayList<InternalContact> ics) {

        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        args.putSerializable(ARG_LIST, ics);
        FriendFragment fragment = new FriendFragment();
        fragment.setArguments(args);
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
        View view = inflater.inflate(R.layout.friend_list_holder, container, false);
        btnInvite = (Button)view.findViewById(R.id.btnInvite);
        final LinearLayout ll = (LinearLayout)view.findViewById(R.id.llListHolderFr);
        getFriends(ll);

        btnInvite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
                Bundle params = new Bundle();
                ArrayList<String> selUser = new ArrayList<String>();
                for(TaggableFriend tf:friendAdapter.mUsers){

                    if(tf.getSelected()){
                        selUser.add(tf.getId());
                    }
                }
                params.putString("ids", android.text.TextUtils.join(",", selUser));
                params.putString("action_type","INVITE");
                params.putString("message","Join me at Mell!");
                new GraphRequest(
                        AccessToken.getCurrentAccessToken(),
                        "apprequests",
                        params,
                        HttpMethod.POST,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                Log.d("a","b");
                            }
                        }
                ).executeAsync();
            }
        });

        return view;
    }

    private void displayContact(final LinearLayout ll){
        if (friendListFacebook != null && friendListFacebook.size() > 0) {
            ListView qList = new ListView(getActivity());
            qList.setLayoutParams(new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            friendAdapter = new FriendAdapter(friendListFacebook, getActivity());
            qList.setAdapter(friendAdapter);
            ll.addView(qList);
        } else {
            //showAskButtion();
        }
    }

    public void getFriends(final LinearLayout ll)
    {
        if(AccessToken.getCurrentAccessToken() != null)
        {
            GraphRequest graphRequest = GraphRequest.newGraphPathRequest(
                    AccessToken.getCurrentAccessToken(),
                    "me/invitable_friends",
                    new GraphRequest.Callback()
                    {
                        @Override
                        public void onCompleted(GraphResponse graphResponse)
                        {
                            if(graphResponse != null)
                            {
                                JSONObject jsonObject = graphResponse.getJSONObject();
                                String taggableFriendsJson = jsonObject.toString();
                                Gson gson = new Gson();
                                TaggableFriends taggableFriendsWrapper= gson.fromJson(taggableFriendsJson, TaggableFriends.class);
                                ArrayList<TaggableFriend> invitableFriends = new ArrayList<TaggableFriend>();
                                invitableFriends = taggableFriendsWrapper.getData();
                                int i;
                                for(i = 0; i < invitableFriends.size(); i++)
                                {
                                    try
                                    {
                                        friendListFacebook.add(invitableFriends.get(i));
                                        friendListFacebook.get(friendListFacebook.size()-1).setSelected(false);
                                    }
                                    catch(Exception e){}
                                }
                                displayContact(ll);
                            }else {

                            }

                        }
                    }
            );
            Bundle parameters = new Bundle();
            parameters.putInt("limit", 5000); //5000 is maximum number of friends you can have on Facebook

            graphRequest.setParameters(parameters);
            graphRequest.executeAsync();
        }
    }
}
