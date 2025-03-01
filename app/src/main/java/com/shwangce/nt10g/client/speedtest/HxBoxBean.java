package com.shwangce.nt10g.client.speedtest;

/**
 * Created by Administrator on 2016/12/14 0014.
 */

public class HxBoxBean {

    //-i, --index  配置中 url 组的索引，默认 0
    //-m, --method 测速方法，1 表示下行测速，2 表示上行测速，3 表示上下行测速。默认 3
    //-n, --name 线务员名字
    //-p, --pwd 线务员密码
    //-u, --userid 线务员工号
    //-t, --worktype 工单类型(int[=0]), 0 新装, 1 维修。默认 0
    //-s, --worksheetnum 工单号

    private int index = 0;
    private int method = 3;
    private String name = "";
    private String pwd = "";
    private String userid = "";
    private int worktype = 0;
    private String worksheetnum = "";

    private int usertype = 0;   //公客：0；政企：1
    private int upthreadnum = 4;
    private int downthreadnum = 4;
    private int testlong = 15;
    public HxBoxBean() {}

    public HxBoxBean(String name, String pwd, String userid, String worksheetnum) {
        this.name = name;
        this.pwd = pwd;
        this.userid = userid;
        this.worksheetnum = worksheetnum;
    }

    public HxBoxBean(int index, int method, String name, String pwd, String userid,
                     int worktype, String worksheetnum) {
        this.index = index;
        this.method = method;
        this.name = name;
        this.pwd = pwd;
        this.userid = userid;
        this.worktype = worktype;
        this.worksheetnum = worksheetnum;
    }
    public HxBoxBean(int index, int method, String name, String pwd, String userid,
                     int worktype, String worksheetnum,
                     int usertype, int upthreadnum, int downthreadnum, int testlong) {
        this.index = index;
        this.method = method;
        this.name = name;
        this.pwd = pwd;
        this.userid = userid;
        this.worktype = worktype;
        this.worksheetnum = worksheetnum;
        this.usertype = usertype;
        this.upthreadnum = upthreadnum;
        this.downthreadnum = downthreadnum;
        this.testlong = testlong;
    }
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    public String getUserName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getWorktype() {
        return worktype;
    }

    public void setWorktype(int worktype) {
        this.worktype = worktype;
    }

    public String getWorksheetnum() {
        return worksheetnum;
    }

    public void setWorksheetnum(String worksheetnum) {
        this.worksheetnum = worksheetnum;
    }

    public int getUsertype() {
        return usertype;
    }

    public void setUsertype(int usertype) {
        this.usertype = usertype;
    }

    public int getUpthreadnum() {
        return upthreadnum;
    }

    public void setUpthreadnum(int upthreadnum) {
        this.upthreadnum = upthreadnum;
    }

    public int getDownthreadnum() {
        return downthreadnum;
    }

    public void setDownthreadnum(int downthreadnum) {
        this.downthreadnum = downthreadnum;
    }

    public int getTestlong() {
        return testlong;
    }

    public void setTestlong(int testlong) {
        this.testlong = testlong;
    }

    @Override
    public String toString() {
        return getIndex() + "|" +
               getMethod() + "|" +
               getUserName() + "|" +
               getPwd() + "|" +
               getUserid() + "|" +
               getWorktype() + "|" +
               getWorksheetnum()  + "|" +
                getUsertype() + "|" +
                getUpthreadnum() + "|" +
                getDownthreadnum() + "|" +
                getTestlong();
    }
}
