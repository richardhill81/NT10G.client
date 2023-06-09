package com.shwangce.nt10g.client.library.ControlFrame;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

public enum ResultKind {
    Unknown,

    BOXAPP_VERSIONINFO,
    BOXAPP_RECEIVESUCCESS,
    BOXAPP_RECEIVEFAIL,

    WORKMODESETTING_RESULT,

    OTA_CANUPDATE,
    OTA_STATE,
    OTA_UPDATEBEGIN,
    OTA_UPDATEFAIL,
    BOXAPP_CANUPDATE,

    BOX_UPDATESTATE,
    BOXAPP_UPDATESTART,
    BOXAPP_UPDATEFAIL,


    SETACCESS_SUCCESS,
    SETACCESS_FAIL,
    ETHERNET_LINKSTATE,
    SPEEDTEST_STATECHANGE,
    SPEEDTEST_TCPSPEEDTEST_CLIENTIP,
    SPEEDTEST_TCPSPEEDTEST_SERVERIP,
    SPEEDTEST_SPEED_DOWNLOADING,
    SPEEDTEST_SPEED_UPLOADING,
    SPEEDTEST_SPEED_DOWNLOADED,
    SPEEDTEST_SPEED_UPLOADED,
    SPEEDTEST_USERINFO,
    SPEEDTEST_SUCCESS,
    SPEEDTEST_FAIL,

    NETTOOLS_INFO,
    NETTOOLS_COMPLETE,
    NETTOOLS_FAIL,
    NETTOOLS_INFO_NANJING,
    NETTOOLS_COMPLETE_NANJING,
    NETTOOLS_INFO_PING_NANJING,
    TIMERSCHEDULESUCCESS,
    TIMERSCHEDULE_SEARCHANDDELETE_SUCCESS,
    TIMERSCHEDULE_GETFILEPATH_FAIL,
    GETIP_FAIL,

    IPERF_ERROR,
    IPERF_DOWNLOADING,
    IPERF_DOWNLOADED,
    IPERF_UPLOADING,
    IPERF_UPLOADED,
    IPERF_COMPLETE,

    TCPDUMP_STARTED,
    TCPDUMP_STOPED,
    TCPDUMP_ERROR,

    ZHEJIANG10086_GETAUTHRESPONSE,
    ZHEJIANG10086_DOWNLOADED,
    ZHEJIANG10086_REPORTED,

    GUANGXI10000_MESSAGE,
    GUANGXI10000_COMPLETE,

    /* 2022.06.10去除
    IPTVSTART_SUCCESS,  //IPTV启动测试成功
    IPTVSTART_FAIL,     //IPTV启动测试失败
    IPTV_ETHERNETINFO,  //目的MAC,源MAC地址
    IPTV_VLANINFO,      //vlan优先级,vlanId
    IPTV_IPV4INFO,      //TOS,TTL,源IP,目的IP
    IPTV_UDPINFO,       //源端口,目的端口
    IPTV_QUALITYINFO,    //IP包数(个/s),总丢包数(个),当前抖动，最小抖动(ms),最大抖动(ms),当前业务数据传输速率(kbps)，平均速率(kbps)，最大速率(kbps)，最小速率(kbps)

     */

    //add by hzj at 2018.12.3
    WEBTEST_RESOLVE_ERROR,
    WEBTEST_RESOLVE_SUCCESS,

    WEBTEST_PING_ERROR,
    WEBTEST_PING_INFO,
    WEBTEST_PING_SUCCESS,

    WEBTEST_TRACEROUTE_ERROR,
    WEBTEST_TRACEROUTE_INFO,
    WEBTEST_TRACEROUTE_SUCCESS,

    WEBTEST_DELAY_ERROR,
    WEBTEST_DELAY_SUCCESS,

    WEBTEST_OVER,        //网站测试完成。

    DNSTEST_ERROR,
    DNSTEST_INFO,
    DNSTEST_SUCCESS,

    QUERYMEMORY_PROCESS,    //add by hzj in 2018.12.13
    QUERYMEMORY_ERROR,
    QUERYMEMORY_SUCCESS,

    QUERYPACKAGEFILE_PROCESS,
    QUERYPACKAGEFILE_ERROR,
    QUERYPACKAGEFILE_SUCCESS,

    QUERYLOGFILE_PROCESS,
    QUERYLOGFILE_ERROR,
    QUERYLOGFILE_SUCCESS,

    DELETEFILE_PROCESS,
    DELETEFILE_ERROR,
    DELETEFILE_SUCCESS,

    QUERYLOGFILESIZE,
    EXPORTLOGFILE,
    EXPORTLOGFILEComplete,
    EXPORTLOGFILEERROR,

    WIFI_STARTSCAN,
    WIFI_FINDAP,
    WIFI_SCANFINISH,
    WIFI_CONNECT,
    WIFI_STOPSCAN,
    WIFI_QUERYSTATE,
    WIFI_ERROR,

    MODE_SINGLELANINTERNAL,
    MODE_SINGLELANEXTERNAL,
    MODE_DOUBLELAN,
    MODE_WIFIDHCP,
    MODE_LANANDWIFI,
    MODE_QUERY,
    MODE_UPDATEBLE

}
