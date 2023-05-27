package com.shwangce.nt10g.client.util;

import android.content.pm.PackageInfo;
import android.os.Environment;

import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.bean.BoxInfoBean;
import com.shwangce.nt10g.client.library.FtpServerBean;
import com.shwangce.nt10g.client.library.communicate.BoxController;
import com.shwangce.nt10g.client.speedtest.HxBoxBean;
import com.shwangce.nt10g.client.speedtest.SpeedTestKind;
import com.shwangce.nt10g.client.wifitest.WifiBean;

import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Administrator on 2016/12/14 0014.
 */

public class ProjectUtil {

    public static final int K = 1024;
    public static final int M = K * K;
    public static final String DeviceName = String.valueOf(R.string.device_name);
    public static final String SDCardDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Shwangce";

    public static int APIVERSION = android.os.Build.VERSION.SDK_INT;

    public static final int MESSAGE_BOXAPP_VERSIONSHOW = 1;

    public static final int MESSAGE_ACCESSDIALOG_SHOW = 2;
    public static final int MESSAGE_MODEDIALOG_SHOW = 3;

    public static final int MESSAGE_SETMODE_SUCCESS = 4;
    public static final int MESSAGE_CONNECTAP_SUCCESS = 5;

    public static final int MESSAGE_CONNECTED = 10;
    public static final int MESSAGE_UPDATE_STATE = 11;
    public static final int MESSAGE_UPDATE_ETHERNETLINK = 12;
    public static final int MESSAGE_ACCESS_DHCP = 21;
    public static final int MESSAGE_ACCESS_PPPOE = 22;
    public static final int MESSAGE_ACCESS_STATIC= 23;

    public static final int MESSAGE_BOXUPDATE_INFO = 24;
    public static final int MESSAGE_BOXUPDATE_STATE = 25;
    public static final int MESSAGE_BOXUPDATE_START = 26;
    public static final int MESSAGE_BOXUPDATE_FAIL = 27;

    public static final int MESSAGE_OPEN_LOCATION_ACCEPT = 28;
    public static final int MESSAGE_OPEN_LOCATION_REJECT = 29;

    public static final int MESSAGE_SCAN_UPDATE = 30;

    public static final int MESSAGE_ACCESS_WIFI_DHCP = 40;
    public static final int MESSAGE_UPDATE_AP_INFO = 41;

    public static final int MESSAGE_CONNECTLOSS = 90;



    public static String channel = "";
    public static String app_name = "";
    public static String app_update_url = "";
    public static String[] setModeString = new String[5];
    public static boolean isFirstConnect = true;

    public static int maxFrameDataLength = 0;
    public static PackageInfo clientPackageInfo = null;

    public static HxBoxBean hxBoxBean = new HxBoxBean(0,3,"sj","111111","60101",1,"123456");

    public static BoxInfoBean boxInfoBean = new BoxInfoBean();

    public static FtpServerBean ftpServerBean = new FtpServerBean();

    public static SetModeEnum currentMode;

    public static List<WifiBean> historyApArray = null;

    public enum TabEnum {
        Testspeed,
        Setting,
        NetTools,
        IptvTest
    }

    public enum SetModeEnum {
        SingleLanInternal,
        SingleLanExternal,
        DoubleLan,
        WifiDHCP,
        LanAndWifi,
        //ITVSimulate
    }

    public enum SetModeResultEnum {
        Success,
        Fail,
        Reboot
    }

    public enum ShowSpeedType {
        TEXT,
        CHART,
        NEEDLE
    }

    // add by hzj on 20191018
    public static String ConnectedDeviceName = "";


    public static ShowSpeedType showSpeedType = ShowSpeedType.CHART;

    public static SpeedTestKind speedTestKind = SpeedTestKind.Unknown;
    public static String Httpdownloadurl = "";
    public static String Httpuploadurl = "";
    //public static FtpServerBean ftpServerBean = new FtpServerBean();
    public static boolean isFromSetting = true;
    public static BoxController.CommunicateType communicateType = BoxController.CommunicateType.BLE;

    public static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
    public static final DecimalFormat df0 = new DecimalFormat("######0");
    public static final DecimalFormat df1 = new DecimalFormat("######0.0");
    public static final DecimalFormat df2 = new DecimalFormat("######0.00");

    public static boolean isBoxSe() {
        if(boxInfoBean.getVersion().contains("_se_"))
            return true;
        else
            return false;
    }

    public static byte[] getFileData(String filename) {
        byte[] data;
        File dir = new File(filename);
        if(!dir.exists()) {
            return null;
        }
        try{
            FileInputStream fileInputStream = new FileInputStream(filename);
            int length = fileInputStream.available();
            data = new byte[length];
            fileInputStream.read(data);
            fileInputStream.close();
        } catch(Exception e){
            e.printStackTrace();
            data = null;
        }
        return data;
    }

    public static float getKBs(float speed) {
        float speedK = speed / K;
        return Float.parseFloat(df2.format(speedK));
    }

    public static float getMBs(float speed) {
        float speedM = speed / M;
        return Float.parseFloat(df2.format(speedM));
    }

    public static String getBandwidth(float speed) {
        String bandwidth = "";
        float b = speed * 8f;
        if(b >= M) {
            b = b / M;
            bandwidth = df2.format(b) + "Mbps";
        } else if(b >= K) {
            b = b / K;
            bandwidth = df2.format(b) + "Kbps";
        } else {
            bandwidth = df2.format(b) + "bps";
        }
/*
        long speedLong = (long)((speed * 8) / K);
        double b = speedInt * 8d;
        bandwidth = df0.format(b) + "Kbps";     //修改江苏测速，手机显示与服务器显示差别。
        if( b >= 1024){
            double bb = b/(double) K;
            bandwidth = df2.format(bb)+"Mbps";
        }
*/
//        if(b < M) {
//            double b = speed /(double)K ;
//            bandwidth = df2.format(b) + "Kbps";
//        } else {
//            double b = speed /(double)M ;
//            bandwidth = df2.format(b) + "Mbps";
//        }
        return bandwidth;
    }
    public static String getBandwidthJS(float speed) {
        String bandwidth = "";
//        speed = speed * 8f;
//            double b = speed /(double)K ;
//            bandwidth = df0.format(b) + "Kbps";

        float band = speed * 8;        //modify by hzj on 2019.8.14
        bandwidth = df0.format(band / K) + "Kbps";
        if(band >= M) {
            bandwidth = bandwidth + " ("+df1.format(band / M)+"Mbps)";
        }
        return bandwidth;
    }
    public static String getBandwidthRateJS(long bandwidthrate) {
        String bandwidth = "";
        bandwidth = df0.format(bandwidthrate / K) + "Kbps";

        //add by hzj on 2019.1.17 修改江苏测速测试结果显示（2048Kbps → 2.0Mbps）
        if(bandwidthrate>=M){
            bandwidth = bandwidth + " ("+df1.format(bandwidthrate / M)+"Mbps)";
        }
        return bandwidth;
    }
}
