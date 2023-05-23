package com.shwangce.nt10g.client.speedtest;

import com.shwangce.nt10g.client.BasePresenter;
import com.shwangce.nt10g.client.BaseView;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public interface SpeedTestContract {
    interface View extends BaseView<Presenter> {
        void doShowTesting();
        void updateTestProgressInfo(String msg);

        void updateClientInfo(String info);
        void updateServerInfo(String info);
        void updateUserInfo_GD10000(GD10000_serveripsBean bean);
        void updateUserInfo_JS10000(JS10000_UserAuthBean bean);
        void updateUserInfo_JS10000(String userInfo);
        void doShowDownloadSpeed(float speed);
        void doShowUploadSpeed(float speed);

        void doDownloadTestComplete(float downloadAvgSpeed,float downloadPeakSpeed);
        void doUploadTestComplete(float uploadAvgSpeed,float uploadPeakSpeed);

        void doTestspeedComplete(SpeedTestResultBean result);
        void doTestFail(String failreason);

    }

    interface Presenter extends BasePresenter {
        void startTest(SpeedTestKind testKind,String additionString);
    }
}
