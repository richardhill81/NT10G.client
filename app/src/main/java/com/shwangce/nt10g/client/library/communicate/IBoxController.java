package com.shwangce.nt10g.client.library.communicate;

import com.shwangce.nt10g.client.library.bluetoothLe.DeviceBean;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

interface IBoxController {
    void startDiscovery();
    void stopDiscovery();
    void connect(String[] deviceAddresses);
    void connectFindedDevice(DeviceBean deviceBean);
    void sendData(byte[] data);
    void sendData(String data);
    void disconnect();
    void close();
    boolean getConnectState();
    int getMaxDataLength();
}
