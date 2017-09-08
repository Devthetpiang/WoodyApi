package com.xavey.woody.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xavey.woody.R;
import com.xavey.woody.api.SampleClient;
import com.xavey.woody.api.model.API_Response;
import com.xavey.woody.api.model.Comment;
import com.xavey.woody.api.model.Post;
import com.xavey.woody.helper.AppValues;
import com.xavey.woody.helper.Rabbit;
import com.xavey.woody.helper.TypeFaceHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tinmaungaye on 5/7/15.
 */
public class CommentDialogFragment extends DialogFragment {

    @InjectView(R.id.tVQusestionCDai)
    TextView tVQusestionCDai;

    @InjectView(R.id.etCommentCDai)
    EditText etCommentCDai;

    @InjectView(R.id.tVMessageCDia)
    TextView tVMessageCDia;

    @InjectView(R.id.btnOKCDai)
    Button btnOKCatDia;

    private Post post = new Post();
    private String authToken = "";
    private String comment="";
    private Boolean isSubmitOk=false;
    private Context conx;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private OnCommentSettedCallback callback;

    public Context getConx() {
        return conx;
    }

    public void setConx(Context conx) {
        this.conx = conx;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public interface OnCommentSettedCallback {
        void OnCommentSetted(Boolean isok);
    }

    public CommentDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (OnCommentSettedCallback) activity;
        }
        catch(ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_comment, container);
        ButterKnife.inject(this, view);
        if(AppValues.getInstance().getZawGyiDisplay()){
            tVQusestionCDai.setText(Rabbit.uni2zg(post.getTitle()));
        }
        else {
            tVQusestionCDai.setText(post.getTitle());
        }
        TypeFaceHelper.setM3TypeFace(tVQusestionCDai, conx);
        //TODO Set title from string values
        getDialog().setTitle("Post comment");
        return view;
    }

    @OnClick(R.id.btnOKCDai)
    public void onOkClick(){
        submitted();
        btnOKCatDia.setEnabled(false);
        comment=etCommentCDai.getText().toString();

        Comment cm = new Comment();
        cm.setPost(post);
        cm.setComment_Text(comment);

        SampleClient.getWoodyApiClient(this.conx).postAComment(this.authToken, cm.getPost().get_id(), cm, new Callback<API_Response>() {
            @Override
            public void success(API_Response apiRes, Response response) {
                if (apiRes.getMessage().equals(conx.getString(R.string.reg_code_010_success))) {
                    isSubmitOk = true;
                    etCommentCDai.setText("");
                    getDialog().dismiss();
                } else {
                    btnOKCatDia.setEnabled(true);
                    tVMessageCDia.setText("Please check your entry and try again later.");
                    tVMessageCDia.setTextColor(getResources().getColor(R.color.red_500));
                    toBeSubmitted();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                btnOKCatDia.setEnabled(true);
                tVMessageCDia.setText("Please check your entry and try again later.");
                tVMessageCDia.setTextColor(getResources().getColor(R.color.red_500));
                Log.d("regNewPost", error.toString());
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        try {
            getFragmentManager().beginTransaction().remove(CommentDialogFragment.this).commit(); // .addToBackStack(null).commit();
            btnOKCatDia.setEnabled(true);
            callback.OnCommentSetted(isSubmitOk);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void submitted(){
        btnOKCatDia.setText(getResources().getString(R.string.button_label_loading));
        btnOKCatDia.setEnabled(false);
    }

    public void toBeSubmitted(){
        btnOKCatDia.setEnabled(true);
        btnOKCatDia.setText(getResources().getString(R.string.button_label_comment));
    }

}
