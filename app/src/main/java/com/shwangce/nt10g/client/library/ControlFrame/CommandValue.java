package com.shwangce.nt10g.client.library.ControlFrame;

/**
 * Created by Administrator on 2017/2/17 0017.
 */

public abstract class CommandValue {

    public final static byte COMMAND_BOXVERSION_GETINFO = (byte)0xE0;
    public final static byte COMMAND_BOXAPP_RECEIVE = (byte)0xE1;
    public final static byte COMMAND_BOXAPP_UPDATE = (byte)0xE2;
    public final static byte COMMAND_WORKMODE_SETTING = (byte)0xE3;
    public final static byte COMMAND_SYSTEMTIME_UPDATE = (byte)0xE4;
    public final static byte COMMAND_TIMERSCHEDULE_START = (byte)0xE5;
    public final static byte COMMAND_TIMERSCHEDULE_DELETE = (byte)0xE6;
    public final static byte COMMAND_TIMERSCHEDULE_SEARCH = (byte)0xE7;
    public final static byte COMMAND_CONTINUE_DETAIL = 0x00;

    public final static byte COMMAND_OTA_UPDATE = (byte)0xD0;

    public final static byte COMMAND_SETACCESS_DHCP = 0x01;
    public final static byte COMMAND_SETACCESS_PPPOE = 0x02;
    public final static byte COMMAND_SETACCESS_STATIC = 0x03;
    public final static byte COMMAND_SPEEDTEST_HTTPDOWNLOAD = 0x10;
    public final static byte COMMAND_SPEEDTEST_TCPSPEEDTEST = 0x11;
    public final static byte COMMAND_SPEEDTEST_HXBOX = 0x12;
    public final static byte COMMAND_SPEEDTEST_GD10000 = 0x13;
    public final static byte COMMAND_SPEEDTEST_HUNAN10000 = 0x14;
    public final static byte COMMAND_SPEEDTEST_FTPDOWNLOAD = 0x15;
    public final static byte COMMAND_SPEEDTEST_FTPUPLOAD = 0x16;
    public final static byte COMMAND_SPEEDTEST_HTTPUPLOAD = 0x17;
    public final static byte COMMAND_GUANGXI10000_SPEEDTEST = 0x18;
    public final static byte COMMAND_JIANGSU10000_SPEEDTEST = 0x19;


    public final static byte COMMAND_NETTOOLS_PING = 0x20;
    public final static byte COMMAND_NETTOOLS_TRACEROUTE = 0x21;
    public final static byte COMMAND_IPERF_START = 0x22;
    public final static byte COMMAND_TCPDUMP_START = 0x23;
    public final static byte COMMAND_TCPDUMP_STOP = 0x24;
    public final static byte COMMAND_NETTOOLS_PINGANDTRACEROUTE = 0x25;
    public final static byte COMMAND_NETTOOLS_REPING = 0x26;

    public final static byte COMMAND_ZHEJIANG10086_SPEEDTEST = 0x40;
    public final static byte COMMAND_ZHEJIANG10086_REPORTRESULT = 0x41;


    //2022.06.10去除iptv功能
    //public final static byte COMMAND_IPTV_START= (byte) 0x61;
    //public final static byte COMMAND_IPTV_STOP = (byte)0x62;

    //add by hzj in 2018.12.3
    public final static byte COMMAND_WEBTEST = (byte)0x71;
    public final static byte COMMAND_DNSTEST = (byte)0x72;
    //add by hzj in 2018.12.13

    public final static byte COMMAND_QUERYMEMORY = (byte) 0x80;
    public final static byte COMMAND_QUERYPACKAGEFILE  = (byte) 0x81;
    public final static byte COMMAND_QUERYLOGFILE = (byte) 0x82;
    public final static byte COMMAND_DELETEFILE = (byte) 0x83;

    public final static byte COMMAND_QUERYLOGFILESIZE = (byte) 0x84;
    public final static byte COMMAND_EXPORTLOG  = (byte) 0x85;
    public final static byte COMMAND_STOPEXPORTLOG = (byte) 0x86;

    public final static byte COMMAND_WIFI_SCAN = (byte)0xA0;
    public final static byte COMMAND_WIFI_CONNECT = (byte)0xA1;
    public final static byte COMMAND_WIFI_CLOSE = (byte)0xA2;
    public final static byte COMMAND_WIFI_STOPSCAN = (byte)0xA3;
    public final static byte COMMAND_WIFI_QUERYSTATE = (byte)0xA4;

    public final static byte COMMAND_MODE_SINGLELANINTERNAL = (byte)0x60;
    public final static byte COMMAND_MODE_SINGLELANEXTERNAL = (byte)0x61;
    public final static byte COMMAND_MODE_DOUBLELAN = (byte)0x62;
    public final static byte COMMAND_MODE_WIFIDHCP = (byte)0x63;
    public final static byte COMMAND_MODE_LANANDWIFI = (byte)0x64;
    public final static byte COMMAND_MODE_QUERY = (byte)0x65;
    public final static byte COMMAND_MODE_UPDATEBLE = (byte)0x6F;   //模式切换后，更新蓝牙状态

    public final static String BOXVERSION_GETINFO = "BOXVERSION_GETINFO";
    public final static String BOXAPP_RECEIVE = "BOXAPP_RECEIVE";
    public final static String BOXAPP_UPDATE = "BOXAPP_UPDATE";
    public final static String WORKMODE_SETTING = "WORKMODE_SETTING";
    public final static String SYSTEMTIME_UPDATE = "SYSTEMTIME_UPDATE";
    public final static String TIMERSCHEDULESTART = "TIMERSCHEDULESTART";
    public final static String TIMERSCHEDULEDELETE = "TIMERSCHEDULEDELETE";
    public final static String TIMERSCHEDULESEARCH = "TIMERSCHEDULESEARCH";

    public final static String OTA_UPDATE = "OTA_UPDATE";

    public final static String SETACCESS_DHCP = "SETACCESS_DHCP";
    public final static String SETACCESS_PPPOE = "SETACCESS_PPPOE";
    public final static String SETACCESS_STATIC = "SETACCESS_STATIC";

    public final static String SPEEDTEST_HTTPDOWNLOAD = "SPEEDTEST_HTTPDOWNLOAD";
    public final static String SPEEDTEST_HTTPUPLOAD = "SPEEDTEST_HTTPUPLOAD";
    public final static String SPEEDTEST_TCPSPEEDTEST = "SPEEDTEST_TCPSPEEDTEST";
    public final static String SPEEDTEST_HXBOX  = "SPEEDTEST_HXBOX";
    public final static String SPEEDTEST_GD10000  = "SPEEDTEST_GD10000";
    public final static String SPEEDTEST_HUNAN10000 = "SPEEDTEST_HUNAN10000";


    public final static String SPEEDTEST_FTPDOWNLOAD = "SPEEDTEST_FTPDOWNLOAD";
    public final static String SPEEDTEST_FTPUPLOAD = "SPEEDTEST_FTPUPLOAD";

    public final static String NETTOOLS_PING = "NETTOOLS_PING";
    public final static String NETTOOLS_TRACEROUTE = "NETTOOLS_TRACEROUTE";
    public final static String NETTOOLS_PINGANDTRACEROUTE = "NETTOOLS_PINGANDTRACEROUTE";
    public final static String NETTOOLS_REPING = "NETTOOLS_REPING";

    public final static String IPERF_START = "IPERF_START";
    public final static String TCPDUMP_START = "TCPDUMP_START";
    public final static String TCPDUMP_STOP = "TCPDUMP_STOP";

    public final static String ZHEJIANG10086_SPEEDTEST = "ZHEJIANG10086_SPEEDTEST";
    public final static String ZHEJIANG10086_REPORTRESULT = "ZHEJIANG10086_REPORTRESULT";

    public final static String GUANGXI10000_SPEEDTEST = "GUANGXI10000_SPEEDTEST";
    public final static String JIANGSU10000_SPEEDTEST = "JIANGSU10000_SPEEDTEST";

    /* 2022.06.10去除
    public final static String IPTV_START = "IPTV_START";
    public final static String IPTV_STOP = "IPTV_STOP";
     */

    //add by hzj at 2018.12.3
    public final static String WEBTEST = "WEBTEST";
    public final static String DNSTEST = "DNSTEST";
    //add by hzj at 2018.12.13
    public final static String QUERYMEMORY = "QUERYMEMORY";
    public final static String QUERYPACKAGEFILE  = "QUERYPACKAGEFILE";
    public final static String QUERYLOGFILE = "QUERYLOGFILE";
    public final static String DELETEFILE = "DELETEFILE";
    public final static String QUERYLOGFILESIZE = "QUERYLOGFILESIZE";
    public final static String EXPORTLOG  = "EXPORTLOG";
    public final static String STOPEXPORTLOG = "STOPEXPORTLOG";

    public final static String WIFI_SCAN = "WIFI_SCAN";
    public final static String WIFI_CONNECT = "WIFI_CONNECT";
    public final static String WIFI_CLOSE = "WIFI_CLOSE";
    public final static String WIFI_STOPSCAN = "WIFI_STOPSCAN";
    public final static String WIFI_QUERYSTATE = "WIFI_QUERYSTATE";

    public final static String MODE_SINGLELANINTERNAL = "MODE_SINGLELANINTERNAL";
    public final static String MODE_SINGLELANEXTERNAL = "MODE_SINGLELANEXTERNAL";
    public final static String MODE_DOUBLELAN = "MODE_DOUBLELAN";
    public final static String MODE_WIFIDHCP = "MODE_WIFIDHCP";
    public final static String MODE_LANANDWIFI = "MODE_LANANDWIFI";
    public final static String MODE_QUERY = "MODE_QUERY";
    public final static String MODE_UPDATEBLE = "MODE_UPDATEBLE";   //模式切换后，更新蓝牙状态

    public static String getCommandType(byte command) {
        switch (command) {
            case COMMAND_BOXVERSION_GETINFO:
                return BOXVERSION_GETINFO;
            case COMMAND_BOXAPP_RECEIVE:
                return BOXAPP_RECEIVE;
            case COMMAND_BOXAPP_UPDATE:
                return BOXAPP_UPDATE;
            case COMMAND_WORKMODE_SETTING:
                return WORKMODE_SETTING;
            case COMMAND_SYSTEMTIME_UPDATE:
                return SYSTEMTIME_UPDATE;
            case COMMAND_TIMERSCHEDULE_START:
                return TIMERSCHEDULESTART;
            case COMMAND_TIMERSCHEDULE_DELETE:
                return TIMERSCHEDULEDELETE;
            case COMMAND_TIMERSCHEDULE_SEARCH:
                return TIMERSCHEDULESEARCH;

            case COMMAND_OTA_UPDATE:
                return OTA_UPDATE;

            case COMMAND_SETACCESS_DHCP:
                return SETACCESS_DHCP;
            case COMMAND_SETACCESS_PPPOE:
                return SETACCESS_PPPOE;
            case COMMAND_SETACCESS_STATIC:
                return SETACCESS_STATIC;

            case COMMAND_SPEEDTEST_GD10000:
                return SPEEDTEST_GD10000;

            case COMMAND_SPEEDTEST_HTTPDOWNLOAD:
                return SPEEDTEST_HTTPDOWNLOAD;
            case COMMAND_SPEEDTEST_HTTPUPLOAD:
                return SPEEDTEST_HTTPUPLOAD;
            case COMMAND_SPEEDTEST_HXBOX:
                return SPEEDTEST_HXBOX;
            case COMMAND_SPEEDTEST_TCPSPEEDTEST:
                return SPEEDTEST_TCPSPEEDTEST;
            case COMMAND_SPEEDTEST_HUNAN10000:
                return SPEEDTEST_HUNAN10000;
            case COMMAND_SPEEDTEST_FTPDOWNLOAD:
                return SPEEDTEST_FTPDOWNLOAD;
            case COMMAND_SPEEDTEST_FTPUPLOAD:
                return SPEEDTEST_FTPUPLOAD;
            case COMMAND_NETTOOLS_PING:
                return NETTOOLS_PING;
            case COMMAND_NETTOOLS_TRACEROUTE:
                return NETTOOLS_TRACEROUTE;
            case COMMAND_IPERF_START:
                return IPERF_START;
            case COMMAND_NETTOOLS_PINGANDTRACEROUTE:
                return NETTOOLS_PINGANDTRACEROUTE;
            case COMMAND_NETTOOLS_REPING:
                return NETTOOLS_REPING;
            case COMMAND_TCPDUMP_START:
                return TCPDUMP_START;
            case COMMAND_TCPDUMP_STOP:
                return TCPDUMP_STOP;

            case COMMAND_ZHEJIANG10086_SPEEDTEST:
                return ZHEJIANG10086_SPEEDTEST;
            case COMMAND_ZHEJIANG10086_REPORTRESULT:
                return ZHEJIANG10086_REPORTRESULT;
            case COMMAND_GUANGXI10000_SPEEDTEST:
                return GUANGXI10000_SPEEDTEST;
            case COMMAND_JIANGSU10000_SPEEDTEST:
                return JIANGSU10000_SPEEDTEST;
            /* 2022.06.10去除
            case COMMAND_IPTV_START:
                return IPTV_START;
            case COMMAND_IPTV_STOP:
                return IPTV_STOP;
            */
            case COMMAND_WEBTEST:   //add by hzj at 2018.12.3
                return WEBTEST;
            case COMMAND_DNSTEST:
                return DNSTEST;

            case COMMAND_QUERYMEMORY:   //add by hzj at 2018.12.13
                return QUERYMEMORY;
            case COMMAND_QUERYPACKAGEFILE:
                return QUERYPACKAGEFILE;
            case COMMAND_QUERYLOGFILE:
                return QUERYLOGFILE;
            case COMMAND_DELETEFILE:
                return DELETEFILE;

            case COMMAND_QUERYLOGFILESIZE:
                return QUERYLOGFILESIZE;
            case COMMAND_EXPORTLOG:
                return EXPORTLOG;
            case COMMAND_STOPEXPORTLOG:
                return STOPEXPORTLOG;

            case COMMAND_WIFI_SCAN:
                return WIFI_SCAN;
            case COMMAND_WIFI_CONNECT:
                return WIFI_CONNECT;
            case COMMAND_WIFI_CLOSE:
                return WIFI_CLOSE;

            case COMMAND_WIFI_STOPSCAN:
                return WIFI_STOPSCAN;
            case COMMAND_WIFI_QUERYSTATE:
                return WIFI_QUERYSTATE;
            case COMMAND_MODE_SINGLELANINTERNAL:
                return MODE_SINGLELANINTERNAL;
            case COMMAND_MODE_SINGLELANEXTERNAL:
                return MODE_SINGLELANEXTERNAL;
            case COMMAND_MODE_DOUBLELAN:
                return MODE_DOUBLELAN;
            case COMMAND_MODE_WIFIDHCP:
                return MODE_WIFIDHCP;
            case COMMAND_MODE_LANANDWIFI:
                return MODE_LANANDWIFI;
            case COMMAND_MODE_QUERY:
                return MODE_QUERY;
            case COMMAND_MODE_UPDATEBLE:
                return MODE_UPDATEBLE;
            default:
                return "";
        }
    }

    public static byte getCommandByte(String CommandType) {
        switch (CommandType) {
            case BOXVERSION_GETINFO:
                return COMMAND_BOXVERSION_GETINFO;
            case BOXAPP_RECEIVE:
                return COMMAND_BOXAPP_RECEIVE;
            case BOXAPP_UPDATE:
                return COMMAND_BOXAPP_UPDATE;
            case WORKMODE_SETTING:
                return COMMAND_WORKMODE_SETTING;
            case SYSTEMTIME_UPDATE:
                return COMMAND_SYSTEMTIME_UPDATE;
            case TIMERSCHEDULESTART:
                return COMMAND_TIMERSCHEDULE_START;
            case TIMERSCHEDULEDELETE:
                return COMMAND_TIMERSCHEDULE_DELETE;
            case TIMERSCHEDULESEARCH:
                return COMMAND_TIMERSCHEDULE_SEARCH;
            case SETACCESS_DHCP:
                return COMMAND_SETACCESS_DHCP;
            case SETACCESS_PPPOE:
                return COMMAND_SETACCESS_PPPOE;
            case SETACCESS_STATIC:
                return COMMAND_SETACCESS_STATIC;
            case SPEEDTEST_HTTPDOWNLOAD:
                return COMMAND_SPEEDTEST_HTTPDOWNLOAD;
            case SPEEDTEST_HTTPUPLOAD:
                return COMMAND_SPEEDTEST_HTTPUPLOAD;
            case SPEEDTEST_GD10000:
                return COMMAND_SPEEDTEST_GD10000;
            case JIANGSU10000_SPEEDTEST:
                return COMMAND_JIANGSU10000_SPEEDTEST;
            case SPEEDTEST_HXBOX:
                return COMMAND_SPEEDTEST_HXBOX;
            case SPEEDTEST_TCPSPEEDTEST:
                return COMMAND_SPEEDTEST_TCPSPEEDTEST;
            case SPEEDTEST_HUNAN10000:
                return COMMAND_SPEEDTEST_HUNAN10000;
            case SPEEDTEST_FTPDOWNLOAD:
                return COMMAND_SPEEDTEST_FTPDOWNLOAD;
            case SPEEDTEST_FTPUPLOAD:
                return COMMAND_SPEEDTEST_FTPUPLOAD;
            case NETTOOLS_PING:
                return COMMAND_NETTOOLS_PING;
            case NETTOOLS_TRACEROUTE:
                return COMMAND_NETTOOLS_TRACEROUTE;
            case NETTOOLS_PINGANDTRACEROUTE:
                return COMMAND_NETTOOLS_PINGANDTRACEROUTE;
            case NETTOOLS_REPING:
                return COMMAND_NETTOOLS_REPING;
            case IPERF_START:
                return COMMAND_IPERF_START;
            case TCPDUMP_START:
                return COMMAND_TCPDUMP_START;
            case TCPDUMP_STOP:
                return COMMAND_TCPDUMP_STOP;
            case ZHEJIANG10086_SPEEDTEST:
                return COMMAND_ZHEJIANG10086_SPEEDTEST;
            case ZHEJIANG10086_REPORTRESULT:
                return COMMAND_ZHEJIANG10086_REPORTRESULT;
            case GUANGXI10000_SPEEDTEST:
                return COMMAND_GUANGXI10000_SPEEDTEST;

        /*  2022.06.10去除
            else if(CommandType.equals(IPTV_START)) {
            return COMMAND_IPTV_START;
        } else if(CommandType.equals(IPTV_STOP)) {
            return COMMAND_IPTV_STOP;
        } */
            case OTA_UPDATE:
                return COMMAND_OTA_UPDATE;
            case WEBTEST:         //add by hzj at 2018.12.3
                return COMMAND_WEBTEST;
            case DNSTEST:
                return COMMAND_DNSTEST;
            case QUERYMEMORY:       //add by hzj at 2018.12.13
                return COMMAND_QUERYMEMORY;
            case QUERYPACKAGEFILE:
                return COMMAND_QUERYPACKAGEFILE;
            case QUERYLOGFILE:
                return COMMAND_QUERYLOGFILE;
            case DELETEFILE:
                return COMMAND_DELETEFILE;
            case QUERYLOGFILESIZE:
                return COMMAND_QUERYLOGFILESIZE;
            case EXPORTLOG:
                return COMMAND_EXPORTLOG;
            case STOPEXPORTLOG:
                return COMMAND_STOPEXPORTLOG;
            case WIFI_SCAN:
                return COMMAND_WIFI_SCAN;
            case WIFI_CONNECT:
                return COMMAND_WIFI_CONNECT;
            case WIFI_CLOSE:
                return COMMAND_WIFI_CLOSE;
            case WIFI_STOPSCAN:
                return COMMAND_WIFI_STOPSCAN;
            case WIFI_QUERYSTATE:
                return COMMAND_WIFI_QUERYSTATE;
            case MODE_SINGLELANINTERNAL:
                return COMMAND_MODE_SINGLELANINTERNAL;
            case MODE_SINGLELANEXTERNAL:
                return COMMAND_MODE_SINGLELANEXTERNAL;
            case MODE_DOUBLELAN:
                return COMMAND_MODE_DOUBLELAN;
            case MODE_WIFIDHCP:
                return COMMAND_MODE_WIFIDHCP;
            case MODE_LANANDWIFI:
                return COMMAND_MODE_LANANDWIFI;
            case MODE_QUERY:
                return COMMAND_MODE_QUERY;
            case MODE_UPDATEBLE:
                return COMMAND_MODE_UPDATEBLE;
            default:
                return 0x00;
        }
    }
}
