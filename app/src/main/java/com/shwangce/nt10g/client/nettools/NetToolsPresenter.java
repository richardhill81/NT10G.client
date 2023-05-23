package com.shwangce.nt10g.client.nettools;

import com.shwangce.nt10g.client.library.ControlFrame.CommandValue;
import com.shwangce.nt10g.client.main.MainPresenter;
import com.shwangce.nt10g.client.main.MainPresenterListener;
import com.shwangce.nt10g.client.util.Log;

import java.util.List;


/**
 * Created by Administrator on 2017/3/1 0001.
 */

public class NetToolsPresenter implements NetToolsContract.Presenter{

    private final NetToolsContract.View mView;
    private final MainPresenter mainPresenter;
    private final MainPresenterListener.NetToolsListener netToolsListener = new MainPresenterListener.NetToolsListener() {
        @Override
        public void onNetToolsInfo(String info) {
            mView.doUpdateProgressInfo(info);
        }

        @Override
        public void onNetToolsInfoNanJing(String info) {
            mView.doNanJingPingAndTraceroute(info);
        }


        @Override
        public void onNetToolsInfoNanJingRePing(String info) {
            mView.doUpdateProgressInfoNanJingRePing(info);
        }

        @Override
        public void onTimerScheduleResult(String info) {
            mView.doUpdateTimerScheduleResult(info);

        }

        @Override
        public void onNetToolsComplete() {
            mView.doTestComplete();
        }

        @Override
        public void onNetToolsCompleteNanjing(String info) {
            mView.doTestCompleteNanjing(info);
        }

        @Override
        public void onNetToolsFail(String failString) {
            mView.doTestFail(failString);
        }

        @Override
        public void onNetToolsGetIPFail(String failString) {
            mView.doGetIpFail(failString);
        }
    };
    private final MainPresenterListener.IperfListener iperfListener = new MainPresenterListener.IperfListener() {
        @Override
        public void onIperfDownloading(String bandwidth) {
            mView.doShowIperfDownloadBandwidth(bandwidth);
        }

        @Override
        public void onIperfDownloaded(String bandwidth) {
            mView.doIperfDownloadComplete(bandwidth);
        }

        @Override
        public void onIperfUploading(String bandwidth) {
            mView.doShowIperfUploadBandwidth(bandwidth);
        }

        @Override
        public void onIperfUploaded(String bandwidth) {
            mView.doIperfUploadComplete(bandwidth);
        }

        @Override
        public void onIperfError(String failString) {
            mView.doIperfTestFail(failString);
        }

        @Override
        public void onIperfComplete() {
            mView.doIperfTestComplete();
        }
    };

    //add by hzj at 2018.12.4
    private MainPresenterListener.WebTestListener webTestListener = new MainPresenterListener.WebTestListener() {
        @Override
        public void showWebTest_ResolveSuccess(String reultInfo) {
            String[] infoArr = reultInfo.split("\\|");
            int number = Integer.parseInt(infoArr[0]);

            String ip_str = infoArr[1];
            String resolveTimeDelay = infoArr[2];
            String title = infoArr[3];

            mView.doWebTestResolveSuccess(number,ip_str,resolveTimeDelay,title);

        }

        @Override
        public void showWebTest_ResolveError(String reultInfo) {
            String[] infoArr = reultInfo.split("\\|");
            int number = Integer.parseInt(infoArr[0]);
            String errorInfo = infoArr[1];
            mView.doWebTestResolveError(number,errorInfo);
        }

        @Override
        public void showWebTest_PingProcess(String reultInfo) {
            String[] infoArr = reultInfo.split("\\|");
            int number = Integer.parseInt(infoArr[0]);
            int childNumber = Integer.parseInt(infoArr[1]);
            String processInfo = infoArr[2];
            mView.doWebTestPingProcess(number,childNumber,processInfo);

        }

        @Override
        public void showWebTest_PingSuccess(String reultInfo) {
            String[] infoArr = reultInfo.split("\\|");
            int number = Integer.parseInt(infoArr[0]);
            int childNumber = Integer.parseInt(infoArr[1]);
            String result = infoArr[2];
            mView.doWebTestPingSucess(number,childNumber,result);
        }

        @Override
        public void showWebTest_PingError(String reultInfo) {
            String[] infoArr = reultInfo.split("\\|");
            int number = Integer.parseInt(infoArr[0]);
            int childNumber = Integer.parseInt(infoArr[1]);
            String errorInfo = infoArr[2];
            mView.doWebTestPingError(number,childNumber,errorInfo);
        }

        @Override
        public void showWebTest_TracerouteProcess(String reultInfo) {
            String[] infoArr = reultInfo.split("\\|");
            int number = Integer.parseInt(infoArr[0]);
            String info = infoArr[1];
            mView.doWebTestTracerouteProcess(number,info);
        }

        @Override
        public void showWebTest_TracerouteSuccess(String reultInfo) {
            String[] infoArr = reultInfo.split("\\|");
            int number = Integer.parseInt(infoArr[0]);
            String info = infoArr[1];
            mView.doWebTestTracerouteSucess(number,info);
        }

        @Override
        public void showWebTest_TracerouteError(String reultInfo) {
            String[] infoArr = reultInfo.split("\\|");
            int number = Integer.parseInt(infoArr[0]);
            String errorInfo = infoArr[1];
            mView.doWebTestTracerouteError(number,errorInfo);
        }

        @Override
        public void showWebTest_WebDelaySuccess(String reultInfo) {
            String[] infoArr = reultInfo.split("\\|");
            int number = Integer.parseInt(infoArr[0]);
            String timeInfo = "打开网页延时时长：" + infoArr[1] + "ms";
            mView.doWebTestWebDelaySuccess(number,timeInfo);
        }

        @Override
        public void showWebTest_WebDelayError(String reultInfo) {
            String[] infoArr = reultInfo.split("\\|");
            int number = Integer.parseInt(infoArr[0]);
            String erorInfo = infoArr[1];
            mView.doWebTestWebDelayError(number,erorInfo);
        }
    };

    private MainPresenterListener.DnsTestListener dnsTestListener = new MainPresenterListener.DnsTestListener() {
        @Override
        public void showDnsTest_info(String reultInfo) {

        }

        @Override
        public void showDnsTest_success(String reultInfo) {

        }

        @Override
        public void showDnsTest_error(String reultInfo) {

        }

    };

    public NetToolsPresenter(NetToolsContract.View view, final MainPresenter mainPresenter) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void start() {
        mainPresenter.setNetToolsListener(netToolsListener);
        mainPresenter.setIperfListener(iperfListener);

        //add by hzj at 2018.12.4
        mainPresenter.setWebTestListener(webTestListener);
        mainPresenter.setDnsTestListener(dnsTestListener);
    }

    @Override
    public void stop() {
        mainPresenter.removeNetToolsListener();
        mainPresenter.removeIperfListener();

        //add by hzj at 2018.12.4
        mainPresenter.removeWebTestListener();
        mainPresenter.removeDnsTestListener();
    }

    @Override
    public void doStartPing(String desthost, String count) {
        String additionString = desthost + "|" + count;
        Log.i("点击ping开始","type:"+CommandValue.NETTOOLS_PING+"----"+"data:"+additionString);
        mainPresenter.doSendCommand(CommandValue.NETTOOLS_PING,additionString);
    }

    @Override
    public void doStartRePing(String desthost, String count) {
        String additionString = desthost + "|" + count;
        mainPresenter.doSendCommand(CommandValue.NETTOOLS_REPING,additionString);
    }

    @Override
    public void doStartTraceroute(String desthost) {
        mainPresenter.doSendCommand(CommandValue.NETTOOLS_TRACEROUTE,desthost);
    }

    @Override
    public void doStartIperf(String serverhost, int testtime, int testparallel) {
        String detail = serverhost + "|" + testtime + "|" + testparallel;
        mainPresenter.doSendCommand(CommandValue.IPERF_START,detail);
    }

    @Override
    public void doStartPingAndTraceroute(String[] desthost) {
        //String additionString =null;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < desthost.length; ++i) {
            buf.append(desthost[i]);
            buf.append("|");
        }
        String additionString = buf.toString();
        mainPresenter.doSendCommand(CommandValue.NETTOOLS_PINGANDTRACEROUTE,additionString);
    }

    @Override
    public void doStartTimerScheduleTest(long StartTimestamp,long DeadTimestamp,String Interval,String Threshold,String desthost,String storage) {
        StringBuffer buf = new StringBuffer();
            buf.append(StartTimestamp+"|"+DeadTimestamp+"|"+Interval+"|"+Threshold+"|"+desthost+"|"+storage);
        String additionString = buf.toString();
        mainPresenter.doSendCommand(CommandValue.TIMERSCHEDULESTART,additionString);
    }

    @Override
    public void doSearchTimerSchedule() {

        mainPresenter.doSendCommand(CommandValue.TIMERSCHEDULESEARCH,"");
    }

    @Override
    public void doDeleteTimerSchedule() {

        mainPresenter.doSendCommand(CommandValue.TIMERSCHEDULEDELETE,"");
    }

    //add by hzj at 2018.12.3


    @Override
    public void doStartWebTest(List<String> webNames) {
        StringBuffer buffer = new StringBuffer();
        for (String webName:webNames) {
            if(webName.equals(""))
                webName = "www.baidu.com";
            buffer.append(webName + "|");
        }
        buffer.deleteCharAt(buffer.length()-1);
        String additionString = buffer.toString();
        Log.d("doStartWebTest()","CommandValue.WEBTEST:" + additionString);
        mainPresenter.doSendCommand(CommandValue.WEBTEST,additionString);
    }

    @Override
    public void doStartDnsTest(List<String> webNames) {
        StringBuffer buffer = new StringBuffer();
        for (String webName:webNames) {
            buffer.append(webName + "|");
        }
        buffer.deleteCharAt(buffer.length()-1);
        String additionString = buffer.toString();
        Log.d("doStartDnsTest()","CommandValue.DNSTEST:" + additionString);
        mainPresenter.doSendCommand(CommandValue.DNSTEST,additionString);
    }
}
