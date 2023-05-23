package com.shwangce.nt10g.client.library.ControlFrame;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class ResultValue {
    public final static byte RESULT_BOXAPP_GETINFO = (byte)0xE0;
    public final static byte RESULT_BOXAPP_RECEIVESUCCESS = (byte)0xEA;
    public final static byte RESULT_BOXAPP_RECEIVEFAIL = (byte)0xEB;

    public final static byte RESULT_WORKMODESETTING_RESULT = (byte)0xEE;

    public final static byte RESULT_OTA_CANUPDATE = (byte)0xD1;
    public final static byte RESULT_OTA_STATE = (byte)0xD2;
    public final static byte RESULT_OTA_UPDATEBEGIN = (byte)0xD3;
    public final static byte RESULT_OTA_UPDATEFAIL = (byte)0xD4;
    public final static byte RESULT_BOXAPP_CANUPDATE = (byte)0xD5;
    public final static byte RESULT_BOX_UPDATESTATE = (byte)0xD6;
    public final static byte RESULT_BOXAPP_UPDATESTART = (byte)0xD7;
    public final static byte RESULT_BOXAPP_UPDATEFAIL = (byte)0xD8;


    public final static byte RESULT_SETACCESS_SUCCESS = 0x01;
    public final static byte RESULT_SETACCESS_FAIL = 0x02;
    public final static byte RESULT_ETHERNET_LINKSTATE = 0x03;
    public final static byte RESULT_SPEEDTEST_STATECHANGE = 0x10;
    public final static byte RESULT_SPEEDTEST_TCPSPEEDTEST_CLIENTIP = 0x11;
    public final static byte RESULT_SPEEDTEST_TCPSPEEDTEST_SERVERIP = 0x12;
    public final static byte RESULT_SPEEDTEST_SPEED_DOWNLOADING = 0x13;
    public final static byte RESULT_SPEEDTEST_SPEED_UPLOADING = 0x14;
    public final static byte RESULT_SPEEDTEST_SPEED_DOWNLOADED = 0x15;
    public final static byte RESULT_SPEEDTEST_SPEED_UPLOADED = 0x16;
    public final static byte RESULT_SPEEDTEST_USERINFO = 0x17;
    public final static byte RESULT_SPEEDTEST_SUCCESS = 0x18;
    public final static byte RESULT_SPEEDTEST_FAIL= 0x19;

    public final static byte RESULT_NETTOOLS_INFO = 0x20;
    public final static byte RESULT_NETTOOLS_COMPLETE = 0x21;
    public final static byte RESULT_NETTOOLS_FAIL = 0x22;
    public final static byte RESULT_NETTOOLS_TRANCEROUTE_NANJING = 0x23;
    public final static byte RESULT_NETTOOLS_PING_NANJING = 0x24;
    public final static byte RESULT_NETTOOLS_INFO_REPING_NANJING = 0x25;
    public final static byte RESULT_TIMERSCHEDULE_SEARCHANDDELETE_SUCCESS = (byte)0x26;
    public final static byte RESULT_TIMERSCHEDULESUCCESS = (byte)0x27;
    public final static byte RESULT_TIMERSCHEDULE_GETFILEPATH_FAIL = (byte)0x28;

    public final static byte RESULT_GETIP_FAIL = (byte)0x29;       //域名转换IP失败

    public final static byte RESULT_IPERF_DOWNLOADING = 0x2A;
    public final static byte RESULT_IPERF_DOWNLOADED = 0x2B;
    public final static byte RESULT_IPERF_UPLOADING = 0x2C;
    public final static byte RESULT_IPERF_UPLOADED = 0x2D;
    public final static byte RESULT_IPERF_ERROR = 0x2E;
    public final static byte RESULT_IPERF_COMPLETE = 0x2F;

    public final static byte RESULT_TCPDUMP_STARTED = 0x3A;
    public final static byte RESULT_TCPDUMP_STOPED = 0x3B;
    public final static byte RESULT_TCPDUMP_ERROR = 0x3C;

    public final static byte RESULT_ZHEJIANG10086_GETAUTHRESPONSE = 0x41;
    public final static byte RESULT_ZHEJIANG10086_DOWNLOADED = 0x42;
    public final static byte RESULT_ZHEJIANG10086_REPORTED = 0x43;

    public final static byte RESULT_GUANGXI10000_MESSAGE = 0x51;
    public final static byte RESULT_GUANGXI10000_COMPLETE = 0x52;

    /* 2022.06.10去除
    public final static byte RESULT_IPTVSTART_SUCCESS = 0x60;
    public final static byte RESULT_IPTVSTART_FAIL = 0x6f;
    public final static byte RESULT_IPTV_ETHERNETINFO = 0x61;
    public final static byte RESULT_IPTV_VLANINFO = 0x62;
    public final static byte RESULT_IPTV_IPV4INFO = 0x63;
    public final static byte RESULT_IPTV_UDPINFO = 0x64;
    public final static byte RESULT_IPTV_QUALITYINFO = 0x65;
    */

    //add by hzj at 2018.12.3  网站测试返回结果标志
    public final static byte RESULT_WEBTEST_RESOLVE_ERROR = 0x70;
    public final static byte RESULT_WEBTEST_RESOLVE_SUCCESS = 0x71;
    public final static byte RESULT_WEBTEST_PING_ERROR = 0x72;
    public final static byte RESULT_WEBTEST_PING_INFO = 0x73;
    public final static byte RESULT_WEBTEST_PING_SUCCESS = 0x74;
    public final static byte RESULT_WEBTEST_TRACEROUTE_ERROR = 0x75;
    public final static byte RESULT_WEBTEST_TRACEROUTE_INFO = 0x76;
    public final static byte RESULT_WEBTEST_TRACEROUTE_SUCCESS = 0x77;
    public final static byte RESULT_WEBTEST_DELAY_ERROR = 0x78;
    public final static byte RESULT_WEBTEST_DELAY_SUCCESS = 0x79;
    public final static byte RESULT_WEBTEST_OVER = 0x7A;

    public final static byte RESULT_DNSTEST_ERROR = 0x7B;
    public final static byte RESULT_DNSTEST_INFO = 0x7C;
    public final static byte RESULT_DNSTEST_SUCCESS = 0x7D;

    public final static byte RESULT_QUERYMEMORY_PROCESS = (byte)0x80;
    public final static byte RESULT_QUERYMEMORY_ERROR = (byte)0x81;
    public final static byte RESULT_QUERYMEMORY_SUCCESS = (byte)0x82;

    public final static byte RESULT_QUERYPACKAGEFILE_PROCESS = (byte)0x83;
    public final static byte RESULT_QUERYPACKAGEFILE_ERROR = (byte)0x84;
    public final static byte RESULT_QUERYPACKAGEFILE_SUCCESS = (byte)0x85;

    public final static byte RESULT_QUERYLOGFILE_PROCESS = (byte)0x86;
    public final static byte RESULT_QUERYLOGFILE_ERROR = (byte)0x87;
    public final static byte RESULT_QUERYLOGFILE_SUCCESS = (byte)0x88;

    public final static byte RESULT_DELETEFILE_PROCESS = (byte)0x89;
    public final static byte RESULT_DELETEFILE_ERROR = (byte)0x8A;
    public final static byte RESULT_DELETEFILE_SUCCESS = (byte)0x8B;

    public final static byte RESULT_QUERYLOGFILESIZE = (byte)0x8C;
    public final static byte RESULT_EXPORTLOGFILE = (byte)0x8D;
    public final static byte RESULT_EXPORTLOGFILEERROR = (byte)0x8E;
    public final static byte RESULT_EXPORTLOGFILEComplete = (byte)0x8F;

    public final static byte RESULT_WIFI_STARTSCAN = (byte)0xA0;
    public final static byte RESULT_WIFI_FINDAP = (byte)0xA8;
    public final static byte RESULT_WIFI_SCANFINISH = (byte)0xA9;
    public final static byte RESULT_WIFI_CONNECT = (byte)0xA1;
    public final static byte RESULT_WIFI_STOPSCAN = (byte)0xA3;
    public final static byte RESULT_WIFI_QUERYSTATE = (byte)0xA4;
    public final static byte RESULT_WIFI_ERROR = (byte)0xAF;

    public final static byte RESULT_MODE_SINGLELANINTERNAL = (byte)0x60;
    public final static byte RESULT_MODE_SINGLELANEXTERNAL = (byte)0x61;
    public final static byte RESULT_MODE_DOUBLELAN = (byte)0x62;
    public final static byte RESULT_MODE_WIFIDHCP = (byte)0x63;
    public final static byte RESULT_MODE_LANANDWIFI = (byte)0x64;
    public final static byte RESULT_MODE_QUERY = (byte)0x65;
    public final static byte RESULT_MODE_UPDATEBLE = (byte)0x6F;

    public static ResultKind getResultType(byte command) {
        switch (command) {
            case RESULT_BOXAPP_GETINFO:
                return ResultKind.BOXAPP_VERSIONINFO;
            case RESULT_BOXAPP_RECEIVESUCCESS:
                return ResultKind.BOXAPP_RECEIVESUCCESS;
            case RESULT_BOXAPP_RECEIVEFAIL:
                return ResultKind.BOXAPP_RECEIVEFAIL;

            case RESULT_WORKMODESETTING_RESULT:
                return ResultKind.WORKMODESETTING_RESULT;

            case RESULT_SETACCESS_SUCCESS:
                return ResultKind.SETACCESS_SUCCESS;
            case RESULT_SETACCESS_FAIL:
                return ResultKind.SETACCESS_FAIL;
            case RESULT_ETHERNET_LINKSTATE:
                return ResultKind.ETHERNET_LINKSTATE;
            case RESULT_SPEEDTEST_STATECHANGE:
                return ResultKind.SPEEDTEST_STATECHANGE;
            case RESULT_SPEEDTEST_TCPSPEEDTEST_CLIENTIP:
                return ResultKind.SPEEDTEST_TCPSPEEDTEST_CLIENTIP;
            case RESULT_SPEEDTEST_TCPSPEEDTEST_SERVERIP:
                return ResultKind.SPEEDTEST_TCPSPEEDTEST_SERVERIP;
            case RESULT_SPEEDTEST_SPEED_DOWNLOADING:
                return ResultKind.SPEEDTEST_SPEED_DOWNLOADING;
            case RESULT_SPEEDTEST_SPEED_UPLOADING:
                return ResultKind.SPEEDTEST_SPEED_UPLOADING;
            case RESULT_SPEEDTEST_SPEED_DOWNLOADED:
                return ResultKind.SPEEDTEST_SPEED_DOWNLOADED;
            case RESULT_SPEEDTEST_SPEED_UPLOADED:
                return ResultKind.SPEEDTEST_SPEED_UPLOADED;
            case RESULT_SPEEDTEST_USERINFO:
                return ResultKind.SPEEDTEST_USERINFO;
            case RESULT_SPEEDTEST_SUCCESS:
                return ResultKind.SPEEDTEST_SUCCESS;
            case RESULT_SPEEDTEST_FAIL:
                return ResultKind.SPEEDTEST_FAIL;

            case RESULT_NETTOOLS_INFO:
                return ResultKind.NETTOOLS_INFO;
            case RESULT_NETTOOLS_COMPLETE:
                return ResultKind.NETTOOLS_COMPLETE;
            case RESULT_NETTOOLS_FAIL:
                return ResultKind.NETTOOLS_FAIL;
            case RESULT_NETTOOLS_TRANCEROUTE_NANJING:
                return ResultKind.NETTOOLS_INFO_NANJING;
            case RESULT_NETTOOLS_PING_NANJING:
                return ResultKind.NETTOOLS_COMPLETE_NANJING;
            case RESULT_NETTOOLS_INFO_REPING_NANJING:
                return ResultKind.NETTOOLS_INFO_PING_NANJING;
            case RESULT_GETIP_FAIL:
                return ResultKind.GETIP_FAIL;
            case RESULT_IPERF_DOWNLOADING:
                return ResultKind.IPERF_DOWNLOADING;
            case RESULT_IPERF_DOWNLOADED:
                return ResultKind.IPERF_DOWNLOADED;
            case RESULT_IPERF_UPLOADING:
                return ResultKind.IPERF_UPLOADING;
            case RESULT_IPERF_UPLOADED:
                return ResultKind.IPERF_UPLOADED;
            case RESULT_IPERF_ERROR:
                return ResultKind.IPERF_ERROR;
            case RESULT_IPERF_COMPLETE:
                return ResultKind.IPERF_COMPLETE;

            case RESULT_TCPDUMP_STARTED:
                return ResultKind.TCPDUMP_STARTED;
            case RESULT_TCPDUMP_STOPED:
                return ResultKind.TCPDUMP_STOPED;
            case RESULT_TCPDUMP_ERROR:
                return ResultKind.TCPDUMP_ERROR;

            case RESULT_ZHEJIANG10086_GETAUTHRESPONSE:
                return ResultKind.ZHEJIANG10086_GETAUTHRESPONSE;

            case RESULT_ZHEJIANG10086_DOWNLOADED:
                return ResultKind.ZHEJIANG10086_DOWNLOADED;

            case RESULT_ZHEJIANG10086_REPORTED:
                return ResultKind.ZHEJIANG10086_REPORTED;

            case RESULT_GUANGXI10000_MESSAGE:
                return ResultKind.GUANGXI10000_MESSAGE;

            case RESULT_GUANGXI10000_COMPLETE:
                return ResultKind.GUANGXI10000_COMPLETE;
            /* 2022.06.10去除
            case RESULT_IPTVSTART_SUCCESS:
                return ResultKind.IPTVSTART_SUCCESS;

            case RESULT_IPTVSTART_FAIL:
                return ResultKind.IPTVSTART_FAIL;

            case RESULT_IPTV_ETHERNETINFO:
                return ResultKind.IPTV_ETHERNETINFO;

            case RESULT_IPTV_VLANINFO:
                return ResultKind.IPTV_VLANINFO;

            case RESULT_IPTV_IPV4INFO:
                return ResultKind.IPTV_IPV4INFO;

            case RESULT_IPTV_UDPINFO:
                return ResultKind.IPTV_UDPINFO;

            case RESULT_IPTV_QUALITYINFO:
                return ResultKind.IPTV_QUALITYINFO;

            case RESULT_OTA_CANUPDATE:
                return ResultKind.OTA_CANUPDATE;

             */
            case RESULT_OTA_STATE:
                return ResultKind.OTA_STATE;
            case RESULT_OTA_UPDATEBEGIN:
                return ResultKind.OTA_UPDATEBEGIN;

            case RESULT_OTA_UPDATEFAIL:
                return ResultKind.OTA_UPDATEFAIL;
            case RESULT_BOXAPP_CANUPDATE:
                return ResultKind.BOXAPP_CANUPDATE;
            case RESULT_BOX_UPDATESTATE:
                return ResultKind.BOX_UPDATESTATE;
            case RESULT_BOXAPP_UPDATESTART:
                return ResultKind.BOXAPP_UPDATESTART;
            case RESULT_BOXAPP_UPDATEFAIL:
                return ResultKind.BOXAPP_UPDATEFAIL;
            case RESULT_TIMERSCHEDULESUCCESS:
                return ResultKind.TIMERSCHEDULESUCCESS;
            case RESULT_TIMERSCHEDULE_SEARCHANDDELETE_SUCCESS:
                return ResultKind.TIMERSCHEDULE_SEARCHANDDELETE_SUCCESS;
            case RESULT_TIMERSCHEDULE_GETFILEPATH_FAIL:
                return ResultKind.TIMERSCHEDULE_GETFILEPATH_FAIL;

            //add by hzj at 2018.12.3
            case RESULT_WEBTEST_RESOLVE_ERROR:
                return ResultKind.WEBTEST_RESOLVE_ERROR;
            case RESULT_WEBTEST_RESOLVE_SUCCESS:
                return ResultKind.WEBTEST_RESOLVE_SUCCESS;

            case RESULT_WEBTEST_PING_ERROR:
                return ResultKind.WEBTEST_PING_ERROR;
            case RESULT_WEBTEST_PING_INFO:
                return ResultKind.WEBTEST_PING_INFO;
            case RESULT_WEBTEST_PING_SUCCESS:
                return ResultKind.WEBTEST_PING_SUCCESS;

            case RESULT_WEBTEST_TRACEROUTE_ERROR:
                return ResultKind.WEBTEST_TRACEROUTE_ERROR;
            case RESULT_WEBTEST_TRACEROUTE_INFO:
                return ResultKind.WEBTEST_TRACEROUTE_INFO;
            case RESULT_WEBTEST_TRACEROUTE_SUCCESS:
                return ResultKind.WEBTEST_TRACEROUTE_SUCCESS;

            case RESULT_WEBTEST_DELAY_ERROR:
                return ResultKind.WEBTEST_DELAY_ERROR;
            case RESULT_WEBTEST_DELAY_SUCCESS:
                return ResultKind.WEBTEST_DELAY_SUCCESS;

            case RESULT_WEBTEST_OVER:
                return ResultKind.WEBTEST_OVER;

            case RESULT_DNSTEST_ERROR:
                return ResultKind.DNSTEST_ERROR;
            case RESULT_DNSTEST_INFO:
                return ResultKind.DNSTEST_INFO;
            case RESULT_DNSTEST_SUCCESS:
                return ResultKind.DNSTEST_SUCCESS;
            //add by hzj at 2018.12.13
            case RESULT_QUERYMEMORY_PROCESS:
                return ResultKind.QUERYMEMORY_PROCESS;
            case RESULT_QUERYMEMORY_ERROR:
                return ResultKind.QUERYMEMORY_ERROR;
            case RESULT_QUERYMEMORY_SUCCESS:
                return ResultKind.QUERYMEMORY_SUCCESS;

            case RESULT_QUERYPACKAGEFILE_PROCESS:
                return ResultKind.QUERYPACKAGEFILE_PROCESS;
            case RESULT_QUERYPACKAGEFILE_ERROR:
                return ResultKind.QUERYPACKAGEFILE_ERROR;
            case RESULT_QUERYPACKAGEFILE_SUCCESS:
                return ResultKind.QUERYPACKAGEFILE_SUCCESS;

            case RESULT_QUERYLOGFILE_PROCESS:
                return ResultKind.QUERYLOGFILE_PROCESS;
            case RESULT_QUERYLOGFILE_ERROR:
                return ResultKind.QUERYLOGFILE_ERROR;
            case RESULT_QUERYLOGFILE_SUCCESS:
                return ResultKind.QUERYLOGFILE_SUCCESS;

            case RESULT_DELETEFILE_PROCESS:
                return ResultKind.DELETEFILE_PROCESS;
            case RESULT_DELETEFILE_ERROR:
                return ResultKind.DELETEFILE_ERROR;
            case RESULT_DELETEFILE_SUCCESS:
                return ResultKind.DELETEFILE_SUCCESS;

            case RESULT_QUERYLOGFILESIZE:
                return ResultKind.QUERYLOGFILESIZE;
            case RESULT_EXPORTLOGFILE:
                return ResultKind.EXPORTLOGFILE;
            case RESULT_EXPORTLOGFILEERROR:
                return ResultKind.EXPORTLOGFILEERROR;
            case RESULT_EXPORTLOGFILEComplete:
                return ResultKind.EXPORTLOGFILEComplete;

            case RESULT_WIFI_STARTSCAN:
                return ResultKind.WIFI_STARTSCAN;
            case RESULT_WIFI_FINDAP:
                return ResultKind.WIFI_FINDAP;
            case RESULT_WIFI_SCANFINISH:
                return ResultKind.WIFI_SCANFINISH;
            case RESULT_WIFI_CONNECT:
                return ResultKind.WIFI_CONNECT;
            case RESULT_WIFI_STOPSCAN:
                return ResultKind.WIFI_STOPSCAN;
            case RESULT_WIFI_QUERYSTATE:
                return ResultKind.WIFI_QUERYSTATE;
            case RESULT_WIFI_ERROR:
                return ResultKind.WIFI_ERROR;
            case RESULT_MODE_SINGLELANINTERNAL:
                return ResultKind.MODE_SINGLELANINTERNAL;
            case RESULT_MODE_SINGLELANEXTERNAL:
                return ResultKind.MODE_SINGLELANEXTERNAL;
            case RESULT_MODE_DOUBLELAN:
                return ResultKind.MODE_DOUBLELAN;
            case RESULT_MODE_WIFIDHCP:
                return ResultKind.MODE_WIFIDHCP;
            case RESULT_MODE_LANANDWIFI:
                return ResultKind.MODE_LANANDWIFI;
            case RESULT_MODE_QUERY:
                return ResultKind.MODE_QUERY;
            case RESULT_MODE_UPDATEBLE:
                return ResultKind.MODE_UPDATEBLE;
            default:
                return ResultKind.Unknown;
        }
    }
}
