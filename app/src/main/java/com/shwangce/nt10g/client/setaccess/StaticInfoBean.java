package com.shwangce.nt10g.client.setaccess;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class StaticInfoBean {
    private String ipstring = "";
    private String netmask = "";
    private String gateway = "";
    private String dns ="";

    public StaticInfoBean() {  }

    public StaticInfoBean(String ip, String netmask, String gateway, String dns) {
        this.ipstring = ip;
        this.netmask = netmask;
        this.gateway = gateway;
        this.dns = dns;
    }

    public String getIpString() {
        return ipstring;
    }

    public void setIpString(String ipstring) {
        this.ipstring = ipstring;
    }

    public String getNetMask() {
        return netmask;
    }

    public void setNetMask(String netmask) {
        this.netmask = netmask;
    }

    public String getGateWay() {
        return gateway;
    }

    public void setGateWay(String gateway) {
        this.gateway = gateway;
    }

    public String getDNS() {
        return dns;
    }

    public void setDNS(String dns) {
        this.dns = dns;
    }
}
