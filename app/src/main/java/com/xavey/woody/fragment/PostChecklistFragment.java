package com.xavey.woody.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.api.model.Item;
import com.xavey.woody.api.model.Post;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TypeFaceHelper;
import com.xavey.woody.helper.UtilityHelper;


/**
 * Created by tinmaungaye on 8/12/15.
 */
public class PostChecklistFragment extends Fragment{
    // Store instance variables
    private Post post;
    private int page;


    // newInstance constructor for creating fragment with arguments
    public static PostChecklistFragment newInstance(int page, Post post) {
        PostChecklistFragment fragmentFirst = new PostChecklistFragment();
        Bundle args = new Bundle();
        args.putInt("page", page);
        args.putSerializable("post",post);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("page", 0);
        post = (Post)getArguments().getSerializable("post");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ScrollView scroller = new ScrollView(getActivity());
        LinearLayout linear = new LinearLayout(getActivity());
        linear.setOrientation(LinearLayout.VERTICAL);
        linear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        TextView tvQuestion = new TextView(getActivity());
        tvQuestion.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvQuestion.setGravity(Gravity.CENTER);
        tvQuestion.setTextSize(getActivity().getResources().getDimension(R.dimen.text_size_small));
        tvQuestion.setTag(R.id.question_id, post.get_id());
        TypeFaceHelper.setM3TypeFace(tvQuestion, getActivity());
        if (AppValues.getInstance().getZawGyiDisplay()) {
            tvQuestion.setText(Rabbit.uni2zg(post.getTitle()));
        } else {
            tvQuestion.setText(post.getTitle());
        }
        linear.addView(tvQuestion);

        int r = 0;

        for (Item item : post.getItems()) {
            final CheckBox chkbtn = new CheckBox(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 20, 0, 0);
            chkbtn.setLayoutParams(lp);
            chkbtn.setGravity(Gravity.CENTER_VERTICAL);
            chkbtn.setTag(R.id.radio_id, item.get_id());
            final int randomID = UtilityHelper.randInt(page, page*10000);
            chkbtn.setId(randomID);
            TypeFaceHelper.setM3TypeFace(chkbtn, getActivity());
            if (AppValues.getInstance().getZawGyiDisplay()) {
                chkbtn.setText(Rabbit.uni2zg(item.getTitle()));
            } else {
                chkbtn.setText(item.getTitle());
            }
            if(item.getVote_count()==1){
                chkbtn.setChecked(true);
            }
            linear.addView(chkbtn);
            r++;

            chkbtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int selectedId = buttonView.getId();
                    if (selectedId > -1) {
                        String itemID = (String) buttonView.getTag(R.id.radio_id);
                            callback.OnPostVotted(post, itemID, true, isChecked);
                    }

                }
            });
        }

        if(post.getExtra()!= null && post.getExtra()){
            TextView tvExtra = new TextView(getActivity());
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20, 0, 0, 0);
            tvExtra.setPadding(0,40,0,0);
            tvExtra.setLayoutParams(lp);

            TypeFaceHelper.setM3TypeFace(tvExtra, getActivity());
            if (AppValues.getInstance().getZawGyiDisplay()) {
                tvExtra.setText(Rabbit.uni2zg(post.getExtra_title()));
            } else {
                tvExtra.setText(post.getExtra_title());
            }

            final EditText etExtra = new EditText(getActivity());
            lp.setMargins(20, 0, 0, 0);
            etExtra.setLayoutParams(lp);
            etExtra.setText(post.getExtra_value());
            etExtra.setTextColor(getActivity().getResources().getColor(R.color.primary_dark_material_dark));
            etExtra.setTag(R.id.extra_id, post.get_id());

            etExtra.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    callback.OnPostVotted(post, etExtra.getText().toString(), false, false);
                }
            });
            linear.addView(tvExtra);
            linear.addView(etExtra);
        }

        scroller.addView(linear);
        return scroller;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callback = (OnPostVottedCallback) activity;
        }
        catch(ClassCastException e) {
            e.printStackTrace();
        }
    }

    private OnPostVottedCallback callback;

    public interface OnPostVottedCallback {
        void OnPostVotted(Post p, String i, Boolean b, Boolean isChecked);
    }
}
