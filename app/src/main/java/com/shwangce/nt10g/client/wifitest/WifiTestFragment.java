package com.shwangce.nt10g.client.wifitest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.shwangce.nt10g.client.BaseDialogFragment;
import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.sweetalert.SweetAlertDialog;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;
import com.shwangce.nt10g.client.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class WifiTestFragment extends BaseDialogFragment implements WifiTestContract.View {

    private Context context;
    private WifiTestFragment instance;
    private SweetAlertDialog mySweetALertDialog = null;
    private ListView wifiListView;
    private WifiListAdapter wifiListAdapter;
    private Button btn_scan,btn_close;
    private TextView tv_state;
    private final ArrayList<WifiBean> wifiBeans = new ArrayList<>();

    private WifiTestContract.Presenter mPresenter;

    private final int MESSAGE_PRE_SCAN = 1;
    private final int MESSAGE_STARTSCAN = 10;
    private final int MESSAGE_FINDAP = 11;
    private final int MESSAGE_SCAN_FINISHED = 12;
    private final int MESSAGE_CONNECT_SUCCESS = 13;
    private final int MESSAGE_QUERY_SUCCESS = 14;
    private final int MESSAGE_STOPSCAN_SUCCESS = 15;

    private final int MESSAGE_CLOSE_SUCCESS = 19;

    private final int MESSAGE_WIFI_ERROR = 90;

    private final int MESSAGE_STARTSCAN_FAIL = 91;
    private final int MESSAGE_SCAN_FAIL = 92;
    private final int MESSAGE_STOPSCAN_FAIL = 93;
    private final int MESSAGE_CONNECT_FAIL = 94;
    private final int MESSAGE_QUERY_FAIL = 95;



    @SuppressLint("HandlerLeak")
    private final Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_PRE_SCAN:
                    doPreScan();
                    mPresenter.startScan();
                    if(mySweetALertDialog != null && mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                    mySweetALertDialog =  new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("正在扫描WiFi信号...");
                    mySweetALertDialog.show();
                    break;

                case MESSAGE_STARTSCAN:
                    /*
                    if(mySweetALertDialog != null && mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                     */
                    break;

                case MESSAGE_FINDAP:
                    WifiBean bean = (WifiBean)msg.obj;
                    wifiBeans.add(bean);
                    wifiListAdapter.notifyDataSetChanged();/*
                    if(mySweetALertDialog != null && mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();*/
                    break;

                case MESSAGE_SCAN_FINISHED:
                    doAfterScanFinished();
                    if(mySweetALertDialog != null && mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("WIFI扫描完毕！")
                            .show();
                    break;

                case MESSAGE_STOPSCAN_SUCCESS:
                    /*
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("扫描完毕")
                            .setConfirmText("停止扫描成功")
                            .show();
                     */
                    break;

                case MESSAGE_CONNECT_SUCCESS:
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("连接成功！")
                            .show();
                    if(mySweetALertDialog != null && mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                    if(mySweetALertDialog != null && mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                    instance.dismiss();
                    break;

                case MESSAGE_QUERY_SUCCESS:

                    break;

                case MESSAGE_CLOSE_SUCCESS:

                    break;

                case MESSAGE_STARTSCAN_FAIL:
                    doAfterScanFinished();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(msg.obj + "")
                            .setContentText("开始扫描失败")
                            .setConfirmText("重新扫描")
                            .setConfirmClickListener(sweetAlertDialog -> {
                                sweetAlertDialog.dismiss();
                                doPreScan();
                                mPresenter.startScan();
                            })
                            .setCancelText("退出")
                            .setCancelClickListener(sweetAlertDialog -> instance.dismiss())
                            .show();
                    break;

                case MESSAGE_SCAN_FAIL:
                    doAfterScanFinished();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(msg.obj + "")
                            .setContentText("扫描失败")
                            .setConfirmText("重新扫描")
                            .setConfirmClickListener(sweetAlertDialog -> {
                                sweetAlertDialog.dismiss();
                                doPreScan();
                                mPresenter.startScan();
                            })
                            .setCancelText("退出")
                            .setCancelClickListener(sweetAlertDialog -> instance.dismiss())
                            .show();
                    break;

                case MESSAGE_STOPSCAN_FAIL:
                    if(mySweetALertDialog != null && mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(msg.obj + "")
                            .setContentText("停止扫描失败!")
                            .show();
                    break;

                case MESSAGE_CONNECT_FAIL:
                    if(mySweetALertDialog != null && mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(msg.obj + "")
                            .setContentText("连接失败!")
                            .show();
                    break;

                case MESSAGE_QUERY_FAIL:
                    break;

                case MESSAGE_WIFI_ERROR:
                    doAfterScanFinished();
                    if(mySweetALertDialog != null && mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(msg.obj + "")
                            .show();
                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this.getActivity();
        this.instance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_wifi,container,false);
        instance = this;
        tv_state = view.findViewById(R.id.fragment_wifi_tx_state);
        btn_scan = view.findViewById(R.id.wifilist_btn_scan);
        btn_close = view.findViewById(R.id.wifilist_btn_close);
        btn_scan.setVisibility(View.VISIBLE);
        btn_scan.setOnClickListener(v -> myHandler.sendEmptyMessage(MESSAGE_PRE_SCAN));
        btn_close.setOnClickListener(v -> {
            mPresenter.stopScan();
            instance.dismiss();
        });
        wifiListAdapter = new WifiListAdapter(context, wifiBeans);
        wifiListView = view.findViewById(R.id.wifi_list);
        wifiListView.setAdapter(wifiListAdapter);
        wifiListView.setOnItemClickListener((adapterView, view1, i, l) -> {
            final WifiBean wifiBean = wifiListAdapter.getItem(i);
            switch (wifiBean.getSecurityMode()) {
                case WEP:
                    if (mySweetALertDialog != null && mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("暂不支持WEP")
                            .show();
                    break;

                case Unknown:
                    if (mySweetALertDialog != null && mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("未知的加密类型")
                            .setContentText(wifiBean.getSecurityModeString())
                            .show();
                    break;

                case NULL:
                case OPN:
                    wifiBean.setPassword("");
                    if(!isApInHistory(wifiBean))
                        updateHistoryAp(wifiBean);
                    if (mySweetALertDialog != null && mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                    mySweetALertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("正在连接 " + wifiBean.getEssid() + " ，请稍候...");
                    mySweetALertDialog.show();
                    if(mPresenter.isScanning()) {
                        mPresenter.stopScan();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                mPresenter.startConnect(wifiBean);
                            }
                        },3000);
                    } else {
                        mPresenter.startConnect(wifiBean);
                    }
                    break;

                default:
                    final EditText editText = new EditText(context);
                    final WifiBean historyAP = mPresenter.getHistoryApByBssid(wifiBean.getBssid());
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    if(historyAP != null)
                        editText.setText(historyAP.getPassword());
                    dialog.setTitle("请输入密码");
                    dialog.setView(editText);
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String pwd = editText.getText().toString().trim();
                            if(pwd.length() ==0 ) {
                                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("请输入密码!")
                                        .setContentText("开始扫描失败")
                                        .show();
                                return;
                            }
                            if (mySweetALertDialog != null && mySweetALertDialog.isShowing())
                                mySweetALertDialog.dismiss();
                            if(historyAP == null)  {
                                wifiBean.setPassword(editText.getText().toString().trim());
                                updateHistoryAp(wifiBean);
                            } else {
                                if (!historyAP.getPassword().equals(editText.getText().toString().trim())) {
                                    wifiBean.setPassword(editText.getText().toString().trim());
                                    updateHistoryAp(wifiBean);
                                } else {
                                    wifiBean.setPassword(historyAP.getPassword());
                                }
                            }
                            mySweetALertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                                    .setTitleText("正在连接 " + wifiBean.getEssid() + " ，请稍候...");
                            mySweetALertDialog.show();
                            if(mPresenter.isScanning()) {
                                mPresenter.stopScan();
                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        mPresenter.startConnect(wifiBean);
                                    }
                                },3000);
                            } else {
                                mPresenter.startConnect(wifiBean);
                            }
                        }
                    });
                    dialog.setNegativeButton("取消", (dialog1, which) -> {
                        dialog1.cancel();
                    });
                    dialog.show();
            }
        });
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPreScan();
                mPresenter.startScan();
            }
        });
        doPreScan();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(MESSAGE_PRE_SCAN);
            }
        },100);
        return view;
    }

    @Override
    public void setPresenter(WifiTestContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /*
    @Override
    public void showScanningDialog() {
        myHandler.sendEmptyMessage(MESSAGE_STARTSCAN);
    }
*/
    @Override
    public void doStartScanSuccess() {
        myHandler.sendEmptyMessage(MESSAGE_STARTSCAN);
    }

    @Override
    public void doStartScanFail(String failReason) {
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_STARTSCAN_FAIL,failReason));
    }

    @Override
    public void doStopScanSuccess() {
        myHandler.sendEmptyMessage(MESSAGE_STOPSCAN_SUCCESS);
    }

    @Override
    public void doStopScanFail(String failReason) {
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_STOPSCAN_FAIL,failReason));
    }

    @Override
    public void doConnectFail(String failReason) {
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_CONNECT_FAIL,failReason));
    }

    @Override
    public void doQueryStateFail(String failReason) {
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_QUERY_FAIL,failReason));
    }
    @Override
    public void doQueryStateSuccess() {

    }
    @Override
    public void doFindAP(WifiBean bean) {
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_FINDAP,bean));
    }

    @Override
    public void doScanFinished() {
        myHandler.sendEmptyMessage(MESSAGE_SCAN_FINISHED);
    }

    @Override
    public void doScanFail(String failReason) {
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_SCAN_FAIL,failReason));
    }

    @Override
    public void doConnectSuccess() {
        myHandler.sendEmptyMessage(MESSAGE_CONNECT_SUCCESS);
    }

    @Override
    public void doCloseSuccess() {
        myHandler.sendEmptyMessage(MESSAGE_CLOSE_SUCCESS);
    }

    @Override
    public void doGetError(String errString) {
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_WIFI_ERROR,errString));
    }

    private void doPreScan() {
        wifiBeans.clear();
        if(wifiListAdapter != null)
            wifiListAdapter.notifyDataSetChanged();
        //btn_scan.setVisibility(View.GONE);
        btn_scan.setEnabled(false);
        btn_close.setEnabled(false);
        tv_state.setText("正在扫描");
    }

    private void doAfterScanFinished() {
        tv_state.setText("扫描完毕");
        btn_scan.setVisibility(View.VISIBLE);
        btn_scan.setEnabled(true);
        btn_close.setEnabled(true);
    }

    private boolean isApInHistory(WifiBean bean) {
        for (WifiBean historyAp : ProjectUtil.historyApArray) {
            if (historyAp.getBssid().equals(bean.getBssid())) {
                return true;
            }
        }
        return false;
    }

    private void updateHistoryAp(WifiBean bean) {
        Log.d("updateHistoryAp",bean.getInfoString());
        int index = 0;
        for(WifiBean historyAp : ProjectUtil.historyApArray) {
            if (historyAp.getBssid().equals(bean.getBssid())) {
                ProjectUtil.historyApArray.remove(index);
                break;
            }
            index++;
        }
        ProjectUtil.historyApArray.add(bean);
        SharedPreferencesUtil.updateApHistory(context,ProjectUtil.historyApArray.toArray(new WifiBean[0]));
    }
}
