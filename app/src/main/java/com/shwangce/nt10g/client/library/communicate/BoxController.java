package com.shwangce.nt10g.client.library.communicate;

import android.content.Context;

import androidx.annotation.NonNull;

import com.shwangce.nt10g.client.library.bluetoothLe.DeviceBean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class BoxController implements IBoxController {

    public enum CommunicateType {
        BLE,
    }

    private ArrayList<DeviceBean> deviceBeans = new ArrayList<>();
    private BoxControllerListener myboxControlListener = new BoxControllerListener() {
        @Override
        public void onFindNewDevice(DeviceBean deviceBean) {
            boolean oldDevice =false;
            for(int i=0;i<deviceBeans.size();i++) {
                if(deviceBeans.get(i).equals(deviceBean)) {
                    oldDevice = true;
                    break;
                }
            }
            if(!oldDevice) {
                deviceBeans.add(deviceBean);
                notifyListeners(EventType.FindNewDevice, deviceBean);
            }
        }

        @Override
        public void onDiscoveryFinished() {
            notifyListeners(EventType.DiscoveryFinished,"");
        }

        @Override
        public void onConnectSuccess(DeviceBean deviceBean) {
            notifyListeners(EventType.ConnectSuccess,deviceBean);
        }

        @Override
        public void onConnectError(String error) {
            notifyListeners(EventType.ConnectError,error);
        }

        @Override
        public void onConnectLoss() {
            notifyListeners(EventType.ConnectLoss,"");
        }

        @Override
        public void onDataReceive(byte[] recvdata) {
            notifyListeners(EventType.DataReceive,recvdata);
        }
    };

    private Collection listeners;

    private BoxController_bluetoothLe bluetoothLe = null;

    private CommunicateType communicateType = CommunicateType.BLE;
    private Context context;


    public BoxController(@NonNull Context context, @NonNull BoxControllerListener listener, @NonNull CommunicateType communicateType) {
        this.context = context;
        addBoxControllerListener(listener);
        this.communicateType = communicateType;
        switch (communicateType) {

            case BLE:
                bluetoothLe = new BoxController_bluetoothLe(context,myboxControlListener);
                break;
        }
    }

    public void addBoxControllerListener(BoxControllerListener listener) {
        if(listeners == null) listeners = new HashSet();
        listeners.add(listener);
    }

    public void removeBoxControllerListener(BoxControllerListener listener) {
        if (listeners == null) return;
        listeners.remove(listener);
    }


    @Override
    public void startDiscovery() {
        deviceBeans.clear();
        switch (communicateType) {
            case BLE:
                bluetoothLe.startDiscovery();
                break;
        }
    }

    @Override
    public void stopDiscovery() {
        switch (communicateType) {
            case BLE:
                bluetoothLe.stopDiscovery();
                break;
        }
    }



    @Override
    public void connect(String[] deviceAddresses) {
        switch (communicateType) {
            case BLE:
                bluetoothLe.connect(deviceAddresses);
                break;
        }
    }

    @Override
    public void connectFindedDevice(DeviceBean deviceBean) {
        switch (communicateType) {
            case BLE:
                bluetoothLe.connectFindedDevice(deviceBean);
                break;
        }
    }

    @Override
    public int getMaxDataLength() {
        switch (communicateType) {
            case BLE:
                return bluetoothLe.getMaxDataLength();

            default:
                return 0;

        }
    }

    @Override
    public void sendData(String data) {
        switch (communicateType) {
            case BLE:
                bluetoothLe.sendData(data);
                break;
        }
    }

    @Override
    public void sendData(byte[] data) {
        switch (communicateType) {
            case BLE:
                bluetoothLe.sendData(data);
                break;
        }
    }

    @Override
    public void disconnect() {
        switch (communicateType) {
            case BLE:
                bluetoothLe.disconnect();
                break;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {
        switch (communicateType) {
            case BLE:
                bluetoothLe.close();
                bluetoothLe = null;
                break;
        }
    }

    @Override
    public boolean getConnectState() {
        switch (communicateType) {
            case BLE:
                return bluetoothLe.getConnectState();
            default:
                return false;
        }
    }

    private void notifyListeners(EventType eventType, Object msg) {
        Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            BoxControllerListener listener = (BoxControllerListener) iter.next();
            switch (eventType) {
                case FindNewDevice:
                    listener.onFindNewDevice((DeviceBean)msg);
                    break;

                case DiscoveryFinished:
                    listener.onDiscoveryFinished();
                    break;

                case ConnectSuccess:
                    listener.onConnectSuccess((DeviceBean)msg);
                    break;

                case ConnectError:
                    listener.onConnectError(msg + "");
                    break;

                case ConnectLoss:
                    listener.onConnectLoss();
                    break;

                case DataReceive:
                    listener.onDataReceive((byte[])msg);
                    break;
            }
        }
    }

}
