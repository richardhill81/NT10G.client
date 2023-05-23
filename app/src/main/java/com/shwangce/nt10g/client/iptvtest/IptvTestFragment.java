package com.shwangce.nt10g.client.iptvtest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.sweetalert.SweetAlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Yannik on 2017/11/27.
 */

public class IptvTestFragment extends Fragment implements IptvTestContract.View {

    private final String TRANSFERFORMAT_NOVLAN = "eth/ipv4/udp/rtp/mp2t";
    private final String TRANSFERFORMAT_VLAN = "eth/vlan/ipv4/udp/rtp/mp2t";
    private final String RTPTYPE = "mp2t";
    private final String VLANPRIORITY = "无";

    private TabHost tabHost;
    private TabWidget tabWidget;
    private Context context;
    private Activity activity;

    private String srcIP,srcPort,srcMac,dstIP,dstPort,dstMac,vlanid,vlanpriority,tos,ttl,transferformat,rtptype;
    private String packagecount,lostcount,jitter, minjitter, maxjitter, speed, avgspeed, maxspeed, minspeed;

    @BindView(R.id.textview_iptv_transferFormat) TextView textview_iptv_transferFormat;
    @BindView(R.id.textview_iptv_rtpType) TextView textview_iptv_rtpType;
    @BindView(R.id.textview_iptv_vlanInfo) TextView textview_iptv_vlanInfo;
    @BindView(R.id.textview_iptv_MacInfo) TextView textview_iptv_MacInfo;
    @BindView(R.id.textview_iptv_IpandPortInfo) TextView textview_iptv_IpandPortInfo;
    @BindView(R.id.textview_iptv_tosandttl) TextView textview_iptv_tosandttl;
    @BindView(R.id.textview_iptv_packageInfo) TextView textview_iptv_packageInfo;
    @BindView(R.id.textview_iptv_speedInfo) TextView textview_iptv_speedInfo;
    @BindView(R.id.textview_iptv_jitterInfo) TextView textview_iptv_jitterInfo;

    @BindView(R.id.button_iptv_start)
    Button btn_iptv_start;
    @OnClick(R.id.button_iptv_start)
    void onIptvStartClick(){
        String buttontext = btn_iptv_start.getText().toString();
        if(buttontext.equals("开始测试")) {
            clearResult();
            if(sweetAlertDialog != null && sweetAlertDialog.isShowing())
                sweetAlertDialog.dismissWithAnimation();
            sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("正在启动IPTV测试，请稍候...");
            sweetAlertDialog.show();
            sweetAlertDialog.setCancelable(false);
            mPresenter.startIptvTest();
        } else {
            mPresenter.stopIptvTest();
            btn_iptv_start.setText("开始测试");
        }
    }
    /*
    @BindView(R.id.button_iptv_stop) Button button_iptv_stop;
    @OnClick(R.id.button_iptv_stop)
    void onIptvStopClick() {
        mPresenter.stopIptvTest();
    }
    */

    private SweetAlertDialog sweetAlertDialog;
    private IptvTestContract.Presenter mPresenter;

    @Override
    public void setPresenter(IptvTestContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void doShowIptvStartSuccess() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btn_iptv_start.setText("停止测试");
                if(sweetAlertDialog != null && sweetAlertDialog.isShowing())
                    sweetAlertDialog.dismissWithAnimation();
            }
        });
    }

    @Override
    public void doShowIptvStartFail(String failinfo) {
        final String errorinfo = failinfo;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(sweetAlertDialog != null && sweetAlertDialog.isShowing())
                    sweetAlertDialog.dismissWithAnimation();
                sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("")
                        .setContentText(errorinfo)
                        .setConfirmText("关闭");
                sweetAlertDialog.show();
            }
        });
    }


    @Override
    public void doShowIptvEthernetInfo(IptvTestResultBean.EthernetInfoBean result) {
        dstMac = result.getDstMac();
        srcMac = result.getSrcMac();
        rtptype = RTPTYPE;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            updateEthernetInfo();
            }
        });
    }

    @Override
    public void doShowIptvVlanInfo(IptvTestResultBean.VlanInfoBean result) {
        if(result.getVlanId().equals("")) {
            vlanid = "无";
        } else {
            vlanid = result.getVlanId();
        }
        vlanpriority = result.getVlanPriority();
        if(result.getVlanId().equals("无")) {
            transferformat = TRANSFERFORMAT_NOVLAN;
        } else {
            transferformat = TRANSFERFORMAT_VLAN;
        }
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateVlanInfo();
            }
        });
    }

    @Override
    public void doShowIptvIPv4Info(IptvTestResultBean.IPv4InfoBean result) {
        final boolean showvlan;
        srcIP = result.getSrcIP();
        dstIP = result.getDstIP();
        tos = result.getTos();
        ttl = result.getTtl();
        if(vlanid == "" ) {
            vlanid = "无";
            vlanpriority = "/";
            transferformat = TRANSFERFORMAT_NOVLAN;
            showvlan = true;
        } else
            showvlan = false;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(showvlan)
                    updateVlanInfo();
                updateIpv4Info();
            }
        });
    }

    @Override
    public void doShowIptvUdpInfo(IptvTestResultBean.UdpInfoBean result) {
        srcPort = result.getSrcPort();
        dstPort = result.getDstPort();
        final IptvTestResultBean.UdpInfoBean bean = result;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUdpInfo();
            }
        });
    }

    @Override
    public void doShowIptvQualityInfo(IptvTestResultBean.QualityInfoBean result) {
        packagecount = result.getPackagecount() + "个/秒";
        lostcount = result.getLostcount() + "个";
        jitter = result.getJitter() + "ms";
        minjitter = result.getMinjitter() + "ms";
        maxjitter = result.getMaxjitter() + "ms";
        speed = result.getSpeed() + "Kbps";
        avgspeed = result.getAvgspeed() + "Kbps";
        maxspeed = result.getMaxspeed() + "Kbps" ;
        minspeed = result.getMinspeed() + "Kbps";
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
            updateQualityInfo();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this.getActivity();
        activity = this.getActivity();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_iptvtest,container,false);
        ButterKnife.bind(this,view);
        clearResult();
        return view;
    }

    private void clearResult() {
        srcIP=srcPort=srcMac=dstIP=dstPort=dstMac=vlanid=vlanpriority=tos=ttl=transferformat=rtptype = "";
        packagecount=lostcount= jitter=minjitter= maxjitter= speed= avgspeed= maxspeed= minspeed = "";
        textview_iptv_transferFormat.setText("网络协议：");
        textview_iptv_rtpType.setText("RTP类型：");
        textview_iptv_vlanInfo.setText("Vlan：");
        textview_iptv_MacInfo.setText("Mac地址：");
        textview_iptv_IpandPortInfo.setText("IP地址：");
        textview_iptv_tosandttl.setText("TOS值：   ，TTL值：");
        textview_iptv_packageInfo.setText("每秒包数：");
        textview_iptv_speedInfo.setText("传输速率：");
        textview_iptv_jitterInfo.setText("抖动：");
    }

    private void updateEthernetInfo() {
        String macInfo = srcMac + " -> " + dstMac;
        String rtpInfo = "RTP类型：" + rtptype;
        textview_iptv_rtpType.setText(rtpInfo);
        textview_iptv_MacInfo.setText(macInfo);
    }

    private void updateVlanInfo() {
        String vlanInfo = "Priority：" + vlanpriority + "," + "Id：" + vlanid  ;
        String transferformatInfo = "网络协议：" + transferformat;
        textview_iptv_transferFormat.setText(transferformatInfo);
        textview_iptv_vlanInfo.setText(vlanInfo);
    }

    private void updateIpv4Info() {
        String ipandportInfo = srcIP + ":" + srcPort  + " -> "  + dstIP + ":" + dstPort;
        String tosandttl = "TOS：" + tos +  "，" + "TTL：" + ttl;
        textview_iptv_IpandPortInfo.setText(ipandportInfo);
        textview_iptv_tosandttl.setText(tosandttl);
    }

    private void updateUdpInfo() {
        String ipandportInfo = srcIP + ":" + srcPort  + " -> "  + dstIP + ":" + dstPort;
        textview_iptv_IpandPortInfo.setText(ipandportInfo);
    }

    private void updateQualityInfo() {
        String packageInfo = "每秒包数：" + packagecount + "\n" + "总丢包数：" + lostcount;
        String speedInfo = "当前速率：" + speed + "\n平均速率：" + avgspeed;
        String jitterInfo = "当前抖动："+ jitter + "\n最大抖动："+ maxjitter + "\n" + "最小抖动：" + minjitter;
        textview_iptv_packageInfo.setText(packageInfo);
        textview_iptv_speedInfo.setText(speedInfo);
        textview_iptv_jitterInfo.setText(jitterInfo);
    }
}
