package com.xavey.woody.api;

import android.content.Context;

import com.xavey.woody.R;
import com.xavey.woody.helper.DBHelper;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tinmaungaye on 4/14/15.
 */
public class CustomErrorHandler implements ErrorHandler {

    private Context context;

    public CustomErrorHandler(Context context) {
        this.context = context;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {

        Response response = cause.getResponse();
        if(response!=null) {
            if (response.getStatus() == 401 || response.getStatus() == 403) {
                try {
                    DBHelper dbh = new DBHelper(this.context);
                    dbh.deleteAuth();
                    return new Exception(this.context.getString(R.string.api_error_unauthorized));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (response != null && response.getStatus() == 404) {
                try {
                    return new Exception(this.context.getString(R.string.api_error_notfound));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (response != null && response.getStatus() == 500) {
                try {
                    return new Exception(this.context.getString(R.string.api_error_servererror));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (response == null) {
                try {
                    return new Exception(this.context.getString(R.string.api_error_offline));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return new Exception(this.context.getString(R.string.api_error_offline));
    }
}
