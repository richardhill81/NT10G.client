package com.shwangce.nt10g.client.setting;

import androidx.annotation.NonNull;

import com.shwangce.nt10g.client.library.ControlFrame.CommandValue;
import com.shwangce.nt10g.client.main.MainPresenter;
import com.shwangce.nt10g.client.main.MainPresenterListener;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/3/14 0014.
 */

public class SettingPresenter implements SettingContract.Presenter {

    private final SettingContract.View mView;
    private final MainPresenter mainPresenter;
    private final MainPresenterListener.BoxSettingListener boxSettingListener = new MainPresenterListener.BoxSettingListener() {
        @Override
        public void onWorkModeSetResult(String result) {
            ProjectUtil.isFromSetting = false;
            mView.doSetBoxWorkModeResult(result);
        }
    };

    private final MainPresenterListener.FileManagerListener FileManagerListener = new MainPresenterListener.FileManagerListener() {
        @Override
        public void showQueryMemorySuccess(String resultInfo) {
            mView.doShowQueryMemoryResult(resultInfo);
        }

        @Override
        public void showQueryMemoryError(String resultInfo) {
            mView.doShowQueryMemoryError(resultInfo);
        }

        @Override
        public void showQueryMemoryProcess(String resultInfo) {

        }

        @Override
        public void showQueryPackageFileSuccess(String resultInfo) {
            mView.doShowQueryPackageFileResult(resultInfo);
        }

        @Override
        public void showQueryPackageFileError(String resultInfo) {
            mView.doShowQueryPackageFileError(resultInfo);
        }

        @Override
        public void showQueryPackageFileProcess(String resultInfo) {

        }

        @Override
        public void showQueryLogFileSuccess(String resultInfo) {
            mView.doShowQueryLogFileResult(resultInfo);
        }

        @Override
        public void showQueryLogFileError(String resultInfo) {
            mView.doShowQueryLogFileError(resultInfo);
        }

        @Override
        public void showQueryLogFileProcess(String resultInfo) {

        }

        @Override
        public void showDeleteFileSuccess(String resultInfo) {
            mView.doShowDeleteFileResult("刪除文件完成。");
        }

        @Override
        public void showDeleteFileError(String resultInfo) {
            mView.doShowDeleteFileError(resultInfo);
        }

        @Override
        public void showDeleteFileProcess(String resultInfo) {
            mView.doShowDeleteFileProcess(resultInfo);
        }
    };

    private final MainPresenterListener.ExportLogListener exportLogListener = new MainPresenterListener.ExportLogListener() {
        @Override
        public void showQueryLogSize(String size) {
            mView.doSetQueryLogSize(size);
        }

        @Override
        public void onExportLog(String data) {
            mView.doOnExportLog(data);
        }

        @Override
        public void onExportLogComplete(String info) {
            mView.doShowExportLogComplete(info);
        }

        @Override
        public void onExportLogError(String errorInfo) {
            mView.doShowExportLogError(errorInfo);
        }
    };

    public SettingPresenter(SettingContract.View view, @NonNull MainPresenter mainPresenter) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mainPresenter = mainPresenter;
    }

    @Override
    public void start() {
        mainPresenter.setBoxSettingListener(boxSettingListener);
        mainPresenter.setFileManagerListener(FileManagerListener);
        // add by hzj on 20191018
        mainPresenter.setExportLogListener(exportLogListener);
    }

    @Override
    public void stop() {
        mainPresenter.removeBoxSettingListener();
        mainPresenter.removeFileManagerListener();
        // add by hzj on 20191018
        mainPresenter.removeExportLogListener();
    }

    @Override
    public void doSetBoxWorkMode(String workmode) {
        ProjectUtil.isFromSetting = true;
        mainPresenter.doSendCommand(CommandValue.WORKMODE_SETTING,workmode);
    }
    @Override
    public void doStartQueryMemory(){
        mainPresenter.doSendCommand(CommandValue.QUERYMEMORY,"");
    }

    @Override
    public void doStartQueryPackageFile() {
        mainPresenter.doSendCommand(CommandValue.QUERYPACKAGEFILE,"");
    }

    @Override
    public void doStartQueryLogFile() {
        mainPresenter.doSendCommand(CommandValue.QUERYLOGFILE,"");
    }

    @Override
    public void doDeleteFile(String fileName) {
        mainPresenter.doSendCommand(CommandValue.DELETEFILE,fileName);
    }

    @Override
    public void doExportLog() {
        mainPresenter.doSendCommand(CommandValue.EXPORTLOG,"");
    }

    @Override
    public void doQueryLogFileSize() {
        mainPresenter.doSendCommand(CommandValue.QUERYLOGFILESIZE,"");
    }

    @Override
    public void doStopExportLog() {
        mainPresenter.doSendCommand(CommandValue.STOPEXPORTLOG,"");
    }

    private Timer timer;

    @Override
    public void setTaskTimeOut(int timeOut) {
        Log.i("TaskTimeOut","开启超时线程");
        if(timer != null){
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.i("TaskTimeOut","任务超时！");
//                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_TASK_TIMEOUT));
                mView.doTaskTimeOut();
            }
        },timeOut);
    }

    @Override
    public void stopTaskTimeOut() {
        Log.i("TaskTimeOut","停止超时线程");
        if(timer != null){
            timer.cancel();
            timer = null;
        }
    }

    private Timer timer_showExportLogFileProgress;

    @Override
    public void startTimerShowExportLogFileProgress() {
        if(timer_showExportLogFileProgress != null){
            timer_showExportLogFileProgress.cancel();
            timer_showExportLogFileProgress = null;
        }

        timer_showExportLogFileProgress = new Timer();
        timer_showExportLogFileProgress.schedule(new TimerTask() {
            @Override
            public void run() {
//                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SHOW_EXPORT_PROGRESS));
                mView.doShowExportProgress();
            }
        },1000,1000);
    }

    @Override
    public void stopTimerShowExportLogFileProgress() {
        if(timer_showExportLogFileProgress != null){
            timer_showExportLogFileProgress.cancel();
            timer_showExportLogFileProgress = null;
        }
    }


}
