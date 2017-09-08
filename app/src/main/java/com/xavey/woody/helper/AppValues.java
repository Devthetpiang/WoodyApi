package com.xavey.woody.helper;

import android.os.Environment;

import com.xavey.woody.R;
import com.xavey.woody.api.model.Categories;
import com.xavey.woody.api.model.Category;
import com.xavey.woody.api.model.User;
import com.xavey.woody.api.model.VoteSet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tinmaungaye on 5/19/15.
 */
public class AppValues{
    private static AppValues mInstance = null;

    public static String AuthToken ="";
    public static String CurrentPost = "";

    private boolean mZawGyiDisplay = true;
    private boolean mCategoryDisplay = true;

    private static String root = Environment.getExternalStorageDirectory().toString();
    public static File XAVEY_DIRECTORY = new File(root,"/XaveyWoody");
    public static int REQUEST_CAMERA = 101;
    public static int SELECT_FILE = 102;
    public static String FIELD_NAME_TMP = "";
    public static String FIELD_TYPE_TMP = "";
    public static String FIELD_HELP_TMP = "";
    public static String VIEW_ID_TMP = "";
    public static String IMAGE_PATH_TMP = "";
    public static String PHOTO_NAME_TMP = "";
    public static User loginUser = new User();
    public static String SEARCH_VIEW_QUERY = "";
    public static List<Category> opt_out_categories=new ArrayList<>();

    public static final String ITEM_VALUE_SKIP = "skip";
    public static final String POST_TYPE_CHECKLIST = "chk";
    public static final String POST_TYPE_RADIOGROUP = "rdo";

    public static String[] LAYOUT_PROFILE_LIST_NORMAL={"question","follower", "following", "favourite"};
    public static String[] LAYOUT_PROFILE_LIST_PREMIUM={"question_set","question","follower", "following", "favourite"};
    public static String[] LAYOUT_REFERRAL_LIST={"Contacts","Your Mellers"};
    public static String[] LAYOUT_PROMOTION_POST_LIST={"post","postset"};
    public static int[] LAYOUT_REFERRAL_IC_LIST={
            R.drawable.ic_followings,
            R.drawable.ic_followers
    };

    public static int[] LAYOUT_PROFILE_LIST_IC_NORMAL = {
            R.drawable.ic_questions,
            R.drawable.ic_followers,
            R.drawable.ic_followings,
            R.drawable.ic_favs
    };

    public static int[] LAYOUT_PROFILE_LIST_IC_PREMIUM = {
            R.drawable.ic_post_set,
            R.drawable.ic_questions,
            R.drawable.ic_followers,
            R.drawable.ic_followings,
            R.drawable.ic_favs
    };

    public static int[]LAYOUT_PROMOTION_POST_IC_LIST ={
            R.drawable.ic_questions,
            R.drawable.ic_post_set
    };
    public static String loadedTown="";

    public static String GCM_ID="";

    public static VoteSet VOTE_SET = null;

    private AppValues(){
    }

    public static AppValues getInstance(){
        if(mInstance == null)
        {
            mInstance = new AppValues();
        }
        return mInstance;
    }

    public static String getCurrentPost() {
        return CurrentPost;
    }

    public static void setCurrentPost(String currentPost) {
        CurrentPost = currentPost;
    }

    public static String getAuthToken() {
        return AuthToken;
    }

    public static void setAuthToken(String authToken) {
        AuthToken = authToken;
    }

    public boolean getZawGyiDisplay(){
        return this.mZawGyiDisplay;
    }

    public void setZawGyiDisplay(boolean value){
        mZawGyiDisplay = value;
    }

    public static File getXAVEY_DIRECTORY() {
        return XAVEY_DIRECTORY;
    }

    public static void setXAVEY_DIRECTORY(File XAVEY_DIRECTORY) {
        AppValues.XAVEY_DIRECTORY = XAVEY_DIRECTORY;
    }

    public static int getREQUEST_CAMERA() {
        return REQUEST_CAMERA;
    }

    public static void setREQUEST_CAMERA(int REQUEST_CAMERA) {
        AppValues.REQUEST_CAMERA = REQUEST_CAMERA;
    }

    public static int getSELECT_FILE() {
        return SELECT_FILE;
    }

    public static void setSELECT_FILE(int SELECT_FILE) {
        AppValues.SELECT_FILE = SELECT_FILE;
    }

    public static String getFIELD_NAME_TMP() {
        return FIELD_NAME_TMP;
    }

    public static void setFIELD_NAME_TMP(String FIELD_NAME_TMP) {
        AppValues.FIELD_NAME_TMP = FIELD_NAME_TMP;
    }

    public static String getFIELD_TYPE_TMP() {
        return FIELD_TYPE_TMP;
    }

    public static void setFIELD_TYPE_TMP(String FIELD_TYPE_TMP) {
        AppValues.FIELD_TYPE_TMP = FIELD_TYPE_TMP;
    }

    public static String getFIELD_HELP_TMP() {
        return FIELD_HELP_TMP;
    }

    public static void setFIELD_HELP_TMP(String FIELD_HELP_TMP) {
        AppValues.FIELD_HELP_TMP = FIELD_HELP_TMP;
    }

    public static String getVIEW_ID_TMP() {
        return VIEW_ID_TMP;
    }

    public static void setVIEW_ID_TMP(String VIEW_ID_TMP) {
        AppValues.VIEW_ID_TMP = VIEW_ID_TMP;
    }

    public static String getIMAGE_PATH_TMP() {
        return IMAGE_PATH_TMP;
    }

    public static void setIMAGE_PATH_TMP(String IMAGE_PATH_TMP) {
        AppValues.IMAGE_PATH_TMP = IMAGE_PATH_TMP;
    }

    public static String getPHOTO_NAME_TMP() {
        return PHOTO_NAME_TMP;
    }

    public static void setPHOTO_NAME_TMP(String PHOTO_NAME_TMP) {
        AppValues.PHOTO_NAME_TMP = PHOTO_NAME_TMP;
    }

    public static String getGcmId() {
        return GCM_ID;
    }

    public static void setGcmId(String gcmId) {
        GCM_ID = gcmId;
    }

    public static String getLoadedTown() {
        return loadedTown;
    }

    public static void setLoadedTown(String loadedTown) {
        AppValues.loadedTown = loadedTown;
    }

    public static VoteSet getVoteSet() {
        return VOTE_SET;
    }

    public static void setVoteSet(VoteSet voteSet) {
        VOTE_SET = voteSet;
    }

    public static List<Category> getOpt_out_categories() {
        return opt_out_categories;
    }

    public static void setOpt_out_categories(List<Category> opt_out_categories) {
        AppValues.opt_out_categories = opt_out_categories;
    }
}
