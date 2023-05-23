package com.shwangce.nt10g.client.nettools;

import com.shwangce.nt10g.client.BasePresenter;
import com.shwangce.nt10g.client.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public interface NetToolsContract {
    interface View extends BaseView<Presenter> {
        void doUpdateProgressInfo(String info);

        void doTestComplete();
        void doTestFail(String failString);
        void doTestCompleteNanjing(String info);
        void doGetIpFail(String failString);

        void doShowIperfDownloadBandwidth(String bandwidth);
        void doShowIperfUploadBandwidth(String bandwidth);
        void doIperfDownloadComplete(String bandwidth);
        void doIperfUploadComplete(String bandwidth);
        void doIperfTestComplete();
        void doIperfTestFail(String failReason);

        void doNanJingPingAndTraceroute(String info);

        void doUpdateProgressInfoNanJingRePing(String info);

        void doUpdateTimerScheduleResult(String info);

        void doWebTestResolveError(int number, String errorInfo);
        void doWebTestResolveSuccess(int number, String ip, String resolveTimeDelay, String title);

        void doWebTestPingProcess(int number, int childNumber, String info);
        void doWebTestPingError(int number, int childNumber, String errorInfo);
        void doWebTestPingSucess(int number, int childNumber, String info);

        void doWebTestTracerouteProcess(int number, String info);
        void doWebTestTracerouteError(int number, String errorInfo);
        void doWebTestTracerouteSucess(int number, String info);

        void doWebTestWebDelayError(int number, String errorInfo);
        void doWebTestWebDelaySuccess(int number, String info);
    }

    interface Presenter extends BasePresenter {
        void doStartPing(String desthost, String count);
        void doStartRePing(String desthost, String count);
        void doStartTraceroute(String desthost);
        void doStartIperf(String serverhost, int testtime, int testparallel);
        void doStartPingAndTraceroute(String[] desthost);
        void doStartTimerScheduleTest(long StartTimestamp, long DeadTimestamp, String Interval, String Threshold, String desthost, String sdcard);
        void doSearchTimerSchedule();
        void doDeleteTimerSchedule();
        //add by hzj at2018.12.3
        void doStartWebTest(List<String> webNames);
        void doStartDnsTest(List<String> webNames);
    }
}
