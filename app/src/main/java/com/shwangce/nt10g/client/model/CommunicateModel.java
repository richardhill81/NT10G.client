package com.shwangce.nt10g.client.model;

import android.content.Context;

import com.shwangce.nt10g.client.library.ControlFrame.ResultBean;
import com.shwangce.nt10g.client.library.bluetoothLe.DeviceBean;
import com.shwangce.nt10g.client.util.DeviceCommunicateControl;
import com.shwangce.nt10g.client.util.Log;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class CommunicateModel {

    private final DeviceCommunicateControl deviceCommunicateControl;
    private final DeviceCommunicateControl.DeviceControllerListener deviceControllerListener =
            new DeviceCommunicateControl.DeviceControllerListener() {
                @Override
                public void onFindNewDevice(DeviceBean deviceBean) {
                    mSubscriber.onNext(new CommunicateEvent(this, CommunicateEvent.EventType.FindNewDevice,deviceBean));
                }

                @Override
                public void onDiscoveryFinished() {
                    mSubscriber.onNext(new CommunicateEvent(this, CommunicateEvent.EventType.DiscoveryFinished,null));
                }

                @Override
                public void onConnectSuccess(DeviceBean deviceBean) {
                    mSubscriber.onNext(new CommunicateEvent(this, CommunicateEvent.EventType.ConnectSuccess,deviceBean));
                }

                @Override
                public void onConnectError(String error) {
                    mSubscriber.onNext(new CommunicateEvent(this, CommunicateEvent.EventType.ConnectError,error));
                }

                @Override
                public void onConnectLoss() {
                    mSubscriber.onNext(new CommunicateEvent(this, CommunicateEvent.EventType.ConnectLoss,null));
                }

                @Override
                public void onResultReceive(ResultBean resultBean) {
                    mSubscriber.onNext(new CommunicateEvent(this, CommunicateEvent.EventType.ResultReceive,resultBean));
                }
            };


    private Subscriber<CommunicateEvent> mSubscriber;
    private Observable<CommunicateEvent> obserable;

    public CommunicateModel(Context context) {
        deviceCommunicateControl = DeviceCommunicateControl.getInstance(context);
        deviceCommunicateControl.addListener(deviceControllerListener);
    }

    public Observable<CommunicateEvent> getResultBeanObservable() {
        obserable = Observable.create(new Observable.OnSubscribe<CommunicateEvent>() {
            @Override
            public void call(Subscriber<? super CommunicateEvent> subscriber) {
                mSubscriber = (Subscriber<CommunicateEvent>) subscriber;
            }
        });
        return obserable;
    }


    public void startDiscovery() {
        deviceCommunicateControl.startDiscovery();
    }

    public void stopDiscovery() {
        deviceCommunicateControl.stopDiscovery();
    }


    public void connect(String[] deviceAddresses) {
        deviceCommunicateControl.connect(deviceAddresses);
    }


    public void connectFindedDevice(DeviceBean deviceBean) {
        deviceCommunicateControl.connectFindedDevice(deviceBean);
    }

    public void sendCommand(String commandType,String detail) {
        Log.d("蓝牙指令",commandType + "   " + detail);
        byte[] data = deviceCommunicateControl.getCommandBytes(commandType,detail);
        deviceCommunicateControl.sendData(data);
    }

    public void sendCommand(String commandType,byte[] detail) {
        byte[] data = deviceCommunicateControl.getCommandBytes(commandType,detail);
        deviceCommunicateControl.sendData(data);
    }

    public void disconnect() {
        deviceCommunicateControl.disconnect();
    }

    public void close() {
        deviceCommunicateControl.removeListener(deviceControllerListener);
        disconnect();
        //deviceCommunicateControl.stopDiscovery();
        deviceCommunicateControl.close();
    }

    public boolean getConnectState() {
        return deviceCommunicateControl.getConnectState();
    }

}
