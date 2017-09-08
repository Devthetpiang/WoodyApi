package com.xavey.woody.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xavey.woody.api.model.Auth;
import com.xavey.woody.api.model.Notification;
import com.xavey.woody.api.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tinmaungaye on 4/9/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "XaveyWoody";

    // Table Names
    protected static final String TABLE_NOTI = "noti";
    protected static final String TABLE_AUTH = "auth";
    protected static final String TABLE_USER = "user";

    // post column names
    protected static final String N__ID = "_id";
    protected static final String N_USER = "user";
    protected static final String N_TITLE = "title";
    protected static final String N_POST = "post";
    protected static final String N_CREATED_ON = "created";
    protected static final String N_TYPE = "type";
    protected static final String N_CHECKED = "checked";

    // auth column names
    protected static final String A_ATOKENT = "access_token";
    protected static final String A_RTOKEN = "refresh_token";
    protected static final String A_EXPIRESIN = "expires_in";
    protected static final String A_TOKENTYPE = "token_type";

    // user column names
    protected static final String U__ID = "_id";
    protected static final String U_USER_NAME = "user_name";
    protected static final String U_HASHED_PASSWORD = "hashed_password";
    protected static final String U_NAME = "name";
    protected static final String U_EMAIL = "email";
    protected static final String U_PHONE = "phone";
    protected static final String U_DOB = "dob";

    // Table Create Statements

    // Auth table create statement
    private static final String CREATE_TABLE_AUTH = "CREATE TABLE IF NOT EXISTS "
            + TABLE_AUTH + "(" + A_ATOKENT + " TEXT PRIMARY KEY,"
            + A_RTOKEN + " TEXT,"
            + A_EXPIRESIN + " INTEGER,"
            + A_TOKENTYPE + " TEXT" + ")";

    // User table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
            + TABLE_USER + "(" + U__ID + " TEXT PRIMARY KEY,"
            + U_USER_NAME + " TEXT,"
            + U_HASHED_PASSWORD + " TEXT,"
            + U_NAME + " TEXT,"
            + U_EMAIL + " TEXT,"
            + U_PHONE + " TEXT,"
            + U_DOB + " DATETIME" + ")";

    private static final String CREATE_TABLE_NOTI = "CREATE TABLE IF NOT EXISTS "
            + TABLE_NOTI + "(" + N__ID + " TEXT PRIMARY KEY,"
            + N_CHECKED + " INTEGER,"
            + N_CREATED_ON + " DATETIME,"
            + N_USER + " TEXT,"
            + N_POST + " TEXT,"
            + N_TITLE + " TEXT,"
            + N_TYPE + " TEXT " + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_AUTH);
        db.execSQL(CREATE_TABLE_NOTI);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTH);
        // create new tables
        onCreate(db);
    }

    /**
     * get datetime
     * */
    public String getDateTime(Date d) throws Exception{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return dateFormat.format(d);

    }
    public Date getDateTime(String d) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return format.parse(d);
    }

    public Boolean getBoolean(Integer b){
        return b==1? true:false;
    }



    public ArrayList<Notification> getNoti() throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Notification> LNoti= new ArrayList<Notification>();
        String selectQuery = "SELECT * FROM " + TABLE_NOTI + " ORDER BY date("+ N_CREATED_ON +") DESC";
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            if (c.moveToFirst()) {

                while (c.isAfterLast() == false) {
                    Notification n = new Notification();
                    n.set_id(c.getString(c.getColumnIndex(N__ID)));
                    n.setPost(c.getString(c.getColumnIndex(N_POST)));
                    n.setTitle(c.getString(c.getColumnIndex(N_TITLE)));
                    n.setType(c.getString(c.getColumnIndex(N_TYPE)));
                    n.setChecked(getBoolean(c.getInt(c.getColumnIndex(N_CHECKED))));
                    n.setCreated_on(getDateTime(c.getString(c.getColumnIndex(N_CREATED_ON))));

                    LNoti.add(n);
                    c.moveToNext();
                }
            }
        }
        return LNoti;
    }

    public void appendNoti(Notification[] lN, String user,int check) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(CREATE_TABLE_NOTI); //with if not exists

        ContentValues values = new ContentValues();

        for(Notification n : lN){

            Cursor mCount= db.rawQuery("select count(*) from "+ TABLE_NOTI +" where "+ N__ID +"='" + n.get_id() + "'", null);
            mCount.moveToFirst();
            int count= mCount.getInt(0);
            mCount.close();
            if(count<=0) {
                int setCheck = check > -1 && n.getChecked()==null ? check : 0;
                values.put(N__ID, n.get_id());
                values.put(N_CHECKED, setCheck);
                values.put(N_CREATED_ON, getDateTime(n.getCreated_on()));
                values.put(N_POST, n.getPost());
                values.put(N_TITLE, n.getTitle());
                values.put(N_TYPE, n.getType());
                values.put(N_USER, user);
                //inset new row
                db.insert(TABLE_NOTI, null, values);
            }
        }
        String triggerQuery="DELETE FROM " + TABLE_NOTI + " WHERE " + N__ID + " NOT IN (SELECT " + N__ID + " FROM "+  TABLE_NOTI +
                " WHERE " + N_USER + " = '" + user + "' ORDER BY date("+ N_CREATED_ON +") DESC LIMIT 100)";
        db.rawQuery(triggerQuery,null);
    }

    public void updateCheckedNoti(String id, Date date) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(CREATE_TABLE_NOTI); //with if not exists
        ContentValues cv=new ContentValues();
        cv.put(N_CHECKED, 1);
        db.update(TABLE_NOTI, cv , N_POST+"='"+id+"' AND date("+N_CREATED_ON+")<=?",new String[]{"date('"+getDateTime(date)+"')"});
    }

    public void deleteNoti() throws Exception{
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NOTI, null, null);
    }

    public void createAuth(Auth a) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(A_ATOKENT, a.getAccess_token());
        values.put(A_RTOKEN, a.getRefresh_token());
        values.put(A_EXPIRESIN, a.getExpires_in());
        values.put(A_TOKENTYPE, a.getToken_type());

        //maintain single row in auth table
        db.delete(TABLE_AUTH, null, null);
        //inset new row
        db.insert(TABLE_AUTH, null, values);
    }

    public Auth getAUTH() throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_AUTH + " LIMIT 1";
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        Auth a = new Auth();
        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            a.setAccess_token(c.getString(c.getColumnIndex(A_ATOKENT)));
            a.setRefresh_token(c.getString(c.getColumnIndex(A_RTOKEN)));
            a.setExpires_in(c.getInt(c.getColumnIndex(A_EXPIRESIN)));
            a.setToken_type(c.getString(c.getColumnIndex(A_TOKENTYPE)));
        }
        return a;
    }

    public void deleteAuth() throws Exception{
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_AUTH, null, null);
    }

    public void createUser(User u) throws Exception {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(U_DOB, getDateTime(u.getDob()));
        values.put(U_EMAIL, u.getEmail());
        values.put(U_HASHED_PASSWORD, u.getHashed_password());
        //values.put(U_NAME, u.getName());
        values.put(U_PHONE, u.getPhone());
        values.put(U_USER_NAME,u.getUser_name());
        //inset new row
        db.insert(TABLE_USER, null, values);
    }

    public void deleteUser() throws Exception{
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_USER, null, null);
    }

    public User getUser() throws Exception {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_USER + " LIMIT 1";
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        User u = new User();

        if (c != null && c.getCount()>0) {
            c.moveToFirst();
            u.set_id(c.getString(c.getColumnIndex(U__ID)));
            //u.setDob(getDateTime(c.getString(c.getColumnIndex(U_DOB))));
            u.setEmail(c.getString(c.getColumnIndex(U_EMAIL)));
            u.setHashed_password(c.getString(c.getColumnIndex(U_HASHED_PASSWORD)));
            u.setPhone(c.getString(c.getColumnIndex(U_PHONE)));
            u.setUser_name(c.getString(c.getColumnIndex(U_USER_NAME)));
            //u.setName(c.getString(c.getColumnIndex(U_NAME)));
        }

        return u;
    }
}
