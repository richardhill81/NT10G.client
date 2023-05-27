package com.shwangce.nt10g.client.setaccess;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.shwangce.nt10g.client.BaseDialogFragment;
import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.sweetalert.SweetAlertDialog;
import com.shwangce.nt10g.client.util.ProjectUtil;
import com.shwangce.nt10g.client.util.SharedPreferencesUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class SetAccessDialogFragment extends BaseDialogFragment implements View.OnClickListener,SetAccessContract.View{

    private SetAccessDialogFragment instance;
    private AccessType selectType = AccessType.ACCESS_DHCP;
    private Context context;
    private Button btn_access_dhcp,btn_access_pppoe,btn_access_static,btn_back,btn_access_back,btn_access_submit;
    private EditText et_ip,et_nm,et_gw,et_dns;
    private EditText et_account,et_password;
    private LinearLayout ll_accessType,ll_accessDetail,ll_pppoeview,ll_staticview;
    private SetAccessContract.Presenter mPresenter;
    private SweetAlertDialog sweetAlertDialog;

    private final int MESSAGE_SUCCESS = 1;
    private final int MESSAGE_FAIL = 2;
    private final int MESSAGE_SETAUTODHCP = 3;
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SUCCESS:
                    if(sweetAlertDialog!=null && sweetAlertDialog.isShowing())
                        sweetAlertDialog.dismiss();
                    instance.dismiss();
                    break;

                case MESSAGE_FAIL:
                    String errString = msg.obj + "";
                    if(sweetAlertDialog!=null && sweetAlertDialog.isShowing())
                        sweetAlertDialog.dismiss();
                    sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("")
                            .setContentText(errString)
                            .setConfirmText("关闭");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            switch (selectType) {
                                case ACCESS_DHCP:
                                    ll_accessDetail.setVisibility(View.GONE);
                                    ll_accessType.setVisibility(View.VISIBLE);
                                    break;
                                case ACCESS_PPPOE:
                                    ll_accessType.setVisibility(View.GONE);
                                    ll_accessDetail.setVisibility(View.VISIBLE);
                                    ll_pppoeview.setVisibility(View.VISIBLE);
                                    ll_staticview.setVisibility(View.GONE);
                                    break;
                                case ACCESS_STATIC:
                                    ll_accessType.setVisibility(View.GONE);
                                    ll_accessDetail.setVisibility(View.VISIBLE);
                                    ll_pppoeview.setVisibility(View.GONE);
                                    ll_staticview.setVisibility(View.VISIBLE);
                                    break;
                            }
                        }
                    });
                    sweetAlertDialog.show();
                    break;

                case MESSAGE_SETAUTODHCP:
                    btn_access_dhcp.callOnClick();
                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };


    public SetAccessDialogFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        instance = this;
        context = getActivity();
        View v = inflater.inflate(R.layout.dialog_accesstype, container, false);
        ll_accessType = v.findViewById(R.id.ll_accessType);
        ll_accessDetail = v.findViewById(R.id.ll_accessDetail);
        ll_pppoeview = v.findViewById(R.id.ll_pppoe_view);
        ll_staticview = v.findViewById(R.id.ll_static_view);
        et_ip = v.findViewById(R.id.Edit_static_ip);
        et_nm = v.findViewById(R.id.Edit_static_netmask);
        et_gw = v.findViewById(R.id.Edit_static_gateway);
        et_dns = v.findViewById(R.id.Edit_static_dns);
        et_account = v.findViewById(R.id.Edit_pppoe_account);
        et_password = v.findViewById(R.id.Edit_pppoe_password);
        btn_access_dhcp = v.findViewById(R.id.btn_access_dhcp);
        btn_access_pppoe = v.findViewById(R.id.btn_access_pppoe);
        btn_access_static = v.findViewById(R.id.btn_access_static);
        btn_back = v.findViewById(R.id.btn_back);
        btn_access_submit = v.findViewById(R.id.btn_access_submit);
        btn_access_back = v.findViewById(R.id.btn_access_back);
        btn_access_dhcp.setOnClickListener(this);
        btn_access_pppoe.setOnClickListener(this);
        btn_access_static.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_access_submit.setOnClickListener(this);
        btn_access_back.setOnClickListener(this);
        initData();
        btn_access_dhcp.setVisibility(View.VISIBLE);
        btn_access_static.setVisibility(View.VISIBLE);
        btn_access_pppoe.setVisibility(View.GONE);
        /*
        switch (ProjectUtil.currentMode) {
            case SingleLanExternal:     //单外，显示设置dhcp和static
                btn_access_dhcp.setVisibility(View.VISIBLE);
                btn_access_pppoe.setVisibility(View.GONE);
                btn_access_static.setVisibility(View.VISIBLE);
                btn_back.setVisibility(View.VISIBLE);
                break;

            case WifiDHCP:
            case DoubleLan:
            case LanAndWifi:
                Timer timer = new Timer();
                btn_access_dhcp.setVisibility(View.VISIBLE);
                btn_access_pppoe.setVisibility(View.GONE);
                btn_access_static.setVisibility(View.GONE);
                btn_back.setVisibility(View.VISIBLE);
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(MESSAGE_SETAUTODHCP);
                    }
                },50);
                break;
            case SingleLanInternal:
                break;
        }
         */

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_access_static) {
            doAccessTypeSelect(AccessType.ACCESS_STATIC);
        } else if(v.getId() == R.id.btn_access_dhcp) {
            doAccessTypeSelect(AccessType.ACCESS_DHCP);
        } else if(v.getId() == R.id.btn_access_pppoe) {
            doAccessTypeSelect(AccessType.ACCESS_PPPOE);
        } else if(v.getId() == R.id.btn_back) {
            //mPresenter.doBack();
            this.dismiss();
        } else if(v.getId() == R.id.btn_access_submit) {
            switch (selectType) {
                case ACCESS_PPPOE:
                    startPPPoE();
                    break;

                case ACCESS_STATIC:
                    startStatic();
                    break;

                case ACCESS_DHCP:
                    startDhcp();
                    break;
            }
        } else if(v.getId() == R.id.btn_access_back) {
            ll_accessType.setVisibility(View.VISIBLE);
            ll_accessDetail.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPresenter(SetAccessContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void doShowAccessFail(String failreason) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_FAIL,failreason));
    }

    @Override
    public void doAccessSuccess() {
        handler.sendEmptyMessage(MESSAGE_SUCCESS);
    }


    private void initData() {
        StaticInfoBean staticInfo = SharedPreferencesUtil.getStaticInfo(context);
        et_ip.setText(staticInfo.getIpString());
        et_nm.setText(staticInfo.getNetMask());
        et_gw.setText(staticInfo.getGateWay());
        et_dns.setText(staticInfo.getDNS());
        PPPoEInfoBean pppoeInfo =  SharedPreferencesUtil.getPPPoEInfo(context);
        et_account.setText(pppoeInfo.getPPPoEAccount());
        et_password.setText(pppoeInfo.getPPPoEPassword());
    }

    private void doAccessTypeSelect(AccessType accessType) {
        selectType = accessType;
        switch (accessType) {
            case ACCESS_STATIC:
                ll_accessType.setVisibility(View.GONE);
                ll_staticview.setVisibility(View.VISIBLE);
                ll_pppoeview.setVisibility(View.GONE);
                ll_accessDetail.setVisibility(View.VISIBLE);
                break;
            case ACCESS_PPPOE:
                ll_accessType.setVisibility(View.GONE);
                ll_staticview.setVisibility(View.GONE);
                ll_pppoeview.setVisibility(View.VISIBLE);
                ll_accessDetail.setVisibility(View.VISIBLE);
                break;

            default:
                ll_accessType.setVisibility(View.VISIBLE);
                ll_staticview.setVisibility(View.GONE);
                ll_pppoeview.setVisibility(View.GONE);
                ll_accessDetail.setVisibility(View.GONE);
                btn_access_submit.callOnClick();
                break;
        }
    }

    private void startDhcp() {
        sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在获取地址，请稍候...");
        sweetAlertDialog.show();
        sweetAlertDialog.setCancelable(false);
        mPresenter.doDhcp();
    }

    private void startPPPoE() {
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        PPPoEInfoBean pppoeinfo = new PPPoEInfoBean(account,password);
        SharedPreferencesUtil.setPPPoEInfo(context,pppoeinfo);
        sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在拨号，请稍候...");
        sweetAlertDialog.show();
        sweetAlertDialog.setCancelable(false);
        mPresenter.doPPPoe(account,password);
    }

    private void startStatic() {
        String ip = et_ip.getText().toString();
        String netmask = et_nm.getText().toString();
        String gateway = et_gw.getText().toString();
        String dns = et_dns.getText().toString();
        StaticInfoBean staticInfo = new StaticInfoBean(ip,netmask,gateway,dns);
        SharedPreferencesUtil.setStaticInfo(context,staticInfo);
        sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("正在设置，请稍候...");
        sweetAlertDialog.show();
        sweetAlertDialog.setCancelable(false);
        mPresenter.doStatic(ip,netmask,gateway,dns);
    }
}
