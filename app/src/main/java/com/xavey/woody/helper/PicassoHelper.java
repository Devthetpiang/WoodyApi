package com.xavey.woody.helper;

import android.content.Context;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * Created by tinmaungaye on 4/11/15.
 */
public class PicassoHelper {

    private static final String TAG = "Woody/PicassoHelper";

    private static PicassoHelper singleton;

    private Picasso mPicasso;
    private static String authToken;
    private LruCache mCache;
    private PicassoHelper() {
    }

    public static PicassoHelper getInstance(Context context, final String token) {

        PicassoHelper.authToken = token;
        if (singleton == null) {
            singleton = new PicassoHelper();
            LruCache cache = new LruCache(context);
            singleton.mCache = cache;

            OkHttpClient picassoClient = new OkHttpClient();

            picassoClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", PicassoHelper.authToken )
                            .build();
                    return chain.proceed(newRequest);
                }
            });

            singleton.mPicasso= new Picasso.Builder(context).downloader(new OkHttpDownloader(picassoClient)).build();

            /*singleton.mPicasso =new Picasso.Builder(context)
                                    .downloader(new OkHttpDownloader(context) {
                                        @Override
                                        protected HttpURLConnection openConnection(Uri uri) throws IOException {
                                            HttpURLConnection connection = super..openConnection(uri);
                                            connection.setRequestProperty("Authorization", authToken);
                                            return connection;
                                        }
                                    })
                                    .memoryCache(cache)
                                    .build();*/
        }
        return singleton;
    }

    public Picasso getPicasso() {
        return mPicasso;
    }

    public LruCache getCache() {
        return mCache;
    }
}