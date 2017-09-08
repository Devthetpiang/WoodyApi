package com.xavey.woody.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xavey.woody.R;

/**
 * Created by tinmaungaye on 3/24/16.
 */
public class BottomSheet {
    public static void openBottomSheet  (Activity a) {

        View view = a.getLayoutInflater().inflate (R.layout.bottom_sheet, null);
        final TextView txtUninstall = (TextView)view.findViewById( R.id.txt_uninstall);

        final Dialog mBottomSheetDialog = new Dialog (a,
                R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow ().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow ().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
    }

    public static void openTopSheet(Activity context, String text) {
        int Y = context.getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.bottom_sheet, null);
        TextView tv = (TextView)layout.findViewById(R.id.txt_uninstall);
        tv.setText(text);
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, Y);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
