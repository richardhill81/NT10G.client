package com.shwangce.nt10g.client.library.communicate;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import androidx.annotation.NonNull;

import com.shwangce.nt10g.client.library.ControlFrame.FrameHelper;
import com.shwangce.nt10g.client.library.bluetoothLe.BluetoothLeHelper;
import com.shwangce.nt10g.client.library.bluetoothLe.DeviceBean;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class BoxController_bluetoothLe implements IBoxController {

    private BoxControllerListener listener = null;
    private int maxDataLength  = 0;
    private BluetoothLeHelper bluetooth = null;
    private HashMap<String,BluetoothDevice> deviceMap = new HashMap<>();

    private Context context ;
    private boolean bOpened = false;
    private boolean bConnected = false;

    private BluetoothLeHelper.BluetoothLeListener bluetoothListener = new BluetoothLeHelper.BluetoothLeListener() {

        @Override
        public void onFindNewDevice(BluetoothDevice device) {
            Log.d("onFindNewDevice", device.getAddress() + " => " + device.getName());
            if(device != null && device.getName()!= null) {
                String devicename = device.getName();
                if (devicename.indexOf(ProjectUtil.DeviceName)>=0) {
                    deviceMap.put(devicename, device);
                    if (listener != null) {
                        listener.onFindNewDevice(new DeviceBean(device.getName(),device.getAddress()));
                    }
                } else {

                }
            }
        }

        @Override
        public void onConnected(DeviceBean deviceBean) {
            bOpened = true;
            bConnected = true;
            maxDataLength = bluetooth.getMaxDataLength();
            if(listener != null) {
                listener.onConnectSuccess(deviceBean);
            }
        }

        @Override
        public void onOpenFail(String failReason) {
            bOpened = false;
            bConnected = false;
            if(listener != null) {
                listener.onConnectError(failReason);
            }
        }

        @Override
        public void onConnectFail(String failReason) {
            bConnected = false;
            if (listener != null) {
                listener.onConnectError(failReason);
            }
        }

        @Override
        public void onConnectLoss() {
            bConnected = false;
            if(listener != null) {
                listener.onConnectLoss();
            }
        }

        @Override
        public void onBluetoothRead(byte[] recvdata) {
            if(listener != null) {
                listener.onDataReceive(recvdata);
            }
        }
    };

    public BoxController_bluetoothLe(Context context, BoxControllerListener listener) {
        this.listener = listener;
        this.context = context;
        bluetooth = new BluetoothLeHelper(context, bluetoothListener);
        if(bluetooth.open())
            bOpened = true;
        else
            bOpened = false;
    }


    @Override
    public void startDiscovery() {
        if(bOpened) {
            deviceMap.clear();
            bluetooth.startLeScan();
        }
    }

    @Override
    public void stopDiscovery() {
        if(bOpened) {
            bluetooth.stopLeScan();
        }
    }

    @Override
    public void connect(String[] addressList) {
        bluetooth.connect(addressList);
    }

    @Override
    public void connectFindedDevice(DeviceBean deviceBean) {
        BluetoothDevice bledevice = deviceMap.get(deviceBean.getDeviceName());
        if(bledevice != null) {
            bluetooth.connect(bledevice);
        }
    }

    @Override
    public int getMaxDataLength() {
        return maxDataLength;
    }

    @Override
    public void sendData(@NonNull String data) {
        String sends = data;
        String s;
        while(sends.length() >0) {
            if(sends.length() > maxDataLength) {
                s = sends.substring(0,maxDataLength -1);
                sends = sends.substring(maxDataLength);
            } else {
                s = sends;
                while(s.length() < maxDataLength) {
                    s += " ";
                }
                sends = "";
            }
            if(bConnected) {
                bluetooth.write(s);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendData(byte[] data) {
        long totallength = data.length;
        byte[] sendbyte = FrameHelper.getFrameBytes(FrameHelper.FrameKind.COMMAND,data,maxDataLength);
        byte[] simplesend;
        int sendstart = 0;

        while(sendstart < sendbyte.length) {
            simplesend = new byte[maxDataLength];
            for(int j=0;j<maxDataLength;j++) {
                if(sendstart +j >= sendbyte.length) {
                    simplesend[j] = 0x00;
                } else
                    simplesend[j] = sendbyte[sendstart + j];
            }
            sendstart += maxDataLength;
            if(bConnected) {
                bluetooth.write(simplesend);
                //Log.d("","蓝牙 已发送 " + sendstart + " / 总共 " + totallength );
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void disconnect() {
        bluetooth.disconnect();
    }

    @Override
    public void close() {
        bluetooth.close();
    }

    private BluetoothDevice searchDevicebyName(String devicename) {
        BluetoothDevice device = null;
        if(deviceMap.size() > 0 ) {
            if(deviceMap.containsKey(devicename))
                device = deviceMap.get(devicename);
        }
        return device;
    }

    @Override
    public boolean getConnectState() {
        return bConnected;
    }
}
