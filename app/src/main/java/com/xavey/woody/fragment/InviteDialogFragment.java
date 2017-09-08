package com.xavey.woody.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.xavey.woody.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by tinmaungaye on 8/28/15.
 */
public class InviteDialogFragment extends DialogFragment {

    @InjectView(R.id.etMessageInvDai)
    EditText etMessageInvDai;

    @InjectView(R.id.etPhoneInvDai)
    EditText etPhoneInvDai;

    @InjectView(R.id.btnOKInvDai)
    Button btnOKInvDai;

    private String phone = "";

    private OnInviteConfirmedCallback callback;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public InviteDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public interface OnInviteConfirmedCallback {
        void OnInviteConfirmed(String phoneNo, String message);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (OnInviteConfirmedCallback) activity;
        }
        catch(ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_invite, container);
        ButterKnife.inject(this, view);
        //TODO Set title from string values
        etPhoneInvDai.setText("");
        etPhoneInvDai.setText(getPhone());
        getDialog().setTitle("Grow your meller network");
        return view;
    }

    @OnClick(R.id.btnOKInvDai)
    public void onOkClick(){
        callback.OnInviteConfirmed(etPhoneInvDai.getText().toString(), etMessageInvDai.getText().toString());
        getDialog().dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        try {
            getFragmentManager().beginTransaction().remove(InviteDialogFragment.this).commit(); // .addToBackStack(null).commit();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
