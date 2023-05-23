package com.shwangce.nt10g.client.deviceselect;

import androidx.annotation.NonNull;

import com.shwangce.nt10g.client.library.ControlFrame.CommandValue;
import com.shwangce.nt10g.client.library.bluetoothLe.DeviceBean;
import com.shwangce.nt10g.client.main.MainPresenter;
import com.shwangce.nt10g.client.main.MainPresenterListener;
import com.shwangce.nt10g.client.util.ProjectUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public class DeviceSelectPresenter implements DeviceSelectContract.Presenter {

    private final DeviceSelectDialogFragment mDeviceView;
    private final MainPresenter mainPresenter;
    private final int MAX_SCAN_SECONDS = 15;
    private Timer timer;
    private TimerTask timerTask;
    private int timercount = 0;

    public DeviceSelectPresenter(@NonNull DeviceSelectDialogFragment view, @NonNull MainPresenter mainPresenter) {
        mDeviceView = view;
        mDeviceView.setPresenter(this);
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void start() {
        timer = new Timer();
        mainPresenter.setDeviceSelectListener(new MainPresenterListener.DeviceSelectListener() {

            @Override
            public void onFindNewDevice(DeviceBean deviceBean) {
                mDeviceView.doFindNewDevice(deviceBean);
            }

            @Override
            public void onScanfinished() {
                mDeviceView.doScanfinished();
            }

            @Override
            public void onConnectSuccess(DeviceBean deviceBean) {
                mDeviceView.doConnected(deviceBean);
                doGetBoxVersion();
            }

            @Override
            public void onConnectError(String failString) {
                mDeviceView.doConnectError(failString);
            }

            @Override
            public void onGetBoxVersionSuccess(String versionString) {
                if (timer != null) {
                    timerTask.cancel();
                    timerTask = null;
                }
                ProjectUtil.boxInfoBean.setVersion(versionString);
                mDeviceView.doGetVersionSuccess();
                //更新盒子系统时间20180801
                doSendLocalTime();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //doSetBoxWorkMode(ProjectUtil.boxInfoBean.getWorkmode());
                doDeviceSelectComplete();
            }

            @Override
            public void onGetBoxVersionFail(String failString) {
                if (timer != null) {
                    timerTask.cancel();
                    timerTask = null;
                }
                mDeviceView.doGetVersionFail(failString);
            }
/*
            @Override
            public void onWorkModeSetResult(String result) {
                mDeviceView.doSetBoxWorkModeResult(result);
            }*/

            @Override
            public void doSelfStartScan() {
                mDeviceView.doSelfStartScan();
            }//add by hzj 2018-11-15
        });
    }

    @Override
    public void stop() {
        mainPresenter.removeDeviceSelectListener();
    }


    @Override
    public void doConnectDeviceByMac(String deviceMac) {
        mainPresenter.doConnectByDeviceMac(new String[]{deviceMac});
    }

    @Override
    public void doConnectDeviceByName(String deviceName) {
        String m = deviceName.substring(7).toUpperCase();
        ArrayList<String> macList = new ArrayList<>();
        macList.add("7C:EC:79:" + m);
        macList.add("7C:01:0A:" + m);
        macList.add("20:C3:8F:" + m);
        mainPresenter.doConnectByDeviceMac(macList.toArray(new String[macList.size()]));
    }


    @Override
    public void doConnectFindedDevice(DeviceBean deviceBean) {
        mainPresenter.doConnectFindedDevice(deviceBean);
    }

    /*
            @Override
            public void doDeviceItemClicked(String devicename) {
                if(timer != null){
                    if(timerTask != null)
                    timerTask.cancel();
                    timerTask = null;
                }
                mainPresenter.doStopDiscovery();
                if(timer != null) {
                    if(timerTask != null)
                        timerTask.cancel();
                    timercount  = 0;
                    timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            timercount++;
                            if(timercount >= 5) {
                                timerTask.cancel();
                                mDeviceView.doConnectError("设备连接失败，请确认手机蓝牙是否工作正常");
                            }
                        }
                    };
                    timer.schedule(timerTask,0,1000);
                }
                mainPresenter.doConnect(devicename);
            }
        */
    @Override
    public void doBleScan() {
        mainPresenter.doStartDiscovery();
        if (timer != null) {
            if (timerTask != null)
                timerTask.cancel();
            timercount = 0;
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    timercount++;
                    if (timercount > MAX_SCAN_SECONDS) {
                        timerTask.cancel();
                        mainPresenter.doStopDiscovery();
                        mDeviceView.doScanfinished();
                    }
                }
            };
            timer.schedule(timerTask, 0, 1000);
        }
    }



//
//    public boolean checkDeviceIsFound(){
//        List<DeviceBean> deviceList = mDeviceView.getDeviceList();
//        int num = deviceList.size();
//        for(int i = 0; i < num;i++){
//            String name = deviceList.get(i).getDeviceName();
//            if(name.contains("NT201L")){
//                mDeviceView.setDevicePositionInList(i);
//                return true;
//            }
//        }
//        return false;
//    }
    /************/

    @Override
    public void doQuitClicked() {
        mainPresenter.doQuit();
    }

    /*
    @Override
    public void doSetBoxWorkMode(String workmode) {
        mainPresenter.doSendCommand(CommandValue.WORKMODE_SETTING,workmode);
    }
    */

    private void doGetBoxVersion() {
        if (timer != null) {
            if (timerTask != null)
                timerTask.cancel();
            timercount = 0;
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    timercount++;
                    if (timercount >= 10) {
                        timerTask.cancel();
                        mDeviceView.doGetVersionFail("获取设备版本超时");
                        mainPresenter.doDisconnect();
                    }
                }
            };
            timer.schedule(timerTask, 0, 1000);
        }
        mainPresenter.doSendCommand(CommandValue.BOXVERSION_GETINFO, "");
    }

    @Override
    public void doSendLocalTime() {
        Calendar c = Calendar.getInstance();
        long when = c.getTimeInMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd HH mm ss");
        Date date = new Date(when);
        simpleDateFormat.format(date);

        mainPresenter.doSendCommand(CommandValue.SYSTEMTIME_UPDATE, String.valueOf(simpleDateFormat.format(date)));
    }

    private void doDeviceSelectComplete() {
        mDeviceView.doDeviceSelectComplete();
        mainPresenter.doDeviceSelectComplete();
    }
}
