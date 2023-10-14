package com.shwangce.nt10g.client.speedtest;

import static com.shwangce.nt10g.client.util.ProjectUtil.K;
import static com.shwangce.nt10g.client.util.ProjectUtil.channel;
import static com.shwangce.nt10g.client.util.ProjectUtil.df1;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.webkit.ValueCallback;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.databinding.FragmentSpeedtestBinding;
import com.shwangce.nt10g.client.library.FtpServerBean;
import com.shwangce.nt10g.client.sweetalert.SweetAlertDialog;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class SpeedTestFragment extends Fragment implements SpeedTestContract.View {

    public static final String KIND_ARGUMENT = "SpeedTestKind";
    public static final String HTTPDOWNLOADSOURCE_ARGUMENT = "HttpDonwloadSource";
    public static final String FTPDOWNLOADSOURCE_ARGUMENT = "FtpDonwloadSource";
    public static final String HXBOX_ARGUMENT = "HxBoxInfo";
    private Context context;
    private SpeedTestFragment instance;
    private SpeedTestContract.Presenter mPresenter;

    //private FragmentSpeedtestBinding binding;
    private SpeedTestKind testKind = SpeedTestKind.Unknown;
    private String testAdditional = "";
    private SweetAlertDialog mySweetALertDialog = null;
    private List<String> jiangsu10000infodata = new ArrayList<>();
    private float lastDegree = 0f;
    private float maxSpeed = 0;
    private float minSpeed = 0;
    private final int DOWNLOAD = 0;
    private final int UPLOAD = 1;

    private final int MESSAGE_SUCCESS = 1;
    private final int MESSAGE_FAIL = 2;
    private final int MESSAGE_SPEEDTEST_STATECHANGE = 3;
    private final int MESSAGE_SPEEDTEST_TCPSPEEDTEST_CLIENTIP = 4;
    private final int MESSAGE_SPEEDTEST_SERVERINFO = 5;
    private final int MESSAGE_SPEEDTEST_SPEED_DOWNLOADING = 6;
    private final int MESSAGE_SPEEDTEST_SPEED_UPLOADING = 7;
    private final int MESSAGE_SPEEDTEST_SPEED_DOWNLOADED = 8;
    private final int MESSAGE_SPEEDTEST_SPEED_UPLOADED = 9;
    private final int MESSAGE_SPEEDTEST_USERINFO  = 10;
    private final int MESSAGE_RESULT_SPEEDTEST_USERINFO = 11;

    private final int MESSAGE_SPEEDTEST_TEST_TIMEOUT = 12;

    private String jsTestSpeed = "";	//江苏电信测速结果显示（下行）
    private class BandwidthClass {
        private float avgspeed = 0f;
        private float peakspeed = 0f;
        private String avgBandwidth = "";
        private String peakBandwidth = "";

        public BandwidthClass(float avgspeed, float peakspeed) {
            setAvgspeed(avgspeed);
            setPeakspeed(peakspeed);
        }

        public float getAvgspeed() {
            return avgspeed;
        }

        public void setAvgspeed(float avgspeed) {
            this.avgspeed = avgspeed;
            this.avgBandwidth = ProjectUtil.getBandwidth(avgspeed);
        }

        public float getPeakspeed() {
            return peakspeed;
        }

        public void setPeakspeed(float peakspeed) {
            this.peakspeed = peakspeed;
            this.peakBandwidth = ProjectUtil.getBandwidth(peakspeed);
        }

        public String getAvgBandwidth() {
            return avgBandwidth;
        }

        public String getAvgBandwidthJS() {
            this.avgBandwidth = ProjectUtil.getBandwidthJS(avgspeed);
            return avgBandwidth;
        }

        //add by hzj on 2019.1.23 江苏电信，在测试结果一栏中添加网速显示（下行）
        public String getTestResultJs(){
            String result = "";

            float speed = avgspeed * 8f;

            double b = speed /(double)K ;

            if(b >= K){
                double bb = b/(double)K;
                result = "您的网速是 "+df1.format(bb) + "M\n";
            }else{
                result = "您的网速是 "+df1.format(b) + "K\n";
            }

            return result;
        }

        public String getPeakBandwidth() {
            return peakBandwidth;
        }

    }

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SUCCESS:
                    showTestSuccess((SpeedTestResultBean)msg.obj);
                    //add by hzj on 20191010
                    Log.i("TaskTimeOut","MESSAGE_SUCCESS:");
                    stopTaskTimeOut();
                    //js_state.setText("测试成功");
                    break;

                case MESSAGE_FAIL:
                    showTestFail(msg.obj + "");
                    js_state.setText(msg.obj + "");
                    tv_server.setText("");
                    //add by hzj on 20191010
                    Log.i("TaskTimeOut","MESSAGE_FAIL:" + msg.obj);
                    stopTaskTimeOut();
                    break;

                case MESSAGE_SPEEDTEST_STATECHANGE:
                    String s = "" + msg.obj;
                    //江苏电信测速服务器地址
                    if (testKind==SpeedTestKind.JIANGSU10000&&s.indexOf("http") >= 0) {
                        int qq =s.indexOf("/");
                        int ww = s.lastIndexOf("/");
                        String ee = s.substring(qq+2,ww);
                        tv_server.setText(ee);
                    }else if(testKind==SpeedTestKind.JIANGSU10000&&s.indexOf("upRate") >= 0){
                        js_upRate_server.setText(s.substring(6));
                    }else if(testKind==SpeedTestKind.JIANGSU10000&&s.indexOf("contractRate") >= 0){
                        js_contractRate_server.setText(s.substring(12));
                    }else{
                        if (s.indexOf("|") >= 0) {
                            String[] ss = s.split("\\|");
                            updateDialogText(ss[0], ss[1]);
                        } else
                            updateDialogText(s);
                    }


  /*                  switch (ProjectUtil.showSpeedType) {
                        case TEXT:
                            updateTestspeedInfo("" + msg.obj,"");
                            break;
                        case CHART:
                            updateTestState("" + msg.obj);
                            break;
                        case NEEDLE:
                            break;
                    }*/
                    break;

                case MESSAGE_SPEEDTEST_TCPSPEEDTEST_CLIENTIP:

                    break;

                case MESSAGE_SPEEDTEST_SERVERINFO:
                    tv_server.setText(msg.obj + "");
                    break;

                case MESSAGE_SPEEDTEST_USERINFO:
                    GD10000_serveripsBean bean = (GD10000_serveripsBean)msg.obj;
                    String[] servers = bean.getServerIps();
                    String serverip = "";
                    for(int i =0 ;i<servers.length;i++) {
                        serverip += servers[i] + ",";
                    }
                    serverip = serverip.substring(0,serverip.length()-1);
                    tv_server.setText(serverip);
                    gd_useraccount.setText(bean.getAccount());
                    gd_city.setText(bean.getCity());
                    gd_bandwidthDown.setText(bean.getBandwidthDown());
                    gd_bandwidthUp.setText(bean.getBandwidthUp());
                    break;

                case MESSAGE_SPEEDTEST_SPEED_DOWNLOADING:
                    updateTestspeedInfo(DOWNLOAD,(float)msg.obj);
                    updateMaxSpeed(DOWNLOAD,(float)msg.obj);

                    break;

                case MESSAGE_SPEEDTEST_SPEED_UPLOADING:
                    updateTestspeedInfo(UPLOAD,(float)msg.obj);
                    updateMaxSpeed(UPLOAD,(float)msg.obj);

                    break;

                case MESSAGE_SPEEDTEST_SPEED_DOWNLOADED:
                    BandwidthClass dbandwidth = (BandwidthClass)msg.obj;
                    if(testKind == SpeedTestKind.GD10000) {
                        gd_avgdownspeed.setText(dbandwidth.getAvgBandwidth());
                    }
                    if(testKind == SpeedTestKind.JIANGSU10000) {
                        js_avgdownspeed.setText(dbandwidth.getAvgBandwidthJS());
                        jsTestSpeed = dbandwidth.getTestResultJs();		//记下江苏电信测速下行速度
                    }else {
                        tv_avgspeed.setText(dbandwidth.getAvgBandwidth());
                        tv_peakspeed.setText(dbandwidth.getPeakBandwidth());
                    }
                    updateDialogText("下行测试完成");
                    maxSpeed = 0;   //最值速度清零
                    minSpeed = 0;   // add by hzj on2019.8.14 修改最大速度显示bug
                    break;

                case MESSAGE_SPEEDTEST_SPEED_UPLOADED:
                    BandwidthClass ubandwidth = (BandwidthClass)msg.obj;
                    if(testKind == SpeedTestKind.GD10000) {
                        gd_avgupspeed.setText(ubandwidth.getAvgBandwidth());
                    }
                    if(testKind == SpeedTestKind.JIANGSU10000) {
                        js_avgupspeed.setText(ubandwidth.getAvgBandwidthJS());
                    }
                    else {
                        tv_avgupspeed.setText(ubandwidth.getAvgBandwidth());
                        tv_peakupspeed.setText(ubandwidth.getPeakBandwidth());
                    }
                    updateDialogText("上行测试完成");
                    maxSpeed = 0;   //最值速度清零
                    minSpeed = 0;   // add by hzj on2019.8.14 修改最大速度显示bug
                    break;

                case MESSAGE_RESULT_SPEEDTEST_USERINFO:

                    //updateDialogText(jiangsuUserinfo);
                    if(msg.arg1 == 0) {
                        String jiangsuUserinfo = "" + msg.obj;
                        String[] jiangsuUserinfos = jiangsuUserinfo.split("\\|");
                        int contractRate = Integer.parseInt(jiangsuUserinfos[4]);
                        int upRate = Integer.parseInt(jiangsuUserinfos[5]);
                        js_clientIp.setText(jiangsuUserinfos[0]);
                        js_clientPort.setText(jiangsuUserinfos[1]);
                        js_cityName.setText(jiangsuUserinfos[2]);
                        js_broadbandAccount.setText(jiangsuUserinfos[3]);
                        js_contractRate.setText(ProjectUtil.getBandwidthRateJS(contractRate));
                        js_upRate.setText(ProjectUtil.getBandwidthRateJS(upRate));
                    } else {
                        JS10000_UserAuthBean js10000UserAuthBean = (JS10000_UserAuthBean)msg.obj;
                        js_clientIp.setText(js10000UserAuthBean.getClientIp());
                        js_clientPort.setText(js10000UserAuthBean.getClientPort());
                        js_cityName.setText(js10000UserAuthBean.getCityName());
                        js_broadbandAccount.setText(js10000UserAuthBean.getBroadbandAccount());
                        js_contractRate.setText(ProjectUtil.getBandwidthRateJS(js10000UserAuthBean.getContractRate()));
                        js_upRate.setText(ProjectUtil.getBandwidthRateJS(js10000UserAuthBean.getUpRate()));
                    }
                    break;
                    //add by hzj on 2019.10.10
                case MESSAGE_SPEEDTEST_TEST_TIMEOUT:
                    SpeedTestKind testKind = (SpeedTestKind) msg.obj;

                    if(testKind == SpeedTestKind.JIANGSU10000){
                        showTestFail("测速超时");
                        js_state.setText("测速超时");
                        tv_server.setText("");
                    }else{

                    }

                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };
    private View content_testspeed_userinfo;
    private TextView tv_testkind,tv_server;
    //图表界面
    private View content_speedchart;
    private WebView speedChartWebView;
    //上海电信测速结果界面
    private TextView tv_worksheetnum,tv_userid,tv_useraccount,tv_devicenumber,tv_paydownspeed,
            tv_payupspeed,tv_avgspeed,tv_avgupspeed,tv_peakspeed,tv_peakupspeed,tv_speedresult,
            tv_netdelay,tv_netloss,
            tv_speedtime;
    private TableRow tableRow_peakspeed,tableRow_netdelay;

    //广东电信测速结果界面
    private View content_testspeed_userinfo_gd10000;
    private TextView gd_useraccount,gd_city,gd_bandwidthDown,gd_bandwidthUp,gd_avgdownspeed,
            gd_avgupspeed,gd_speedtime;

    //江苏电信测速结果界面
    private View content_testspeed_userinfo_js10000;
    private TextView js_clientIp,js_clientPort,js_cityName,js_broadbandAccount,js_contractRate,
            js_upRate,js_avgdownspeed,js_avgupspeed,js_maxdownspeed,js_maxupspeed,js_state,
            js_speedtime,js_upRate_server,js_contractRate_server;

    private Button btn_testspeed;

    // add by hzj on 20191010
    private Timer timer;   //检测超时TIMER

    /**
     * 开启测速超时线程
     * @param timeOut   超时时间（单位ms）
     * @param testKind  测速类型
     */
    public void setTaskTimeOut(int timeOut,final SpeedTestKind testKind){
        Log.i("TaskTimeOut","开启超时线程");
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("TaskTimeOut","江苏测速超时");
                handler.sendMessage(handler.obtainMessage(MESSAGE_SPEEDTEST_TEST_TIMEOUT,testKind));
            }
        },timeOut);
    }

    /**
     * 关闭测速超时线程
     */
    public void stopTaskTimeOut(){
        Log.i("TaskTimeOut","停止超时线程");
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }


    public static final SpeedTestFragment newInstance(SpeedTestKind type, String HttpDownloadSource, FtpServerBean ftpServerBean, HxBoxBean hxBoxBean){
        Bundle bdl = new Bundle();
        bdl.putString(KIND_ARGUMENT,type.toString());
        bdl.putString(HTTPDOWNLOADSOURCE_ARGUMENT,HttpDownloadSource);
        //bdl.putString(FTPDOWNLOADSOURCE_ARGUMENT,ftpServerBean.toString());
        if(hxBoxBean == null)
            bdl.putString(HXBOX_ARGUMENT,"");
        else
            bdl.putString(HXBOX_ARGUMENT,hxBoxBean.toString());
        SpeedTestFragment f = new SpeedTestFragment();
        f.setArguments(bdl);
        return f;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this.getActivity();
        this.instance = this;
        Bundle bundle = getArguments();
        if(bundle != null) {
            String mKindArgument = bundle.getString(KIND_ARGUMENT);
            String mHttpDownloadSourceArgument = bundle.getString(HTTPDOWNLOADSOURCE_ARGUMENT);
            String mFtpDownloadSourceArgument = bundle.getString(FTPDOWNLOADSOURCE_ARGUMENT);
            String mHxboxArgument = bundle.getString(HXBOX_ARGUMENT);
            if(mKindArgument.equals(SpeedTestKind.GD10000.toString())) {
                this.testKind = SpeedTestKind.GD10000;
                this.testAdditional = "";
            } else if(mKindArgument.equals(SpeedTestKind.HTTP_DOWNLOAD.toString())) {
                this.testKind = SpeedTestKind.HTTP_DOWNLOAD;
                this.testAdditional = mHttpDownloadSourceArgument;
            } else if(mKindArgument.equals(SpeedTestKind.FTP_DOWNLOAD.toString())) {
                this.testKind = SpeedTestKind.FTP_DOWNLOAD;
                this.testAdditional = mFtpDownloadSourceArgument;
            } else if(mKindArgument.equals(SpeedTestKind.HXBOX.toString())) {
                this.testKind = SpeedTestKind.HXBOX;
                this.testAdditional = mHxboxArgument;
            } else if(mKindArgument.equals(SpeedTestKind.TCP_SPEEDTEST.toString())) {
                this.testKind = SpeedTestKind.TCP_SPEEDTEST;
                this.testAdditional = "";
            } else if(mKindArgument.equals(SpeedTestKind.HUNAN10000.toString())) {
                this.testKind = SpeedTestKind.HUNAN10000;
                this.testAdditional = "";
            } else if(mKindArgument.equals(SpeedTestKind.JIANGSU10000.toString())) {
                this.testKind = SpeedTestKind.JIANGSU10000;
                this.testAdditional = "";
            } else if(mKindArgument.equals(SpeedTestKind.Unknown.toString())) {
                this.testKind = SpeedTestKind.Unknown;
                this.testAdditional = "";
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_speedtest,container,false);
        /*
        binding = FragmentSpeedtestBinding.inflate(inflater,container,true);
        tv_testkind = binding.textviewSpeedtestTestkind;
        content_testspeed_userinfo = binding.flytIncludeSpeedtestResult;
        tv_server = binding.textviewSpeedtestServerinfo;
        //图表界面
        content_speedchart = binding.flytIncludeSpeedchart;
        speedChartWebView = binding.includeSpeedchart.speedtestEchartWebview;
        //指针界面
        content_speedwheel = binding.flytSpeedwheel;
        imageView_needle = binding.includeSpeedwheel.speedwheelImgNeedle;
        textView_state_wheel = binding.includeSpeedwheel.speedwheelTextviewState;
        textView_speed_wheel = binding.includeSpeedwheel.speedwheelTextviewSpeed;

        //上海电信测速结果界面
        tv_worksheetnum = binding.includeSpeedtestResult.textviewSpeedtestResultWorksheetnum;
        tv_userid = binding.includeSpeedtestResult.textviewSpeedtestResultUserid;
        tv_useraccount = binding.includeSpeedtestResult.textviewSpeedtestResultUseraccount;
        tv_devicenumber = binding.includeSpeedtestResult.textviewSpeedtestResultDevicenumber;
        tv_paydownspeed = binding.includeSpeedtestResult.textviewSpeedtestResultPaydownspeed;
        tv_payupspeed = binding.includeSpeedtestResult.textviewSpeedtestResultPayupspeed;
        tv_avgspeed = binding.includeSpeedtestResult.textviewSpeedtestResultAvgspeed;
        tv_avgupspeed = binding.includeSpeedtestResult.textviewSpeedtestResultAvgupspeed;
        tv_peakspeed = binding.includeSpeedtestResult.textviewSpeedtestResultPeakspeed;
        tv_peakupspeed = binding.includeSpeedtestResult.textviewSpeedtestResultPeakupspeed;
        tv_speedresult = binding.includeSpeedtestResult.textviewSpeedtestResultSpeedresult;
        tv_speedtime = binding.includeSpeedtestResult.textviewSpeedtestResultSpeedtime;
        tableRow_peakspeed = binding.includeSpeedtestResult.tablerowSpeedtestResultPeakspeed;

        //广东电信测速结果界面
        content_testspeed_userinfo_gd10000 = binding.flytIncludeSpeedtestResultGd10000;
        gd_useraccount = binding.includeSpeedtestResultGd10000.textviewSpeedtestResultUseraccountGd10000;
        gd_city = binding.includeSpeedtestResultGd10000.textviewSpeedtestResultCityGd10000;
        gd_bandwidthDown = binding.includeSpeedtestResultGd10000.textviewSpeedtestResultBandwidthDownGd10000;
        gd_bandwidthUp = binding.includeSpeedtestResultGd10000.textviewSpeedtestResultBandwidthDownGd10000;
        gd_avgdownspeed = binding.includeSpeedtestResultGd10000.textviewSpeedtestResultAvgdownspeedGd10000;
        gd_avgupspeed = binding.includeSpeedtestResultGd10000.textviewSpeedtestResultAvgupspeedGd10000;
        gd_speedtime = binding.includeSpeedtestResultGd10000.textviewSpeedtestResultSpeedtimeGd10000;

        //江苏电信测速结果界面
        content_testspeed_userinfo_js10000 = binding.flytIncludeSpeedtestResultJiangsu10000;
        js_clientIp = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestResultClientIpJiangsu10000;
        js_clientPort = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestResultClientPortJiangsu10000;
        js_cityName = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestResultCityNameJiangsu10000;
        js_broadbandAccount = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestResultBroadbandAccountJiangsu10000;
        js_contractRate = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestContractRateServer;
        js_upRate = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestResultUpRateJiangsu10000;
        js_avgdownspeed = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestResultAvgdownspeedJiangsu10000;
        js_avgupspeed = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestResultAvgupspeedJiangsu10000;
        js_maxdownspeed = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestResultMaxdownspeedJiangsu10000;
        js_maxupspeed = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestResultMaxupspeedJiangsu10000;
        js_state = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestResultStateJiangsu10000;
        js_speedtime = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestResultSpeedtimeJiangsu10000;
        js_upRate_server = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestUpRateServer;
        js_contractRate_server = binding.includeSpeedtestResultJiangsu10000.textviewSpeedtestContractRateServer;
        btn_testspeed = binding.btnSpeedtestStarttest;
         */
        tv_testkind = view.findViewById(R.id.textview_speedtest_testkind);
        content_testspeed_userinfo = view.findViewById(R.id.flyt_speedtest_result);
        tv_server = view.findViewById(R.id.textview_speedtest_serverinfo);
        //图表界面
        content_speedchart = view.findViewById(R.id.flyt_speedview);
        speedChartWebView = view.findViewById(R.id.speedtest_echart_webview);
        //上海电信测速结果界面
        tv_worksheetnum = view.findViewById(R.id.textview_speedtest_result_worksheetnum);
        tv_userid = view.findViewById(R.id.textview_speedtest_result_userid);
        tv_useraccount = view.findViewById(R.id.textview_speedtest_result_useraccount);
        tv_devicenumber = view.findViewById(R.id.textview_speedtest_result_devicenumber);
        tv_paydownspeed = view.findViewById(R.id.textview_speedtest_result_paydownspeed);
        tv_payupspeed = view.findViewById(R.id.textview_speedtest_result_payupspeed);
        tv_avgspeed = view.findViewById(R.id.textview_speedtest_result_avgspeed);
        tv_avgupspeed = view.findViewById(R.id.textview_speedtest_result_avgupspeed);
        tv_peakspeed = view.findViewById(R.id.textview_speedtest_result_peakspeed);
        tv_peakupspeed = view.findViewById(R.id.textview_speedtest_result_peakupspeed);
        tv_speedresult = view.findViewById(R.id.textview_speedtest_result_speedresult);
        tv_speedtime = view.findViewById(R.id.textview_speedtest_result_speedtime);
        tableRow_peakspeed = view.findViewById(R.id.tablerow_speedtest_result_peakspeed);
        tv_netdelay = view.findViewById(R.id.textview_speedtest_result_netdelay);
        tv_netloss = view.findViewById(R.id.textview_speedtest_result_netloss);
        tableRow_netdelay = view.findViewById(R.id.tablerow_speedtest_result_netdelay);
        //广东电信测速结果界面
        content_testspeed_userinfo_gd10000 = view.findViewById(R.id.flyt_include_speedtest_result_gd10000);
        gd_useraccount = view.findViewById(R.id.textview_speedtest_result_useraccount_gd10000);
        gd_city = view.findViewById(R.id.textview_speedtest_result_city_gd10000);
        gd_bandwidthDown = view.findViewById(R.id.textview_speedtest_result_bandwidthDown_gd10000);
        gd_bandwidthUp = view.findViewById(R.id.textview_speedtest_result_bandwidthUp_gd10000);
        gd_avgdownspeed = view.findViewById(R.id.textview_speedtest_result_avgdownspeed_gd10000);
        gd_avgupspeed = view.findViewById(R.id.textview_speedtest_result_avgupspeed_gd10000);
        gd_speedtime = view.findViewById(R.id.textview_speedtest_result_speedtime_gd10000);
        //江苏电信测速结果界面
        content_testspeed_userinfo_js10000 = view.findViewById(R.id.flyt_include_speedtest_result_jiangsu10000);
        js_clientIp = view.findViewById(R.id.textview_speedtest_result_clientIp_jiangsu10000);
        js_clientPort = view.findViewById(R.id.textview_speedtest_result_clientPort_jiangsu10000);
        js_cityName = view.findViewById(R.id.textview_speedtest_result_cityName_jiangsu10000);
        js_broadbandAccount = view.findViewById(R.id.textview_speedtest_result_broadbandAccount_jiangsu10000);
        js_contractRate = view.findViewById(R.id.textview_speedtest_result_contractRate_jiangsu10000);
        js_upRate = view.findViewById(R.id.textview_speedtest_result_upRate_jiangsu10000);
        js_avgdownspeed = view.findViewById(R.id.textview_speedtest_result_avgdownspeed_jiangsu10000);
        js_avgupspeed = view.findViewById(R.id.textview_speedtest_result_avgupspeed_jiangsu10000);
        js_maxdownspeed = view.findViewById(R.id.textview_speedtest_result_maxdownspeed_jiangsu10000);
        js_maxupspeed = view.findViewById(R.id.textview_speedtest_result_maxupspeed_jiangsu10000);
        js_state = view.findViewById(R.id.textview_speedtest_result_state_jiangsu10000);
        js_speedtime = view.findViewById(R.id.textview_speedtest_result_speedtime_jiangsu10000);
        js_upRate_server = view.findViewById(R.id.textview_speedtest_upRate_server);
        js_contractRate_server = view.findViewById(R.id.textview_speedtest_contractRate_server);
        btn_testspeed = view.findViewById(R.id.btn_speedtest_starttest);

        btn_testspeed.setOnClickListener(view1 -> {
            InitTestSpeed();
            if(testKind == SpeedTestKind.HXBOX) {
                HxBoxInfoDialog hxBoxInfoDialog = new HxBoxInfoDialog();
                hxBoxInfoDialog.setSubmitClickListener(new HxBoxInfoDialog.OnHxBoxInfoClickListener() {
                    @Override
                    public void onClick(HxBoxInfoDialog hxBoxInfoDialog, String AdditionalInfo) {
                        hxBoxInfoDialog.dismiss();
                        btn_testspeed.setEnabled(false);
                        doStartSpeedTest(AdditionalInfo);
                    }
                });
                hxBoxInfoDialog.show(getFragmentManager(),"HxBoxInfoDialog");
            } else {
                //btn_testspeed.setEnabled(false);
                maxSpeed = 0;
                minSpeed = 0;
                doStartSpeedTest(testAdditional);
                if(testKind == SpeedTestKind.JIANGSU10000)
                    setTaskTimeOut(90000,testKind);
            }
        });

        updateTestKind(testKind,testAdditional);
        switch (ProjectUtil.showSpeedType) {
            case TEXT -> {
                InitTestSpeed();
                content_speedchart.setVisibility(View.GONE);
            }
            case CHART -> {
                initChartView();
                content_speedchart.setVisibility(View.VISIBLE);
            }
        }
        return view;
    }

    @Override
    public void setPresenter(SpeedTestContract.Presenter presenter) {
        mPresenter = presenter;
    }


    //public void setTesttype(SpeedTestKind testKind,String HttpDownloadSource, FtpServerBean ftpServerBean,HxBoxBean hxBoxBean) {
    public void setTesttype(SpeedTestKind testKind,String HttpDownloadSource, FtpServerBean ftpServerBean,HxBoxBean hxBoxBean) {
            if(testKind == SpeedTestKind.HXBOX)
            if(hxBoxBean != null)
                updateTestKind(testKind,hxBoxBean.toString());
            else
                updateTestKind(testKind,"");
        else if(testKind == SpeedTestKind.FTP_DOWNLOAD)
            //updateTestKind(testKind,ftpServerBean.toString());
            updateTestKind(testKind,"");
        else
            updateTestKind(testKind,HttpDownloadSource);
    }


    private void doStartSpeedTest(String testAdditional) {
        switch (ProjectUtil.showSpeedType) {
            case TEXT -> {
                content_speedchart.setVisibility(View.GONE);
                mySweetALertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("")
                        .setContentText("");
                mySweetALertDialog.show();
                mySweetALertDialog.setCancelable(false);
            }
            case CHART -> {
                content_speedchart.setVisibility(View.VISIBLE);
                mySweetALertDialog = null;
            }
        }
        speedChartWebView.evaluateJavascript("javascript:clearData()", s -> {    });
        mPresenter.startTest(testKind,testAdditional);
    }

    @Override
    public void doShowTesting() {
        handler.sendMessage(handler.obtainMessage(MESSAGE_SPEEDTEST_STATECHANGE,"正在测速，请稍候!"));
    }

    @Override
    public void updateTestProgressInfo(String msg) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_SPEEDTEST_STATECHANGE,msg));
    }



    @Override
    public void updateClientInfo(String info) {

    }

    @Override
    public void updateServerInfo(String info) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_SPEEDTEST_SERVERINFO,info));
    }

    @Override
    public void doShowDownloadSpeed(float speed) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_SPEEDTEST_SPEED_DOWNLOADING,speed));
    }

    @Override
    public void doShowUploadSpeed(float speed) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_SPEEDTEST_SPEED_UPLOADING,speed));
    }

    @Override
    public void doDownloadTestComplete(float downloadAvgSpeed,float downloadPeakSpeed) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_SPEEDTEST_SPEED_DOWNLOADED,new BandwidthClass(downloadAvgSpeed,downloadPeakSpeed)));
    }

    @Override
    public void doUploadTestComplete(float uploadAvgSpeed,float uploadPeakSpeed) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_SPEEDTEST_SPEED_UPLOADED,new BandwidthClass(uploadAvgSpeed,uploadPeakSpeed)));
    }

    @Override
    public void doTestspeedComplete(SpeedTestResultBean result) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_SUCCESS,result));
    }

    @Override
    public void doTestFail(String failreason) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_FAIL,failreason));
    }

    @Override
    public void updateUserInfo_GD10000(GD10000_serveripsBean bean) {
        String[] servers = bean.getServerIps();
        String serverip = "";
        for(int i =0 ;i<servers.length;i++) {
            serverip += servers[i] + ",";
        }
        serverip = serverip.substring(0,serverip.length()-1);
        handler.sendMessage(handler.obtainMessage(MESSAGE_SPEEDTEST_USERINFO,bean));
        handler.sendMessage(handler.obtainMessage(MESSAGE_SPEEDTEST_SERVERINFO,serverip));
    }

    @Override
    public void updateUserInfo_JS10000(String js1000UserString) {
        //String clientIP = strings[0];
        handler.sendMessage(handler.obtainMessage(MESSAGE_RESULT_SPEEDTEST_USERINFO,0,0,js1000UserString));
        //handler.sendMessage(handler.obtainMessage(MESSAGE_SUCCESS));
    }

    @Override
    public void updateUserInfo_JS10000(JS10000_UserAuthBean bean) {
        //handler.sendMessage(handler.obtainMessage(MESSAGE_RESULT_SPEEDTEST_USERINFO));
        handler.sendMessage(handler.obtainMessage(MESSAGE_RESULT_SPEEDTEST_USERINFO,1,0,bean));
    }

    private void InitTestSpeed() {
        tv_server.setText("");
        tv_userid.setText("");
        tv_worksheetnum.setText("");
        tv_useraccount.setText("");
        tv_devicenumber.setText("");
        tv_paydownspeed.setText("");
        tv_payupspeed.setText("");
        tv_avgspeed.setText("");
        tv_avgupspeed.setText("");
        tv_peakspeed.setText("");
        tv_peakupspeed.setText("");
        tv_speedresult.setText("");
        tv_speedtime.setText("");
        tv_netdelay.setText("");
        tv_netloss.setText("");
        gd_useraccount.setText("");
        gd_city.setText("");
        gd_bandwidthDown.setText("");
        gd_bandwidthUp.setText("");
        gd_avgdownspeed.setText("");
        gd_avgupspeed.setText("");
        gd_speedtime.setText("");
        js_clientIp.setText("");
        js_clientPort.setText("");
        js_broadbandAccount.setText("");
        js_cityName.setText("");
        js_contractRate.setText("");
        js_upRate.setText("");
        js_avgdownspeed.setText("");
        js_avgupspeed.setText("");
        js_maxdownspeed.setText("");
        js_maxupspeed.setText("");
        js_state.setText("");
        js_speedtime.setText("");

        //add by hzj on 2019.10.11
        js_upRate_server.setText("");
        js_contractRate_server.setText("");
    }

    private void showTestSuccess(SpeedTestResultBean result) {
        switch (ProjectUtil.showSpeedType) {
            case TEXT:
                if(mySweetALertDialog!=null && mySweetALertDialog.isShowing())
                    mySweetALertDialog.dismiss();
                break;
        }
        if(result != null) {
            HxBoxBean hxbean = result.getHxbean();
            UserInfoBean userInfoBean = result.getUserInfo();
            if (hxbean == null) {
                tv_worksheetnum.setText("获取失败");
                tv_userid.setText("获取失败");
            } else {
                tv_userid.setText(hxbean.getUserid());
                tv_worksheetnum.setText(hxbean.getWorksheetnum());
            }
            if (userInfoBean == null) {
                tv_useraccount.setText("获取失败");
                tv_devicenumber.setText("获取失败");
                tv_paydownspeed.setText("获取失败");
                tv_payupspeed.setText("获取失败");
            } else {
                tv_useraccount.setText(userInfoBean.getUseraccount());
                tv_devicenumber.setText(userInfoBean.getDevicenumber());
                tv_paydownspeed.setText(userInfoBean.getPaydownspeed());
                tv_payupspeed.setText(userInfoBean.getPayupspeed());
            }
            tv_avgspeed.setText(result.getAvgspeed());
            tv_avgupspeed.setText(result.getAvgupspeed());
            tv_peakspeed.setText(result.getPeakspeed());
            tv_peakupspeed.setText(result.getPeakupspeed());
            tv_speedresult.setText(result.getSpeedresult());
            if(testKind == SpeedTestKind.JIANGSU10000) {
                js_state.setText(jsTestSpeed + result.getSpeedresult());	//modify by hzj on 2019.1.23(前面添加了下行速度)
            }
        }
        tv_speedtime.setText(ProjectUtil.formatter.format(new Date(System.currentTimeMillis())));
        if(result.getNetdelay().isEmpty()) {
            tv_netdelay.setText("-");
            tv_netloss.setText("-");
        } else {
            tv_netdelay.setText(result.getNetdelay());
            tv_netloss.setText(result.getNetloss());
        }
        gd_speedtime.setText(ProjectUtil.formatter.format(new Date(System.currentTimeMillis())));
        js_speedtime.setText(ProjectUtil.formatter.format(new Date(System.currentTimeMillis())));
        btn_testspeed.setEnabled(true);
    }

    private void showTestFail(String errString) {
        switch (ProjectUtil.showSpeedType) {
            case TEXT:
                if(mySweetALertDialog!=null && mySweetALertDialog.isShowing())
                    mySweetALertDialog.dismiss();
                break;
        }
        tv_avgspeed.setText("----");
        tv_avgupspeed.setText("----");
        tv_peakspeed.setText("----");
        tv_peakupspeed.setText("----");
        tv_speedresult.setText("测试失败：" + errString);
        tv_speedtime.setText(ProjectUtil.formatter.format(new Date(System.currentTimeMillis())));
        gd_speedtime.setText(ProjectUtil.formatter.format(new Date(System.currentTimeMillis())));
        js_speedtime.setText(ProjectUtil.formatter.format(new Date(System.currentTimeMillis())));
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("")
                .setContentText(errString)
                .setConfirmText("关闭")
                .show();
        btn_testspeed.setEnabled(true);
    }

  /*  private void updateTestspeedInfo(String titlemsg, String contentmsg) {
        if(mySweetALertDialog != null && mySweetALertDialog.isShowing()) {
            mySweetALertDialog.setTitleText(titlemsg);
            mySweetALertDialog.setContentText(contentmsg);
        }
    }*/

    private void updateTestspeedInfo(int speedtype,float speed) {
        Log.i("speed","------"+speed);
        String speedString = "";
        String stateString = "";
        if(speedtype == DOWNLOAD)
            stateString = "正在下载测试";
        else
            stateString = "正在上传测试";
        if(speed >= ProjectUtil.M) {
            speedString = ProjectUtil.getMBs(speed) + "MB/s";
        } else {
            speedString = ProjectUtil.getKBs(speed) + "KB/s";
        }
        switch (ProjectUtil.showSpeedType) {
            case TEXT:
                if(mySweetALertDialog != null && mySweetALertDialog.isShowing()) {
                    mySweetALertDialog.setContentText(stateString);
                    mySweetALertDialog.setTitleText(speedString);
                }
                break;

            case CHART:
                //textView_state_chart.setText(stateString);
                //addEntry(lineChart,speedtype,speed);
                addSpeedTestData(speedtype, 8 * ProjectUtil.getMBs(speed));
                break;
        }

    }

    private  void updateMaxSpeed(int speedtype,float speed){
        String speedString = "";

        if(speedtype == DOWNLOAD) {
            if (maxSpeed == minSpeed && maxSpeed == 0) {
                maxSpeed = speed;
                minSpeed = speed;
            } else {
                if (speed > maxSpeed) {
                    maxSpeed = speed;
                }
                if (speed < minSpeed) {
                    minSpeed = speed;
                }
            }
            speedString = ProjectUtil.getBandwidthJS(maxSpeed);
            Log.i("speedStringMaxDown","---------------" + speedString);
            js_maxdownspeed.setText(speedString);
        }else{
                if(maxSpeed == minSpeed && maxSpeed == 0) {
                    maxSpeed = speed;
                    minSpeed = speed;
                } else {
                    if(speed > maxSpeed) {
                        maxSpeed = speed;
                    }
                    if(speed < minSpeed) {
                        minSpeed = speed;
                    }
                }
            speedString = ProjectUtil.getBandwidthJS(maxSpeed);
            Log.i("speedStringMaxUp","---------------" + speedString);
            js_maxupspeed.setText(speedString);
            }
//        if(speed >= ProjectUtil.M) {
//            speedString = ProjectUtil.getMBs(maxSpeed) + "MB/s";
//            js_maxdownspeed.setText(speedString);
//        } else {
//            speedString = ProjectUtil.getKBs(maxSpeed) + "KB/s";
//            js_maxupspeed.setText(speedString);
//        }

    }
    private void updateDialogText(String titleText) {
        switch (ProjectUtil.showSpeedType) {
            case TEXT:
                if(mySweetALertDialog != null && mySweetALertDialog.isShowing()) {
                    mySweetALertDialog.setTitleText(titleText);
                }
                break;
            case CHART:
                //textView_state_chart.setText(titleText);
                break;
        }
    }

    private void updateDialogText(String titleText,String contentText) {
        switch (ProjectUtil.showSpeedType) {
            case TEXT:
                if(mySweetALertDialog != null && mySweetALertDialog.isShowing()) {
                    mySweetALertDialog.setContentText(contentText);
                    mySweetALertDialog.setTitleText(titleText);
                }
                break;
            case CHART:
                //textView_state_chart.setText(titleText);
                break;
        }
    }


    private void updateTestKind(SpeedTestKind testKind,String testAdditional) {
        this.testKind  = testKind;
        this.testAdditional = testAdditional;
        switch (testKind) {
            case HTTP_DOWNLOAD:
                content_testspeed_userinfo.setVisibility(View.VISIBLE);
                content_testspeed_userinfo_gd10000.setVisibility(View.GONE);
                content_testspeed_userinfo_js10000.setVisibility(View.GONE);
                tv_testkind.setText("HTTP下载测速");
                break;

            case FTP_DOWNLOAD:
                content_testspeed_userinfo.setVisibility(View.VISIBLE);
                content_testspeed_userinfo_gd10000.setVisibility(View.GONE);
                content_testspeed_userinfo_js10000.setVisibility(View.GONE);
                tv_testkind.setText("FTP下载测速");
                break;
            case TCP_SPEEDTEST:
                content_testspeed_userinfo.setVisibility(View.VISIBLE);
                content_testspeed_userinfo_gd10000.setVisibility(View.GONE);
                content_testspeed_userinfo_js10000.setVisibility(View.GONE);
                tv_testkind.setText("SpeedTest测速");
                break;

            case HXBOX:
                content_testspeed_userinfo.setVisibility(View.VISIBLE);
                content_testspeed_userinfo_gd10000.setVisibility(View.GONE);
                content_testspeed_userinfo_js10000.setVisibility(View.GONE);
                tv_testkind.setText("上海电信测速");
                break;

            case GD10000:
                content_testspeed_userinfo_gd10000.setVisibility(View.VISIBLE);
                content_testspeed_userinfo.setVisibility(View.GONE);
                content_testspeed_userinfo_js10000.setVisibility(View.GONE);
                tv_testkind.setText("广东电信测试");
                break;

            case JIANGSU10000:
                content_testspeed_userinfo_js10000.setVisibility(View.VISIBLE);
                content_testspeed_userinfo.setVisibility(View.GONE);
                content_testspeed_userinfo_gd10000.setVisibility(View.GONE);
                if("js10000".equals(channel))
                    tv_testkind.setText("江苏电信测试");
                else if ("ah10086".equals(channel))
                    tv_testkind.setText("安徽移动测试");
                else
                    tv_testkind.setText("江苏电信测试");
                break;

            case HUNAN10000:
                content_testspeed_userinfo.setVisibility(View.VISIBLE);
                content_testspeed_userinfo_gd10000.setVisibility(View.GONE);
                content_testspeed_userinfo_js10000.setVisibility(View.GONE);
                tv_testkind.setText("湖南电信测速");
                break;

            default:
                content_testspeed_userinfo.setVisibility(View.VISIBLE);
                content_testspeed_userinfo_gd10000.setVisibility(View.GONE);
                content_testspeed_userinfo_js10000.setVisibility(View.GONE);
                tv_testkind.setText("其他测速");
                break;
        }
    }

    /*
    // 添加一个坐标点
    private void addEntry(LineChart mChart,int speedtype,float speed) {
        int lastIndex ;
        float Mspeed = ProjectUtil.getMBs(speed);
        LineDataSet lastSet;
        LineData lineData = mChart.getData();
        if(lineData == null)
            lineData = new LineData();
        switch (speedtype) {
            case DOWNLOAD:
                if(downloadLineDataSet == null) {
                    downloadLineDataSet = createDownloadLineDataSet();
                    lineData.addDataSet(downloadLineDataSet);
                    mChart.setData(lineData);
                    mChart.setVisibility(View.VISIBLE);
                }
                lastSet = downloadLineDataSet;
                lastIndex = lineData.getIndexOfDataSet(downloadLineDataSet);
                break;
            default:
                if(uploadLineDataSet == null) {
                    uploadLineDataSet = createUploadLineDataSet();
                    lineData.addDataSet(uploadLineDataSet);
                    mChart.setData(lineData);
                }
                lastSet = uploadLineDataSet;
                lastIndex = lineData.getIndexOfDataSet(uploadLineDataSet);
                break;
        }
        if(lineData != null) {
            int count = lastSet.getEntryCount();
            lineData.addEntry(new Entry(count,Mspeed),lastIndex);
            mChart.notifyDataSetChanged();
            mChart.moveViewTo(count, Mspeed, YAxis.AxisDependency.LEFT);
        }
    }

    private LineDataSet createDownloadLineDataSet() {
        LineDataSet set = new LineDataSet(null, "下载速度");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        // 折线的颜色
        set.setColor(Color.RED);
        set.setCircleColor(Color.YELLOW);
        set.setCircleColorHole(Color.BLUE);
        set.setHighLightColor(Color.GREEN);
        set.setValueTextColor(Color.RED);
        set.setValueTextSize(5f);
        set.setDrawValues(true);
        set.setValueFormatter(new IValueFormatter(){
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                            ViewPortHandler viewPortHandler) {
                DecimalFormat decimalFormat = new DecimalFormat("#.00MB/s");
                String s = decimalFormat.format(value);
                return s;
            }
        });

        return set;
    }

    private LineDataSet createUploadLineDataSet() {
        LineDataSet set = new LineDataSet(null, "上传");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        // 折线的颜色
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(Color.BLUE);
        set.setHighLightColor(Color.DKGRAY);
        set.setValueTextColor(Color.BLACK);
        set.setCircleColorHole(Color.RED);
        set.setValueTextSize(5f);
        set.setDrawValues(true);
        set.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex,
                                            ViewPortHandler viewPortHandler) {
                DecimalFormat decimalFormat = new DecimalFormat("#.00MB/s");
                String s =  decimalFormat.format(value);
                return s;
            }
        });

        return set;
    }

     */

    private class EChartsWebViewClient extends WebViewClient {
        private String option;
        public EChartsWebViewClient(String option) {
            this.option = option;
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(final WebView view, String url) {
            android.util.Log.i("图表", "html加载完成 onPageFinished");
            view.post(() -> {
                view.loadUrl(String.format("javascript:setOption(%s)", option));
                speedChartWebView.evaluateJavascript("javascript:clearData()", s -> {});
            });
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            android.util.Log.i("图表加载出错", error.toString());
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initChartView() {
        WebSettings settings = speedChartWebView.getSettings();
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        speedChartWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        speedChartWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        speedChartWebView.loadUrl("file:///android_asset/index.html");
        setOption(getOptionString());
    }

    private String getOptionString() {
        return "{" +
                "xAxis: {type: 'category',show: false}," +
                "yAxis: {type: 'value',show: false}," +
                "grid: { top: '80%' }," +
                "series:[" +
                " {" +
                "  type: 'gauge'," +
                "  min: 0," +
                "  max: 10000," +
                "  radius: '80%'," +
                "  center: ['50%', '50%']," +
                "  axisLine: {lineStyle: {width: 40,color: [[0.3, '#67e0e3'],[0.7, '#37a2da'],[1, '#fd666d']]}}," +
                "  pointer: {itemStyle: {color: 'inherit'}}," +
                "  axisTick: {distance: -30,length: 8,lineStyle: {color: '#fff',width: 2}}," +
                "  splitLine: {distance: -30,length: 30,lineStyle: {color: '#fff',width: 4}}," +
                "  axisLabel: {color: '#000',distance: 50,fontSize: 30}," +
                "  detail: {valueAnimation: true,formatter: '{value} Mbps',color: '#37a2da'}," +
                "  title:{ fontSize:25,color:'#37a2da'}," +
                "  animationDuration: 800" +
                " }" +
                "]" +
                "}";
    }

    private void setOption(String option){
        speedChartWebView.loadUrl("file:///android_asset/index.html");
        speedChartWebView.setWebViewClient(new EChartsWebViewClient(option));
    }

    private void addSpeedTestData(int dnORup,float speedData) {
        String js = "javascript:addSpeedData(" + dnORup + "," + speedData + ")";
        speedChartWebView.evaluateJavascript(js, s -> Log.d("addSpeedTestData","get " + js + "return: " + s ));
    }
}
