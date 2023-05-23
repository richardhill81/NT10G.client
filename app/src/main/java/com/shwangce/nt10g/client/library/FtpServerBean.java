package com.shwangce.nt10g.client.library;

/**
 * Created by Administrator on 2017/3/29 0029.
 */

public class FtpServerBean {
    private String serverhost = "";
    private int serverport = 21;
    private String username = "";
    private String password = "";
    private String remotefilename = "";
    private String remotepath = "";

    public FtpServerBean() {
    }

    public FtpServerBean(String serverhost) {
        this.serverhost = serverhost;
        this.serverport = 21;
        this.username = "anonymous";
        this.password = "";
    }

    public FtpServerBean(String serverhost, int serverport) {
        this.serverhost = serverhost;
        this.serverport = serverport;
        this.username = "anonymous";
        this.password = "";
    }

    public FtpServerBean(String serverhost, String username, String password) {
        this.serverhost = serverhost;
        this.serverport = 21;
        this.username = username;
        this.password = password;
    }

    public FtpServerBean(String serverhost, int serverport, String username, String password) {
        this.serverhost = serverhost;
        this.serverport = serverport;
        this.username = username;
        this.password = password;
    }

    public String getServerhost() {
        return serverhost;
    }

    public void setServerhost(String serverhost) {
        this.serverhost = serverhost;
    }

    public int getServerport() {
        return serverport;
    }

    public void setServerport(int serverport) {
        this.serverport = serverport;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemotePath() {
        return remotepath;
    }

    public void setRemotePath(String remotepath) {
        this.remotepath = remotepath;
    }

    public String getRemoteFileName() {
        return remotefilename;
    }

    public void setRemoteFileName(String remotefilename) {
        this.remotefilename = remotefilename;
    }

    @Override
    public String toString() {
        //ftp地址|端口|用户名|密码|远程路径|文件名
        return getServerhost() + "|" +
                getServerport() + "|" +
                getUsername() + "|" +
                getPassword() + "|" +
                getRemotePath() + "|" +
                getRemoteFileName();

    }
}
