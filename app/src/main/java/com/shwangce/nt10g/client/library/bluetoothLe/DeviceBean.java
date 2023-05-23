package com.shwangce.nt10g.client.library.bluetoothLe;

public class DeviceBean {
    private String deviceName = "";
    private String deviceMac = "";

    public DeviceBean() {
    }

    public DeviceBean(String deviceName, String deviceMac) {
        this.deviceName = deviceName;
        this.deviceMac = deviceMac;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    @Override
    public String toString() {
        return  deviceName + '&' + deviceMac;
    }

    public boolean equals(DeviceBean bean) {
       return this.getDeviceMac().equals(bean.getDeviceMac()) && this.getDeviceName().equals(bean.getDeviceName());
    }


}
