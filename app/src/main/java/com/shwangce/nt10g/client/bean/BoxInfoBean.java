package com.shwangce.nt10g.client.bean;

/**
 * Created by linhao on 2017/11/14.
 */

public class BoxInfoBean {
    private String workmode = "0";
    private String tcpdumpsave = "0";
    private String version = "";
    public BoxInfoBean() {
    }

    public BoxInfoBean(String workmode, String tcpdumpsave, String version) {
        this.workmode = workmode;
        this.tcpdumpsave = tcpdumpsave;
        this.version = version;
    }

    public String getWorkmode() {
        return workmode;
    }

    public void setWorkmode(String workmode) {
        this.workmode = workmode;
    }

    public String getTcpdumpsave() {
        return tcpdumpsave;
    }

    public void setTcpdumpsave(String tcpdumpsave) {
        this.tcpdumpsave = tcpdumpsave;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
