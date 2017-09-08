package com.xavey.woody.helper;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;

/**
 * Created by tinmaungaye on 5/9/15.
 */
public class TypeFaceHelper {
    private static Typeface mTypeFace;

    public TypeFaceHelper(Activity act) {

    }

    /*public static void setZGTypeFace(View v, Activity act) {
        tfZG = Typeface.createFromAsset(act.getAssets(),
                "fonts/zawgyione2008.ttf");
        if (v.getClass().getName().toString().equals("android.widget.TextView")) {
            ((TextView) v).setTypeface(tfZG);
        } else if (v.getClass().getName().toString().equals("android.widget.EditText")) {
            ((EditText) v).setTypeface(tfZG);
        } else if (v.getClass().getName().toString().equals("android.widget.RadioButton")) {
            ((RadioButton) v).setTypeface(tfZG);
        }
    }*/

    public static Typeface getCurrentTypeFace(AssetManager act){
        if(AppValues.getInstance().getZawGyiDisplay()){
            mTypeFace = Typeface.createFromAsset(act, "fonts/zawgyione2008.ttf");
        }
        else{
            mTypeFace = Typeface.createFromAsset(act,
                    "fonts/mmrCensus.v5.minbe5.ttf");
        }
        return mTypeFace;
    }
    public static void setM3TypeFace(View v, Context act) {
/** not force
//        tfM3 = Typeface.createFromAsset(act.getAssets(),
//                "fonts/myanmar3.ttf");
        if(AppValues.getInstance().getZawGyiDisplay()){
            mTypeFace = Typeface.createFromAsset(act.getAssets(), "fonts/zawgyione2008.ttf");
        }
        else{
            mTypeFace = Typeface.createFromAsset(act.getAssets(),
                    "fonts/mmrCensus.v5.minbe5.ttf");
        }
        if (v.getClass().getName().toString().equals("android.widget.TextView") || v.getClass().getName().toString().equals("android.support.v7.widget.AppCompatTextView")) {
            ((TextView) v).setTypeface(mTypeFace);
        } else if (v.getClass().getName().toString().equals("android.widget.EditText")
                || v.getClass().getName().toString().equals("android.support.v7.widget.AppCompatEditText")) {
            ((EditText) v).setTypeface(mTypeFace);
        } else if (v.getClass().getName().toString().equals("android.widget.RadioButton")) {
            ((RadioButton) v).setTypeface(mTypeFace);
        }else if(v.getClass().getName().toString().equals("android.widget.Button")){
            ((Button) v).setTypeface(mTypeFace);
        }
 end not force**/
    }

    public static void setTypeFace(Boolean zawgyi, View v, Activity act){
/** not force

        Typeface typeface;
        if(zawgyi){
            typeface = Typeface.createFromAsset(act.getAssets(), "fonts/zawgyione2008.ttf");
        }
        else{
            typeface = Typeface.createFromAsset(act.getAssets(),
                    "fonts/mmrCensus.v5.minbe5.ttf");
        }
        if (v.getClass().getName().toString().equals("android.widget.TextView") || v.getClass().getName().toString().equals("android.support.v7.widget.AppCompatTextView")) {
            ((TextView) v).setTypeface(typeface);
        } else if (v.getClass().getName().toString().equals("android.widget.EditText")
                || v.getClass().getName().toString().equals("android.support.v7.widget.AppCompatEditText")) {
            ((EditText) v).setTypeface(typeface);
        } else if (v.getClass().getName().toString().equals("android.widget.RadioButton")) {
            ((RadioButton) v).setTypeface(typeface);
        }else if(v.getClass().getName().toString().equals("android.widget.Button")){
            ((Button) v).setTypeface(typeface);
        }
 end not force**/
    }

    public static Typeface getUniTypeFace(Activity act){
        if(AppValues.getInstance().getZawGyiDisplay()){
            mTypeFace = Typeface.createFromAsset(act.getAssets(), "fonts/zawgyione2008.ttf");
        }
        else{
            mTypeFace = Typeface.createFromAsset(act.getAssets(),"fonts/mmrCensus.v5.minbe5.ttf");
        }
        return mTypeFace;
    }
}


