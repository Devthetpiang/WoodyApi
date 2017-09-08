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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
 * Created by tinmaungaye on 19/8/15.
 */
public class PostRadioGroupFragment extends Fragment{
    // Store instance variables
    private Post post;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static PostRadioGroupFragment newInstance(int page, Post post) {
        PostRadioGroupFragment fragmentFirst = new PostRadioGroupFragment();
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

        final RadioGroup rgQuestions = new RadioGroup(getActivity());
        RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 20, 0, 0);
        rgQuestions.setLayoutParams(lp);
        rgQuestions.setOrientation(LinearLayout.VERTICAL);
        rgQuestions.setGravity(Gravity.LEFT);
        int r = 0;

        for (Item item : post.getItems()) {
            RadioButton rdbtn = new RadioButton(getActivity());

            rdbtn.setTag(R.id.radio_id, item.get_id());
            final int randomID = UtilityHelper.randInt(page, page*10000);
            rdbtn.setId(randomID);
            TypeFaceHelper.setM3TypeFace(rdbtn, getActivity());
            if (AppValues.getInstance().getZawGyiDisplay()) {
                rdbtn.setText(Rabbit.uni2zg(item.getTitle()));
            } else {
                rdbtn.setText(item.getTitle());
            }
            if(item.getVote_count()==1){
                rdbtn.setChecked(true);
            }
            rgQuestions.addView(rdbtn);
            r++;
        }
        rgQuestions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = group.getCheckedRadioButtonId();
                if(selectedId>-1) {
                    // find the radiobutton by returned id
                    RadioButton rbAnswer = (RadioButton) getActivity().findViewById(selectedId);
                    String itemID = (String) rbAnswer.getTag(R.id.radio_id);
                    callback.OnPostVotted(post, itemID, true,true);
                }

            }
        });
        linear.addView(tvQuestion);
        linear.addView(rgQuestions);

        if(post.getExtra()!= null && post.getExtra()){
            TextView tvExtra = new TextView(getActivity());
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            llp.setMargins(20, 0, 0, 0);
            tvExtra.setPadding(0,40,0,0);
            tvExtra.setLayoutParams(llp);

            TypeFaceHelper.setM3TypeFace(tvExtra, getActivity());
            if (AppValues.getInstance().getZawGyiDisplay()) {
                tvExtra.setText(Rabbit.uni2zg(post.getExtra_title()));
            } else {
                tvExtra.setText(post.getExtra_title());
            }

            final EditText etExtra = new EditText(getActivity());
            llp.setMargins(20, 0, 0, 0);
            etExtra.setLayoutParams(llp);
            etExtra.setText(post.getExtra_value());
            etExtra.setTextColor(getActivity().getResources().getColor(R.color.primary_dark_material_dark));
            etExtra.setTag(R.id.extra_id, post.get_id());

            etExtra.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count){}

                @Override
                public void beforeTextChanged(CharSequence s, int start,int count, int after) {}

                @Override
                public void afterTextChanged(Editable s)
                {
                    callback.OnPostVotted(post, etExtra.getText().toString() , false, false);
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