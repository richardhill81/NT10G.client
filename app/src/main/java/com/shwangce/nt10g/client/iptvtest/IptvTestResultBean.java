package com.shwangce.nt10g.client.iptvtest;

/**
 * Created by linhao on 2017/12/17.
 */

public class IptvTestResultBean {
    //IPTV_ETHERNETINFO,  //目的MAC,源MAC地址
    //IPTV_VLANINFO,      //vlan优先级,vlanId
    //IPTV_IPV4INFO,      //TOS,TTL,源IP,目的IP
    //IPTV_UDPINFO,       //源端口,目的端口
    //IPTV_QUALITYINFO    //IP包数(个/s),总丢包数(个),抖动(ms),当前业务数据传输速率(kbps)，平均速率(kbps)，最大速率(kbps)，最小速率(kbps)

    public static class EthernetInfoBean {
        private String srcMac = "";
        private String dstMac = "";

        public EthernetInfoBean() {
        }

        /**
         *  modify by hzj on 2019.9.23 ddstMac/srcMac调换位置
         *  修改前  public EthernetInfoBean(String srcMac, String dstMac)
         * @param dstMac 目的MAC
         * @param srcMac 源MAC
         */

        public EthernetInfoBean(String dstMac, String srcMac) {
            this.srcMac = srcMac;
            this.dstMac = dstMac;
        }

        public String getSrcMac() {
            return srcMac;
        }

        public void setSrcMac(String srcMac) {
            this.srcMac = srcMac;
        }

        public String getDstMac() {
            return dstMac;
        }

        public void setDstMac(String dstMac) {
            this.dstMac = dstMac;
        }
    }

    public static class VlanInfoBean {
        private String vlanId = "";
        private String vlanPriority = "";

        public VlanInfoBean() {
        }

        public VlanInfoBean(String vlanPriority,String vlanId) {
            this.vlanId = vlanId;
            this.vlanPriority = vlanPriority;
        }

        public String getVlanId() {
            return vlanId;
        }

        public void setVlanId(String vlanId) {
            this.vlanId = vlanId;
        }

        public String getVlanPriority() {
            return vlanPriority;
        }

        public void setVlanPriority(String vlanPriority) {
            this.vlanPriority = vlanPriority;
        }
    }

    public static class IPv4InfoBean{
        private String tos = "";
        private String ttl = "";
        private String srcIP = "";
        private String dstIP = "";

        public IPv4InfoBean() {
        }

        public IPv4InfoBean(String tos, String ttl, String srcIP, String dstIP) {
            this.tos = tos;
            this.ttl = ttl;
            this.srcIP = srcIP;
            this.dstIP = dstIP;
        }

        public String getTos() {
            return tos;
        }

        public void setTos(String tos) {
            this.tos = tos;
        }

        public String getTtl() {
            return ttl;
        }

        public void setTtl(String ttl) {
            this.ttl = ttl;
        }

        public String getSrcIP() {
            return srcIP;
        }

        public void setSrcIP(String srcIP) {
            this.srcIP = srcIP;
        }

        public String getDstIP() {
            return dstIP;
        }

        public void setDstIP(String dstIP) {
            this.dstIP = dstIP;
        }
    }

    public static class UdpInfoBean {
        private String srcPort = "";
        private String dstPort = "";

        public UdpInfoBean() {
        }

        public UdpInfoBean(String srcPort, String dstPort) {
            this.srcPort = srcPort;
            this.dstPort = dstPort;
        }

        public String getSrcPort() {
            return srcPort;
        }

        public void setSrcPort(String srcPort) {
            this.srcPort = srcPort;
        }

        public String getDstPort() {
            return dstPort;
        }

        public void setDstPort(String dstPort) {
            this.dstPort = dstPort;
        }
    }

    public static class QualityInfoBean {
        private String packagecount = "";
        private String lostcount = "";
        private String jitter = "";
        private String minjitter = "";
        private String maxjitter = "";
        private String speed = "";
        private String avgspeed = "";
        private String maxspeed = "";
        private String minspeed = "";

        public QualityInfoBean() {
        }

        public QualityInfoBean(String packagecount, String lostcount, String jitter,String minjitter,String maxjitter, String speed, String avgspeed, String maxspeed, String minspeed) {
            this.packagecount = packagecount;
            this.lostcount = lostcount;
            this.jitter = jitter;
            this.minjitter = minjitter;
            this.maxjitter = maxjitter;
            this.speed = speed;
            this.avgspeed = avgspeed;
            this.maxspeed = maxspeed;
            this.minspeed = minspeed;
        }

        public String getPackagecount() {
            return packagecount;
        }

        public void setPackagecount(String packagecount) {
            this.packagecount = packagecount;
        }

        public String getLostcount() {
            return lostcount;
        }

        public void setLostcount(String lostcount) {
            this.lostcount = lostcount;
        }

        public String getJitter() {
            return jitter;
        }

        public void setJitter(String jitter) {
            this.jitter = jitter;
        }

        public String getMinjitter() {
            return minjitter;
        }

        public void setMinjitter(String minjitter) {
            this.minjitter = minjitter;
        }

        public String getMaxjitter() {
            return maxjitter;
        }

        public void setMaxjitter(String maxjitter) {
            this.maxjitter = maxjitter;
        }

        public String getSpeed() {
            return speed;
        }

        public void setSpeed(String speed) {
            this.speed = speed;
        }

        public String getAvgspeed() {
            return avgspeed;
        }

        public void setAvgspeed(String avgspeed) {
            this.avgspeed = avgspeed;
        }

        public String getMaxspeed() {
            return maxspeed;
        }

        public void setMaxspeed(String maxspeed) {
            this.maxspeed = maxspeed;
        }

        public String getMinspeed() {
            return minspeed;
        }

        public void setMinspeed(String minspeed) {
            this.minspeed = minspeed;
        }
    }
}
