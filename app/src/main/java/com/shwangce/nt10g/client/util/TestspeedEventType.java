package com.shwangce.nt10g.client.util;

/**
 * Created by Administrator on 2016/9/14 0014.
 */
public enum TestspeedEventType {
    BLUETOOTH_EVENT_OPENED,
    BLUETOOTH_EVENT_FINDNEWDEVICE,
    BLUETOOTH_EVENT_FINDPAIREDDEVICES,
    BLUETOOTH_EVENT_DISCOVERYFINISHED,
    BLUETOOTH_EVENT_CONNECTED,
    BLUETOOTH_EVENT_OPENFAILED,
    BLUETOOTH_EVENT_CONNECTFAILED,
    BLUETOOTH_EVENT_CONNECTLOSS,
    BLUETOOTH_EVENT_READ,

    ETHERNET_EVENT_LINKSTATE,

    SETACCESS_EVENT_SUCCESS,
    SETACCESS_EVENT_FAIL,

    TEST_EVENT_FAIL,
    TEST_EVENT_INFO,
    TEST_EVENT_GETCLIENT,
    TEST_EVENT_GETSERVER,
    TEST_EVENT_DOWNLOADING,
    TEST_EVENT_DOWNLOADED,
    TEST_EVENT_UPLOADING,
    TEST_EVENT_UPLOADED,
    TEST_EVENT_COMPLETE,

    TEST_EVENT_HXBOXRESULT,

    TEST_EVENT_GETUSERINFO_GD10000

}
