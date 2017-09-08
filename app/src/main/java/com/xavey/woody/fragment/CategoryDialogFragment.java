package com.xavey.woody.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.xavey.woody.R;
import com.xavey.woody.adapter.CategoryDialogItemAdapter;
import com.xavey.woody.api.model.Category;
import com.xavey.woody.interfaces.OnItemClickListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by tinmaungaye on 5/5/15.
 */
public class CategoryDialogFragment extends DialogFragment implements OnItemClickListener {

    @InjectView(R.id.gVcategoryDia)
    GridView gVcategoryDia;

    @InjectView(R.id.btnOKCatDia)
    Button btnOKCatDia;

    public CategoryDialogItemAdapter mCategories;

    private ArrayList<Category> alCategories = new ArrayList<Category>();

    public ArrayList<Category> getAlCategories() {
        return alCategories;
    }

    public void setAlCategories(ArrayList<Category> alCategories) {
        this.alCategories = alCategories;
    }

    private OnCategorySettedCallback callback;

    public interface OnCategorySettedCallback {
        void OnCategorySetted(ArrayList<Category> selectedList);
    }

    public CategoryDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (OnCategorySettedCallback) activity;
        }
        catch(ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_category, container);
        ButterKnife.inject(this,view);

        mCategories = new CategoryDialogItemAdapter(alCategories, getActivity(),this);
        gVcategoryDia.setAdapter(mCategories);
        //TODO Set title from string values
        getDialog().setTitle( getResources().getString(R.string.category_dialog_title) + " ("+ mCategories.getSelectedList().size() +" / 3)" );
        return view;
    }

    @OnClick(R.id.btnOKCatDia)
    public void onOkClick(){
        getDialog().dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        getFragmentManager().beginTransaction().remove(CategoryDialogFragment.this).commit();
        callback.OnCategorySetted(mCategories.getSelectedList());
    }

    @OnItemClick(R.id.gVcategoryDia)
    public void onItemClick(int position) {
        Log.d("xxx", "Position is "+position);

    }

    @Override
    public void onClick(View view, int position) {
        getDialog().setTitle( getResources().getString(R.string.category_dialog_title) + " ("+ mCategories.getSelectedList().size() +" / 3)" );
    }

}
