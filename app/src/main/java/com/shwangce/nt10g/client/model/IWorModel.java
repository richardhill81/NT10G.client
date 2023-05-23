package com.shwangce.nt10g.client.model;

import com.shwangce.nt10g.client.bean.WorkEventBean;
import com.shwangce.nt10g.client.speedtest.SpeedTestKind;

import rx.Observable;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public interface IWorModel {
    Observable<WorkEventBean> getWorkModelObservable();
    void startDiscoveryBox();
    void stopDiscoveryBox();
    void connectBox(String deviceName);
    void setAccess_DHCP();
    void setAccess_STATIC(String ipaddress,String netmask,String gateway,String dns);
    void setAccess_PPPOE(String username,String password);
    void disconnectBox();
    boolean getConnectState();
    void close();

    void startTestSpeed(SpeedTestKind speedTestKind, String info);
    void stopTestSpeed();

    void startTradeoffTest(String serverhost,int testtime,int testparallel );
    void stopTradeoffTest();
}
