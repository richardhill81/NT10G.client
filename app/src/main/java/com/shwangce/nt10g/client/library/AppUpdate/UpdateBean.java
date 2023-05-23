package com.shwangce.nt10g.client.library.AppUpdate;

public class UpdateBean {
    private final String TAG = "UpdateBean";
    private String client_appname = "";
    private String client_version = "";
    private String client_code = "";
    private String client_updateinfo = "";
    private String client_url = "";
    private String server_appname = "";
    private String server_version = "";
    private String server_code = "";
    private String server_updateinfo = "";
    private String server_url = "";
    private String ota_version = "";
    private String ota_code = "";
    private String ota_updateinfo = "";
    private String ota_url = "";

    public UpdateBean() {
    }

    public UpdateBean(String client_appname, String client_version, String client_code, String client_updateinfo, String client_url, String server_appname, String server_version, String server_code, String server_updateinfo, String server_url, String ota_version, String ota_code, String ota_updateinfo, String ota_url) {
        this.client_appname = client_appname;
        this.client_version = client_version;
        this.client_code = client_code;
        this.client_updateinfo = client_updateinfo;
        this.client_url = client_url;
        this.server_appname = server_appname;
        this.server_version = server_version;
        this.server_code = server_code;
        this.server_updateinfo = server_updateinfo;
        this.server_url = server_url;
        this.ota_version = ota_version;
        this.ota_code = ota_code;
        this.ota_updateinfo = ota_updateinfo;
        this.ota_url = ota_url;
    }

    public String getClient_appname() {
        return client_appname;
    }

    public void setClient_appname(String client_appname) {
        this.client_appname = client_appname;
    }

    public String getClient_version() {
        return client_version;
    }

    public void setClient_version(String client_version) {
        this.client_version = client_version;
    }

    public String getClient_code() {
        return client_code;
    }

    public void setClient_code(String client_code) {
        this.client_code = client_code;
    }

    public String getClient_updateinfo() {
        return client_updateinfo;
    }

    public void setClient_updateinfo(String client_updateinfo) {
        this.client_updateinfo = client_updateinfo;
    }

    public String getClient_url() {
        return client_url;
    }

    public void setClient_url(String client_url) {
        this.client_url = client_url;
    }

    public String getServer_appname() {
        return server_appname;
    }

    public void setServer_appname(String server_appname) {
        this.server_appname = server_appname;
    }

    public String getServer_version() {
        return server_version;
    }

    public void setServer_version(String server_version) {
        this.server_version = server_version;
    }

    public String getServer_code() {
        return server_code;
    }

    public void setServer_code(String server_code) {
        this.server_code = server_code;
    }

    public String getServer_updateinfo() {
        return server_updateinfo;
    }

    public void setServer_updateinfo(String server_updateinfo) {
        this.server_updateinfo = server_updateinfo;
    }

    public String getServer_url() {
        return server_url;
    }

    public void setServer_url(String server_url) {
        this.server_url = server_url;
    }


    public String getOta_version() {
        return ota_version;
    }

    public void setOta_version(String ota_version) {
        this.ota_version = ota_version;
    }

    public String getOta_code() {
        return ota_code;
    }

    public void setOta_code(String ota_code) {
        this.ota_code = ota_code;
    }

    public String getOta_updateinfo() {
        return ota_updateinfo;
    }

    public void setOta_updateinfo(String ota_updateinfo) {
        this.ota_updateinfo = ota_updateinfo;
    }

    public String getOta_url() {
        return ota_url;
    }

    public void setOta_url(String ota_url) {
        this.ota_url = ota_url;
    }

    @Override
    public String toString() {
        return "UpdateBean{" +
                "client_appname='" + client_appname + '\'' +
                ", client_version='" + client_version + '\'' +
                ", client_code='" + client_code + '\'' +
                ", client_updateinfo='" + client_updateinfo + '\'' +
                ", client_url='" + client_url + '\'' +
                ", server_appname='" + server_appname + '\'' +
                ", server_version='" + server_version + '\'' +
                ", server_code='" + server_code + '\'' +
                ", server_updateinfo='" + server_updateinfo + '\'' +
                ", server_url='" + server_url + '\'' +
                ", ota_version='" + ota_version + '\'' +
                ", ota_code='" + ota_code + '\'' +
                ", ota_updateinfo='" + ota_updateinfo + '\'' +
                ", ota_url='" + ota_url + '\'' +
                '}';
    }
}
