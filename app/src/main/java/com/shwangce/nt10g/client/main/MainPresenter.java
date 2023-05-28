package com.shwangce.nt10g.client.main;

import android.content.Context;

import androidx.annotation.NonNull;

import com.shwangce.nt10g.client.library.AppUpdate.UpdateWork;
import com.shwangce.nt10g.client.library.ControlFrame.CommandValue;
import com.shwangce.nt10g.client.library.ControlFrame.ResultBean;
import com.shwangce.nt10g.client.library.bluetoothLe.DeviceBean;
import com.shwangce.nt10g.client.model.CommunicateEvent;
import com.shwangce.nt10g.client.model.CommunicateModel;
import com.shwangce.nt10g.client.setaccess.AccessType;
import com.shwangce.nt10g.client.speedtest.SpeedTestPresenter;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;

import rx.Observable;
import rx.Subscriber;

//import com.shwangce.nt201.client.util.SharedPreferencesUtil;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class MainPresenter {

    private final Context context;
    private final MainActivity mainView;
    private final CommunicateModel communicateModel;
    private final Subscriber<CommunicateEvent> subscriber = new Subscriber<CommunicateEvent>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(CommunicateEvent communicateEvent) {
           //Log.d("MainPresenter","communicateEvent : " + communicateEvent.getEventType().toString());
           switch (communicateEvent.getEventType()) {
                case FindNewDevice:
                    if (deviceSelectListener != null) {
                           deviceSelectListener.onFindNewDevice((DeviceBean) communicateEvent.getMessage());
                    }

                    break;

                case DiscoveryFinished:
                    if (deviceSelectListener != null)
                        deviceSelectListener.onScanfinished();
                    break;

                case ConnectError:
                    connectedDeviceName = "";
                    if (deviceSelectListener != null)
                        deviceSelectListener.onConnectError(communicateEvent.getMessage() + "");
                    break;

                case ConnectSuccess:
                    DeviceBean deviceBean = (DeviceBean) communicateEvent.getMessage();
                    connectedDeviceName =  deviceBean.getDeviceName();
                    if (deviceSelectListener != null)
                        deviceSelectListener.onConnectSuccess(deviceBean);
                    mainView.showConnected(connectedDeviceName);
                    break;

                case ConnectLoss:
                    connectedDeviceName = "";
                    mainView.doConnectLoss();
                    break;

                case ResultReceive:
                    ResultBean resultBean = (ResultBean) communicateEvent.getMessage();
                    //Log.d("MainPresenter","ResultReceive : " + resultBean.getResultType().toString());
                    switch (resultBean.getResultType()) {
                        case BOXAPP_VERSIONINFO:
                            String[] versioninfo = resultBean.getResultParams().split("\\|");
                            if(versioninfo.length > 1) {
                                boxVersionCode = Long.parseLong(versioninfo[0]);
                                mainView.showBoxVersion(versioninfo[1]);
                                if (deviceSelectListener != null)
                                    deviceSelectListener.onGetBoxVersionSuccess(versioninfo[1]);
                            } else if(versioninfo.length == 1) {
                                boxVersionCode = Long.parseLong(versioninfo[0]);
                                mainView.showBoxVersion("");
                                if (deviceSelectListener != null)
                                    deviceSelectListener.onGetBoxVersionSuccess("");
                            } else {
                                boxVersionCode = 0;
                                mainView.showBoxVersion("");
                                if (deviceSelectListener != null)
                                    deviceSelectListener.onGetBoxVersionFail("版本号为空");
                            }
                            break;
                        case ETHERNET_LINKSTATE:
                            mainView.updateLinkState(resultBean.getResultParams());
                            break;

                        /*case WORKMODESETTING_RESULT:
                            String wmr = resultBean.getResultParams();
                            if(boxSettingListener != null && ProjectUtil.isFromSetting)
                                boxSettingListener.onWorkModeSetResult(wmr);
                            else
                                if(deviceSelectListener != null ) {
                                    deviceSelectListener.onWorkModeSetResult(wmr);
                                    mainView.showAccessDialog();
                                }

                            break;*/

                        case SETACCESS_SUCCESS:
                            if (accessSelectListener != null)
                                accessSelectListener.onAccessSuccess();
                            mainView.updateAccessType(mAccessType, resultBean.getResultParams());
                            break;

                        case SETACCESS_FAIL:
                            if (accessSelectListener != null)
                                accessSelectListener.onAccessFail(resultBean.getResultParams());
                            break;

                        case SPEEDTEST_SUCCESS:
                            if (speedTestListener != null)
                                speedTestListener.onSpeedTestResult(resultBean);
                            break;

                        case SPEEDTEST_FAIL:
                            String errorString = "" + resultBean.getResultParams();
                            if(speedTestListener != null)
                                speedTestListener.onTestFail(errorString);
                            break;

                        case SPEEDTEST_USERINFO:
                            if(speedTestListener != null) {
                                String userinfo = resultBean.getResultParams();
                                speedTestListener.onUserInfo(userinfo);
                            }
                            break;


                        case SPEEDTEST_STATECHANGE:
                            if(speedTestListener != null)
                                speedTestListener.onTestProgressInfo("" + resultBean.getResultParams());
                            break;

                        case SPEEDTEST_TCPSPEEDTEST_CLIENTIP:
                            if(speedTestListener != null)
                                speedTestListener.onClientInfo(resultBean.getResultParams());
                            break;

                        case SPEEDTEST_TCPSPEEDTEST_SERVERIP:
                            if(speedTestListener != null)
                                speedTestListener.onServerInfo(resultBean.getResultParams());
                            break;

                        case SPEEDTEST_SPEED_DOWNLOADING:
                            if(speedTestListener != null)
                                speedTestListener.onSpeedTestSpeed(resultBean, SpeedTestPresenter.DOWNLOADING);
                            break;

                        case SPEEDTEST_SPEED_DOWNLOADED:
                            if(speedTestListener != null)
                                speedTestListener.onSpeedTestSpeed(resultBean, SpeedTestPresenter.DOWNLOADED);
                            break;

                        case SPEEDTEST_SPEED_UPLOADING:
                            if(speedTestListener != null)
                                speedTestListener.onSpeedTestSpeed(resultBean, SpeedTestPresenter.UPLOADING);
                            break;

                        case SPEEDTEST_SPEED_UPLOADED:
                            if(speedTestListener != null)
                                speedTestListener.onSpeedTestSpeed(resultBean, SpeedTestPresenter.UPLOADED);
                            break;

                        case NETTOOLS_INFO:
                            if(netToolsListener != null) {
                                netToolsListener.onNetToolsInfo(resultBean.getResultParams());
                            }
                            break;

                        case NETTOOLS_FAIL:
                            if(netToolsListener != null) {
                                netToolsListener.onNetToolsFail(resultBean.getResultParams());
                            }
                            break;

                        case GETIP_FAIL:
                            if(netToolsListener != null) {
                                netToolsListener.onNetToolsGetIPFail(resultBean.getResultParams());
                            }
                            break;

                        case NETTOOLS_COMPLETE:
                            if(netToolsListener != null) {
                                netToolsListener.onNetToolsComplete();
                            }
                            break;

                        case NETTOOLS_INFO_NANJING:
                            if(netToolsListener != null) {
                                netToolsListener.onNetToolsInfoNanJing(resultBean.getResultParams());
                            }
                            break;

                        case NETTOOLS_INFO_PING_NANJING:
                            if(netToolsListener != null) {
                                netToolsListener.onNetToolsInfoNanJingRePing(resultBean.getResultParams());
                            }
                            break;

                        case NETTOOLS_COMPLETE_NANJING:
                            if(netToolsListener != null) {
                                netToolsListener.onNetToolsCompleteNanjing(resultBean.getResultParams());
                            }
                            break;

                        case IPERF_DOWNLOADING:
                            if(iperfListener != null)
                                iperfListener.onIperfDownloading(resultBean.getResultParams());
                            break;

                        case IPERF_DOWNLOADED:
                            if(iperfListener != null)
                                iperfListener.onIperfDownloaded(resultBean.getResultParams());
                            break;

                        case IPERF_UPLOADING:
                            if(iperfListener != null)
                                iperfListener.onIperfUploading(resultBean.getResultParams());
                            break;

                        case IPERF_UPLOADED:
                            if(iperfListener != null)
                                iperfListener.onIperfUploaded(resultBean.getResultParams());
                            break;

                        case IPERF_ERROR:
                            if(iperfListener != null)
                                iperfListener.onIperfError(resultBean.getResultParams());
                            break;

                        case IPERF_COMPLETE:
                            if(iperfListener != null)
                                iperfListener.onIperfComplete();
                            break;

                        case TCPDUMP_STARTED:

                            mainView.doShowTcpdumpStarted();
                            break;

                        case TCPDUMP_STOPED:
                            mainView.doShowTcpdumpStopped();
                            break;


                        case TCPDUMP_ERROR:
                            mainView.doShowTcpdumpError(resultBean.getResultParams());
                            break;
/*                      2022.06.10去除
                        case IPTVSTART_SUCCESS:
                            if(iptvTestListener != null)
                                iptvTestListener.showIptv_StartSuccess();
                            break;

                        case IPTVSTART_FAIL:
                            if(iptvTestListener != null) {
                                String iptvstarterror = resultBean.getResultParams();
                                iptvTestListener.showIptv_StartFail(iptvstarterror);
                            }
                            break;

                        case IPTV_ETHERNETINFO:
                            if(iptvTestListener != null) {
                                String iptvresult = resultBean.getResultParams();
                                iptvTestListener.showIptv_Result_EthnetInfo(iptvresult);
                            }
                            break;

                        case IPTV_VLANINFO:
                            if(iptvTestListener != null) {
                                String iptvresult = resultBean.getResultParams();
                                iptvTestListener.showIptv_Result_VlanInfo(iptvresult);
                            }
                            break;

                        case IPTV_IPV4INFO:
                            if(iptvTestListener != null) {
                                String iptvresult = resultBean.getResultParams();
                                iptvTestListener.showIptv_Result_Ipv4Info(iptvresult);
                            }
                            break;

                        case IPTV_UDPINFO:
                            if(iptvTestListener != null) {
                                String iptvresult = resultBean.getResultParams();
                                iptvTestListener.showIptv_Result_UdpInfo(iptvresult);
                            }
                            break;

                        case IPTV_QUALITYINFO:
                            if(iptvTestListener != null) {
                                String iptvresult = resultBean.getResultParams();
                                iptvTestListener.showIptv_Result_QualityInfo(iptvresult);
                            }
                            break;

 */
                        case OTA_CANUPDATE:
                            String otaupdateString = resultBean.getResultParams();
                            String[] otaupdateinfo = otaupdateString.split("\\|");
                            mainView.doShowBoxUpdateDialog(UpdateWork.BOXOTA_UPDATE,"版本号：  " + otaupdateinfo[0] + "\n\n\n" + "更新内容：" + otaupdateinfo[1]);
                            break;

                        case OTA_STATE:
                            String getotaUpdateString = resultBean.getResultParams();
                            if(getotaUpdateString.contains("%")) {
                                getotaUpdateString = "正在下载升级包:" + getotaUpdateString;
                            }
                            mainView.doUpdateBoxUpdateDialogState("", getotaUpdateString);
                            break;

                        case OTA_UPDATEBEGIN:
                            mainView.doShowBoxUpdateAlertDialog();
                            break;

                        case OTA_UPDATEFAIL:

                        case BOXAPP_UPDATEFAIL:
                            mainView.doShowBoxUpdateFailAlertDialog(resultBean.getResultParams());
                            break;

                        case BOXAPP_CANUPDATE:
                            String boxappupdateString = resultBean.getResultParams();
                            String[] boxappupdateinfo = boxappupdateString.split("\\|");
                            mainView.doShowBoxUpdateDialog(UpdateWork.BOXAPP_UPDATE,"版本号：  " + boxappupdateinfo[0] + "\n\n\n" + "更新内容：" + boxappupdateinfo[1]);
                            break;

                        case BOX_UPDATESTATE:
                            String getBoxUpdateString = resultBean.getResultParams();
                            if(getBoxUpdateString.contains("%")) {
                                getBoxUpdateString = "正在下载升级包:" + getBoxUpdateString;
                            }
                            mainView.doUpdateBoxUpdateDialogState("", getBoxUpdateString);
                            break;

                        case BOXAPP_UPDATESTART:
                            mainView.doShowBoxUpdateAlertDialog();
                            break;
                        case TIMERSCHEDULE_SEARCHANDDELETE_SUCCESS:
                            if(netToolsListener != null) {
                                netToolsListener.onTimerScheduleResult(resultBean.getResultParams());
                            }
                            break;

                        //add by hzj at2018.12.4
                        case WEBTEST_RESOLVE_ERROR:
                            webTestListener.showWebTest_ResolveError(resultBean.getResultParams());
                            break;
                        case WEBTEST_RESOLVE_SUCCESS:
                            webTestListener.showWebTest_ResolveSuccess(resultBean.getResultParams());
                            break;
                        case WEBTEST_PING_INFO:
                            webTestListener.showWebTest_PingProcess(resultBean.getResultParams());
                            break;
                        case WEBTEST_PING_ERROR:
                            webTestListener.showWebTest_PingError(resultBean.getResultParams());
                            break;
                        case WEBTEST_PING_SUCCESS:
                            webTestListener.showWebTest_PingSuccess(resultBean.getResultParams());
                            break;
                        case WEBTEST_TRACEROUTE_INFO:
                            webTestListener.showWebTest_TracerouteProcess(resultBean.getResultParams());
                            break;
                        case WEBTEST_TRACEROUTE_ERROR:
                            webTestListener.showWebTest_TracerouteError(resultBean.getResultParams());
                            break;
                        case WEBTEST_TRACEROUTE_SUCCESS:
                            webTestListener.showWebTest_TracerouteSuccess(resultBean.getResultParams());
                            break;
                        case WEBTEST_DELAY_ERROR:
                            webTestListener.showWebTest_WebDelayError(resultBean.getResultParams());
                            break;
                        case WEBTEST_DELAY_SUCCESS:
                            webTestListener.showWebTest_WebDelaySuccess(resultBean.getResultParams());
                            break;

                        case DNSTEST_INFO:
                            break;
                        case DNSTEST_ERROR:
                            break;
                        case DNSTEST_SUCCESS:
                            break;

                        //add by hzj in 2018.12.13
                        case QUERYMEMORY_PROCESS:
                            fileManagerListener.showQueryMemoryProcess(resultBean.getResultParams());
                            break;
                        case QUERYMEMORY_ERROR:
                            fileManagerListener.showQueryMemoryError(resultBean.getResultParams());
                            break;
                        case QUERYMEMORY_SUCCESS:
                            fileManagerListener.showQueryMemorySuccess(resultBean.getResultParams());
                            break;
                        case QUERYPACKAGEFILE_PROCESS:
                            fileManagerListener.showQueryPackageFileProcess(resultBean.getResultParams());
                            break;
                        case QUERYPACKAGEFILE_ERROR:
                            fileManagerListener.showQueryPackageFileError(resultBean.getResultParams());
                            break;
                        case QUERYPACKAGEFILE_SUCCESS:
                            fileManagerListener.showQueryPackageFileSuccess(resultBean.getResultParams());
                            break;
                        case QUERYLOGFILE_PROCESS:
                            fileManagerListener.showQueryLogFileProcess(resultBean.getResultParams());
                            break;
                        case QUERYLOGFILE_ERROR:
                            fileManagerListener.showQueryLogFileError(resultBean.getResultParams());
                            break;
                        case QUERYLOGFILE_SUCCESS:
                            fileManagerListener.showQueryLogFileSuccess(resultBean.getResultParams());
                            break;
                        case DELETEFILE_PROCESS:
                            fileManagerListener.showDeleteFileProcess(resultBean.getResultParams());
                            break;
                        case DELETEFILE_ERROR:
                            fileManagerListener.showDeleteFileError(resultBean.getResultParams());
                            break;
                        case DELETEFILE_SUCCESS:
                            fileManagerListener.showDeleteFileSuccess(resultBean.getResultParams());
                            break;

                            // add by hzj on 20191018
                        case QUERYLOGFILESIZE:
                            exportLogListener.showQueryLogSize(resultBean.getResultParams());
                            break;
                        case EXPORTLOGFILE:
                            exportLogListener.onExportLog(resultBean.getResultParams());
                            break;
                        case EXPORTLOGFILEComplete:
                            exportLogListener.onExportLogComplete(resultBean.getResultParams());
                            break;
                        case EXPORTLOGFILEERROR:
                            exportLogListener.onExportLogError(resultBean.getResultParams());
                            break;

                        case WIFI_STARTSCAN:
                            Log.d("WIFI_STARTSCAN",resultBean.getResultParams());
                            /*
                            if(wifiTestListener != null) {
                                String[] startScanResult = resultBean.getResultParams().split("\\|");
                                if (startScanResult[0].equals("1"))
                                    wifiTestListener.onStartScan(true,"");
                                else if(startScanResult[0].equals("0")) {
                                    if(startScanResult.length > 1)
                                        wifiTestListener.onStartScan(false,startScanResult[1]);
                                    else
                                        wifiTestListener.onStartScan(false,"");
                                } else {
                                    Log.w("WIFI_STARTSCAN","Unknown result : " + resultBean.getResultParams());
                                }
                            }

                             */
                            break;

                        case WIFI_FINDAP:
                            Log.d("WIFI_FINDAP",resultBean.getResultParams());
                            /*
                            if(wifiTestListener != null) {
                                String s1 = resultBean.getResultParams().replaceAll("\n","");
                                String[] apString = s1.split("\\|");
                                if (apString.length >= 4) {
                                    WifiBean wifiBean = new WifiBean();
                                    wifiBean.setSignalLevel(apString[0]);
                                    wifiBean.setEssid(apString[1]);
                                    wifiBean.setSecurityModeString(apString[2]);
                                    wifiBean.setBssid(apString[3]);
                                    wifiTestListener.onFindAp(wifiBean);
                                } else {
                                    Log.e("WIFI_FINDAP","apString.length <3 !!!");
                                }
                            }

                             */
                            break;

                        case WIFI_SCANFINISH:
                            Log.d("WIFI_SCANFINISH",resultBean.getResultParams());
                            /*
                            if(wifiTestListener != null) {
                                String[] scanfinishString = resultBean.getResultParams().split("\\|");
                                if (scanfinishString[0].equals("1"))
                                    wifiTestListener.onScanFinish(true,"");
                                else if(scanfinishString[0].equals("0")) {
                                    if(scanfinishString.length > 1)
                                        wifiTestListener.onScanFinish(false,scanfinishString[1]);
                                    else
                                        wifiTestListener.onScanFinish(false,"");
                                } else {
                                    Log.w("WIFI_SCANFINISH","Unknown result : " + resultBean.getResultParams());
                                }
                            }

                             */
                            break;

                        case WIFI_CONNECT:
                            Log.d("WIFI_CONNECT",resultBean.getResultParams());
                            /*
                            if(wifiTestListener != null) {
                                String[] connectResultString = resultBean.getResultParams().split("\\|");
                                if (connectResultString[0].equals("1"))
                                    wifiTestListener.onConnect(true,"");
                                else if(connectResultString[0].equals("0")) {
                                    if(connectResultString.length > 1)
                                        wifiTestListener.onConnect(false,connectResultString[1]);
                                    else
                                        wifiTestListener.onConnect(false,"");
                                } else {
                                    Log.w("WIFI_CONNECT","Unknown result : " + resultBean.getResultParams());
                                }
                            }

                             */
                            break;

                        case WIFI_STOPSCAN:
                            Log.d("WIFI_STOPSCAN",resultBean.getResultParams());
                            /*
                            if(wifiTestListener != null) {
                                String[] stopScanString = resultBean.getResultParams().split("\\|");
                                if (stopScanString[0].equals("1"))
                                    wifiTestListener.onStopScan(true,"");
                                else if(stopScanString[0].equals("0")) {
                                    if(stopScanString.length > 1)
                                        wifiTestListener.onStopScan(false,stopScanString[1]);
                                    else
                                        wifiTestListener.onStopScan(false,"");
                                } else {
                                    Log.w("WIFI_STOPSCAN","Unknown result : " + resultBean.getResultParams());
                                }
                            }

                             */
                            break;

                        case WIFI_QUERYSTATE:
                            Log.d("WIFI_QUERYSTATE",resultBean.getResultParams());
                            /*
                            String[] stateStrings = resultBean.getResultParams().split("\\|");
                            if(wifiTestListener != null) {
                                if (stateStrings[0].equals("1")) {
                                    if (stateStrings.length >= 4) {
                                        WifiBean wifiBean = new WifiBean();
                                        wifiBean.setSignalLevel(stateStrings[1]);
                                        wifiBean.set_rxBandWidth(stateStrings[2]);
                                        wifiBean.set_txBandWidth(stateStrings[3]);
                                        wifiTestListener.onQueryState(true, wifiBean);
                                    } else {
                                        Log.e("WIFI_QUERYSTATE","stateStrings.length < 4!!");
                                    }
                                } else if(stateStrings[0].equals("0")) {
                                        wifiTestListener.onQueryState(false,null);
                                } else {
                                    Log.w("WIFI_QUERYSTATE","Unknown result : " + resultBean.getResultParams());
                                }
                            }

                             */
                            break;

                        case WIFI_ERROR:
                            /*
                            if(wifiTestListener != null) {
                                wifiTestListener.getError(resultBean.getResultParams());
                            }

                             */
                            break;

                        case MODE_SINGLELANINTERNAL:
                            Log.d("MODE_SINGLELANINTERNAL",resultBean.getResultParams());
                            if(setModeListener != null) {
                                doSetModeResult(ProjectUtil.SetModeEnum.SingleLanInternal,resultBean.getResultParams());
                            }
                            break;

                        case MODE_SINGLELANEXTERNAL:
                            Log.d("MODE_SINGLELANEXTERNAL",resultBean.getResultParams());
                            if(setModeListener != null) {
                                doSetModeResult(ProjectUtil.SetModeEnum.SingleLanExternal,resultBean.getResultParams());
                            }
                            break;

                        case MODE_DOUBLELAN:
                            Log.d("MODE_DOUBLELAN",resultBean.getResultParams());
                            if(setModeListener != null) {
                                doSetModeResult(ProjectUtil.SetModeEnum.DoubleLan,resultBean.getResultParams());
                            }
                            break;

                        case MODE_WIFIDHCP:
                            Log.d("MODE_WIFIDHCP",resultBean.getResultParams());
                            if(setModeListener != null) {
                                doSetModeResult(ProjectUtil.SetModeEnum.WifiDHCP,resultBean.getResultParams());
                            }
                            break;
                        case MODE_LANANDWIFI:
                            Log.d("MODE_LANANDWIFI",resultBean.getResultParams());
                            if(setModeListener != null) {
                                doSetModeResult(ProjectUtil.SetModeEnum.LanAndWifi,resultBean.getResultParams());
                            }
                            break;
                        case MODE_QUERY:
                            Log.d("MODE_QUERY",resultBean.getResultParams());
                            if (setModeListener != null) {
                                switch (resultBean.getResultParams()) {
                                    case "0":
                                        setModeListener.onGetModeQuery(ProjectUtil.SetModeEnum.SingleLanInternal);
                                        break;
                                    case "1":
                                        setModeListener.onGetModeQuery(ProjectUtil.SetModeEnum.SingleLanExternal);
                                        break;
                                    case "2":
                                        setModeListener.onGetModeQuery(ProjectUtil.SetModeEnum.DoubleLan);
                                        break;
                                    case "3":
                                        setModeListener.onGetModeQuery(ProjectUtil.SetModeEnum.WifiDHCP);
                                        break;
                                    case "4":
                                        setModeListener.onGetModeQuery(ProjectUtil.SetModeEnum.LanAndWifi);
                                        break;
                                    default:
                                        setModeListener.onGetModeQuery(null);
                                        break;
                                }
                            }
                            break;
                        case MODE_UPDATEBLE:
                            Log.d("MODE_UPDATEBLE","");
                            if (setModeListener != null) {
                               setModeListener.onUpdateBle();
                            }
                            break;
                        default:
                            break;
                    }
            }
        }
    };

    private Observable<CommunicateEvent> observable = null;

    private long boxVersionCode = 0l;
    private String connectedDeviceName = "";
    private AccessType mAccessType = AccessType.ACCESS_DHCP;

    private MainPresenterListener.DeviceSelectListener deviceSelectListener;
    private MainPresenterListener.AccessSelectListener accessSelectListener;
    private MainPresenterListener.SpeedTestListener speedTestListener;
    private MainPresenterListener.NetToolsListener netToolsListener;
    private MainPresenterListener.IperfListener iperfListener;
    private MainPresenterListener.BoxSettingListener boxSettingListener;
    private MainPresenterListener.IptvTestListener iptvTestListener;
    private MainPresenterListener.WifiTestListener wifiTestListener;

    //add by hzj at 2018.12.4
    private MainPresenterListener.WebTestListener webTestListener;
    private MainPresenterListener.DnsTestListener dnsTestListener;
    //add by hzj at 2018.12.13
    private MainPresenterListener.FileManagerListener fileManagerListener;

    //add by hzj on 20191018
    private MainPresenterListener.ExportLogListener exportLogListener;

    private MainPresenterListener.SetModeListener setModeListener;

    public MainPresenter(Context context, MainActivity view) {
        this.context = context;
        this.mainView = view;
        this.communicateModel = new CommunicateModel(context);
        this.communicateModel.stopDiscovery();
    }

    public void setDeviceSelectListener(@NonNull MainPresenterListener.DeviceSelectListener deviceSelectListener) {
        this.deviceSelectListener = deviceSelectListener;
    }

    public void removeDeviceSelectListener() {
        this.deviceSelectListener = null;
    }

    public void setAccessSelectListener(@NonNull MainPresenterListener.AccessSelectListener accessSelectListener) {
        this.accessSelectListener = accessSelectListener;
    }

    public void removeAccessSelectListener() {
        this.accessSelectListener = null;
    }

    public void setSpeedTestListener(@NonNull MainPresenterListener.SpeedTestListener speedTestListener) {
        this.speedTestListener = speedTestListener;
    }

    public void removeSpeedTestListener() {
        this.speedTestListener = null;
    }

    public void setNetToolsListener(MainPresenterListener.NetToolsListener netToolsListener) {
        this.netToolsListener = netToolsListener;
    }

    public void removeNetToolsListener() {
        this.netToolsListener = null;
    }

    public void setIperfListener(MainPresenterListener.IperfListener iperfListener) {
        this.iperfListener = iperfListener;
    }

    public void removeIperfListener() {
        this.iperfListener = null;
    }

    public void removeBoxSettingListener() {
        this.boxSettingListener = null;
    }

    public void setBoxSettingListener(MainPresenterListener.BoxSettingListener listener) {
        this.boxSettingListener = listener;
    }

    public void setIptvTestListener(MainPresenterListener.IptvTestListener listener) {
        this.iptvTestListener = listener;
    }

    public void removeIptvTestListener() {
        this.iptvTestListener = null;
    }

    //add by hzj at 2018.12.4
    public void setWebTestListener(MainPresenterListener.WebTestListener listener){
        this.webTestListener = listener;
    }

    public void removeWebTestListener(){
        this.webTestListener = null;
    }

    public void setDnsTestListener(MainPresenterListener.DnsTestListener listener){
        this.dnsTestListener = listener;
    }

    public void removeDnsTestListener(){
        this.dnsTestListener = null;
    }

    public void setFileManagerListener(MainPresenterListener.FileManagerListener listener){
        this.fileManagerListener = listener;
    }
    public void removeFileManagerListener(){this.fileManagerListener = null;}

    //add by hzj on20191018
    public void setExportLogListener(MainPresenterListener.ExportLogListener listener){
        this.exportLogListener = listener;
    }
    public void removeExportLogListener(){
        this.exportLogListener = null;
    }

    public void setWifiTestListener(MainPresenterListener.WifiTestListener listener) {
        this.wifiTestListener = listener;
    }

    public void removeWifiTestListener() {
        this.wifiTestListener = null;
    }

    public void setSetModeListener(MainPresenterListener.SetModeListener listener) {
        this.setModeListener = listener;
    }

    public void removeSetModeListener() {
        this.setModeListener = null;
    }

    public void setAccessType(AccessType accessType) {
        this.mAccessType = accessType;
    }

    public void start() {
        if(observable == null)
            observable = communicateModel.getResultBeanObservable();
        observable.subscribe(subscriber);
    }

    public void stop() {
        if(observable != null) {
            observable.unsafeSubscribe(subscriber);
            observable = null;
        }
        communicateModel.close();
    }

    public void doSelfStartScan(){deviceSelectListener.doSelfStartScan();}

    //////////////////////////////

    public void doStartDiscovery() {
        mainView.updateTestState("正在搜索设备..");
        communicateModel.startDiscovery();
    }

    public void doStopDiscovery() {
        mainView.updateTestState("扫描完毕");
        communicateModel.stopDiscovery();
    }

    public void doDisconnect() {
        communicateModel.disconnect();
    }

    public void doConnectByDeviceMac(String[] deviceAddresses) {
        mainView.updateTestState("正在连接设备，请稍候");
        connectedDeviceName = "";
        communicateModel.connect(deviceAddresses);
    }

    public void doConnectFindedDevice(DeviceBean deviceBean) {
        mainView.updateTestState("正在连接设备，请稍候");
        connectedDeviceName = "";
        communicateModel.connectFindedDevice(deviceBean);
    }

    public void doDeviceSelectComplete() {
        mainView.showAccessDialog();
        //mainView.showModeDialog();
    }

    public void doSendCommand(String commandType,String detail) {
        communicateModel.sendCommand(commandType,detail);
    }

    public void doSendCommand(String commandType,byte[] detail) {
        communicateModel.sendCommand(commandType,detail);
    }

    public void doSetModeSuccess(ProjectUtil.SetModeEnum testMode) {
        mainView.doShowTestMode(testMode);
    }

    public void doBacktoDeviceSelect() {
        communicateModel.disconnect();
        mainView.showDeviceSelectDialog();
    }
/*
    public void doShowAPInfo(WifiBean wifiBean) {
        mainView.doShowAPInfo(wifiBean);
    }

    public void doAPConnected(WifiBean wifiBean) {mainView.doAPConnected(wifiBean);}

 */

    public void doSendBoxApp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] filedata = ProjectUtil.getFileData("/sdcard/shwangce/box.apk");
                if(filedata.length >0) {
                    doSendCommand(CommandValue.BOXAPP_RECEIVE,filedata);
                }
            }
        }).start();
    }

    public void doBoxAppUpdate() {
        doSendCommand(CommandValue.BOXAPP_UPDATE,"");
    }

    public void doBoxOTAUpdate() {
        doSendCommand(CommandValue.OTA_UPDATE,"");
    }

    public void doQuit() {
        stop();
        mainView.Exit();
    }

    private void doSetModeResult(ProjectUtil.SetModeEnum mode, String resultString) {
        if(setModeListener != null) {
            String[] params = resultString.split("\\|");
            ProjectUtil.SetModeResultEnum result = null;
            String message = "";
            if(params.length >= 1) {
                switch (params[0]) {
                    case "0":
                        result = ProjectUtil.SetModeResultEnum.Fail;
                        break;
                    case "1":
                        result = ProjectUtil.SetModeResultEnum.Success;
                        break;
                    case "2":
                        result = ProjectUtil.SetModeResultEnum.Reboot;
                        break;
                    default:
                        break;
                }
                if (params.length >= 2)
                    message = params[1];
            }
            setModeListener.onSetModeResult(mode,result,message);
        }
    }
}
