package com.shwangce.nt10g.client.deviceselect;

import com.shwangce.nt10g.client.BasePresenter;
import com.shwangce.nt10g.client.BaseView;
import com.shwangce.nt10g.client.library.bluetoothLe.DeviceBean;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public interface DeviceSelectContract {
    interface View extends BaseView<Presenter> {
        void doFindNewDevice(DeviceBean deviceBean) ;
        void doScanfinished();
        void doConnected(DeviceBean deviceBean);
        void doConnectError(String failString);
        void doGetVersionSuccess();
        void doGetVersionFail(String failString);
        //void doSetBoxWorkModeResult(String result);
        void doDeviceSelectComplete();
    }

    interface Presenter extends BasePresenter {
        //void doDeviceItemClicked(String devicename);

        //boolean doCheckDeviceIsFound(DeviceBean deviceBean);

        //void doFindDevicePositionInList(ArrayList<DeviceBean> deviceList);
        //void setIsFirstConnect(boolean isFirstConnect);
        //boolean getIsFirstConnect();
        /**********/

        void doBleScan();
        void doConnectDeviceByMac(String deviceMac);
        void doConnectDeviceByName(String deviceName);
        void doConnectFindedDevice(DeviceBean deviceBean);
        void doQuitClicked();

        //void doSetBoxWorkMode(String workmode);

        void doSendLocalTime();
    }
}
