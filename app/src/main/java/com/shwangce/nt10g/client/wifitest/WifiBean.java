package com.shwangce.nt10g.client.wifitest;

import androidx.annotation.NonNull;

/**
 * Created by linhao on 2017/9/25.
 */

public class WifiBean {
    private String _ESSID = "";
    private String _BSSID = "";
    private String _mac = "";
    private String _level = "";
    private String _frequency = "";
    private String _securityString = "";
    private WifiTestHelper.SecurityModeEnum _securityMode = WifiTestHelper.SecurityModeEnum.Unknown;
    private WifiTestHelper.EncryptionTypeEnum _encryptionType = WifiTestHelper.EncryptionTypeEnum.NULL;
    private String _signalLevel = "";
    private String _rxBandWidth = "";
    private String _txBandWidth = "";
    private String _password = "";

    public WifiBean() {    }


    public String getEssid() {
        return _ESSID;
    }

    public void setEssid(String _ESSID) {
        this._ESSID = _ESSID;
    }

    public String getBssid() {return _BSSID;}

    public void setBssid(String _BSSID) {this._BSSID = _BSSID;}

    public String getMac() {
        return _mac;
    }

    public void setMac(String _mac) {
        this._mac = _mac;
    }

    public String getLevel() {
        return _level;
    }

    public void setLevel(String _level) {
        this._level = _level;
    }

    public String getFrequency() {
        return _frequency;
    }

    public void setFrequency(String _frequency) {
        this._frequency = _frequency;
    }

    public void setSecurityModeString(String  security) {
        this._securityString= security;
        setSecurityMode();
        setEncryptionType();
    }

    public String getSecurityModeString() {
        return _securityString;
    }

    public void setSignalLevel(String signalLevel) {
        this._signalLevel = signalLevel;
    }

    public int getSignalLevel() {
        return Integer.parseInt(this._signalLevel);
    }

    public String get_rxBandWidth() {
        return _rxBandWidth;
    }

    public void set_rxBandWidth(String _rxBandWidth) {
        this._rxBandWidth = _rxBandWidth;
    }

    public String get_txBandWidth() {
        return _txBandWidth;
    }

    public void set_txBandWidth(String _txBandWidth) {
        this._txBandWidth = _txBandWidth;
    }

    public WifiTestHelper.SecurityModeEnum getSecurityMode() {
        return _securityMode;
    }

    public WifiTestHelper.EncryptionTypeEnum getEncryptionType() {
        return _encryptionType;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String _password) {
        this._password = _password;
    }

    private void setSecurityMode() {
        switch (_securityString) {
            case "WPA2":
                _securityMode = WifiTestHelper.SecurityModeEnum.WPA2;
                break;
            case "WPA2 WPA":
                _securityMode = WifiTestHelper.SecurityModeEnum.WPA2_WPA;
                break;
            case "WPA3 WPA2":
                _securityMode = WifiTestHelper.SecurityModeEnum.WPA3_WPA2;
                break;
            case "WPA":
                _securityMode = WifiTestHelper.SecurityModeEnum.WPA;
                break;
            case "OPN":
                _securityMode = WifiTestHelper.SecurityModeEnum.OPN;
                break;
            case "WEP":
                _securityMode = WifiTestHelper.SecurityModeEnum.WEP;
                break;
            case "":
                _securityMode = WifiTestHelper.SecurityModeEnum.NULL;
                break;
            default:
                _securityMode = WifiTestHelper.SecurityModeEnum.Unknown;
                break;
        }
    }

    private void setEncryptionType() {
        _encryptionType =WifiTestHelper.getEncryptionTypeBySecurityMode(_securityMode);
    }

    public String getInfoString() {
        return this._ESSID + "|" +
               this._BSSID + "|" +
               this._password;
    }

    @NonNull
    @Override
    public String toString() {
        return  this._ESSID + "|" +
                this._BSSID + "|" +
                this._signalLevel  + "|" +
                this._securityMode + "|" +
                this._encryptionType + "|" +
                this._txBandWidth + "|" +
                this._rxBandWidth;
    }
}
