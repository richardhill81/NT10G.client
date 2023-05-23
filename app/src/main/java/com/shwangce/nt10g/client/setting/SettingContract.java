package com.shwangce.nt10g.client.setting;

import com.shwangce.nt10g.client.BasePresenter;
import com.shwangce.nt10g.client.BaseView;

/**
 * Created by Administrator on 2017/2/24 0024.
 */

public interface SettingContract {
    interface View extends BaseView<Presenter> {
        void doSetBoxWorkModeResult(String result);

        void doShowQueryMemoryResult(String result);
        void doShowQueryMemoryError(String errorInfo);

        void doShowQueryPackageFileResult(String result);
        void doShowQueryPackageFileError(String errorInfo);

        void doShowQueryLogFileResult(String result);
        void doShowQueryLogFileError(String errorInfo);

        void doShowDeleteFileResult(String result);
        void doShowDeleteFileError(String errorInfo);
        void doShowDeleteFileProcess(String procInfo);

        void doSetQueryLogSize(String size);
        void doOnExportLog(String data);
        void doShowExportLogComplete(String info);
        void doShowExportLogError(String errorInfo);

        // add by hzj on 20191023
        void doTaskTimeOut();       //接收数据超时
        void doShowExportProgress();        //展示导出文件进度

    }

    interface Presenter extends BasePresenter {
        void doSetBoxWorkMode(String workmode);
        void doStartQueryMemory();
        void doStartQueryPackageFile();
        void doStartQueryLogFile();
        void doDeleteFile(String fileName);
        // add by hzj on 20191016
        void doExportLog();
        void doQueryLogFileSize();
        void doStopExportLog();//

        void setTaskTimeOut(int timeOut);
        void stopTaskTimeOut();

        void startTimerShowExportLogFileProgress();
        void stopTimerShowExportLogFileProgress();
    }
}
