package com.shwangce.nt10g.client.library.communicate;

import com.shwangce.nt10g.client.library.bluetoothLe.DeviceBean;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public interface BoxControllerListener {
    void onFindNewDevice(DeviceBean deviceBean);
    void onDiscoveryFinished();
    void onConnectSuccess(DeviceBean deviceBean);
    void onConnectError(String error);
    void onConnectLoss();
    void onDataReceive(byte[] recvdata);
}
