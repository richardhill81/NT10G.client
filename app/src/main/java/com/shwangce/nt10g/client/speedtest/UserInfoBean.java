package com.shwangce.nt10g.client.speedtest;

/**
 * Created by Administrator on 2016/12/14 0014.
 */

public class UserInfoBean {
    private String serialnumber ="";
    private String useraccount ="";
    private String devicenumber ="";
    private String paydownspeed ="";
    private String payupspeed ="";

    public UserInfoBean() {}

    public UserInfoBean(String serialnumber, String useraccount, String devicenumber, String paydownspeed, String payupspeed) {
        this.serialnumber = serialnumber;
        this.useraccount = useraccount;
        this.devicenumber = devicenumber;
        this.paydownspeed = paydownspeed;
        this.payupspeed = payupspeed;
    }

    public String getSerialnumber() {
        return serialnumber;
    }

    public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

    public String getUseraccount() {
        return useraccount;
    }

    public void setUseraccount(String useraccount) {
        this.useraccount = useraccount;
    }

    public String getDevicenumber() {
        return devicenumber;
    }

    public void setDevicenumber(String devicenumber) {
        this.devicenumber = devicenumber;
    }

    public String getPaydownspeed() {
        return paydownspeed;
    }

    public void setPaydownspeed(String paydownspeed) {
        this.paydownspeed = paydownspeed;
    }

    public String getPayupspeed() {
        return payupspeed;
    }

    public void setPayupspeed(String payupspeed) {
        this.payupspeed = payupspeed;
    }
}
