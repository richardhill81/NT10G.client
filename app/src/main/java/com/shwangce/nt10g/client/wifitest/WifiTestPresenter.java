package com.shwangce.nt10g.client.wifitest;

import com.shwangce.nt10g.client.library.ControlFrame.CommandValue;
import com.shwangce.nt10g.client.main.MainPresenter;
import com.shwangce.nt10g.client.main.MainPresenterListener;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;

import java.util.Timer;
import java.util.TimerTask;

public class WifiTestPresenter implements WifiTestContract.Presenter{
    private final WifiTestContract.View mView;
    private final MainPresenter mainPresenter;
    private final MainPresenterListener.WifiTestListener wifiTestListener = new MainPresenterListener.WifiTestListener() {
        @Override
        public void onStartScan(boolean isSuccess, String failReason) {
            Log.d("WifiTestPresenter","onStartScan, isSuccess = " + isSuccess + ", failReason is " + failReason);
            if(isSuccess)
                mView.doStartScanSuccess();
            else {
                isScaning = false;
                stopTimerTask(scanTimerTask);
                mView.doStartScanFail(failReason);
            }
        }

        @Override
        public void onFindAp(WifiBean wifiBean) {
            Log.d("WifiTestPresenter","onFindAp: " + wifiBean.toString());
            if(wifiBean.getEssid().length() > 0) {
                mView.doFindAP(wifiBean);
            }
        }

        @Override
        public void onScanFinish(boolean isSuccess, String failReason) {
            Log.d("WifiTestPresenter","onScanFinish,  isSuccess = " + isSuccess + ", failReason is " + failReason);
            isScaning = false;
            stopTimerTask(scanTimerTask);
            if(isSuccess)
                mView.doScanFinished();
            else
                mView.doScanFail(failReason);
        }

        @Override
        public void onStopScan(boolean isSuccess, String failReason) {
            Log.d("WifiTestPresenter","onStopScan,  isSuccess = " + isSuccess + ", failReason is " + failReason);
            stopTimerTask(stopScanTimerTask);
            if(isSuccess) {
                isScaning = false;
                mView.doStopScanSuccess();
            }
            else
                mView.doStopScanFail(failReason);
        }

        @Override
        public void onConnect(boolean isSuccess, String failReason) {
            stopTimerTask(connectTimerTask);
            if(isSuccess) {
                mView.doConnectSuccess();
                mainPresenter.doShowAPInfo(connectingAP);
                queryState();
            } else {
                connectingAP = null;
                mView.doConnectFail(failReason);
            }
        }

        @Override
        public void onQueryState(boolean isSuccess, WifiBean bean) {
            stopTimerTask(queryStateTimerTask);
            if(isSuccess) {
                mView.doQueryStateSuccess();
                if(connectingAP != null) {
                    connectingAP.set_rxBandWidth(bean.get_rxBandWidth());
                    connectingAP.set_txBandWidth(bean.get_txBandWidth());
                    mainPresenter.doShowAPInfo(connectingAP);
                } else {
                    Log.w("onQueryState","connectingAP is null!!!");
                }
            } else
                mView.doQueryStateFail("");
            mainPresenter.doAPConnected(connectingAP);
            /*
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    queryState();
                }
            },10 * 1000);
            */
        }

        /*
                @Override
                public void getWiFiScanResult(WifiBean bean) {
                    mView.doFindWifiBean(bean);
                }

                @Override
                public void scanFinished() {
                    isScaning = false;
                    if(scanTimerTask != null)
                        scanTimerTask.cancel();
                    timer.purge();
                    mView.doScanFinished();
                }

                @Override
                public void connectSuccess() {
                    if(connectTimerTask != null)
                        connectTimerTask.cancel();
                    timer.purge();
                    mView.doConnectSuccess();
                }


                @Override
                public void closeSuccess() {
                    mView.doCloseSuccess();
                }
        */
        @Override
        public void getError(String errString) {
            mView.doGetError(errString);
        }
    };

    public WifiTestPresenter(WifiTestContract.View view, final MainPresenter mainPresenter) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mainPresenter = mainPresenter;
    }

    private final Timer timer = new Timer();
    private TimerTask scanTimerTask = null;
    private TimerTask connectTimerTask = null;
    private TimerTask stopScanTimerTask = null;
    private TimerTask queryStateTimerTask = null;

    private boolean isScaning = false;
    private WifiBean connectingAP = null;

    @Override
    public void start() {
        mainPresenter.setWifiTestListener(wifiTestListener);
    }

    @Override
    public void stop() {
        mainPresenter.removeWifiTestListener();
    }

    @Override
    public void startScan() {
        if(isScaning)
            return;
        isScaning = true;
        //mView.showScanningDialog();
        mainPresenter.doSendCommand(CommandValue.WIFI_SCAN,"");
        if(scanTimerTask != null) scanTimerTask.cancel();
        scanTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.w("WifiTestPresenter","Wi-Fi scan Timeout!!");
                isScaning = false;
                scanTimerTask.cancel();
                timer.purge();
                mView.doScanFail("扫描超时");
            }
        };
        timer.schedule(scanTimerTask,120 * 1000);
    }

    @Override
    public void startConnect(WifiBean bean) {
        connectingAP = bean;
        String encryptionTypeString = WifiTestHelper.getEncryptionTypeString(bean.getEncryptionType());
        String connectInfo = bean.getBssid() + "|" + bean.getPassword() + "|" + encryptionTypeString;
        mainPresenter.doSendCommand(CommandValue.WIFI_CONNECT,connectInfo);
        if(connectTimerTask != null) connectTimerTask.cancel();
        connectTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.w("WifiTestPresenter","Wi-Fi connect Timeout!!");
                connectTimerTask.cancel();
                timer.purge();
                mView.doConnectFail("连接超时");
            }
        };
        timer.schedule(connectTimerTask,30 * 1000);
    }

    @Override
    public void stopScan() {
        mainPresenter.doSendCommand(CommandValue.WIFI_STOPSCAN,"");
        if(stopScanTimerTask != null) stopScanTimerTask.cancel();
        stopScanTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.w("WifiTestPresenter","Wi-Fi stopScan Timeout!!");
                stopScanTimerTask.cancel();
                timer.purge();
                mView.doStopScanFail("停止扫描超时");
            }
        };
        timer.schedule(stopScanTimerTask,5 * 1000);
    }

    @Override
    public void queryState() {
        mainPresenter.doSendCommand(CommandValue.WIFI_QUERYSTATE,"");
        if(queryStateTimerTask != null) queryStateTimerTask.cancel();
        queryStateTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.w("WifiTestPresenter","Wi-Fi queryState Timeout!!");
                queryStateTimerTask.cancel();
                timer.purge();
                mView.doQueryStateFail("查询超时");
                mainPresenter.doAPConnected(connectingAP);
            }
        };
        timer.schedule(queryStateTimerTask,10 * 1000);
    }

    @Override
    public boolean isScanning() {
        return isScaning;
    }

    @Override
    public WifiBean getHistoryApByBssid(String Bssid) {
        for(WifiBean historyAp : ProjectUtil.historyApArray) {
            if(historyAp.getBssid().equals(Bssid)) {
                return historyAp;
            }
        }
        return null;
    }



    private void stopTimerTask(TimerTask timerTask) {
        if(timerTask != null)
            timerTask.cancel();
    }
}
