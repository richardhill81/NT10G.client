package com.shwangce.nt10g.client.modeselect;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.shwangce.nt10g.client.BaseDialogFragment;
import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.sweetalert.SweetAlertDialog;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;

public class SetModeDialogFragment extends BaseDialogFragment {
    public SetModeDialogFragment() {
        super();
    }
    private SetModeDialogFragment instance;
    private Context context;
    private SetModePresenter mPresenter;
    private boolean clickFlag = false;
    private static class SetModeResultBean {
        private ProjectUtil.SetModeEnum mode = null;
        private String message = "";

        public SetModeResultBean(ProjectUtil.SetModeEnum mode, String message) {
            this.mode = mode;
            this.message = message;
        }

        public ProjectUtil.SetModeEnum getMode() {
            return mode;
        }

        public String getMessage() {
            return message;
        }
    }
    private SweetAlertDialog dialog;

    private final int MESSAGE_SETMODE_SUCCESS = 10;
    private final int MESSAGE_WAITING_REBOOT = 12;
    private final int MESSAGE_SETMODE_FAIL = 19;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SETMODE_SUCCESS:       //模式设置完成，进入测试项
                    clickFlag = false;
                    Log.d("SetModeDialogFragment","MESSAGE_SETMODE_SUCCESS");
                    if(dialog !=null && dialog.isShowing())
                    {
                        dialog.dismiss();
                        dialog = null;
                    }
                    instance.dismiss();
                    break;

                case MESSAGE_WAITING_REBOOT:
                    if(dialog.isShowing())
                        dialog.setTitleText("正在切换模式，请稍候...");
                    break;

                case MESSAGE_SETMODE_FAIL:          //模式设置失败，重新选择
                    clickFlag = false;
                    Log.d("SetModeDialogFragment","MESSAGE_SETMODE_FAIL");
                    SetModeResultBean resultBean  = (SetModeResultBean)msg.obj;
                    if(dialog !=null && dialog.isShowing())   dialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(resultBean.getMessage())
                            .setContentText(mPresenter.getModeString(resultBean.getMode()))
                            .setConfirmText("关闭")
                            .show();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v =  inflater.inflate(R.layout.dialog_selectmode, container, false);
        instance = this;
        context = getActivity();
        v.findViewById(R.id.btn_quitmode).setOnClickListener(v1 ->
                doQuitMode());
        v.findViewById(R.id.btn_singleLanInternal).setOnClickListener(v1 ->
                doSelectMode(ProjectUtil.SetModeEnum.SingleLanInternal));
        v.findViewById(R.id.btn_singleLanExternal).setOnClickListener(v1 ->
                doSelectMode(ProjectUtil.SetModeEnum.SingleLanExternal));
        v.findViewById(R.id.btn_doubleLan).setOnClickListener(v1 ->
                doSelectMode(ProjectUtil.SetModeEnum.DoubleLan));
        v.findViewById(R.id.btn_wifiDHCP).setOnClickListener(v1 ->
                doSelectMode(ProjectUtil.SetModeEnum.WifiDHCP));
        v.findViewById(R.id.btn_lanAndWifi).setOnClickListener(v1 ->
                doSelectMode(ProjectUtil.SetModeEnum.LanAndWifi));
        //v.findViewById(R.id.btn_iTVSimulate).setOnClickListener(v1 ->
        //        doSelectMode(ProjectUtil.SetModeEnum.ITVSimulate));
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog_MinWidth);
    }

    public void setPresenter(SetModePresenter presenter) {
        mPresenter = presenter;
    }

    private void doQuitMode() {
        //instance.getActivity().finish();
        instance.dismiss();
    }

    private void doSelectMode(ProjectUtil.SetModeEnum testMode) {
        if(clickFlag)  return;
        else clickFlag = true;
        String titleText;
        switch (testMode)
        {
            case SingleLanInternal:
                titleText = "正在设置'单内模式'，请稍候...";
                break;
            case SingleLanExternal:
                titleText = "正在设置'单外模式'，请稍候...";
                break;
            case DoubleLan:
                titleText = "正在设置'双口模式'，请稍候...";
                break;
            case WifiDHCP:
                titleText = "正在设置'WiFi模式'，请稍候...";
                break;
            case LanAndWifi:
                titleText = "正在设置'单内WIFI模式'，请稍候...";
                break;
            default:
                titleText = "";
                break;
        }
        if(dialog !=null && dialog.isShowing())
        {
            dialog.dismiss();
            dialog = null;
        }
        dialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText(titleText);
        dialog.setCancelable(false);
        dialog.show();
        mPresenter.setMode(testMode);
    }

    public void doSetModeSuccess(ProjectUtil.SetModeEnum mode) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_SETMODE_SUCCESS,mode));
    }

    public void doSetModeFail(ProjectUtil.SetModeEnum modeEnum,String failReason) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_SETMODE_FAIL, new SetModeResultBean(modeEnum, failReason)));
    }

    public void doWaitReboot(ProjectUtil.SetModeEnum mode) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_WAITING_REBOOT,mode));
    }
}