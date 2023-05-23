package com.shwangce.nt10g.client.setaccess;

import androidx.annotation.NonNull;

import com.shwangce.nt10g.client.library.ControlFrame.CommandValue;
import com.shwangce.nt10g.client.main.MainPresenter;
import com.shwangce.nt10g.client.main.MainPresenterListener;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class SetAccessPresenter implements SetAccessContract.Presenter {

    private final int MAX_TIMEOUT_VALUE = 45;
    private final SetAccessContract.View mView;
    private final MainPresenter mainPresenter;
    private final MainPresenterListener.AccessSelectListener accessSelectListener = new MainPresenterListener.AccessSelectListener() {
        @Override
        public void onAccessSuccess() {
            stopTimer();
            mView.doAccessSuccess();
        }

        @Override
        public void onAccessFail(String failreason) {
            stopTimer();
            mView.doShowAccessFail(failreason);
        }
    };
    private final Timer timer = new Timer();
    private TimerTask timerTask;
    private long timercount;

    public SetAccessPresenter(@NonNull SetAccessContract.View view,@NonNull MainPresenter mainPresenter) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void start(){
        mainPresenter.setAccessSelectListener(accessSelectListener);
    }

    @Override
    public void stop() {
        mainPresenter.removeAccessSelectListener();
    }

    @Override
    public void doDhcp() {
        mainPresenter.setAccessType(AccessType.ACCESS_DHCP);
        startTimer();
        mainPresenter.doSendCommand(CommandValue.SETACCESS_DHCP,"");
    }

    @Override
    public void doStatic(String ipaddress, String netmask, String gateway, String dns) {
        mainPresenter.setAccessType(AccessType.ACCESS_STATIC);
        String dataString = ipaddress + "|" + netmask + "|" + gateway + "|" + dns;
        startTimer();
        mainPresenter.doSendCommand(CommandValue.SETACCESS_STATIC,dataString);
    }

    @Override
    public void doPPPoe(String username, String password) {
        mainPresenter.setAccessType(AccessType.ACCESS_PPPOE);
        String dataString = username + "|" + password;
        startTimer();
        mainPresenter.doSendCommand(CommandValue.SETACCESS_PPPOE,dataString);
    }

    @Override
    public void doBack() {
        mainPresenter.doBacktoDeviceSelect();
    }

    private void startTimer() {
        if(timerTask != null)
            timerTask.cancel();
        timercount  = 0;
        timerTask = new TimerTask() {
            @Override
            public void run() {
                timercount++;
                if(timercount > MAX_TIMEOUT_VALUE) {
                    mView.doShowAccessFail("网络连接设置超时！");
                    timerTask.cancel();
                    timerTask = null;
                }
            }
        };
        timer.schedule(timerTask,0,1000);
    }

    private void stopTimer() {
        timerTask.cancel();
        timerTask = null;
    }
}
