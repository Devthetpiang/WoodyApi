package com.xavey.woody.interfaces;

/**
 * Created by tinmaungaye on 3/28/15.
 */

import android.view.View;

/**
 * Interface for receiving click events from cells.
 */
public interface OnItemClickListener {
    void onClick(View view, int position);
}
