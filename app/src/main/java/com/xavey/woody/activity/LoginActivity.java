package com.xavey.woody.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
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
import com.xavey.woody.helper.UtilityHelper;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity{

    public static final String EXTRA_FROM_ACTIVITY = "LoginActivity.FROM_ACTIVITY";

    // UI references.
    @InjectView(R.id.email)
    AutoCompleteTextView mEmailView;

    @InjectView(R.id.password)
    EditText mPasswordView;

    @InjectView(R.id.login_progress)
    View mProgressView;

    @InjectView(R.id.login_form)
    View mLoginFormView;

    @InjectView(R.id.f_user_name)
    EditText f_user_name;

    @InjectView(R.id.f_phone)
    EditText f_phone;

    @InjectView(R.id.r_token)
    EditText r_token;

    @InjectView(R.id.r_new_password)
    EditText r_new_password;

    @InjectView(R.id.r_confirm_password)
    EditText r_confirm_password;

    @InjectView(R.id.logFErrorMessage)
    TextView logFErrorMessage;

    @InjectView(R.id.logRErrorMessage)
    TextView logRErrorMessage;

    @InjectView(R.id.llForget_form)
    LinearLayout llForget_form;

    @InjectView(R.id.llReset_form)
    LinearLayout llReset_form;

    @InjectView(R.id.llLoginForm)
    LinearLayout llLoginForm;

    @InjectView(R.id.login_register_button)
    Button login_register_button;

    @InjectView(R.id.login_sign_in_button)
    Button login_sign_in_button;

    @InjectView(R.id.fb_login_button)
    LoginButton fb_login_button;

    public static final String EXTRA_USER = "LoginActivity.EXTRA_USER";
    public static final String EXTRA_PWD = "LoginActivity.EXTRA_PWD";

    CallbackManager callbackManager;
    FBUser fbUser;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().logOut();
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
        setTypeFace();
        // Set up the login form.
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
        public void onBackPressed(){
        if (mProgressView.getVisibility() == View.VISIBLE) {
            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            new AlertDialog.Builder(this).setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }).setNegativeButton("No", null).show();
        }
    }

    @OnClick(R.id.tvForgotPassword)
    public void showForgetPanel(){
        showForgetForm();
    }

    @OnClick(R.id.login_register_button)
    public void showRegistrationForm(){
        finish();
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.login_sign_in_button)
    public void tryLogin(){
        UtilityHelper uh = new UtilityHelper();
        UtilityHelper.hideSoftKeyboard(this);
        attemptLogin();
    }

    @OnClick(R.id.btnGetToken)
    public void sendRequestToken() {
        final User rUser = new User();
        rUser.setUser_name(f_user_name.getText().toString());
        rUser.setPhone(f_phone.getText().toString());

        SampleClient.getWoodyApiClient(this).postGenToken(rUser, new Callback<API_Response>() {

            @Override
            public void success(API_Response pAuth, Response response) {
                if (pAuth != null) {
                    try {
                        if (pAuth.getMessage().equals("010")) {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(rUser.getPhone(), null, "<Mell> Password reset token: " + pAuth.getExtra(), null, null);

                            showResetForm();

                        } else if (pAuth.getMessage().equals("001")) {
                            logFErrorMessage.setText(pAuth.getExtra());
                        } else {
                            logFErrorMessage.setText("Please try again");
                        }
                    } catch (Exception e) {
                        Log.d("AuthAPI", e.getStackTrace().toString());
                        logFErrorMessage.setText("Please try again");
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("AuthAPI", error.toString());

                if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                    logFErrorMessage.setText(getResources().getString(R.string.api_error_offline_message));
                } else {
                    logFErrorMessage.setText("Please try again");
                }

            }
        });
    }

    @OnClick(R.id.btnReset)
    public void sendResetPassword(){
        String rToken = r_token.getText().toString();
        String nPassword = r_new_password.getText().toString();
        String cPassword = r_confirm_password.getText().toString();
        if (rToken.equalsIgnoreCase("")){
            logRErrorMessage.setText("You've not filled all the fields!");
            logRErrorMessage.setTextColor(getResources().getColor(R.color.red_500));
        } else if (!nPassword.equals(cPassword)) { //user name already exist
            logRErrorMessage.setText("Password and re-enter password are not match. Please enter the passwords again.");
            logRErrorMessage.setTextColor(getResources().getColor(R.color.red_500));
        }
        else {

            final User rUser = new User();
            rUser.setResetPasswordToken(rToken);
            rUser.setHashed_password(nPassword);

            SampleClient.getWoodyApiClient(this).postResetPassword(rUser, new Callback<API_Response>() {

                @Override
                public void success(API_Response pAuth, Response response) {
                    if (pAuth != null) {
                        try {
                            if (pAuth.getMessage().equals("010")) {
                                logRErrorMessage.setText("Your password has been reset successfully!");
                                showLoginForm();
                            } else if (pAuth.getMessage().equals("001")) {
                                logRErrorMessage.setText(pAuth.getExtra());
                            } else {
                                logRErrorMessage.setText("Please try again");
                            }
                        } catch (Exception e) {
                            Log.d("AuthAPI", e.getStackTrace().toString());
                            logRErrorMessage.setText("Please try again");
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("AuthAPI", error.toString());
                    if(error.getMessage().equals(getResources().getString(R.string.api_error_offline))){
                        logFErrorMessage.setText(getResources().getString(R.string.api_error_offline_message));
                    }
                    else{
                        logFErrorMessage.setText("Please try again");
                    }
                }
            });
        }
    }

    @OnClick(R.id.btnResetCancel)
    public void cancelReset(){
        showLoginForm();
    }
    @OnClick(R.id.btnTokenCancel)
    public void showLoginForm(){
        llLoginForm.setVisibility(View.VISIBLE);
        llLoginForm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        llForget_form.setVisibility(View.INVISIBLE);
        llForget_form.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));

        llReset_form.setVisibility(View.INVISIBLE);
        llReset_form.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
    }

    private void showForgetForm(){
        llForget_form.setVisibility(View.VISIBLE);
        llForget_form.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        llReset_form.setVisibility(View.INVISIBLE);
        llReset_form.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));

        llLoginForm.setVisibility(View.INVISIBLE);
        llLoginForm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
    }

    private void showResetForm(){
        llReset_form.setVisibility(View.VISIBLE);
        llReset_form.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        llForget_form.setVisibility(View.INVISIBLE);
        llForget_form.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));

        llLoginForm.setVisibility(View.INVISIBLE);
        llLoginForm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
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

        showProgress(true);
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
                            intent.setClass(LoginActivity.this, AnswerActivity.class);
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
                        showProgress(false);
                        mPasswordView.setError(getString(R.string.error_incorrect_password));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    if(error.getMessage().equals(getResources().getString(R.string.api_error_offline))){
                        mPasswordView.setError(getResources().getString(R.string.api_error_offline_message));
                    }
                    Log.d("AuthAPI", error.toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        DBHelper dh = new DBHelper(this);
        try {
            dh.deleteUser();
            dh.deleteAuth();
            dh.deleteNoti();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            try {

                String grant = this.getString(R.string.api_grant_type);
                String clientID = this.getString(R.string.api_client_id);
                String clientSecret = this.getString(R.string.api_client_secret);

                final DBHelper dbH = new DBHelper(this);

                SampleClient.getWoodyApiClient(this).postAuthToken(grant, clientID, clientSecret, email, password, new Callback<Auth>() {

                    @Override
                    public void success(Auth pAuth, Response response) {
                        if (pAuth != null) {
                            Log.d("AuthAPI", pAuth.getAccess_token());
                            try {
                                dbH.createAuth(pAuth);
                                finish();
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this, AnswerActivity.class);
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
                            showProgress(false);
                            mPasswordView.setError(getString(R.string.error_incorrect_password));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        if(error.getMessage().equals(getResources().getString(R.string.api_error_offline))){
                            mPasswordView.setError(getResources().getString(R.string.api_error_offline_message));
                        }
                        Log.d("AuthAPI", error.toString());
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        //return email.contains("@");
        return true;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return true;//password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private void setTypeFace(){
        TypeFaceHelper.setM3TypeFace(login_sign_in_button, LoginActivity.this);

        if (AppValues.getInstance().getZawGyiDisplay()) {
            login_sign_in_button.setText(Rabbit.uni2zg(login_sign_in_button.getText().toString()));
        } else {
            login_sign_in_button.setText(login_sign_in_button.getText().toString());
        }

        TypeFaceHelper.setM3TypeFace(login_register_button, LoginActivity.this);
        if (AppValues.getInstance().getZawGyiDisplay()) {
            login_register_button.setText(Rabbit.uni2zg(login_register_button.getText().toString()));
        } else {
            login_register_button.setText(login_register_button.getText().toString());
        }
    }
}



