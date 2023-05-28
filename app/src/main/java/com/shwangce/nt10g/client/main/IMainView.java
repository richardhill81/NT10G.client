package com.shwangce.nt10g.client.main;

import com.shwangce.nt10g.client.setaccess.AccessType;
import com.shwangce.nt10g.client.util.ProjectUtil;

/**
 * Created by Administrator on 2017/2/8 0008.
 */

public interface IMainView {
    void showConnected(String devicename);
    void showBoxVersion(String versionname);
    void showModeDialog();
    void showAccessDialog();
    void updateTestState(String msg);
    void updateLinkState(String msg);
    void updateAccessType(AccessType accessType,String ipString);
    void showDeviceSelectDialog();
    void doConnectLoss();
    void doShowTcpdumpStarted();
    void doShowTcpdumpStopped();
    void doShowTcpdumpError(String errormsg);
    void doShowBoxUpdateDialog(int boxupdatetype,String update_describe);
    void doUpdateBoxUpdateDialogState(String titleText,String contentText);
    void doShowBoxUpdateAlertDialog();
    void doShowBoxUpdateFailAlertDialog(String contentText);
    void doShowTestMode(ProjectUtil.SetModeEnum testMode);
    void doCleanTestMode();
    //void doShowAPInfo(WifiBean bean);
    //void doAPConnected(WifiBean bean);

    //void doUpdateDownloadingBar(int value);
    //void doDownloadComplete(String fileName);
    void Exit();

}
