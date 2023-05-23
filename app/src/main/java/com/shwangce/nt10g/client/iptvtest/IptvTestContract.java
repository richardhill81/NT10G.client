package com.shwangce.nt10g.client.iptvtest;

import com.shwangce.nt10g.client.BasePresenter;
import com.shwangce.nt10g.client.BaseView;
import com.shwangce.nt10g.client.iptvtest.IptvTestResultBean.EthernetInfoBean;
import com.shwangce.nt10g.client.iptvtest.IptvTestResultBean.IPv4InfoBean;
import com.shwangce.nt10g.client.iptvtest.IptvTestResultBean.QualityInfoBean;
import com.shwangce.nt10g.client.iptvtest.IptvTestResultBean.UdpInfoBean;
import com.shwangce.nt10g.client.iptvtest.IptvTestResultBean.VlanInfoBean;
/**
 * Created by Yannik on 2017/11/27.
 */

public interface IptvTestContract {
    interface View extends BaseView<Presenter> {
        void doShowIptvStartSuccess();
        void doShowIptvStartFail(String failString);
        void doShowIptvEthernetInfo(EthernetInfoBean result);
        void doShowIptvVlanInfo(VlanInfoBean result);
        void doShowIptvIPv4Info(IPv4InfoBean result);
        void doShowIptvUdpInfo(UdpInfoBean result);
        void doShowIptvQualityInfo(QualityInfoBean result);
        /*
        void doShowIptvResult1(IptvTestResult1Bean result);
        void doShowIptvResult2(IptvTestResult2Bean result);
        void doShowIptvResult3(IptvTestResult3Bean result);
        void doShowIptvResult4(IptvTestResult4Bean result);
        void doShowIptvResult5(IptvTestResult5Bean result);
        */
    }

    interface Presenter extends BasePresenter {
        void startIptvTest();
        void stopIptvTest();
    }
}
