package com.shwangce.nt10g.client.speedtest;

/**
 * Created by Administrator on 2016/12/30 0030.
 */

public class GD10000_serveripsBean {
    private String bandwidthDown = "";      //下行速率（单位M）
    private String bandwidthUp = "";        //上行速率（单位M）
    private String account = "";            //宽带账号
    private String city = "";               //用户所在地市
    private String localIp = "";            //本机IP
    private String serverIpDown = "";       //下行测试的服务端IP，以“,”逗号隔开，作为测速接口传入参数
    private String[] serverIps = null;

    public GD10000_serveripsBean() {}

    public GD10000_serveripsBean(String bandwidthDown, String bandwidthUp, String account, String city, String localIp, String serverIpDown) {
        setBandwidthDown(bandwidthDown);
        setBandwidthUp(bandwidthUp);
        setAccount(account);
        setCity(city);
        setLocalIp(localIp);
        setServerIpDown(serverIpDown);
    }

    public String getBandwidthDown() {
        return bandwidthDown;
    }

    public void setBandwidthDown(String bandwidthDown) {
        this.bandwidthDown = bandwidthDown;
    }

    public String getBandwidthUp() {
        return bandwidthUp;
    }

    public void setBandwidthUp(String bandwidthUp) {
        this.bandwidthUp = bandwidthUp;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public String[] getServerIps() {
        return serverIps;
    }

    public void setServerIpDown(String serverIpDown) {
        this.serverIpDown = serverIpDown;
        this.serverIps = serverIpDown.split(",");
    }

    public void saveIps() {
        this.serverIps = serverIpDown.split(",");
    }

    public String toString() {
        return  "bandwidthDown:" + bandwidthDown + "|" +
                "bandwidthUp:" + bandwidthUp + "|" +
                "account:" + account + "|" +
                "city:" + city + "|" +
                "localIp:" + localIp + "|" +
                "serverIpDown:" + serverIpDown +";";
    }
}