package com.xavey.woody;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by tinmaungaye on 3/29/15.
 */
public class WoodyApp extends Application {

    public static GoogleAnalytics analytics;
    public static Tracker tracker;

    @Override public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        else {

            analytics = GoogleAnalytics.getInstance(this);
            analytics.setLocalDispatchPeriod(1800);

            tracker = analytics.newTracker("UA-65161654-1"); // Replace with actual tracker/property Id
            tracker.enableExceptionReporting(true);
            tracker.enableAdvertisingIdCollection(true);
            tracker.enableAutoActivityTracking(true);

            Fabric.with(this, new Crashlytics());
        }

        //
        /*try {
            fProperties.load(getBaseContext().getAssets().open("fabric.properties"));

            TwitterAuthConfig authConfig = new TwitterAuthConfig(fProperties.getProperty("apiKey").toString(), fProperties.getProperty("apiSecret").toString());
            Fabric.with(this, new Twitter(authConfig));

        } catch (IOException e) {
            e.printStackTrace();
        }*/

        // Example: multiple kits
        // Fabric.with(this, new Twitter(authConfig),
        //                  new Crashlytics());




    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
