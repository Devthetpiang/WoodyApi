package com.xavey.woody.helper;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

/**
 * Created by tinmaungaye on 5/11/15.
 */
public class WValueFormatter implements ValueFormatter {

    private DecimalFormat mFormat;

    public WValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value) {

        if(value > 0)
            return mFormat.format(value);
        else
            return "";
    }
}
