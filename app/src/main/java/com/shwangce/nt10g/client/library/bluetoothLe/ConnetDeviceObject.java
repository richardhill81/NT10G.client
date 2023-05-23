package com.shwangce.nt10g.client.library.bluetoothLe;

import android.bluetooth.BluetoothGatt;

import androidx.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;

public class ConnetDeviceObject {
    private final String TAG = "ConnetDeviceObject";

    public interface TimerOutCallBack {
        void onTimerOut(ConnetDeviceObject connetDeviceObject);
    }

    private final int BLE_TIMEROUT = 10 * 1000;

    private int tryCount = 0;
    private String connectStep = "";
    private DeviceBean deviceBean;
    private BluetoothGatt gatt = null;
    private TimerTask timerTask = null;
    private Timer timer = new Timer();
    private TimerOutCallBack timerOutCallBack = null;

    private boolean isConnectFail = false;

    public ConnetDeviceObject(DeviceBean deviceBean, BluetoothGatt gatt, TimerOutCallBack timerOutCallBack) {
        this.deviceBean = deviceBean;
        this.gatt = gatt;
        this.timerOutCallBack = timerOutCallBack;
    }

    public DeviceBean getDeviceBean() {
        return deviceBean;
    }

    public void setDeviceBean(DeviceBean deviceBean) {
        this.deviceBean = deviceBean;
    }

    public int getTryCount() {
        return tryCount;
    }

    public void setGatt(BluetoothGatt gatt) {
        this.gatt = gatt;
    }

    public BluetoothGatt getGatt() {
        return gatt;
    }

    public String getConnectStep() {
        return connectStep;
    }

    public void setConnectStep(String connectStep) {
        this.connectStep = connectStep;
    }

    public void setTimerTask(@NonNull TimerTask timerTask) {
        if(this.timerTask != null) this.timerTask.cancel();
        this.timerTask = timerTask;
    }

    public void startTimer() {
        tryCount++;
        if(timerTask != null) timerTask.cancel();
        if(timer == null)
            timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(timerOutCallBack != null)
                    timerOutCallBack.onTimerOut(ConnetDeviceObject.this);
            }
        };
        if(timerTask != null)
            timer.schedule(timerTask,BLE_TIMEROUT);
    }

    public void stopTimer() {
        if(timerTask!=null) {
            //Log.d(TAG,deviceBean.getDeviceMac() + "  stopTimerTask");
            timerTask.cancel();
            timerTask = null;
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    public boolean isConnectFail() {
        return isConnectFail;
    }

    public void setConnectFail() {
        isConnectFail = true;
    }
}
