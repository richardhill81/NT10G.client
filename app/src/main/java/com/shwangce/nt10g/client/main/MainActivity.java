package com.shwangce.nt10g.client.main;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.shwangce.nt10g.client.BuildConfig;
import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.databinding.ActivityMainBinding;
import com.shwangce.nt10g.client.deviceselect.DeviceSelectDialogFragment;
import com.shwangce.nt10g.client.deviceselect.DeviceSelectPresenter;
import com.shwangce.nt10g.client.iptvtest.IptvTestFragment;
import com.shwangce.nt10g.client.iptvtest.IptvTestPresenter;
import com.shwangce.nt10g.client.library.AppUpdate.UpdateBean;
import com.shwangce.nt10g.client.library.AppUpdate.UpdateWork;
import com.shwangce.nt10g.client.library.communicate.BoxController;
import com.shwangce.nt10g.client.modeselect.SetModeDialogFragment;
import com.shwangce.nt10g.client.modeselect.SetModePresenter;
import com.shwangce.nt10g.client.nettools.NetToolsFragment;
import com.shwangce.nt10g.client.nettools.NetToolsPresenter;
import com.shwangce.nt10g.client.setaccess.AccessType;
import com.shwangce.nt10g.client.setaccess.SetAccessDialogFragment;
import com.shwangce.nt10g.client.setaccess.SetAccessPresenter;
import com.shwangce.nt10g.client.setting.SettingFragment;
import com.shwangce.nt10g.client.setting.SettingPresenter;
import com.shwangce.nt10g.client.speedtest.SpeedTestFragment;
import com.shwangce.nt10g.client.speedtest.SpeedTestKind;
import com.shwangce.nt10g.client.speedtest.SpeedTestPresenter;
import com.shwangce.nt10g.client.sweetalert.SweetAlertDialog;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;
import com.shwangce.nt10g.client.util.SharedPreferencesUtil;
import com.shwangce.nt10g.client.wifitest.WifiBean;
import com.shwangce.nt10g.client.wifitest.WifiTestFragment;
import com.shwangce.nt10g.client.wifitest.WifiTestPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements IMainView {

    private static final String TAG = "NT-201.MainActivity";

    private Context context;
    private ActivityMainBinding binding;
    private TextView tabTestspeed,tabSetting,tabNetTools,tabIptvTest;
    private TextView tv_clientversion,tv_devicename,tv_devicechange,tv_testmode,tv_testmodechange,
                     tv_accesstype,tv_accesschange,tv_lanip;
    private LinearLayout llyt_wifi_info;
    private TextView tv_wifi_ssid,tv_wifi_txbandwidth,tv_wifi_rxbandwidth;
    private String connectedDeviceName = "";
    private String boxVersionName = "";
    private SpeedTestFragment speedTestFragment;
    private SettingFragment settingFragment;
    private NetToolsFragment netToolsFragment;
    private IptvTestFragment iptvTestFragment;
    private WifiTestFragment wifiTestFragment;
    private SweetAlertDialog sweetAlertDialog;
    private MainPresenter mainPresenter;

    private UpdateBean updateBeanInfo = null;
    private ProgressDialog progressDialog = null;


    /*
    private boolean isFirstConnect = true;

    public void setIsFirstConnect(boolean isFirstConnect) {
        this.isFirstConnect = isFirstConnect;
    }

    public boolean getIsFirstConnect() {
        return this.isFirstConnect;
    }
    */

    private boolean clickDisconnect = false;

    private final String[] PERMISSIONS = {"android.permission.INTERNET",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.BLUETOOTH",
            "android.permission.BLUETOOTH_ADMIN",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.READ_PHONE_STATE",
            "android.permission.ACCESS_NETWORK_STATE"};


    private UpdateWork appUpdate = null;

    //private UpdateWork appUpdate = new UpdateWork("http://www.shwangce.com/nt201/update_test.php");

    private final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private final Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ProjectUtil.MESSAGE_CONNECTED:
                    tv_devicename.setText(msg.obj + "");
                    tv_devicechange.setVisibility(View.VISIBLE);
                    //tv_state.setText(msg.obj + " 连接成功");
                    boxVersionName = "";
                    // add by hzj on 20191018
                    ProjectUtil.ConnectedDeviceName = msg.obj + "";
                    break;

                case ProjectUtil.MESSAGE_CONNECTLOSS:
                    tv_devicename.setText("未连接");
                    tv_devicechange.setVisibility(View.GONE);
                    //tv_state.setText("连接中断");
                    connectedDeviceName = "";
                    boxVersionName = "";
                    if (!clickDisconnect) {
                        showConnectionLossDialog();
                    }
                    clickDisconnect = false;
                    break;

                case ProjectUtil.MESSAGE_BOXAPP_VERSIONSHOW:
                    tv_devicename.setText(connectedDeviceName + "_" + msg.obj + "");
                    break;

                case ProjectUtil.MESSAGE_ACCESSDIALOG_SHOW:
                    showAccessSelectDialog();
                    break;

                case ProjectUtil.MESSAGE_MODEDIALOG_SHOW:
                    showModeSelectDialog();
                    break;
                case ProjectUtil.MESSAGE_SETMODE_SUCCESS:
                    tv_testmodechange.setVisibility(View.VISIBLE);
                    ProjectUtil.SetModeEnum testMode = (ProjectUtil.SetModeEnum)msg.obj;
                    switch (testMode) {
                        case DoubleLan:
                            llyt_wifi_info.setVisibility(View.GONE);
                            tv_testmode.setText(R.string.doubleLan);
                            showAccessSelectDialog();
                            break;
                        case SingleLanExternal:
                            llyt_wifi_info.setVisibility(View.GONE);
                            tv_testmode.setText(R.string.singleLanExternal);
                            showAccessSelectDialog();
                            break;
                        case SingleLanInternal:
                            llyt_wifi_info.setVisibility(View.GONE);
                            tv_testmode.setText(R.string.singleLanInternal);
                            showAccessSelectDialog();
                            break;
                        case WifiDHCP:
                            llyt_wifi_info.setVisibility(View.VISIBLE);
                            tv_testmode.setText(R.string.wifiDHCP);
                            wifiTestFragment = new WifiTestFragment();
                            wifiTestFragment.show(getFragmentManager(), "WifiTestFragment");
                            new WifiTestPresenter(wifiTestFragment, mainPresenter).start();
                            break;
                        case LanAndWifi:
                            llyt_wifi_info.setVisibility(View.VISIBLE);
                            tv_testmode.setText(R.string.lanAndWifi);
                            wifiTestFragment = new WifiTestFragment();
                            wifiTestFragment.show(getFragmentManager(), "WifiTestFragment");

                            new WifiTestPresenter(wifiTestFragment, mainPresenter).start();
                            break;
                        /*
                        case ITVSimulate:
                            tv_testmode.setText(R.string.iTVSimulate);
                            break;
                         */
                    }
                    break;

                case ProjectUtil.MESSAGE_CONNECTAP_SUCCESS:
                    tv_lanip.setText("");
                    llyt_wifi_info.setVisibility(View.VISIBLE);
                    showAccessSelectDialog();
                    break;

                case ProjectUtil.MESSAGE_SCAN_UPDATE:
                    if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
                        sweetAlertDialog.dismissWithAnimation();
                    sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("正在获取更新信息,请稍候!");
                    sweetAlertDialog.show();
                    break;

                case ProjectUtil.MESSAGE_UPDATE_STATE:
                    //tv_state.setText(msg.obj + "");
                    break;

                case ProjectUtil.MESSAGE_UPDATE_ETHERNETLINK:
                    //tv_linelink.setText(msg.obj + "");
                    break;

                case ProjectUtil.MESSAGE_ACCESS_DHCP:
                    tv_lanip.setText(msg.obj + "");
                    tv_accesstype.setText("动态分配");
                    tv_accesschange.setVisibility(View.VISIBLE);
                    tabTestspeed.callOnClick();
                    break;

                case ProjectUtil.MESSAGE_ACCESS_STATIC:
                    tv_lanip.setText(msg.obj + "");
                    tv_accesstype.setText("静态指定");
                    tv_accesschange.setVisibility(View.VISIBLE);
                    tabTestspeed.callOnClick();
                    break;

                case ProjectUtil.MESSAGE_ACCESS_PPPOE:
                    tv_lanip.setText(msg.obj + "");
                    tv_accesstype.setText("PPPoE拨号");
                    tv_accesschange.setVisibility(View.VISIBLE);
                    tabTestspeed.callOnClick();
                    break;

                case ProjectUtil.MESSAGE_ACCESS_WIFI_DHCP:
                    tv_accesstype.setText("Wi-Fi");
                    tv_accesschange.setVisibility(View.VISIBLE);
                    tabTestspeed.callOnClick();
                    break;

                case ProjectUtil.MESSAGE_UPDATE_AP_INFO:
                    WifiBean wifiBean;
                    if(msg.obj == null)
                        wifiBean = new WifiBean();
                    else
                        wifiBean = (WifiBean) msg.obj;
                    llyt_wifi_info.setVisibility(View.VISIBLE);
                    if(wifiBean.getEssid().length() > 0 )
                        tv_wifi_ssid.setText(wifiBean.getEssid());
                    else
                        tv_wifi_ssid.setText("/");
                    if(wifiBean.get_rxBandWidth().length() > 0 )
                        tv_wifi_rxbandwidth.setText(wifiBean.get_rxBandWidth() + "Mbps");
                    else
                        tv_wifi_rxbandwidth.setText("/");
                    if(wifiBean.get_txBandWidth().length() > 0 )
                        tv_wifi_txbandwidth.setText(wifiBean.get_txBandWidth() + "Mbps");
                    else
                        tv_wifi_txbandwidth.setText("/");
                    break;


                case ProjectUtil.MESSAGE_BOXUPDATE_INFO:
                    switch (msg.arg1) {
                        case UpdateWork.BOXAPP_UPDATE:
                            new AlertDialog.Builder(context)
                                    .setTitle("测速盒有新版本更新，是否更新?")
                                    .setMessage(msg.obj + "")
                                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
                                                sweetAlertDialog.dismissWithAnimation();
                                            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                                                    .setTitleText("正在下载升级包，请稍候...");
                                            sweetAlertDialog.show();
                                            sweetAlertDialog.setCancelable(false);
                                            mainPresenter.doBoxAppUpdate();
                                        }
                                    })
                                    .setNegativeButton("不更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .create().show();
                            break;
                        case UpdateWork.BOXOTA_UPDATE:
                            new AlertDialog.Builder(context)
                                    .setTitle("测速盒系统可升级，是否升级?")
                                    .setMessage(msg.obj + "")
                                    .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
                                                sweetAlertDialog.dismissWithAnimation();
                                            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                                                    .setTitleText("正在下载升级包，请稍候...");
                                            sweetAlertDialog.show();
                                            sweetAlertDialog.setCancelable(false);
                                            mainPresenter.doBoxOTAUpdate();
                                        }
                                    })
                                    .setNegativeButton("不更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .create().show();
                            break;
                    }

                    break;

                case ProjectUtil.MESSAGE_BOXUPDATE_STATE:
                    String[] updateDialoginfo = (String[]) msg.obj;
                    if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                        if (updateDialoginfo[0].length() > 0)
                            sweetAlertDialog.setTitleText(updateDialoginfo[0]);
                        if (updateDialoginfo[1].length() > 0)
                            sweetAlertDialog.setContentText(updateDialoginfo[1]);
                    }
                    break;

                case ProjectUtil.MESSAGE_BOXUPDATE_START:
                    if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                    sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("测速盒开始升级")
                            .setContentText("在升级时请确保测速盒供电正常。请等蓝灯闪烁后重新连接");
                    sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            clickDisconnect = true;
                            mainPresenter.stop();
                            Exit();
                        }
                    });
                    sweetAlertDialog.show();
                    break;

                case ProjectUtil.MESSAGE_BOXUPDATE_FAIL:
                    if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                    sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("测速盒升级失败")
                            .setContentText("" + msg.obj);
                    sweetAlertDialog.show();
                    break;
                case ProjectUtil.MESSAGE_OPEN_LOCATION_ACCEPT:
                    Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MainActivity.this.startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
                    break;
                case ProjectUtil.MESSAGE_OPEN_LOCATION_REJECT:
                    Toast.makeText(context,"未开启位置信息可能导致蓝牙异常！",Toast.LENGTH_LONG).show();
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    private final View.OnClickListener onChangeDeviceClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clickDisconnect = true;
            ProjectUtil.isFirstConnect = false;
            mainPresenter.doBacktoDeviceSelect();
        }
    };

    private final View.OnClickListener onChangeTestmodeClickListener = v -> showModeSelectDialog();

    private final View.OnClickListener onChangeAccesstypeClickListener = v -> {
        showAccessSelectDialog();
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tabTestspeed = binding.tabMenuTestspeed;
        tabSetting = binding.tabMenuSetting;
        tabNetTools = binding.tabMenuNettoolstest;
        tabIptvTest = binding.tabMenuIptvtest;
        tv_clientversion = binding.contentDeviceinfo.deviceinfoTvClientversion;
        tv_devicename = binding.contentDeviceinfo.deviceinfoTvDevicename;
        tv_devicechange = binding.contentDeviceinfo.deviceinfoTvDevicechange;
        tv_testmode = binding.contentDeviceinfo.deviceinfoTvTestmode;
        tv_testmodechange = binding.contentDeviceinfo.deviceinfoTvTestmodechange;
        tv_accesstype = binding.contentDeviceinfo.deviceinfoTvAccesstype;
        tv_accesschange = binding.contentDeviceinfo.deviceinfoTvAccesstypechange;
        tv_lanip = binding.contentDeviceinfo.textviewLanip;
        llyt_wifi_info = binding.contentDeviceinfo.llytWifiInfo;
        tv_wifi_ssid = binding.contentDeviceinfo.textviewWifiSsid;
        tv_wifi_rxbandwidth = binding.contentDeviceinfo.textviewWifiRxbandwidth;
        tv_wifi_txbandwidth = binding.contentDeviceinfo.textviewWifiTxbandwidth;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);      //强制竖屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   //应用运行时，保持屏幕高亮，不锁屏
        checkPermissions();
        //doSelfStartScan();  //add by hzj 2018-11-15
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        if(mainPresenter != null)
            mainPresenter.stop();
        Exit();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (ProjectUtil.APIVERSION >= 23) {
            for (int i = 0; i < permissions.length; i++) {
                switch (requestCode) {
                    case REQUEST_CODE_ASK_PERMISSIONS:
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            setLocationService();
                        } else {
                            //用户拒绝
                            showMessageOKCancel("APP权限被禁止使用,请开启权限", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Exit();
                                }
                            });
                        }
                        break;
                    default:
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                }
            }
        }
    }

    /*
    @OnClick(R.id.button_tcpdump)
    void onTcpDmp() {
        if(ProjectUtil.isBoxSe()) {
            new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("SE版本暂不支持抓包！")
                    .show();
            return;
        }
        if (btn_tcpdump.getText().toString().equals("开始抓包")) {
            mainPresenter.doSendCommand(CommandValue.TCPDUMP_START, ProjectUtil.boxInfoBean.getTcpdumpsave() + "");
            if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
                sweetAlertDialog.dismissWithAnimation();
            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("正在准备抓包，请稍候...");
            sweetAlertDialog.show();
            sweetAlertDialog.setCancelable(false);

        } else {
            mainPresenter.doSendCommand(CommandValue.TCPDUMP_STOP, ProjectUtil.boxInfoBean.getTcpdumpsave() + "");
            if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
                sweetAlertDialog.dismissWithAnimation();
            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("正在停止抓包，请稍候...");
            sweetAlertDialog.show();
            sweetAlertDialog.setCancelable(false);
        }
    }

    @OnClick(R.id.tv_updateboxapp)
    void onUpdateBoxApp() {
        //mainPresenter.doUpdateBoxApp();
        mainPresenter.doSendCommand(CommandValue.BOXAPP_UPDATE, "");
    }
*/

    private void doClicktab(ProjectUtil.TabEnum tab) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        clearSelectedTab();
        hideAllFragment(transaction);
        switch (tab) {
            case Testspeed:
                tabTestspeed.setSelected(true);
                if (speedTestFragment == null) {
                    speedTestFragment = SpeedTestFragment.newInstance(ProjectUtil.speedTestKind, ProjectUtil.Httpdownloadurl, ProjectUtil.ftpServerBean, ProjectUtil.hxBoxBean);
                    transaction.add(R.id.fragment_container, speedTestFragment);
                    new SpeedTestPresenter(speedTestFragment, mainPresenter).start();
                } else {
                    speedTestFragment.setTesttype(ProjectUtil.speedTestKind, ProjectUtil.Httpdownloadurl, ProjectUtil.ftpServerBean, ProjectUtil.hxBoxBean);
                    transaction.show(speedTestFragment);
                }
                break;

            case Setting:
                tabSetting.setSelected(true);
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                    transaction.add(R.id.fragment_container, settingFragment);
                    new SettingPresenter(settingFragment, mainPresenter).start();
                } else {
                    transaction.show(settingFragment);
                }
                break;

            case NetTools:
                tabNetTools.setSelected(true);
                if (netToolsFragment == null) {
                    netToolsFragment = new NetToolsFragment();
                    transaction.add(R.id.fragment_container, netToolsFragment);
                    new NetToolsPresenter(netToolsFragment, mainPresenter).start();
                } else {
                    transaction.show(netToolsFragment);
                }
                break;

            case IptvTest:
                if(ProjectUtil.isBoxSe()) {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("SE版本暂不支持iTV测试！")
                            .show();
                    return;
                }
                tabIptvTest.setSelected(true);
                if (iptvTestFragment == null) {
                    iptvTestFragment = new IptvTestFragment();
                    transaction.add(R.id.fragment_container, iptvTestFragment);
                    new IptvTestPresenter(iptvTestFragment, mainPresenter).start();
                } else {
                    transaction.show(iptvTestFragment);
                }
                break;
        }
        transaction.commit();
    }

    @Override
    public void showDeviceSelectDialog() {
        //tv_state.setText("正在搜索设备..");
        tv_devicename.setText("未连接");
        tv_devicechange.setVisibility(View.GONE);
        DeviceSelectDialogFragment deviceSelectDialog = new DeviceSelectDialogFragment();
        deviceSelectDialog.show(getFragmentManager(), "DeviceSelectDialogFragment");
        DeviceSelectPresenter deviceSelectPresenter = new DeviceSelectPresenter(deviceSelectDialog, mainPresenter);
        deviceSelectPresenter.start();
    }

    @Override
    public void showConnected(String devicename) {
        devicename = devicename.replace(ProjectUtil.DeviceName + "_","");
        connectedDeviceName = devicename;
        mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_CONNECTED, devicename));
    }

    @Override
    public void showBoxVersion(String versionname) {
        boxVersionName = versionname;
        mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_BOXAPP_VERSIONSHOW, versionname));
    }

    @Override
    public void showAccessDialog() {
        mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_ACCESSDIALOG_SHOW,false));
    }

    @Override
    public void showModeDialog() {
        mainHandler.sendEmptyMessage(ProjectUtil.MESSAGE_MODEDIALOG_SHOW);
    }

    @Override
    public void doShowTestMode(ProjectUtil.SetModeEnum testMode) {
        mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_SETMODE_SUCCESS,testMode));
    }

    @Override
    public void doCleanTestMode() {
        tv_testmode.setText("  ");
        //tv_testmodechange.setVisibility(View.GONE);
    }

    @Override
    public void updateTestState(String msg) {
        mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_UPDATE_STATE, msg));
    }

    @Override
    public void updateLinkState(String msg) {
        mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_UPDATE_ETHERNETLINK, msg));
    }

    @Override
    public void updateAccessType(AccessType accessType, String ipString) {
        switch (accessType) {
            case ACCESS_DHCP:
                mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_ACCESS_DHCP, ipString));
                break;
            case ACCESS_PPPOE:
                mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_ACCESS_PPPOE, ipString));
                break;
            case ACCESS_STATIC:
                mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_ACCESS_STATIC, ipString));
                break;
            case ACCESS_WIFI:
                mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_ACCESS_WIFI_DHCP,ipString));
                break;
        }
    }

    @Override
    public void doConnectLoss() {
        mainHandler.sendEmptyMessage(ProjectUtil.MESSAGE_CONNECTLOSS);
    }

    @Override
    public void doShowTcpdumpStarted() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //btn_tcpdump.setText("停止抓包");
                if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                    sweetAlertDialog.dismissWithAnimation();
                }
                SweetAlertDialog mySweetALertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("")
                        .setContentText("已开始抓包")
                        .setConfirmText("确认");
                mySweetALertDialog.show();
            }
        });
    }

    @Override
    public void doShowTcpdumpStopped() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //btn_tcpdump.setText("开始抓包");
                if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                    sweetAlertDialog.dismissWithAnimation();
                }
                SweetAlertDialog mySweetALertDialog = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("")
                        .setContentText("已停止抓包")
                        .setConfirmText("确认");
                mySweetALertDialog.show();
            }
        });
    }

    @Override
    public void doShowTcpdumpError(String errormsg) {
        final String emsg = errormsg;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                    sweetAlertDialog.dismissWithAnimation();
                }
                SweetAlertDialog mySweetALertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("开启抓包失败")
                        .setContentText(emsg)
                        .setConfirmText("确认");
                mySweetALertDialog.show();
            }
        });
    }

    @Override
    public void doShowBoxUpdateDialog(int boxupdatetype, String update_describe) {
        mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_BOXUPDATE_INFO, boxupdatetype, 0, update_describe));
    }

    @Override
    public void doUpdateBoxUpdateDialogState(String titleText, String contentText) {
        String[] dialogstate = new String[2];
        dialogstate[0] = titleText;
        dialogstate[1] = contentText;
        mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_BOXUPDATE_STATE, dialogstate));
    }

    @Override
    public void doShowBoxUpdateAlertDialog() {
        mainHandler.sendEmptyMessage(ProjectUtil.MESSAGE_BOXUPDATE_START);
    }

    @Override
    public void doShowBoxUpdateFailAlertDialog(String contentText) {
        mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_BOXUPDATE_FAIL, contentText));
    }

    @Override
    public void doShowAPInfo(WifiBean bean) {
        mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_UPDATE_AP_INFO, bean));
    }

    @Override
    public void doAPConnected(WifiBean bean) {
        mainHandler.sendMessage(mainHandler.obtainMessage(ProjectUtil.MESSAGE_CONNECTAP_SUCCESS, bean));
    }

    private void doShowUpdateDialog(String update_describe) {
        new AlertDialog.Builder(context)
                .setTitle("检测到新版本！")
                .setMessage(update_describe)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDownloadDialog();
                        appUpdate.startUpdate(UpdateWork.CLIENT_UPDATE);
                    }
                })
                .setNegativeButton("不更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startWork();
                    }
                })
                .create().show();
    }

    private void doUpdateDownloadingBar(int value) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.setProgress(value);
    }

    private void doDownloadComplete(String fileName) {
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        installApp(fileName);
    }

    //////////add by hzj 2018-11-15//////

    private static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
    private volatile boolean isCheckLocationOver = false;

    private void doSelfStartScan(){
        Log.d("MAINACTIVITY","doSelfStartScan()");
        new Thread(){
            @Override
            public void run() {
                while(true){
                    Log.d("isCheckLocationOver","-----" + isCheckLocationOver);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if(isCheckLocationOver){
                        mainPresenter.doSelfStartScan();
                        break;
                    }
                }
            }
        }.start();
    }

    private boolean isLocationEnable(@NonNull Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return networkProvider || gpsProvider;
    }

    private void init() {
        getUriInfo();
        ProjectUtil.clientPackageInfo = getPackageInfo();
        File sddir = new File(ProjectUtil.SDCardDir);
        if (!sddir.exists()) {
            sddir.mkdirs();
        }
        tv_devicechange.setOnClickListener(onChangeDeviceClickListener);
        tabTestspeed.setOnClickListener(v -> doClicktab(ProjectUtil.TabEnum.Testspeed));
        tabSetting.setOnClickListener(v -> doClicktab(ProjectUtil.TabEnum.Setting));
        tabNetTools.setOnClickListener(v -> doClicktab(ProjectUtil.TabEnum.NetTools));
        tabIptvTest.setOnClickListener(v -> doClicktab(ProjectUtil.TabEnum.IptvTest));
        tv_testmodechange.setOnClickListener(onChangeTestmodeClickListener);
        tv_accesschange.setOnClickListener(onChangeAccesstypeClickListener);
        tv_devicename.setText("未连接");
        tv_devicechange.setVisibility(View.GONE);
        //tv_linelink.setVisibility(View.GONE);
        //tv_updateboxapp.setVisibility(View.GONE);
        tv_testmode.setText("  ");
        //tv_testmodechange.setVisibility(View.GONE);
        tv_accesstype.setText("  ");
        //tv_accesschange.setVisibility(View.GONE);
        tv_clientversion.setText(ProjectUtil.clientPackageInfo.versionName);
        ProjectUtil.communicateType = BoxController.CommunicateType.BLE;
        ProjectUtil.speedTestKind = SharedPreferencesUtil.getTestType(context);
        ProjectUtil.Httpdownloadurl = SharedPreferencesUtil.getHttpDownloadUrl(context);
        ProjectUtil.Httpuploadurl = SharedPreferencesUtil.getHttpUploadUrl(context);
        //ProjectUtil.ftpServerBean = SharedPreferencesUtil.getFtpDownloadServerBean(context);
        ProjectUtil.hxBoxBean = SharedPreferencesUtil.getHxUserInfo(context);
        ProjectUtil.boxInfoBean = SharedPreferencesUtil.getBoxInfo(context);
        ProjectUtil.historyApArray = SharedPreferencesUtil.getApHistory(context);
        if (ProjectUtil.Httpdownloadurl.isEmpty()) {
            ProjectUtil.Httpdownloadurl = getString(R.string.default_download_url);
        }
        Resources resources = getResources();
        String channel = "";
        if(BuildConfig.DEBUG)
            channel = "common";
        else
            channel = getAppMetaData(context,"CHANNEL");
        ProjectUtil.channel = channel;
        ProjectUtil.app_name = resources.getString(R.string.app_name);
        ProjectUtil.setModeString[0] = resources.getString(R.string.singleLanInternal);
        ProjectUtil.setModeString[1] = resources.getString(R.string.singleLanExternal);
        ProjectUtil.setModeString[2] = resources.getString(R.string.doubleLan);
        ProjectUtil.setModeString[3] = resources.getString(R.string.wifiDHCP);
        ProjectUtil.setModeString[4] = resources.getString(R.string.lanAndWifi);
        /*
        if("js10000".equals(channel)) {
            ProjectUtil.speedTestKind = SpeedTestKind.JIANGSU10000;
            ProjectUtil.app_update_url = resources.getString(R.string.update_url_js10000);
        } else if("sh10000".equals(channel)) {
            ProjectUtil.speedTestKind = SpeedTestKind.HXBOX;
            ProjectUtil.app_update_url = resources.getString(R.string.update_url_sh10000);
        } else if("ah10086".equals(channel)) {
            ProjectUtil.speedTestKind = SpeedTestKind.JIANGSU10000;
            ProjectUtil.app_update_url = resources.getString(R.string.update_url_ah10086);
        } else if("debugtest".equals(channel)) {
            ProjectUtil.app_update_url = resources.getString(R.string.update_url_test);
        } else {
            ProjectUtil.app_update_url = resources.getString(R.string.update_url);
        }*/
        ProjectUtil.app_update_url = resources.getString(R.string.update_url);
        appUpdate = new UpdateWork(ProjectUtil.app_update_url);
        appUpdate.setUpdateListener(new UpdateWork.OnUpdateListener() {
            @Override
            public void onGetUpdateInfo(UpdateBean updateBean) {
                if(sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                    sweetAlertDialog.dismissWithAnimation();
                }
                if (updateBean != null) {
                    updateBeanInfo = updateBean;
                    int versioncode_new = Integer.parseInt(updateBean.getClient_code());
                    if (ProjectUtil.clientPackageInfo.versionCode < versioncode_new) {
                        String updateMessage = updateBean.getClient_appname() + updateBean.getClient_version() + "\n\n\n" +
                                "更新内容：" + updateBean.getClient_updateinfo();
                        doShowUpdateDialog(updateMessage);
                    } else {
                        //无需更新
                        if(sweetAlertDialog != null && sweetAlertDialog.isShowing()) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                        startWork();
                    }
                } else {
                    startWork();
                }
            }

            @Override
            public void onDownloading(int progressvalue) {
                doUpdateDownloadingBar(progressvalue);
            }

            @Override
            public void onDownloadComplete(String fileName, int updatetype) {
                doDownloadComplete(fileName);
            }
        });
        if(isNetworkAvalible(context)) {
            mainHandler.sendEmptyMessage(ProjectUtil.MESSAGE_SCAN_UPDATE);
            appUpdate.getUpdateInfo();
        } else {
            startWork();
        }
    }

    private void setLocationService() {
        if (!isLocationEnable(this)) {
            new AlertDialog.Builder(context)
                    .setTitle("提示")
                    .setMessage(ProjectUtil.app_name + "需要开启位置信息功能！")
                    .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent locationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            MainActivity.this.startActivityForResult(locationIntent, REQUEST_CODE_LOCATION_SETTINGS);
                            //mainHandler.sendEmptyMessage(ProjectUtil.MESSAGE_OPEN_LOCATION_ACCEPT);
                        }
                    })
                    /*
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mainHandler.sendEmptyMessage(ProjectUtil.MESSAGE_OPEN_LOCATION_REJECT);
                        }
                    })
                    */
                    .setCancelable(false)
                    .create()
                    .show();
        }else{
            isCheckLocationOver = true;
            ProjectUtil.isFirstConnect = true;
            //startWork();
            init();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_LOCATION_SETTINGS) {
            if (isLocationEnable(this)) {
                isCheckLocationOver = true;
                ProjectUtil.isFirstConnect = true;
                startWork();
            } else {
                mainHandler.sendEmptyMessage(ProjectUtil.MESSAGE_OPEN_LOCATION_REJECT);
                isCheckLocationOver = true;
                ProjectUtil.isFirstConnect = true;
                startWork();
            }
        }
    }

// /////////////////////////////////////////////


    @SuppressLint("WrongConstant")
    private void checkPermissions() {
        if (ProjectUtil.APIVERSION >= 23) {
            ArrayList<String> needRequestPermissions = new ArrayList<>();
            for (int i = 0; i < PERMISSIONS.length; i++) {
                Log.d("请求权限：", i + "-----" + ContextCompat.checkSelfPermission(MainActivity.this, PERMISSIONS[i]) + "------" + PackageManager.PERMISSION_GRANTED);
                if (ContextCompat.checkSelfPermission(MainActivity.this, PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED) {
                    //申请WRITE_EXTERNAL_STORAGE权限
                    needRequestPermissions.add(PERMISSIONS[i]);
                }
            }
            if (needRequestPermissions.size() > 0) {
                Log.d("需要请求的权限：", "个数:" + needRequestPermissions.size());
                requestPermissions(needRequestPermissions.toArray(new String[needRequestPermissions.size()]), REQUEST_CODE_ASK_PERMISSIONS);
            } else
                setLocationService();
        } else {
            setLocationService();
        }
    }

    private void installApp(String fileName) {
        File appFile = new File(fileName);
        if (!appFile.exists()) {
            return;
        }

        // 跳转到新版本应用安装页面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context,  BuildConfig.APPLICATION_ID , new File(fileName));
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.parse("file://" + appFile.toString()), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }



    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .create()
                .show();
    }

    private void startWork() {
        mainPresenter = new MainPresenter(context, this);
        mainPresenter.start();
        doSelfStartScan();
        showDeviceSelectDialog();
        //showModeSelectDialog();
    }

    /**
     * 显示下载进度对话框
     */
    private void showDownloadDialog() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("正在下载...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
    }


    private void getUriInfo() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            // 完整的url信息
            String url = uri.toString();
            Log.i(TAG, "url: " + url);
            // scheme部分
            String scheme = uri.getScheme();
            Log.i(TAG, "scheme: " + scheme);
            // host部分
            String host = uri.getHost();
            Log.i(TAG, "host: " + host);
            //port部分
            int port = uri.getPort();
            Log.i(TAG, "host: " + port);
            // 访问路劲
            String path = uri.getPath();
            Log.i(TAG, "path: " + path);
            List<String> pathSegments = uri.getPathSegments();
            // Query部分
            String query = uri.getQuery();
            Log.i(TAG, "query: " + query);
            //获取指定参数值
            String goodsId = uri.getQueryParameter("goodsId");
            Log.i(TAG, "goodsId: " + goodsId);
        }
    }

    private void showAccessSelectDialog() {
//        if(accessDialogFragment == null) {
//            accessDialogFragment = new SetAccessDialogFragment();
//            accessDialogFragment.show(getFragmentManager(),"DeviceSelectDialogFragment");
//            new SetAccessPresenter(accessDialogFragment,mainPresenter).start();
//        } else {
//            System.out.println(accessDialogFragment == null);
//            accessDialogFragment.show(getFragmentManager(),"DeviceSelectDialogFragment");
//            //new SetAccessPresenter(accessDialogFragment,mainPresenter).start();
//        }
        SetAccessDialogFragment accessDialogFragment = new SetAccessDialogFragment();
        accessDialogFragment.show(getFragmentManager(), "DeviceSelectDialogFragment");
        new SetAccessPresenter(accessDialogFragment, mainPresenter).start();
    }

    private void showModeSelectDialog() {
        SetModeDialogFragment modeDialogFragment = new SetModeDialogFragment();
        modeDialogFragment.show(getFragmentManager(), "ModeSelectDialogFragment");
        new SetModePresenter(modeDialogFragment, mainPresenter).start();
    }

    private void showConnectionLossDialog() {
        if (sweetAlertDialog != null && sweetAlertDialog.isShowing())
            sweetAlertDialog.dismissWithAnimation();
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("")
                .setContentText("蓝牙连接丢失")
                .setConfirmText("关闭");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                DeviceSelectDialogFragment deviceSelectDialog = new DeviceSelectDialogFragment();
                deviceSelectDialog.show(getFragmentManager(), "DeviceSelectDialogFragment");
                new DeviceSelectPresenter(deviceSelectDialog, mainPresenter).start();
            }
        });
        sweetAlertDialog.show();
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction transaction) {
        if (speedTestFragment != null)
            transaction.hide(speedTestFragment);
        if (settingFragment != null)
            transaction.hide(settingFragment);
        if (netToolsFragment != null)
            transaction.hide(netToolsFragment);
        if (iptvTestFragment != null)
            transaction.hide(iptvTestFragment);
    }

    //重置所有文本的选中状态
    private void clearSelectedTab() {
        tabTestspeed.setSelected(false);
        tabSetting.setSelected(false);
        tabNetTools.setSelected(false);
        tabIptvTest.setSelected(false);
    }

    private PackageInfo getPackageInfo() {
        try {
            String pkName = this.getPackageName();
            PackageInfo packageInfo = this.getPackageManager().getPackageInfo(pkName, 0);
            return packageInfo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getAppMetaData(Context context, String key) {
        if (context == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String channelNumber = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        channelNumber = applicationInfo.metaData.getString(key);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return channelNumber;
    }

    @Override
    public void Exit() {
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public static boolean isNetworkAvalible(Context context) {
        // 获得网络状态管理器
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 建立网络数组
            //NetworkInfo[] net_info = connectivityManager.getAllNetworkInfo();
            Network[] networks = connectivityManager.getAllNetworks();
            if(networks != null && networks.length>0) {
                for (Network network : networks) {
                    // 判断获得的网络状态是否是处于连接状态
                    NetworkInfo networkInfo = connectivityManager.getNetworkInfo(network);
                    if (networkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}