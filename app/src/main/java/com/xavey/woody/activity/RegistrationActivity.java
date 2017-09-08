package com.xavey.woody.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.xavey.woody.R;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.API_Response;
import com.xavey.woody.api.model.Auth;
import com.xavey.woody.api.model.FBUser;
import com.xavey.woody.api.model.User;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.DBHelper;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TypeFaceHelper;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class RegistrationActivity extends Activity {
    final String LOG_TAG = "myLogs";

    @InjectView(R.id.eTMobileReg)
    EditText eTMobileReg;

    @InjectView(R.id.eTPasswordReg)
    EditText eTPasswordReg;

    @InjectView(R.id.eTUserNameReg)
    EditText eTUserNameReg;

    @InjectView(R.id.tvTitleReg)
    TextView tvTitleReg;

    @InjectView(R.id.btnOk)
    Button btnOk;

    @InjectView(R.id.tVMessageReg)
    TextView tVMessageReg;

    @InjectView(R.id.eTRefer)
            EditText eTRefer;

    @InjectView(R.id.fb_login_button)
    LoginButton fb_login_button;

    DBHelper dbHelper;

    CallbackManager callbackManager;
    FBUser fbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_registration);
        ButterKnife.inject(this);
        setTypeFace();
        List<String> permissions = Arrays.asList(this.getResources().getStringArray(R.array.fb_read_permission));
        fb_login_button.setReadPermissions(permissions);
        fb_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            private ProfileTracker mProfileTracker;

            @Override
            public void onSuccess(LoginResult loginResult) {
                fbUser = new FBUser();
                fbUser.setFbID(loginResult.getAccessToken().getUserId());
                fbUser.setFbAuth(loginResult.getAccessToken().getToken());
                if (Profile.getCurrentProfile() == null) {
                    mProfileTracker = new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
                            // profile2 is the new profile
                            fbUser.setProfile(profile2);
                            mProfileTracker.stopTracking();
                            attemptFBLoginReg(fbUser);
                        }
                    };
                    mProfileTracker.startTracking();
                } else {
                    fbUser.setProfile(Profile.getCurrentProfile());
                    attemptFBLoginReg(fbUser);
                }
            }

            @Override
            public void onCancel() {
                Log.v("facebook - onCancel", "cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                Log.v("facebook - onError", e.getMessage());
            }
        });

        setPhoneNo();
        toBeSubmitted();
        dbHelper = new DBHelper(this);
    }

    private void setPhoneNo(){
        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        eTMobileReg.setText(tMgr.getLine1Number());
    }

    public void attemptFBLoginReg(FBUser fbu) {

        DBHelper dh = new DBHelper(this);
        try {
            dh.deleteUser();
            dh.deleteAuth();
            dh.deleteNoti();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            final DBHelper dbH = new DBHelper(this);

            SampleClient.getWoodyApiClient(this).postAuthTokenFB(fbu, new Callback<Auth>() {

                @Override
                public void success(Auth pAuth, Response response) {
                    if (pAuth != null) {
                        Log.d("AuthAPI", pAuth.getAccess_token());
                        try {
                            dbH.createAuth(pAuth);
                            finish();
                            Intent intent = new Intent();
                            intent.putExtra(AnswerActivity.EXTRA_CHECK_PROFILE,true);
                            intent.setClass(RegistrationActivity.this, AnswerActivity.class);
                            startActivity(intent);
                        } catch (Exception e) {
                            Log.d("AuthAPI", e.getStackTrace().toString());
                        }
                    }
                }
                @Override
                public void failure(RetrofitError error) {
                    try {
                        dbH.deleteUser();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    if(error.getMessage().equals(getResources().getString(R.string.api_error_offline))){
                    }
                    Log.d("AuthAPI", error.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed(){
        finish();
        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnOk)
    public void RegistrationOK() {
        // TODO submit data to server...
        submitted();
        final String strMobile = eTMobileReg.getText().toString();
        String strPassword = eTPasswordReg.getText().toString();
        final String strUserName = eTUserNameReg.getText().toString();
        String strReferalName = eTRefer.getText().toString();

        //TODO: Add dob checking
        if ((strUserName.equalsIgnoreCase("")) || (strPassword.equalsIgnoreCase("")) ||
                (strMobile.equalsIgnoreCase(""))) {
            toBeSubmitted();
            tVMessageReg.setText("You've not filled all the fields!");
            tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
        }  else {

            User rUser = new User();
            rUser.setPhone(strMobile);
            rUser.setHashed_password(strPassword);
            rUser.setUser_name(strUserName);
            strReferalName = strReferalName==null ? "na" : strReferalName;
            SampleClient.getWoodyApiClient(this).postNewUser(rUser, strReferalName, new Callback<API_Response>() {
                @Override
                public void success(API_Response apiRes, Response response) {
                    if(apiRes.getMessage().equals(RegistrationActivity.this.getString(R.string.reg_code_001_mobile_no_invalid))){
                        toBeSubmitted();
                        tVMessageReg.setText("The given mobile no is not valid.");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    }
                    else if(apiRes.getMessage().equals(RegistrationActivity.this.getString(R.string.reg_code_002_duplicate_user))){
                        toBeSubmitted();
                        tVMessageReg.setText("Login " + strUserName + " is already taken! Choose something else please.");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    }
                    else if(apiRes.getMessage().equals(RegistrationActivity.this.getString(R.string.reg_code_005))){
                        btnOk.setEnabled(true);
                        tVMessageReg.setText("Login " + strUserName + " is not a valid name! Only allowed a~z 0~9 _ .");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    }
                    else if(apiRes.getMessage().equals(RegistrationActivity.this.getString(R.string.reg_code_006_age_under_16))){
                        toBeSubmitted();
                        tVMessageReg.setText("This application is for age 15 and above.");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    }
                    else if(apiRes.getMessage().equals(RegistrationActivity.this.getString(R.string.reg_code_007_password_length))){
                        toBeSubmitted();
                        tVMessageReg.setText("No. of Password characters must be 6 to 15");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    }
                    else if(apiRes.getMessage().equals(RegistrationActivity.this.getString(R.string.reg_code_008_userName_phone))){
                        toBeSubmitted();
                        tVMessageReg.setText("Existing account found with given User Name '" + strUserName + "' or Mobile no. '" + strMobile + "'");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    }
                    else if(apiRes.getMessage().equals(RegistrationActivity.this.getString(R.string.reg_code_010_success))){
                        tVMessageReg.setText("Welcome! Your account is created.");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.green_400));
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                        builder.setTitle("Success");
                        builder.setMessage("Your accoount has been created. Please sign in.");

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                    else{
                        btnOk.setEnabled(true);
                        tVMessageReg.setText("Please check your entry and try again later.");
                        tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    }
                    Log.d("regNewUser", response.toString());
                    toBeSubmitted();
                }

                @Override
                public void failure(RetrofitError error) {
                    btnOk.setEnabled(true);
                    if(error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                        tVMessageReg.setText(R.string.api_error_offline_message);
                    }
                    else {
                        tVMessageReg.setText("Please check your entry and try again later.");
                    }
                    tVMessageReg.setTextColor(getResources().getColor(R.color.red_500));
                    toBeSubmitted();
                    Log.d("regNewUser", error.toString());
                }
            });
        }
    }

    public void submitted(){
        btnOk.setText(getResources().getString(R.string.button_label_loading));
        btnOk.setEnabled(false);
    }

    public void toBeSubmitted(){
        btnOk.setEnabled(true);
        btnOk.setText(getResources().getString(R.string.button_label_submit));
    }

    private void setTypeFace(){
        TypeFaceHelper.setM3TypeFace(tvTitleReg, RegistrationActivity.this);
        if (AppValues.getInstance().getZawGyiDisplay()) {
            tvTitleReg.setText(Rabbit.uni2zg(tvTitleReg.getText().toString()));
        } else {
            tvTitleReg.setText(tvTitleReg.getText().toString());
        }
    }
}
