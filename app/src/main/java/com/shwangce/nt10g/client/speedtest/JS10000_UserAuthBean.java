package com.shwangce.nt10g.client.speedtest;

import com.shwangce.nt10g.client.util.Log;

public class JS10000_UserAuthBean {


    private String clientIp = "";               //外网IP
    private String clientPort = "";             //外网端口
    private String cityName = "";               //城市名称
    private String broadbandAccount = "";		//宽带账号
    private long contractRate = 0;		        //签约带宽，下行速率，单位：bps   //2023.02.15 int调整为long
    private long upRate = 0;		            //上线速率，单位：bps       //2023.02.15 int调整为long


    public JS10000_UserAuthBean() {
    }

    public JS10000_UserAuthBean(String clientIp, String clientPort, String cityName, String broadbandAccount, int contractRate, int upRate) {
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.cityName = cityName;
        this.broadbandAccount = broadbandAccount;
        this.contractRate = contractRate;
        this.upRate = upRate;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getClientPort() {
        return clientPort;
    }

    public void setClientPort(String clientPort) {
        this.clientPort = clientPort;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getBroadbandAccount() {
        return broadbandAccount;
    }

    public void setBroadbandAccount(String broadbandAccount) {
        this.broadbandAccount = broadbandAccount;
    }

    public long getContractRate() {
        return contractRate;
    }

    public void setContractRate(int contractRate) {
        this.contractRate = contractRate;
    }

    public long getUpRate() {
        return upRate;
    }

    public void setUpRate(int upRate) {
        this.upRate = upRate;
    }

    @Override
    public String toString() {
        return "UserAuthBean{" +
                "clientIp='" + clientIp + '\'' +
                ", clientPort='" + clientPort + '\'' +
                ", cityName='" + cityName + '\'' +
                ", broadbandAccount='" + broadbandAccount + '\'' +
                ", contractRate='" + contractRate + '\'' +
                ", upRate='" + upRate +
                '}';
    }

    public String sendString() {
        return  clientIp + '|'  +
                clientPort + '|' +
                cityName + '|' +
                broadbandAccount + '|' +
                contractRate + '|' +
                upRate;
    }

    public static JS10000_UserAuthBean getBeanByString(String userString) {
        try {
            JS10000_UserAuthBean bean = new JS10000_UserAuthBean();
            String[] jiangsuUserinfos = userString.split("\\|");
            int contractRate = Integer.parseInt(jiangsuUserinfos[4]);
            int upRate = Integer.parseInt(jiangsuUserinfos[5]);
            bean.setClientIp(jiangsuUserinfos[0]);
            bean.setClientPort(jiangsuUserinfos[1]);
            bean.setCityName(jiangsuUserinfos[2]);
            bean.setBroadbandAccount(jiangsuUserinfos[3]);
            bean.setContractRate(contractRate);
            bean.setUpRate(upRate);
            return bean;
        } catch (Exception e) {
            Log.e("JS10000_UserAuthBean",e.getMessage());
            return null;
        }
    }
}
