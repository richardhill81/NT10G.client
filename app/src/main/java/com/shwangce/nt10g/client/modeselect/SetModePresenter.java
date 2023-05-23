package com.shwangce.nt10g.client.modeselect;

import static com.shwangce.nt10g.client.util.ProjectUtil.SetModeEnum.DoubleLan;
import static com.shwangce.nt10g.client.util.ProjectUtil.SetModeEnum.LanAndWifi;
import static com.shwangce.nt10g.client.util.ProjectUtil.SetModeEnum.SingleLanExternal;
import static com.shwangce.nt10g.client.util.ProjectUtil.SetModeEnum.SingleLanInternal;
import static com.shwangce.nt10g.client.util.ProjectUtil.SetModeEnum.WifiDHCP;

import androidx.annotation.NonNull;

import com.shwangce.nt10g.client.BasePresenter;
import com.shwangce.nt10g.client.library.ControlFrame.CommandValue;
import com.shwangce.nt10g.client.main.MainPresenter;
import com.shwangce.nt10g.client.main.MainPresenterListener;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public class SetModePresenter implements BasePresenter {
    private final SetModeDialogFragment mView;
    private final MainPresenter mainPresenter;
    private final Timer timer = new Timer();

    //private int currentTask = -1;
    private ProjectUtil.SetModeEnum needMode;

    private TimerTask queryModeTimerTask;
    private TimerTask setModeTimerTask;
    private TimerTask updateBleTimerTask;
    private TimerTask waitRebootTask;
    //private int modeTimerCount = 0;
    //private int updateBleTimerCount = 0;
    //private int rebootTimerCount = 0;
    //private int queryModeTimerCount = 0;
    private int tryUpdateBleCount = 0;

    public SetModePresenter(@NonNull SetModeDialogFragment view, @NonNull MainPresenter mainPresenter) {
        this.mView = view;
        this.mainPresenter = mainPresenter;
        mView.setPresenter(this);
    }

    private final MainPresenterListener.SetModeListener setModeListener = new MainPresenterListener.SetModeListener() {
        @Override
        public void onSetModeResult(ProjectUtil.SetModeEnum mode,ProjectUtil.SetModeResultEnum result, String message) {
           stopSetModeTimer();
           switch (result){
               case Success:
                   doSetModeSuccess(mode);
                   break;

               case Fail:
                   mView.doSetModeFail(mode,message);
                   break;

               case Reboot:
                   startRebootTimer(30);
                   mView.doWaitReboot(mode);
                   tryUpdateBleCount = 0;
                   break;
           }
        }

        @Override
        public void onGetModeQuery(ProjectUtil.SetModeEnum mode) {
            stopQueryModeTimer();
            /*
            if(mode != null) {
                if(needMode == mode) {  //模式一致
                    doSetModeSuccess(needMode);
                    return;
                }
            } else {
                Log.w("onGetModeQuery", "getMode is null!!");
            }
            doSetModeTask(needMode);
             */
        }

        @Override
        public void onUpdateBle() {
            Log.d("onUpdateBle","onUpdateBle");
            stopUpdateBleTimer();
            doSetModeTask(needMode);
        }
    };

    @Override
    public void start(){  mainPresenter.setSetModeListener(setModeListener);  }

    @Override
    public void stop() {  mainPresenter.removeSetModeListener(); }

    public void setMode(ProjectUtil.SetModeEnum testMode) {
        needMode = testMode;
        doSetModeTask(testMode);
        //doGetMode();
    }

    public void doSetModeTask(ProjectUtil.SetModeEnum testMode) {
        //currentTask = getTaskIdBySetModeEnum(testMode);
        switch (testMode) {
            case SingleLanInternal:
                startSetModeTimer(10);
                mainPresenter.doSendCommand(CommandValue.MODE_SINGLELANINTERNAL, "");
                break;
            case SingleLanExternal:
                startSetModeTimer(10);
                mainPresenter.doSendCommand(CommandValue.MODE_SINGLELANEXTERNAL, "");
                break;
            case DoubleLan:
                startSetModeTimer(10);
                mainPresenter.doSendCommand(CommandValue.MODE_DOUBLELAN, "");
                break;
            case WifiDHCP:
                startSetModeTimer(10);
                mainPresenter.doSendCommand(CommandValue.MODE_WIFIDHCP, "");
                break;
            case LanAndWifi:
                startSetModeTimer(10);
                mainPresenter.doSendCommand(CommandValue.MODE_LANANDWIFI, "");
                break;
            //case ITVSimulate:
                //mainPresenter.doSendCommand(CommandValue.MODE_, "");
            //    break;
        }
    }

    public void doGetMode() {
        startQueryModeTimer(5);
        mainPresenter.doSendCommand(CommandValue.MODE_QUERY, "");
    }

    public void doUpdateBle() {
        startUpdateBleTimer(5);
        tryUpdateBleCount++;
        mainPresenter.doSendCommand(CommandValue.MODE_UPDATEBLE,"");
    }

    public String getModeString(ProjectUtil.SetModeEnum mode) {
        switch (mode) {
            case SingleLanInternal:
                return ProjectUtil.setModeString[0];
            case SingleLanExternal:
                return ProjectUtil.setModeString[1];
            case DoubleLan:
                return ProjectUtil.setModeString[2];
            case WifiDHCP:
                return ProjectUtil.setModeString[3];
            case LanAndWifi:
                return ProjectUtil.setModeString[4];
            default:
                return "";
        }
    }

    private void doSetModeSuccess(ProjectUtil.SetModeEnum mode) {
        ProjectUtil.currentMode = mode;
        mView.doSetModeSuccess(mode);
        mainPresenter.doSetModeSuccess(mode);
    }
    private void startSetModeTimer(int timeroutvalue) {
        if(setModeTimerTask != null)
            setModeTimerTask.cancel();
        setModeTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(needMode != null) {
                    Log.w(needMode + "", "模式设置超时");
                    mView.doSetModeFail(needMode, "模式设置超时");
                }
                setModeTimerTask.cancel();
            }
        };
        timer.schedule(setModeTimerTask,timeroutvalue * 1000);
    }

    private void stopSetModeTimer() {
        if(setModeTimerTask != null) {
            setModeTimerTask.cancel();
        }
    }

    private void startUpdateBleTimer(int timeroutvalue) {
        if(updateBleTimerTask != null)
            updateBleTimerTask.cancel();
        updateBleTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("updateBleTimerTask","TimerOut, tryUpdateBleCount is " + tryUpdateBleCount);
                updateBleTimerTask.cancel();
                if(tryUpdateBleCount < 6)
                    doUpdateBle();
                else
                    mView.doSetModeFail(needMode,"状态更新超时，请重启设备！");
            }
        };
        timer.schedule(updateBleTimerTask,timeroutvalue * 1000);
    }

    private void stopUpdateBleTimer() {
        if(updateBleTimerTask != null) {
            updateBleTimerTask.cancel();
        }
    }

    private void startQueryModeTimer(int timeroutvalue) {
        if(queryModeTimerTask != null)
            queryModeTimerTask.cancel();
        queryModeTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.w("queryModeTimerTask","TimerOut");
                queryModeTimerTask.cancel();
            }
        };
        timer.schedule(queryModeTimerTask,timeroutvalue * 1000);
    }

    private void stopQueryModeTimer() {
        if(queryModeTimerTask != null) {
            queryModeTimerTask.cancel();
        }
    }

    private void startRebootTimer(int timeroutvalue) {
        if(waitRebootTask != null)
            waitRebootTask.cancel();
        waitRebootTask = new TimerTask() {
            @Override
            public void run() {
                Log.d("waitRebootTask","TimerOK");
                waitRebootTask.cancel();
                doUpdateBle();
            }
        };
        timer.schedule(waitRebootTask,timeroutvalue * 1000);
    }

    private void stopWaitRebootTimer() {
        if(waitRebootTask != null) {
            waitRebootTask.cancel();
        }
    }

    private int getTaskIdBySetModeEnum(ProjectUtil.SetModeEnum mode) {
        switch (mode) {
            case SingleLanInternal:
                return 0;
            case SingleLanExternal:
                return 1;
            case DoubleLan:
                return 2;
            case WifiDHCP:
                return 3;
            case LanAndWifi:
                return 4;
            default:
                return -1;
        }
    }

    private ProjectUtil.SetModeEnum getSetModeEnumByTaskId(int id) {
        switch (id) {
            case 0:
                return SingleLanInternal;
            case 1:
                return SingleLanExternal;
            case 2:
                return DoubleLan;
            case 3:
                return WifiDHCP;
            case 4:
                return LanAndWifi;
            default:
                Log.e("getSetModeEnumByTaskId","id is '" + id + "'");
                return null;
        }
    }
}
