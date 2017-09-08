package com.xavey.woody.helper;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * Created by tinmaungaye on 7/24/15.
 */
public class CustomTypefaceSpan extends MetricAffectingSpan {
    private Typeface mTypeface;
    public CustomTypefaceSpan(Typeface typeface) {
        mTypeface = typeface;
    }

    @Override
    public void updateMeasureState(TextPaint p) {
        p.setTypeface(mTypeface);
        p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setTypeface(mTypeface);
        tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
    }
}
