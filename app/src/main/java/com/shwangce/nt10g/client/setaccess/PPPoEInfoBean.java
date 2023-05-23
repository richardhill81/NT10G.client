package com.shwangce.nt10g.client.setaccess;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class PPPoEInfoBean {
    private String pppoeAccount = "";
    private String pppoePassword = "";

    public PPPoEInfoBean() {    }

    public PPPoEInfoBean(String account, String password) {
        this.pppoeAccount = account;
        this.pppoePassword = password;
    }

    public String getPPPoEAccount() {
        return pppoeAccount;
    }

    public void setPPPoEAccount(String pppoeAccount) {
        this.pppoeAccount = pppoeAccount;
    }

    public String getPPPoEPassword() {
        return pppoePassword;
    }

    public void setPPPoEPassword(String pppoePassword) {
        this.pppoePassword = pppoePassword;
    }
}
