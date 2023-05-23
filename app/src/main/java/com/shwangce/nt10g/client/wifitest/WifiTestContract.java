package com.shwangce.nt10g.client.wifitest;

import com.shwangce.nt10g.client.BasePresenter;
import com.shwangce.nt10g.client.BaseView;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public interface WifiTestContract {
    interface View extends BaseView<Presenter> {
        void doStartScanSuccess();
        void doStartScanFail(String failReason);
        void doStopScanSuccess();
        void doStopScanFail(String failReason);
        void doFindAP(WifiBean bean);
        void doScanFinished();
        void doScanFail(String failReason);
        void doConnectSuccess();
        void doConnectFail(String failReason);
        void doQueryStateSuccess();
        void doQueryStateFail(String failReason);
        //void showScanningDialog();

        void doCloseSuccess();
        void doGetError(String errString);
        //void doScanTimeOut();
        //void doConnectTimeOut();
    }

    interface Presenter extends BasePresenter {
        void startScan();
        void startConnect(WifiBean bean);
        void stopScan();
        void queryState();
        boolean isScanning();
        WifiBean getHistoryApByBssid(String Bssid);
    }
}
