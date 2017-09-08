package com.xavey.woody.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.xavey.woody.R;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.GsonIso8601Datelizer;

import java.util.Date;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;


/**
 * Created by tinmaungaye on 3/27/15.
 */
public class SampleClient {

    //Singleton Instance of our Rest Client
    private static WoodyApiInterface sWoodyService;

    //returns the Singleton Instance of our Rest client or Creates an new instance
    public static WoodyApiInterface getWoodyApiClient(Context ctx) {
        if (sWoodyService == null) {
            OkHttpClient httpClient = new OkHttpClient();
            try {
                httpClient.setCache(new Cache(ctx.getCacheDir(), 50 * 1024 * 1024));
            } catch (Exception e) {
                e.printStackTrace();
            }

            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("GCM_Device", AppValues.getGcmId());
                }
            };

            Client client = new OkClient(httpClient);

            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new GsonIso8601Datelizer());
            Gson gson = builder.create();

            RestAdapter adapter = null;
            try {
                adapter = new RestAdapter.Builder().setEndpoint(ctx.getString(R.string.api_endpoint))
                        .setClient(client)
                        .setLogLevel(RestAdapter.LogLevel.FULL)
                        .setRequestInterceptor(requestInterceptor)
                        .setErrorHandler(new CustomErrorHandler(ctx))
                        .setConverter(new GsonConverter(gson))
                        .build();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("SimpleClient",e.getStackTrace().toString());
            }

            sWoodyService = adapter.create(WoodyApiInterface.class);
        }

        return sWoodyService;
    }
}
