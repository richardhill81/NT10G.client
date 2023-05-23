package com.shwangce.nt10g.client.speedtest;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;

import com.shwangce.nt10g.client.BaseDialogFragment;
import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.sweetalert.SweetAlertDialog;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;
import com.shwangce.nt10g.client.util.SharedPreferencesUtil;

public class HxBoxInfoDialog extends BaseDialogFragment implements View.OnClickListener {

    private Context context;
    private Button hxinfo_button_submit,hxinfo_button_back;
    private EditText hxinfo_edittext_username,hxinfo_edittext_userid,hxinfo_edittext_userpwd,hxinfo_edittext_worksheetnum;
    private RadioButton hxinfo_worktype_xz,hxinfo_worktype_wx;
    private RadioGroup hxinfo_worktype_radiogroup;
    private OnHxBoxInfoClickListener submitClickListener,cancelClickListener;
    private int worktype = 0;
    public HxBoxInfoDialog() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        context = getActivity();
        View v = inflater.inflate(R.layout.content_hxinfo, container, false);
        hxinfo_edittext_username = (EditText)v.findViewById(R.id.hxinfo_edittext_username);
        hxinfo_edittext_userid = (EditText)v.findViewById(R.id.hxinfo_edittext_userid);
        hxinfo_edittext_userpwd = (EditText) v.findViewById(R.id.hxinfo_edittext_userpwd);
        hxinfo_edittext_worksheetnum = (EditText) v.findViewById(R.id.hxinfo_edittext_worksheetnum);
        hxinfo_button_submit = (Button)v.findViewById(R.id.hxinfo_button_submit);
        hxinfo_button_back = (Button)v.findViewById(R.id.hxinfo_button_back);
        hxinfo_worktype_radiogroup = (RadioGroup)v.findViewById(R.id.hxinfo_worktype_radiogroup);
        hxinfo_worktype_xz = (RadioButton)v.findViewById(R.id.hxinfo_worktype_xz);
        hxinfo_worktype_wx = (RadioButton)v.findViewById(R.id.hxinfo_worktype_wx);
        hxinfo_button_submit.setOnClickListener(this);
        hxinfo_button_back.setOnClickListener(this);
        hxinfo_worktype_radiogroup.setOnCheckedChangeListener(worktypeChangeListener);
        initData();
        return v;
    }

    public static interface OnHxBoxInfoClickListener {
        void onClick (HxBoxInfoDialog hxBoxInfoDialog,String AdditionalInfo);
    }

    public HxBoxInfoDialog setSubmitClickListener (OnHxBoxInfoClickListener listener) {
        submitClickListener = listener;
        return this;
    }

    public HxBoxInfoDialog setCancelClickListener (OnHxBoxInfoClickListener listener) {
        cancelClickListener = listener;
        return this;
    }

    private final RadioGroup.OnCheckedChangeListener worktypeChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            switch (checkedId) {
                case R.id.hxinfo_worktype_xz:
                    worktype = 0;
                    break;
                case R.id.hxinfo_worktype_wx:
                    worktype = 1;
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hxinfo_button_submit:
                String hxusername = hxinfo_edittext_username.getText().toString().trim();
                String hxuserpwd = hxinfo_edittext_userpwd.getText().toString().trim();
                String hxuserid = hxinfo_edittext_userid.getText().toString().trim();
                String hxworksheetnum = hxinfo_edittext_worksheetnum.getText().toString().trim();
                HxBoxBean hxBoxBean = null;
                if(hxusername.isEmpty() && hxuserid.isEmpty() && hxuserpwd.isEmpty() && hxworksheetnum.isEmpty()) {
                    toStartTest("");
                } else {
                    //modify by hzj on 2019.4.8
                    //修改华夏测速线务员信息填写限制条件，信息全为空或全不为空。
                    if(!hxusername.isEmpty() && !hxuserid.isEmpty() && !hxuserpwd.isEmpty() && !hxworksheetnum.isEmpty()){
                        hxBoxBean = new HxBoxBean(hxusername,hxuserpwd,hxuserid,hxworksheetnum);
                        hxBoxBean.setUserName(hxusername);
                        hxBoxBean.setPwd(hxuserpwd);
                        hxBoxBean.setUserid(hxuserid);
                        hxBoxBean.setWorksheetnum(hxworksheetnum);
                        hxBoxBean.setWorktype(worktype);
                        ProjectUtil.hxBoxBean = hxBoxBean;
                        SharedPreferencesUtil.setHxUserInfo(context,hxBoxBean);
                        toStartTest(hxBoxBean.toString());
                    }else if(hxusername.isEmpty()){
                        Log.i("HxBoxInfoDialog","hxusername.isEmpty()");
                        new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("用户名不能为空")
                                .show();
                        break;
                    }else if(hxuserpwd.isEmpty()) {
                        Log.i("HxBoxInfoDialog","hxuserpwd.isEmpty()");
                        new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("密码不能为空")
                                .show();
                        break;
                    }else if(hxuserid.isEmpty()) {
                        Log.i("HxBoxInfoDialog","hxuserid.isEmpty()");
                        new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("工号不能为空")
                                .show();
                        break;
                    }else if(hxworksheetnum.isEmpty()) {
                        Log.i("HxBoxInfoDialog","hxworksheetnum.isEmpty()");
                        new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("工单号不能为空")
                                .show();
                        break;
                    }


//                    if(hxusername.isEmpty() ) {
//                        new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
//                                .setTitleText("用户名不能为空")
//                                .show();
//                    } else if(hxworksheetnum.isEmpty()) {
//                        new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
//                                .setTitleText("工单号不能为空")
//                                .show();
//                    } else {
//                        hxBoxBean = new HxBoxBean(hxusername,hxuserpwd,hxuserid,hxworksheetnum);
//                        hxBoxBean.setUserName(hxusername);
//                        hxBoxBean.setPwd(hxuserpwd);
//                        hxBoxBean.setUserid(hxuserid);
//                        hxBoxBean.setWorksheetnum(hxworksheetnum);
//                        hxBoxBean.setWorktype(worktype);
//                        ProjectUtil.hxBoxBean = hxBoxBean;
//                        SharedPreferencesUtil.setHxUserInfo(context,hxBoxBean);
//                        toStartTest(hxBoxBean.toString());
//                    }
                }
                break;

            case R.id.hxinfo_button_back:
                if (cancelClickListener != null) {
                    cancelClickListener.onClick(HxBoxInfoDialog.this,"");
                } else {
                    dismiss();
                }
                break;
        }
    }

    private void toStartTest(String hxboxInfo) {
        if (submitClickListener != null) {
            submitClickListener.onClick(HxBoxInfoDialog.this,hxboxInfo);
        } else {
            dismiss();
        }
    }

    private void initData() {
        if(ProjectUtil.hxBoxBean != null) {
            setEditTextValue(hxinfo_edittext_username,ProjectUtil.hxBoxBean.getUserName());
            setEditTextValue(hxinfo_edittext_userid,ProjectUtil.hxBoxBean.getUserid());
            setEditTextValue(hxinfo_edittext_userpwd,ProjectUtil.hxBoxBean.getPwd());
            setEditTextValue(hxinfo_edittext_worksheetnum,ProjectUtil.hxBoxBean.getWorksheetnum());
            if(ProjectUtil.hxBoxBean.getWorktype() == 0) {
                hxinfo_worktype_xz.setChecked(true);
            } else {
                hxinfo_worktype_wx.setChecked(true);
            }
        } else {
            setEditTextValue(hxinfo_edittext_username,"");
            setEditTextValue(hxinfo_edittext_userid,"");
            setEditTextValue(hxinfo_edittext_userpwd,"");
            setEditTextValue(hxinfo_edittext_worksheetnum,"");
            hxinfo_worktype_xz.setChecked(true);
        }
        hxinfo_button_submit.setText("确认");
        hxinfo_button_back.setText("返回");
    }

    private void setEditTextValue(EditText editText,String value) {
        try {
            editText.setText(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
