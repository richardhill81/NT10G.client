package com.shwangce.nt10g.client.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.bean.BoxInfoBean;
import com.shwangce.nt10g.client.library.bluetoothLe.DeviceBean;
import com.shwangce.nt10g.client.setaccess.PPPoEInfoBean;
import com.shwangce.nt10g.client.setaccess.StaticInfoBean;
import com.shwangce.nt10g.client.speedtest.HxBoxBean;
import com.shwangce.nt10g.client.speedtest.SpeedTestKind;
import com.shwangce.nt10g.client.wifitest.WifiBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public abstract class SharedPreferencesUtil {
    public final static String SharedPreferencesName = "nt10g";
    public static void setTestType(Context context, SpeedTestKind testtype) {
        String mytype = "";
        switch (testtype) {
            case HTTP_DOWNLOAD:
                mytype = "1";
                break;
            case HXBOX:
                mytype = "2";
                break;
            case TCP_SPEEDTEST:
                mytype = "3";
                break;
            case GD10000:
                mytype = "4";
                break;
            case HUNAN10000:
                mytype = "5";
                break;
            case FTP_DOWNLOAD:
                mytype = "6";
                break;
            case FTP_UPLOAD:
                mytype = "7";
                break;
            case JIANGSU10000:
                mytype = "8";
                break;
        }
        SharedPreferences preferences= context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("SpeedTestKind", mytype);
        editor.apply();
    }

    public static SpeedTestKind getTestType(Context context) {
        SharedPreferences preferences=context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        String mytype = preferences.getString("SpeedTestKind", "1");
        SpeedTestKind testKind = SpeedTestKind.HTTP_DOWNLOAD;
        switch (mytype) {
            case "1":
                testKind = SpeedTestKind.HTTP_DOWNLOAD;
                break;
            case "2":
                testKind = SpeedTestKind.HXBOX;
                break;
            case "3":
                testKind = SpeedTestKind.TCP_SPEEDTEST;
                break;
            case "4" :
                testKind = SpeedTestKind.GD10000;
                break;
            case "5":
                testKind = SpeedTestKind.HUNAN10000;
                break;
            case "6":
                testKind = SpeedTestKind.FTP_DOWNLOAD;
                break;
            case "7":
                testKind = SpeedTestKind.FTP_UPLOAD;
                break;
            case "8":
                testKind = SpeedTestKind.JIANGSU10000;
                break;
        }
        return testKind;
    }

    public static void setHttpDownloadUrl(Context context,String httpdownloadurl) {
        SharedPreferences preferences= context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("HttpDownloadUrl", httpdownloadurl);
        editor.apply();
    }

    public static String getHttpDownloadUrl(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        return preferences.getString("HttpDownloadUrl", context.getString(R.string.default_download_url));
    }

    public static void setHttpUploadUrl(Context context,String httpdownloadurl) {
        SharedPreferences preferences= context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("HttpUploadUrl", httpdownloadurl);
        editor.apply();
    }

    public static String getHttpUploadUrl(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        return preferences.getString("HttpUploadUrl", context.getString(R.string.default_download_url));
    }

    /*
    public static void setFtpDownloadServerBean(Context context,FtpServerBean ftpServerBean) {
        SharedPreferences preferences= context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("FtpServerHost", ftpServerBean.getServerhost());
        editor.putInt("FtpServerPort",ftpServerBean.getServerport());
        editor.putString("FtpUserName", ftpServerBean.getUsername());
        editor.putString("FtpPassword", ftpServerBean.getPassword());
        editor.putString("FtpRemotePath", ftpServerBean.getRemotePath());
        editor.putString("FtpRemoteFileName", ftpServerBean.getRemoteFileName());
        editor.apply();
    }


    public static FtpServerBean getFtpDownloadServerBean(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        FtpServerBean ftpServerBean = new FtpServerBean();
        ftpServerBean.setServerhost(preferences.getString("FtpServerHost",""));
        ftpServerBean.setServerport(preferences.getInt("FtpServerPort",21));
        ftpServerBean.setUsername(preferences.getString("FtpUserName",""));
        ftpServerBean.setPassword(preferences.getString("FtpPassword",""));
        ftpServerBean.setRemotePath(preferences.getString("FtpRemotePath",""));
        ftpServerBean.setRemoteFileName(preferences.getString("FtpRemoteFileName",""));
        return ftpServerBean;
    }

     */
    public static void setHxUserInfo(Context context, HxBoxBean hxBoxBean) {
        SharedPreferences preferences= context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("HX_UserName", hxBoxBean.getUserName());
        editor.putString("HX_Password",hxBoxBean.getPwd());
        editor.putString("HX_UserId", hxBoxBean.getUserid());
        editor.putString("HX_WorksheetNum",hxBoxBean.getWorksheetnum());
        editor.putInt("HX_Worktype",hxBoxBean.getWorktype());
        editor.apply();
    }

    public static HxBoxBean getHxUserInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        HxBoxBean info = new HxBoxBean();
        info.setUserName(preferences.getString("HX_UserName", ""));
        info.setPwd(preferences.getString("HX_Password", ""));
        info.setUserid(preferences.getString("HX_UserId", ""));
        info.setWorksheetnum(preferences.getString("HX_WorksheetNum",""));
        info.setWorktype(preferences.getInt("HX_Worktype",0));
        if(!info.getUserName().equals(""))
            return info;
        else
            return null;
    }

    public static void setStaticInfo(Context context, StaticInfoBean info) {
        SharedPreferences preferences= context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("static_ip", info.getIpString());
        editor.putString("static_netmask",info.getNetMask());
        editor.putString("static_gateway", info.getGateWay());
        editor.putString("static_dns", info.getDNS());
        editor.apply();
    }

    public static StaticInfoBean getStaticInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        StaticInfoBean info = new StaticInfoBean();
        info.setIpString(preferences.getString("static_ip",""));
        info.setNetMask(preferences.getString("static_netmask","255.255.255.0"));
        info.setGateWay(preferences.getString("static_gateway",""));
        info.setDNS(preferences.getString("static_dns",""));
        return info;
    }

    public static void setPPPoEInfo(Context context, PPPoEInfoBean info) {
        SharedPreferences preferences= context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("pppoe_account", info.getPPPoEAccount());
        editor.putString("pppoe_password",info.getPPPoEPassword());
        editor.apply();
    }

    public static PPPoEInfoBean getPPPoEInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        PPPoEInfoBean info = new PPPoEInfoBean();
        info.setPPPoEAccount(preferences.getString("pppoe_account",""));
        info.setPPPoEPassword(preferences.getString("pppoe_password",""));
        return info;
    }

    public static void setBoxInfo(Context context,BoxInfoBean info) {
        SharedPreferences preferences= context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("box_workmode", info.getWorkmode() + "");
        editor.putString("box_tcpdumpsave",info.getTcpdumpsave() + "");
        editor.apply();
    }

    public static BoxInfoBean getBoxInfo(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        BoxInfoBean info = new BoxInfoBean();
        info.setWorkmode(preferences.getString("box_workmode","0"));
        info.setTcpdumpsave(preferences.getString("box_tcpdumpsave","0"));
        return info;
    }

    public static DeviceBean[] getDeviceHistory(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        String history = preferences.getString("device_history","");
        Log.d("getDeviceHistory",history);
        if(history.length() >0) {
            String deviceStrings[] = history.split("\\|");
            if(deviceStrings.length >0 ) {
                ArrayList<DeviceBean> deviceBeans = new ArrayList<>();
                for(int i=0;i < deviceStrings.length;i++) {
                    String d = deviceStrings[i];
                    int l = d.indexOf("&");
                    if(l >=0) {
                        deviceBeans.add(new DeviceBean(d.substring(0,l).replace("NT10G_",""),d.substring(l+1)));
                        //deviceBeans.add(new DeviceBean(d.substring(0,l),d.substring(l+1)));
                    }
                }
                return deviceBeans.toArray(new DeviceBean[deviceBeans.size()]);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public static void clearDeviceHistory(Context context) {
        SharedPreferences preferences= context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("device_history", "");
        editor.apply();
    }

    public static void setDeviceHistory(Context context,DeviceBean[] deviceBeans) {
        SharedPreferences preferences= context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        StringBuilder stringBuilder = new StringBuilder();
        for (DeviceBean device:deviceBeans) {
            stringBuilder.append(device.toString() + '|');
        }
        String s = stringBuilder.toString();
        s = s.substring(0,s.length() - 1);
        Log.d("setDeviceHistory",s);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("device_history", s);
        editor.apply();
    }

    public static List<WifiBean> getApHistory(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        String history = preferences.getString("ap_history","");
        Log.d("getApHistory",history);
        ArrayList<WifiBean> wifiBeans = new ArrayList<>();
        if(history.length() >0) {
            String[] apStrings = history.split(";");
            if(apStrings.length >0 ) {
                for (String apString : apStrings) {     // ESSID + "|" + BSSID + "|" + password;
                    String[] apparams = apString.split("\\|");
                    if (apparams.length >= 3) {
                        WifiBean bean = new WifiBean();
                        bean.setEssid(apparams[0]);
                        bean.setBssid(apparams[1]);
                        bean.setPassword(apparams[2]);
                        wifiBeans.add(bean);
                    }
                }
            }
        }
        return wifiBeans;
    }


    public static void updateApHistory(Context context, WifiBean[] beans) {
        SharedPreferences preferences= context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        StringBuilder history = new StringBuilder();
        for(WifiBean bean : beans) {
            String apInfo = bean.getInfoString();
            if(history.length() == 0) {
                history = new StringBuilder(apInfo);
            } else {
                history.append(";").append(apInfo);
            }
        }
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("ap_history", history.toString());
        editor.apply();
    }

    public static String getLastEnterDeviceHistory(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        return preferences.getString("lastenterdevicehistory","");
    }

    public static void setLastEnterDeviceHistory(Context context,String lastEnterDeviceHistory) {
        SharedPreferences preferences= context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("lastenterdevicehistory", lastEnterDeviceHistory);
        editor.apply();
    }

    public static void setHistoryWebNames(Context context, List<String> webNames){
        int count = webNames.size();
        SharedPreferences preferences = context.getSharedPreferences("webName",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("count",count);

        for(int i = 0;i < count;i++){
            editor.putString("key"+i,webNames.get(i));
        }
        editor.apply();
    }

    public static List<String> getHistoryWebNames(Context context){
        List<String> webNames = new ArrayList<String>();
        SharedPreferences preferences = context.getSharedPreferences("webName",context.MODE_PRIVATE);

        int count = preferences.getInt("count",-1);
        for(int i = 0;i<count;i++){
            String webName = preferences.getString("key"+i,"");
            webNames.add(webName);
        }
        return webNames;
    }

    public static void addDeviceHistory(Context context,DeviceBean deviceBean) {
        SharedPreferences preferences= context.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
        String history = preferences.getString("device_history","");
        if(history.length() == 0) {
            history = deviceBean.toString();
        } else {
            history = history + "|" + deviceBean.toString();
        }
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("device_history", history);
        editor.apply();
    }
}
