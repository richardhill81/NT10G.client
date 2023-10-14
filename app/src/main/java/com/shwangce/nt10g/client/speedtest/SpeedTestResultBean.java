package com.shwangce.nt10g.client.speedtest;

/**
 * Created by Administrator on 2016/9/27 0027.
 */

public class SpeedTestResultBean {
    private HxBoxBean hxbean = null;
/*    private String serialnumber;
    private String useraccount;
    private String devicenumber;
    private String paydownspeed;
    private String payupspeed;*/
    private UserInfoBean userInfoBean = null;
    private String avgspeed = "";
    private String avgupspeed = "";
    private String peakspeed = "";
    private String peakupspeed = "";
    private String speedresult = "";
    private String speedtime = "";

    //20231012 Add 延迟、丢包率
    private String netdelay = "";
    private String netloss = "";
    public HxBoxBean getHxbean() {
        return hxbean;
    }

    public void setHxbean(HxBoxBean hxbean) {
        this.hxbean = hxbean;
    }

    public UserInfoBean getUserInfo() {
        return userInfoBean;
    }

    public void setUserInfo(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    public void setUserInfoBean(UserInfoBean userInfoBean) {
        this.userInfoBean = userInfoBean;
    }

    /*  public String getSerialnumber() {
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
    }*/

    public String getAvgspeed() {
        return avgspeed;
    }

    public void setAvgspeed(String avgspeed) {
        this.avgspeed = avgspeed;
    }

    public String getAvgupspeed() {
        return avgupspeed;
    }

    public void setAvgupspeed(String avgupspeed) {
        this.avgupspeed = avgupspeed;
    }

    public String getPeakspeed() {
        return peakspeed;
    }

    public void setPeakspeed(String peakspeed) {
        this.peakspeed = peakspeed;
    }

    public String getPeakupspeed() {
        return peakupspeed;
    }

    public void setPeakupspeed(String peakupspeed) {
        this.peakupspeed = peakupspeed;
    }

    public String getSpeedresult() {
        return speedresult;
    }

    public void setSpeedresult(String speedresult) {
        this.speedresult = speedresult;
    }

    public String getSpeedtime() {
        return speedtime;
    }

    public void setSpeedtime(String speedtime) {
        this.speedtime = speedtime;
    }

    public String getNetdelay() {return netdelay;}
    public void setNetdelay(String netdelay) {this.netdelay = netdelay;}

    public String getNetloss() { return netloss; }
    public void setNetloss(String netloss) { this.netloss = netloss; }
}
