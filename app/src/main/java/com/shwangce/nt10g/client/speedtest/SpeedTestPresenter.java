package com.shwangce.nt10g.client.speedtest;

import static com.shwangce.nt10g.client.util.ProjectUtil.channel;
import static com.shwangce.nt10g.client.util.ProjectUtil.df0;
import static com.shwangce.nt10g.client.util.ProjectUtil.df1;
import static com.shwangce.nt10g.client.util.ProjectUtil.df2;

import com.shwangce.nt10g.client.library.ControlFrame.CommandValue;
import com.shwangce.nt10g.client.library.ControlFrame.ResultBean;
import com.shwangce.nt10g.client.main.MainPresenter;
import com.shwangce.nt10g.client.main.MainPresenterListener;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Administrator on 2017/3/1 0001.
 */

public class SpeedTestPresenter implements SpeedTestContract.Presenter{

    public final static int DOWNLOADING = 1;
    public final static int UPLOADING = 2;
    public final static int DOWNLOADED = 3;
    public final static int UPLOADED = 4;

    private final SpeedTestContract.View mView;
    private final MainPresenter mainPresenter;
    private final MainPresenterListener.SpeedTestListener speedTestListener = new MainPresenterListener.SpeedTestListener() {
        @Override
        public void onTestFail(String failreason) {
            mView.doTestFail(failreason);
        }

        @Override
        public void onUserInfo(String userString) {
            switch (testKind) {
                case GD10000 -> {
                    String[] s = userString.split("\\|");
                    int ll = 0;
                    String vvv = "";
                    GD10000_serveripsBean bean = new GD10000_serveripsBean();
                    for (String value : s) {
                        ll = value.indexOf(":");
                        vvv = value.substring(ll + 1);
                        if (value.startsWith("bandwidthDown")) {
                            bean.setBandwidthDown(vvv);
                        } else if (value.startsWith("bandwidthUp")) {
                            bean.setBandwidthUp(vvv);
                        } else if (value.startsWith("account")) {
                            bean.setAccount(vvv);
                        } else if (value.startsWith("city")) {
                            bean.setCity(vvv);
                        } else if (value.startsWith("localIp")) {
                            bean.setLocalIp(vvv);
                        } else if (value.startsWith("serverIpDown")) {
                            bean.setServerIpDown(vvv);
                        }
                    }
                    mView.updateUserInfo_GD10000(bean);
                }
                case JIANGSU10000 -> {
                    js10000_userAuthBean = JS10000_UserAuthBean.getBeanByString(userString);
                    if (js10000_userAuthBean != null)
                        mView.updateUserInfo_JS10000(js10000_userAuthBean);
                    else
                        mView.updateUserInfo_JS10000(userString);
                }
            }
        }

        @Override
        public void onTestProgressInfo(String msg) {
            mView.updateTestProgressInfo(msg);
        }

        @Override
        public void onClientInfo(String info) {
            mView.updateClientInfo(info);
        }

        @Override
        public void onServerInfo(String info) {
            mView.updateServerInfo(info);
        }

        @Override
        public void onSpeedTestResult(ResultBean resultBean) {
            switch (testKind) {
                case HUNAN10000:
                    if(hunanTestStep == Hunan10000.HunanTestStep.DOWNLOAD) {
                        hunanTestStep = Hunan10000.HunanTestStep.UPLOAD;
                        mainPresenter.doSendCommand(CommandValue.SPEEDTEST_HTTPUPLOAD,Hunan10000.getUploadsString());
                        mView.updateTestProgressInfo("正在准备上传测试...");
                    } else {
                        doSpeedTestResult(resultBean);
                    }
                    break;

                case HTTP_DOWNLOAD:
                    if(ProjectUtil.Httpuploadurl.isEmpty()) {
                        doSpeedTestResult(resultBean);
                    } else {
                        testKind = SpeedTestKind.HTTP_UPLOAD;
                        mainPresenter.doSendCommand(CommandValue.SPEEDTEST_HTTPUPLOAD, ProjectUtil.Httpuploadurl);
                        mView.updateTestProgressInfo("正在连接上行服务器|上行测试");
                    }
                    break;

                default:
                    doSpeedTestResult(resultBean);
                    break;
            }
        }

        @Override
        public void onSpeedTestSpeed(ResultBean resultBean, int type) {
            doSpeedTestSpeed(resultBean,type);
        }
    };

    private float downloadPeak,downloadAvg;
    private float uploadPeak,uploadAvg;

    private float downloadDelay,downloadLoss;   //下行延迟、下行丢包率
    private float uploadDelay,uploadLoss;       //上行延迟、上行丢包率

    //20240121 Add
    private float d_recvOpticalPower,u_recvOpticalPower;    //接收光功率
    private SpeedTestKind testKind;
    private Hunan10000.HunanTestStep hunanTestStep = Hunan10000.HunanTestStep.DOWNLOAD;
    private JS10000_UserAuthBean js10000_userAuthBean = null;
    public SpeedTestPresenter(SpeedTestContract.View view,final MainPresenter mainPresenter) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void start() {
        mainPresenter.setSpeedTestListener(speedTestListener);
    }

    @Override
    public void stop() {
        mainPresenter.removeSpeedTestListener();
    }

    @Override
    public void startTest(SpeedTestKind testKind,String additionString) {
        this.testKind = testKind;
        downloadAvg = 0f;
        downloadPeak = 0f;
        downloadDelay = -999f;
        downloadLoss = 0f;
        d_recvOpticalPower = 999f;
        uploadAvg = 0f;
        uploadPeak = 0f;
        uploadDelay = -999f;
        uploadLoss = 0f;
        u_recvOpticalPower = 999f;
        switch (testKind) {
            case HTTP_DOWNLOAD -> {
                mView.updateServerInfo(additionString);
                mainPresenter.doSendCommand(CommandValue.SPEEDTEST_HTTPDOWNLOAD, additionString);
                mView.updateTestProgressInfo("正在连接下载服务器");
            }
            case FTP_DOWNLOAD -> {
                int l = additionString.indexOf("|");
                if (l > 0) {
                    String server = "Ftp://" + additionString.substring(0, l);
                    mView.updateServerInfo(server);
                }
                mainPresenter.doSendCommand(CommandValue.SPEEDTEST_FTPDOWNLOAD, additionString);
                mView.updateTestProgressInfo("正在连接下载服务器");
            }
            case HXBOX -> {
                mView.updateServerInfo("华夏测速平台");
                mView.updateTestProgressInfo("正在测试，请稍候!");
                mView.doShowTesting();
                mainPresenter.doSendCommand(CommandValue.SPEEDTEST_HXBOX, additionString);
            }
            case TCP_SPEEDTEST -> {
                mainPresenter.doSendCommand(CommandValue.SPEEDTEST_TCPSPEEDTEST, "");
                mView.updateTestProgressInfo("测速准备...");
            }
            case GD10000 -> {
                mView.updateServerInfo("广东电信测速");
                mainPresenter.doSendCommand(CommandValue.SPEEDTEST_GD10000, "");
                mView.updateTestProgressInfo("测速准备...");
            }
            case HUNAN10000 -> {
                String downloadUrlInfo = Hunan10000.getDownloadUrlsInfo();
                String uploadUrlInfo = Hunan10000.getUploadUrlsInfo();
                hunanTestStep = Hunan10000.HunanTestStep.DOWNLOAD;
                mView.updateServerInfo(downloadUrlInfo + "\n" + uploadUrlInfo);
                mainPresenter.doSendCommand(CommandValue.SPEEDTEST_HTTPDOWNLOAD, Hunan10000.getDownloadsString());
                mView.updateTestProgressInfo("测速准备...");
            }
            case JIANGSU10000 -> {
                if ("js10000".equals(channel))
                    mView.updateServerInfo("江苏电信测速");
                else if ("ah10086".equals(channel))
                    mView.updateServerInfo("安徽移动测速");
                else
                    mView.updateServerInfo("江苏电信测速");
                mainPresenter.doSendCommand(CommandValue.JIANGSU10000_SPEEDTEST, "");
                mView.updateTestProgressInfo("测速准备...");
            }
        }
    }

    private void doSpeedTestSpeed(ResultBean resultBean,int type) {
        //20231012 Add 延迟、丢包率
        //20240121 Add 光功率
        String dataString = resultBean.getResultParams();
        if(dataString.contains("|"))          {    //结论中包含|字符
            Log.d("doSpeedTestSpeed","dataString is " + dataString);
            String[] d = dataString.split("\\|");
            float sp = Float.parseFloat(d[0]);
            switch (type) {
                case DOWNLOADING -> {
                    if (sp > downloadPeak) downloadPeak = sp;
                    mView.doShowDownloadSpeed(sp);
                }
                case UPLOADING -> {
                    if (sp > uploadPeak) uploadPeak = sp;
                    mView.doShowUploadSpeed(sp);
                }
                case DOWNLOADED -> {
                    downloadAvg = sp;
                    Log.d("doSpeedTestSpeed","d.length is " + d.length);
                    if(d.length >= 3) {
                        downloadDelay = Float.parseFloat(d[1]);
                        Log.d("doSpeedTestSpeed","download_delay is " + downloadDelay);
                        downloadLoss = Float.parseFloat(d[2]) * 100;
                        Log.d("doSpeedTestSpeed","download_loss is " + downloadLoss + "%");
                        //20240121 Add
                        if(d.length >= 4) {
                            try {
                                d_recvOpticalPower = Float.parseFloat(d[3]);
                            } catch (Exception e) {
                                d_recvOpticalPower = 999;
                            }
                            Log.d("doSpeedTestSpeed","downloadOpticalPower is " + d_recvOpticalPower);
                        }
                    }
                    mView.doDownloadTestComplete(downloadAvg, downloadPeak);
                }
                case UPLOADED -> {
                    uploadAvg = sp;
                    Log.d("doSpeedTestSpeed","d.length is " + d.length);
                    if(d.length >= 3) {
                        uploadDelay = Float.parseFloat(d[1]);
                        Log.d("doSpeedTestSpeed","upload_delay is " + uploadDelay);
                        uploadLoss = Float.parseFloat(d[2]) * 100;
                        Log.d("doSpeedTestSpeed","upload_loss is " + uploadLoss + "%");
                        //20240121 Add
                        if(d.length >= 4) {
                            try {
                                u_recvOpticalPower = Float.parseFloat(d[3]);
                            } catch (Exception e) {
                                u_recvOpticalPower = 999;
                            }
                            Log.d("doSpeedTestSpeed","uploadOpticalPower is " + u_recvOpticalPower);
                        }
                    }
                    mView.doUploadTestComplete(uploadAvg, uploadPeak);
                }
            }
        } else {
            float sp = Float.parseFloat(dataString);
            switch (type) {
                case DOWNLOADING -> {
                    if (sp > downloadPeak) downloadPeak = sp;
                    mView.doShowDownloadSpeed(sp);
                }
                case UPLOADING -> {
                    if (sp > uploadPeak) uploadPeak = sp;
                    mView.doShowUploadSpeed(sp);
                }
                case DOWNLOADED -> {
                    downloadAvg = sp;
                    mView.doDownloadTestComplete(downloadAvg, downloadPeak);
                }
                case UPLOADED -> {
                    uploadAvg = sp;
                    mView.doUploadTestComplete(uploadAvg, uploadPeak);
                }
            }
        }

    }




    private void doSpeedTestResult(ResultBean resultBean) {
        SpeedTestResultBean testResultBean = new SpeedTestResultBean();
        switch (testKind) {
            case HXBOX:
                testResultBean.setHxbean(ProjectUtil.hxBoxBean);
                String[] d = resultBean.getResultParams().split("\\|");
                for (int i = 0; i < d.length; i++) {
                    switch (i) {
                        case 0:
                            //modify by hzj on 2019.1.17 修改上海电信测速测试结果显示（2048Kbps → 2.0Mbps）
                            if(Integer.parseInt(d[0]) >= 1024){
                                double b = Integer.parseInt(d[0])/(double)1024;
                                testResultBean.setAvgspeed(d[0] + "Kbps" +"\n(" +df1.format(b) + "Mbps)");
                            }else{
                                testResultBean.setAvgspeed(d[0] + "Kbps");
                            }
//                            testResultBean.setAvgspeed(d[0] + "Kbps");
                            break;
                        case 1:
                            //modify by hzj on 2019.1.17 修改上海电信测速测试结果显示（2048Kbps → 2.0Mbps）
                            if(Integer.parseInt(d[1]) >= 1024){
                                double b = Integer.parseInt(d[1])/(double)1024;
                                testResultBean.setPeakspeed(d[1] + "Kbps" +"\n(" +df1.format(b) + "Mbps)");
                            }else{
                                testResultBean.setPeakspeed(d[1] + "Kbps");
                            }
//                            testResultBean.setPeakspeed(d[1] + "Kbps");
                            break;
                        case 2:
                            //modify by hzj on 2019.1.17 修改上海电信测速测试结果显示（2048Kbps → 2.0Mbps）
                            if(Integer.parseInt(d[2]) >= 1024){
                                double b = Integer.parseInt(d[2])/(double)1024;
                                testResultBean.setAvgupspeed(d[2] + "Kbps" +"\n(" +df1.format(b) + "Mbps)");
                            }else{
                                testResultBean.setAvgupspeed(d[2] + "Kbps");
                            }
//                            testResultBean.setAvgupspeed(d[2] + "Kbps");
                            break;
                        case 3:
                            //modify by hzj on 2019.1.17 修改上海电信测速测试结果显示（2048Kbps → 2.0Mbps）
                            if(Integer.parseInt(d[3]) >= 1024){
                                double b = Integer.parseInt(d[3])/(double)1024;
                                testResultBean.setPeakupspeed(d[3] + "Kbps" +"\n(" +df1.format(b) + "Mbps)");
                            }else{
                                testResultBean.setPeakupspeed(d[3] + "Kbps");
                            }
//                            testResultBean.setPeakupspeed(d[3] + "Kbps");
                            break;
                        case 4:

                            if (d[4].equals("1"))
                                testResultBean.setSpeedresult("合格");
                            else
                                testResultBean.setSpeedresult("不合格");
                            break;
                    }
                }
                break;

            case GD10000:
                testResultBean = null;
                break;

            case JIANGSU10000:
                if(js10000_userAuthBean != null) {
                    String result = "";
                    long upOK = (long)(js10000_userAuthBean.getUpRate() * 0.9);
                    long downOK = (long)(js10000_userAuthBean.getContractRate() * 0.9);
                    long idavg = (long)downloadAvg * 8;
                    long iuavg = (long)uploadAvg * 8;
                    if(idavg < downOK)
                        result = "测试不达标";
                    else
                        result = "测试达标";
                    testResultBean.setSpeedresult(result);
                }
                break;

            case HTTP_DOWNLOAD:
            case HTTP_UPLOAD:
            case FTP_DOWNLOAD:
            case TCP_SPEEDTEST:
            case HUNAN10000:
                testResultBean.setHxbean(ProjectUtil.hxBoxBean);
                if (downloadAvg > 0) {
                    testResultBean.setAvgspeed(ProjectUtil.getBandwidth(downloadAvg));
                    testResultBean.setPeakspeed(ProjectUtil.getBandwidth(downloadPeak));
                } else {
                    testResultBean.setAvgspeed("未测试");
                    testResultBean.setPeakspeed("未测试");
                }
                if (uploadAvg > 0) {
                    testResultBean.setAvgupspeed(ProjectUtil.getBandwidth(uploadAvg));
                    testResultBean.setPeakupspeed(ProjectUtil.getBandwidth(uploadPeak));
                } else {
                    testResultBean.setAvgupspeed("未测试");
                    testResultBean.setPeakupspeed("未测试");
                }
                //2024.03.07,修复downloadDelay与uploadDelay均为0时，不显示延迟
                if(downloadDelay>=0 || uploadDelay>=0) {
                    if(downloadDelay < 0) downloadDelay = 0;
                    if(uploadDelay < 0) uploadDelay = 0;
                    if((downloadDelay + uploadDelay) / 2 < 1) {
                        testResultBean.setNetdelay("<1ms");
                    } else {
                        testResultBean.setNetdelay(df0.format((downloadDelay + uploadDelay) / 2) + "ms");
                    }
                    testResultBean.setNetloss(df2.format((downloadLoss + uploadLoss) / 2) + "%");
                }
                //20240121 Add
                if(d_recvOpticalPower <=0 && u_recvOpticalPower <=0) {
                    testResultBean.setReceiveOpticalPower(df2.format((d_recvOpticalPower + u_recvOpticalPower) / 2) + "dBm");
                } else {
                    testResultBean.setReceiveOpticalPower("——");
                }
                testResultBean.setSpeedresult("测试成功");
                break;
        }
        mView.doTestspeedComplete(testResultBean);
    }
}
