package com.xavey.woody.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.xavey.woody.BuildConfig;
import com.xavey.woody.R;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.Version;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.UtilityHelper;

import java.util.List;
import java.util.Timer;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends Activity {

    static int cnt=0;
    private Timer timer = new Timer();
    private Boolean loadedActivity = false;
    @InjectView(R.id.main_imageView)
    ImageView mImageView;

    @InjectView(R.id.main_radiogroup)
    RadioGroup mRadioGroup;

    @InjectView(R.id.tvMainVersion)
    TextView tvMainVersion;

    Boolean loading=true;
    Boolean singleTon = true;
    final int[] itemColors = UtilityHelper.itemColors;
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Get the shared preferences
        SharedPreferences preferences =  getSharedPreferences("woody_onboard_preferences", MODE_PRIVATE);

        // Check if onboarding_complete is false
        if(preferences.getInt("onboarding_complete",0) != BuildConfig.VERSION_CODE) {
            // Start the onboarding Activity
            Intent onboarding = new Intent(this, OnboardingActivity.class);
            startActivity(onboarding);

            // Close the main Activity
            finish();
            return;
        }

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        String version_name="Version: 0.0.0";
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version_name = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tvMainVersion.setText("Version: " + version_name);

        startTimerThread();
        PROJECT_NUMBER = getString(R.string.gcm_project_number);
        getRegId();

    }

    private void checkVersion(){
        SampleClient.getWoodyApiClient(this).getVersion(new Callback<Version>() {
            @Override
            public void success(Version v, Response response) {
                loading = false;
                if (v != null) {
                    PackageInfo pInfo = null;
                    try {
                        pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        if (v.getAndroid() > pInfo.versionCode) {
                            alertNewVersion();
                        } else {
                            loadAnswerActivity();
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                loading = false;
                loadAnswerActivity();
                Log.d("API_Version", error.toString());
                if (error.getMessage().equals(getResources().getString(R.string.api_error_offline))) {
                    Toast bread = Toast.makeText(getApplicationContext(), R.string.api_error_offline_message, Toast.LENGTH_SHORT);
                    bread.show();
                }

            }
        });
    }

    public void alertNewVersion() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.app_download)));
                        startActivity(browserIntent);
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        loadAnswerActivity();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this); builder.setMessage(getString(R.string.app_download_label))
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void startTimerThread() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            private long startTime = System.currentTimeMillis();

            public void run() {
                while (loading) {
                    try {
                        Thread.sleep(400);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        public void run() {
                           RadioButton rb = (RadioButton)mRadioGroup.getChildAt(cnt);
                            //TintHelper.setTint(rb, getResources().getColor(itemColors[cnt]));
                            rb.setChecked(true);
                            cnt = cnt + 1;
                            if (cnt > 4) {
                                cnt = 0;
                            }
                            else if(cnt>3 && singleTon){
                                checkVersion();
                                singleTon=false;
                            }
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }

    private void loadAnswerActivity(){
        if(!loadedActivity) {
            loadedActivity=true;
            final Intent ointent = getIntent();
            final String action = ointent.getAction();
            Intent intent = new Intent(this, AnswerActivity.class);

            if (Intent.ACTION_VIEW.equals(action)) {
                final List<String> segments = ointent.getData().getPathSegments();
                if (segments.size() == 2 && segments.get(0).equals("questions")) {
                    intent = new Intent(MainActivity.this, ResultActivity.class);
                    intent.putExtra(ResultActivity.EXTRA_POST, segments.get(1));
                }
            }
            startActivity(intent);
            finish();
        }
    }

    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = regid;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                AppValues.setGcmId(msg);
            }
        }.execute(null, null, null);
    }
}
