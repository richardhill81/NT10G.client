package com.shwangce.nt10g.client.main;

import com.shwangce.nt10g.client.library.ControlFrame.ResultBean;
import com.shwangce.nt10g.client.library.bluetoothLe.DeviceBean;
import com.shwangce.nt10g.client.util.ProjectUtil;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

public class MainPresenterListener {
    public interface DeviceSelectListener {
        void onFindNewDevice(DeviceBean deviceBean);
        void onScanfinished();
        void onConnectSuccess(DeviceBean deviceBean);
        void onConnectError(String failString);
        void onGetBoxVersionSuccess(String versionString);
        void onGetBoxVersionFail(String failString);
        //void onWorkModeSetResult(String result);
        void doSelfStartScan();   //by hzj  2018-11-15
    }

    public interface AccessSelectListener {
        void onAccessSuccess();
        void onAccessFail(String failreason);
    }

    public interface SpeedTestListener {
        void onTestFail(String failreason);
        void onUserInfo(String userString);

        void onTestProgressInfo(String msg);
        void onClientInfo(String info);
        void onServerInfo(String info);
        void onSpeedTestResult(ResultBean resultBean);
        void onSpeedTestSpeed(ResultBean resultBean,int type);
    }

    public interface NetToolsListener {
        void onNetToolsInfo(String info);

        void onNetToolsInfoNanJing(String info);

        void onNetToolsInfoNanJingRePing(String info);

        void onTimerScheduleResult(String info);

        void onNetToolsComplete();

        void onNetToolsCompleteNanjing(String info);

        void onNetToolsFail(String failString);

        void onNetToolsGetIPFail(String failString);
    }

    public interface IperfListener {
        void onIperfDownloading(String bandwidth);
        void onIperfDownloaded(String bandwidth);
        void onIperfUploading(String bandwidth);
        void onIperfUploaded(String bandwidth);
        void onIperfError(String failString);
        void onIperfComplete();
    }

    public interface TcpdumpListener {
        void onTcpdumpStarted();
        void onTcpdumpStoped();
        void onTcpdumpError(String errorString);
    }

    public interface BoxSettingListener {
        void onWorkModeSetResult(String result);
    }

    public interface IptvTestListener {
        void showIptv_StartSuccess();
        void showIptv_StartFail(String failInfo);


        void showIptv_Result_EthnetInfo(String resultString);
        void showIptv_Result_VlanInfo(String resultString);
        void showIptv_Result_Ipv4Info(String resultString);
        void showIptv_Result_UdpInfo(String resultString);
        void showIptv_Result_QualityInfo(String resultString);

        /*
        void showIptv_Result_1(String iptvresult);
        void showIptv_Result_2(String iptvresult);
        void showIptv_Result_3(String iptvresult);
        void showIptv_Result_4(String iptvresult);
        void showIptv_Result_5(String iptvresult);
        */
    }

    public interface WifiTestListener {
        /*
        void onStartScan(boolean isSuccess,String failReason);
        void onFindAp(WifiBean wifiBean);
        void onScanFinish(boolean isSuccess,String failReason);
        void onStopScan(boolean isSuccess,String failReason);
        void onConnect(boolean isSuccess,String failReason);
        void onQueryState(boolean isSuccess,WifiBean bean);
        void getError(String errString);

         */
    }

    public interface WebTestListener{
        void showWebTest_ResolveSuccess(String resultInfo);
        void showWebTest_ResolveError(String resultInfo);

        void showWebTest_PingProcess(String resultInfo);
        void showWebTest_PingSuccess(String resultInfo);
        void showWebTest_PingError(String resultInfo);

        void showWebTest_TracerouteProcess(String resultInfo);
        void showWebTest_TracerouteSuccess(String resultInfo);
        void showWebTest_TracerouteError(String resultInfo);

        void showWebTest_WebDelaySuccess(String resultInfo);
        void showWebTest_WebDelayError(String resultInfo);
    }

    public interface DnsTestListener{
        void showDnsTest_info(String resultInfo);
        void showDnsTest_success(String resultInfo);
        void showDnsTest_error(String resultInfo);
    }

    public interface FileManagerListener{
        void showQueryMemorySuccess(String resultInfo);
        void showQueryMemoryError(String resultInfo);
        void showQueryMemoryProcess(String resultInfo);

        void showQueryPackageFileSuccess(String resultInfo);
        void showQueryPackageFileError(String resultInfo);
        void showQueryPackageFileProcess(String resultInfo);

        void showQueryLogFileSuccess(String resultInfo);
        void showQueryLogFileError(String resultInfo);
        void showQueryLogFileProcess(String resultInfo);

        void showDeleteFileSuccess(String resultInfo);
        void showDeleteFileError(String resultInfo);
        void showDeleteFileProcess(String resultInfo);
    }

    public interface ExportLogListener{
        void showQueryLogSize(String size);
        void onExportLog(String data);
        void onExportLogComplete(String info);
        void onExportLogError(String errorInfo);
    }

    public interface SetModeListener {
        void onSetModeResult(ProjectUtil.SetModeEnum mode, ProjectUtil.SetModeResultEnum result, String message);
        /*
        void onSetSingleLanInternal(ProjectUtil.SetModeResultEnum resultEnum,String failReason);
        void onSetSingleLanExternal(ProjectUtil.SetModeResultEnum resultEnum,String failReason);
        void onSetDoubleLan(ProjectUtil.SetModeResultEnum resultEnum,String failReason);
        void onSetWifiDHCP(ProjectUtil.SetModeResultEnum resultEnum,String failReason);
        void onSetLanAndWifi(ProjectUtil.SetModeResultEnum resultEnum,String failReason);
         */
        void onGetModeQuery(ProjectUtil.SetModeEnum modeEnum);
        void onUpdateBle();
    }
}
