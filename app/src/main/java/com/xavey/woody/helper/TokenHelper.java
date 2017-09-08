package com.xavey.woody.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.xavey.woody.R;
import com.xavey.woody.activity.LoginActivity;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.Auth;
import com.xavey.woody.api.model.User;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by tinmaungaye on 4/16/15.
 */
public class TokenHelper {
    private static String tokenKey="";

    public static String genAPIToken(final Context conx,final Activity act) throws Exception {
        String retKey = "";

        final DBHelper dbH = new DBHelper(conx);
        Auth dbAuth = dbH.getAUTH();
        if(dbAuth!=null && dbAuth.getAccess_token()!=null){
         retKey = dbAuth.getToken_type()+" "+dbAuth.getAccess_token();
        }
        else{
            String userName =""; String userPassword="";
            User dbUser = dbH.getUser();
            if(dbUser!=null && dbUser.getUser_name()!=null && dbUser.getHashed_password()!=null) {
                userName = dbUser.getUser_name();
                userPassword = dbUser.getHashed_password();
            }
            else{
                //TODO: bring back to login screen
                loadLoginScreen(conx,act);
            }
            //getting token with local User Name and Password
            if(userName.length()>0 && userPassword.length()>0) {
                String grant = conx.getString(R.string.api_grant_type);
                String clientID = conx.getString(R.string.api_client_id);
                String clientSecret = conx.getString(R.string.api_client_secret);
                SampleClient.getWoodyApiClient(conx).postAuthToken(grant, clientID, clientSecret, userName, userPassword, new Callback<Auth>() {
                    @Override
                    public void success(Auth pAuth, Response response) {
                        if (pAuth != null) {
                            Log.d("AuthAPI", pAuth.getAccess_token());
                            try {
                                dbH.createAuth(pAuth);
                                genAPIToken(conx,act); //will return token from db;
                                Intent intent = new Intent();
                                intent.setClass(conx, act.getClass());
                                conx.startActivity(intent);
                            } catch (Exception e) {
                                //TODO: bring back to login screen
                                loadLoginScreen(conx,act);
                                Log.d("AuthAPI", e.getStackTrace().toString());
                            }
                        }
                    }
                    @Override
                    public void failure(RetrofitError error) {
                        try {
                            dbH.deleteUser();
                            dbH.deleteNoti();
                            loadLoginScreen(conx,act);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        Log.d("AuthAPI", error.toString());
                    }
                });
            }
            else{
                Log.d("AuthAPI Usernam", "");
            }
        }
        return retKey;
    }

    private static void loadLoginScreen(final Context conx, final Activity act){
        Intent intent = new Intent();
        intent.putExtra(LoginActivity.EXTRA_FROM_ACTIVITY, act.getClass());
        intent.setClass(conx, LoginActivity.class);
        conx.startActivity(intent);
    }

}
