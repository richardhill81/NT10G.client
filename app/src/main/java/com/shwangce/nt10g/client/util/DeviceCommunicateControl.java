package com.shwangce.nt10g.client.util;

import android.content.Context;

import androidx.annotation.NonNull;

import com.shwangce.nt10g.client.library.ControlFrame.CommandValue;
import com.shwangce.nt10g.client.library.ControlFrame.FrameHelper;
import com.shwangce.nt10g.client.library.ControlFrame.ResultBean;
import com.shwangce.nt10g.client.library.ControlFrame.ResultFrameBean;
import com.shwangce.nt10g.client.library.ControlFrame.ResultValue;
import com.shwangce.nt10g.client.library.bluetoothLe.DeviceBean;
import com.shwangce.nt10g.client.library.communicate.BoxController;
import com.shwangce.nt10g.client.library.communicate.BoxControllerListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Administrator on 2017/2/27 0027.
 */

public final class DeviceCommunicateControl {

    private final static String TAG = "CommunicateControl";

    public final static BoxController.CommunicateType COMMUNICATE_TYPE = BoxController.CommunicateType.BLE;

    private Collection listeners;
    private final Context context;

    private ArrayList<Byte> receivebytesList = new ArrayList<>();
    private ResultBean resultBean;

    public interface DeviceControllerListener {
        void onFindNewDevice(DeviceBean deviceBean);
        void onDiscoveryFinished();
        void onConnectSuccess(DeviceBean deviceBean);
        void onConnectError(String error);
        void onConnectLoss();
        void onResultReceive(ResultBean resultBean);
    }

    private enum EventType {
        FindNewDevice,
        DiscoveryFinished,
        ConnectSuccess,
        Error,
        ConnectLoss,
        ResultReceive
    }

    private final BoxControllerListener boxControllerListener = new BoxControllerListener() {
        @Override
        public void onFindNewDevice(DeviceBean deviceBean) {
            notifyListeners(EventType.FindNewDevice,deviceBean);
        }

        @Override
        public void onDiscoveryFinished() {
            notifyListeners(EventType.DiscoveryFinished,null);
        }
        @Override
        public void onConnectSuccess(DeviceBean deviceBean) {
            Log.d(TAG,"连接成功");
            notifyListeners(EventType.ConnectSuccess,deviceBean);
            ProjectUtil.maxFrameDataLength = boxController.getMaxDataLength();
        }

        @Override
        public void onConnectError(String error) {
            Log.w(TAG,"连接失败，" + error);
            notifyListeners(EventType.Error,error);
        }

        @Override
        public void onConnectLoss() {
            Log.w(TAG,"连接丢失");
            notifyListeners(EventType.ConnectLoss,null);
        }

        @Override
        public void onDataReceive(byte[] recvdata) {
            boolean bCheck = true;
            for(int i=0;i<recvdata.length;i++) {
                receivebytesList.add(recvdata[i]);
            }
            ResultFrameBean frameBean;
            while(bCheck) {
                if(receivebytesList.size() >0) {
                    switch (receivebytesList.get(0)) {
                        case FrameHelper.RESULTHEAD_FIRST:
                            frameBean = FrameHelper.getResultFrame(receivebytesList);
                            if (frameBean != null) {
                                for(int i=0;i<ProjectUtil.maxFrameDataLength;i++) {
                                    receivebytesList.remove(0);
                                }
                                resultBean = new ResultBean();
                                resultBean.setResultType(ResultValue.getResultType(frameBean.getResultByte()));
                                if (frameBean.getDataLength() >= 2) {
                                    try {
                                        if(frameBean.getData() != null) {
                                            resultBean.addResultParams(frameBean.getData());
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (frameBean.getResultEnd() == FrameHelper.FRAMEEND_COMPLETE) {
                                    Log.d("蓝牙结论",resultBean.getResultType().toString() + "   " +resultBean.getResultParams());
                                    notifyListeners(EventType.ResultReceive,resultBean);
                                }
                            } else  {
                                bCheck = false;
                            }
                            break;

                        case FrameHelper.RESULTHEAD_CONTINUE:
                            if (resultBean != null) {
                                frameBean = FrameHelper.getResultFrame(receivebytesList);
                                if (frameBean != null) {
                                    for(int i=0;i<ProjectUtil.maxFrameDataLength;i++) {
                                        receivebytesList.remove(0);
                                    }
                                    if (frameBean.getDataLength() >= 1) {
                                        try {
                                            if(frameBean.getData() != null) {
                                                resultBean.addResultParams(frameBean.getData());
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (frameBean.getResultEnd() == FrameHelper.FRAMEEND_COMPLETE) {
                                        Log.d("蓝牙结论",resultBean.getResultType().toString() + "   " +resultBean.getResultParams());
                                        notifyListeners(EventType.ResultReceive,resultBean);
                                    }
                                } else {
                                    bCheck = false;
                                }
                            }
                            break;

                        default:
                            receivebytesList.remove(0);
                            break;
                    }
                } else {
                    bCheck = false;
                }
            }
        }
    };

    private final BoxController boxController;

    private static volatile DeviceCommunicateControl instance = null;

    private DeviceCommunicateControl(@NonNull Context context, BoxController.CommunicateType communicateType) {
        this.context = context;
        boxController = new BoxController(context, boxControllerListener, communicateType);
    }

    public static DeviceCommunicateControl getInstance(@NonNull Context context) {
        if (instance == null) {
            synchronized (DeviceCommunicateControl.class) {
                if (instance == null) {
                    instance = new DeviceCommunicateControl(context,COMMUNICATE_TYPE);
                }
            }
        }
        return instance;
    }

    public static DeviceCommunicateControl getInstance() {
        return instance;
    }

    public void addListener(DeviceControllerListener listener) {
        if (listeners == null) {
            listeners = new HashSet();
        }
        listeners.add(listener);
    }

    public void removeListener(DeviceControllerListener listener) {
        if (listeners == null)
            return;
        listeners.remove(listener);
    }


    public void startDiscovery() {
        boxController.startDiscovery();
    }

    public void stopDiscovery() {
        boxController.stopDiscovery();
    }


    public void connect(String[] deviceAddresses) {
        boxController.connect(deviceAddresses);
    }

    public void connectFindedDevice(DeviceBean deviceBean) {
        boxController.connectFindedDevice(deviceBean);
    }

    public byte[] getCommandBytes(String commandType,String detail) {
        byte[] cmddata = null;
        byte[] detailbytes = null;
        if(detail.length() >0) {
            detailbytes = detail.getBytes();
            cmddata = new byte[detailbytes.length + 1];
        } else {
            detailbytes = null;
            cmddata = new byte[1];
        }
        cmddata[0]  = CommandValue.getCommandByte(commandType);
        if(detailbytes != null) {
            for(int i=0;i<detailbytes.length;i++) {
                cmddata[i+1]  = detailbytes[i];
            }
        }
        return cmddata;
    }

    public byte[] getCommandBytes(String commandType,byte[] detail) {
        byte[] cmddata = null;
        byte[] detailbytes = null;
        if(detail.length >0) {
            detailbytes = detail.clone();
            cmddata = new byte[detailbytes.length + 1];
        } else {
            detailbytes = null;
            cmddata = new byte[1];
        }
        cmddata[0]  = CommandValue.getCommandByte(commandType);
        if(detailbytes != null) {
            for(int i=0;i<detailbytes.length;i++) {
                cmddata[i+1]  = detailbytes[i];
            }
        }
        return cmddata;
    }

    public void sendData(byte[] data) {
        boxController.sendData(data);
    }

    public void disconnect() {
        boxController.disconnect();
    }

    public void close() {
        boxController.close();
    }

    public boolean getConnectState() {
        return boxController.getConnectState();
    }

    private void notifyListeners(EventType type,Object detailInfo) {
        Iterator iter = listeners.iterator();
        while (iter.hasNext()) {
            DeviceControllerListener listener = (DeviceControllerListener) iter.next();
            switch (type) {
                case FindNewDevice:
                    listener.onFindNewDevice((DeviceBean)detailInfo);
                    break;
                case DiscoveryFinished:
                    listener.onDiscoveryFinished();
                    break;

                case ConnectSuccess:
                    listener.onConnectSuccess((DeviceBean)detailInfo);
                    break;

                case Error:
                    listener.onConnectError(detailInfo + "");
                    break;

                case ConnectLoss:
                    listener.onConnectLoss();
                    break;

                case ResultReceive:
                    listener.onResultReceive((ResultBean) detailInfo);
                    break;
            }
        }
    }
}
