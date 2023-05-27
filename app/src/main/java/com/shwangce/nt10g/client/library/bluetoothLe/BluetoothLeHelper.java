package com.shwangce.nt10g.client.library.bluetoothLe;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.ParcelUuid;

import androidx.annotation.NonNull;

import com.shwangce.nt10g.client.library.WorkUtils;
import com.shwangce.nt10g.client.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Created by Administrator on 2017/2/14 0014.
 */

public class BluetoothLeHelper {

    private final String TAG = "BluetoothLeHelper";

    public interface BluetoothLeListener {
        void onFindNewDevice(BluetoothDevice device);
        void onConnected(DeviceBean deviceBean);
        void onOpenFail(String failReason);
        void onConnectFail(String failReason);
        void onConnectLoss();
        void onBluetoothRead(byte[] recvdata);
    }
    private BluetoothLeListener listener = null;

    private Context context;

    private static final String UUID_GattService = "0000fff0-0000-1000-8000-00805f9b34fb";
    private static final String UUID_GattCharacteristic_L = "0000fff6-0000-1000-8000-00805f9b34fb";
    private static final String UUID_GattCharacteristic_H = "0000fff7-0000-1000-8000-00805f9b34fb";
    private static final String UUID_GattDescriptor = "00002902-0000-1000-8000-00805f9b34fb";

    private static final int MAX_SENDLENGTH_H = 70;
    private static final int MAX_SENDLENGTH_L = 20;

    private static final int GATT_ERROR = 133;
    private static final int GATT_SUCCESS = 0;

    private final int currentapiVersion = Build.VERSION.SDK_INT;
    private final int HIGH_MTU_VALUE = MAX_SENDLENGTH_H  + 3;
    private int maxDataLength = MAX_SENDLENGTH_L;

    private BluetoothAdapter mAdapter = null;
    private BluetoothLeScanner mLeScanner = null;
    private BluetoothGatt mGatt = null;
    private BluetoothGattCharacteristic mCharacteristic = null;

    private ArrayList<ConnetDeviceObject> connetDeviceObjectArrayList = new ArrayList<>();
    private boolean showDisconnectInfo = true;
    //private BluetoothDevice destDevice = null;
    private DeviceBean destDeviceBean = null;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private static final int STATE_DISCONNECTING = 3;

    private boolean sIsWriting = false;

    private final int MAXTRY = 30;

    //private boolean isNotifyMode = true;

    private static final Queue<WriteBean> sWriteQueue = new ConcurrentLinkedQueue<WriteBean>();

    private final static int READWORK = 1;
    private final static int SENDWORK = 2;
    private volatile Object RSlock = new Object();

    private int mConnectionState = STATE_DISCONNECTED;
    private static int intReadinterval = 0;
    private volatile boolean bReading = false;
    private volatile boolean bWaiting = false;
    private Thread readThread = null;
    private Object obj_ScanCallback = null;

    private final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            if(device != null && device.getName() != null)
                doFindNewDevice(device);
        }

    };

    BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override  //当连接上设备或者失去连接时会回调该函数
        public void onConnectionStateChange(final BluetoothGatt gatt, int status, int newState) {
            Log.d(TAG,gatt.getDevice().getAddress() + " status:  " + status + "  newState:  " + newState);
            ConnetDeviceObject connetDeviceObject = getConnetDeviceObjectByAddress(gatt.getDevice().getAddress());
            switch (status) {
                case GATT_SUCCESS:
                    switch (newState) {
                        case STATE_DISCONNECTED:
                            if (showDisconnectInfo) doConnectLoss();
                            else showDisconnectInfo = true;
                            CloseGatt(gatt);
                            break;

                        case STATE_CONNECTING:

                            break;

                        case STATE_CONNECTED:
                            if(connetDeviceObject != null) {
                                connetDeviceObject.stopTimer();
                                connetDeviceObject.setConnectStep("DiscoverServices");
                                connetDeviceObject.startTimer();
                            }
                            gatt.discoverServices(); //连接成功后就去找出该设备中的服务
                            break;

                        case STATE_DISCONNECTING:
                            break;
                    }
                    break;

                case GATT_ERROR:
                    if(connetDeviceObject != null) {
                        connetDeviceObject.stopTimer();
                        reConnectGatt(connetDeviceObject);
                    } else {
                        CloseGatt(gatt);
                    }

                    break;

                default:
                    if(connetDeviceObject != null) {
                        //Log.d(TAG,connetDeviceObject.getDeviceBean().getDeviceMac() + " to stopTimer");
                        connetDeviceObject.stopTimer();
                        connetDeviceObject.setConnectFail();
                    }
                    CloseGatt(gatt);
                    if(checkAllConnectFail()) {
                        doConnectFail("蓝牙连接失败");
                    }
                    break;
            }
        }

        @Override  //当设备是否找到服务时，会回调该函数
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            ConnetDeviceObject connetDeviceObject = getConnetDeviceObjectByAddress(gatt.getDevice().getAddress());
            boolean isCC2640 = false;
            if (status == GATT_SUCCESS) {   //找到服务
                BluetoothGattService service = gatt.getService(UUID.fromString(UUID_GattService));
                if(service != null) {
                    List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                    if(characteristics != null && characteristics.size() >0) {
                        for (BluetoothGattCharacteristic characteristic:characteristics) {
                            if(characteristic.getUuid().equals(UUID.fromString(UUID_GattCharacteristic_H))) {
                                isCC2640 = true;
                                break;
                            }
                        }
                        if(isCC2640) {
                            if (currentapiVersion >=Build.VERSION_CODES.LOLLIPOP) {
                                boolean bb = false;
                                bb = gatt.requestMtu(HIGH_MTU_VALUE);
                                Log.d(TAG,"requestMtu = " + bb);
                                if(bb) {
                                    mCharacteristic = service.getCharacteristic(UUID.fromString(UUID_GattCharacteristic_H));
                                    maxDataLength = MAX_SENDLENGTH_H;
                                } else {
                                    mCharacteristic = service.getCharacteristic(UUID.fromString(UUID_GattCharacteristic_L));
                                    maxDataLength = MAX_SENDLENGTH_L;
                                }
                            } else {
                                mCharacteristic = service.getCharacteristic(UUID.fromString(UUID_GattCharacteristic_L));
                                maxDataLength = MAX_SENDLENGTH_L;
                            }
                        } else {
                            mCharacteristic = service.getCharacteristic(UUID.fromString(UUID_GattCharacteristic_L));
                            maxDataLength = MAX_SENDLENGTH_L;
                        }
                        if(mCharacteristic != null) {
                            if(connetDeviceObject != null) {
                                connetDeviceObject.stopTimer();
                                destDeviceBean = connetDeviceObject.getDeviceBean();
                                stopOtherConnect(connetDeviceObject);
                            }
                            //Log.d(TAG,"getProperties = " + mCharacteristic.getProperties());
                            mGatt = gatt;
                            enableNotification(mCharacteristic,true);
                            //startReadCharacteristic(mCharacteristic,10);

                            doConnected(destDeviceBean);

                        } else {
                            if(connetDeviceObject != null) {
                                connetDeviceObject.stopTimer();
                            }
                            doConnectFail("未找到可用的特征值");
                            CloseGatt(gatt);
                        }
                    } else {
                        if(connetDeviceObject != null) {
                            connetDeviceObject.stopTimer();
                        }
                        doConnectFail("未找到可用的特征值");
                        CloseGatt(gatt);
                    }
                } else {
                    if(connetDeviceObject != null) {
                        connetDeviceObject.stopTimer();
                    }
                    doConnectFail("未找到可用的GattService");
                    CloseGatt(gatt);
                }
            }
        }

        @Override  //当读取设备时会回调该函数
        public void onCharacteristicRead(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG,"onCharacteristicRead,status = " + status);
            if (status == GATT_SUCCESS) {
                boolean isAll0 = true;
                byte[] recvdata = characteristic.getValue();
                for(int i=0;i<recvdata.length;i++) {
                    if(recvdata[i] != 0x00) {
                        isAll0 = false;
                        break;
                    }
                }
                if(!isAll0) {
                    Log.d("蓝牙接收", WorkUtils.toHexString(recvdata, 0, recvdata.length));
                    doReceiveData(recvdata);
                }
                bWaiting = false;
                //Log.d(TAG,"onCharacteristicRead bWaiting = false");
                //读取到的数据存在characteristic当中，可以通过characteristic.getValue();函数取出。然后再进行解析操作。
                //int charaProp = characteristic.getProperties();if ((charaProp | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0)表示可发出通知。  判断该Characteristic属性
            }
        }

        @Override //当向Characteristic写数据时会回调该函数
        public void onCharacteristicWrite(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic, int status) {
            Log.d(TAG,"onCharacteristicWrite,status = " + status);
            sIsWriting = false;
            nextWrite();
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            byte[] recvdata = characteristic.getValue();
            Log.d("蓝牙接收", WorkUtils.toHexString(recvdata, 0, recvdata.length));
            doReceiveData(recvdata);
            //super.onCharacteristicChanged(gatt, characteristic);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.d("onDescriptorRead","");
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            Log.d(TAG,"onDescriptorWrite,status = " + status);
            sIsWriting = false;
            nextWrite();
        }


        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Log.d(TAG,"MtuChanged to " + mtu + ",status = " + status) ;
        }


    };

    ConnetDeviceObject.TimerOutCallBack timerOutCallBack = new ConnetDeviceObject.TimerOutCallBack() {
        @Override
        public void onTimerOut(ConnetDeviceObject connetDeviceObject) {
            BluetoothGatt gatt = connetDeviceObject.getGatt();
            if(gatt != null) {
                Log.d(TAG, gatt.getDevice().getAddress() + " ConnectGatt TimeOut");
                reConnectGatt(connetDeviceObject);
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BluetoothLeHelper(Context context, @NonNull BluetoothLeListener listener) {
        this.listener = listener;
        this.context = context;
        mAdapter = BluetoothAdapter.getDefaultAdapter();
        //20181015 改扫描方式为直连（华为部分型号手机扫描不到测速盒）
        if (currentapiVersion >= Build.VERSION_CODES.LOLLIPOP) {
            obj_ScanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    byte[] scanData = result.getScanRecord().getBytes();
                    BluetoothDevice device = result.getDevice();
                    //if (device.getAddress() != null)
                        //Log.d(TAG, "callbackType = " + callbackType + ",ScanResult = " + device.getAddress() + " ,Bytes= " + bytesToHex(scanData));
                    doFindNewDevice(device);
                }
                @Override
                public void onBatchScanResults(List<ScanResult> results) {

                    super.onBatchScanResults(results);
                }

                @Override
                public void onScanFailed(int errorCode) {
                    super.onScanFailed(errorCode);
                    Log.d(TAG, "ScanFailed");
                }
            };
        }
    }

    public int getMaxDataLength() {
        return maxDataLength;
    }

    public boolean open() {
        Log.d(TAG, "OpenBluetooth");
        if(mAdapter  == null)
        {
            doOpenFail("当前设备不支持蓝牙功能");
            return false;
        }
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            doOpenFail("设备不支持蓝牙ble");
            return false;
        }
        if (!mAdapter.isEnabled()) {
            if(!mAdapter.enable()) {
                doOpenFail("蓝牙打开失败");
                return false;
            }
        }
        while(mAdapter.getState() != BluetoothAdapter.STATE_ON) {
            try{
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startLeScan() {
        //Log.d(TAG, "Starting scanning with settings:" + bleScanSettings + " and filters:" + bleScanFilters);
        if (currentapiVersion >=Build.VERSION_CODES.LOLLIPOP) {
            mLeScanner = mAdapter.getBluetoothLeScanner();
            ScanSettings.Builder settingsBuilder = new ScanSettings.Builder();
            settingsBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
            ScanSettings scanSettings = settingsBuilder.build();
            List<ScanFilter> scanFilters = new ArrayList<>();
            ScanFilter.Builder filterBuilder = new ScanFilter.Builder();
            filterBuilder.setServiceUuid(new ParcelUuid(UUID.fromString(UUID_GattService)));
            scanFilters.add(filterBuilder.build());
            mLeScanner.startScan(scanFilters,scanSettings,(ScanCallback)obj_ScanCallback);
            //mLeScanner.startScan((ScanCallback)obj_ScanCallback);
        } else {
            mAdapter.startLeScan(mLeScanCallback); //开始搜索
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void stopLeScan() {
        if (currentapiVersion >=Build.VERSION_CODES.LOLLIPOP) {
            if(mLeScanner != null)
                mLeScanner.stopScan((ScanCallback)obj_ScanCallback);
        } else {
            mAdapter.stopLeScan(mLeScanCallback);
        }
    }

    public void connect(@NonNull final BluetoothDevice device) {
        showDisconnectInfo = true;
        if (mAdapter == null) {
            doOpenFail("当前设备不支持蓝牙功能");
        }
        stopLeScan();
        if (mGatt != null) {
            CloseGatt(mGatt);
        }
        destDeviceBean = new DeviceBean(device.getName(),device.getAddress());
        connetDeviceObjectArrayList.clear();
        BluetoothGatt gatt = device.connectGatt(context, false, gattCallback);
        ConnetDeviceObject connetDeviceObject = new ConnetDeviceObject(destDeviceBean,gatt,timerOutCallBack);
        connetDeviceObject.setConnectStep("ConnectDevice");
        connetDeviceObject.startTimer();
        connetDeviceObjectArrayList.add(connetDeviceObject);
    }


    public void connect(@NonNull final String[] addressList) {
        showDisconnectInfo = true;
        if (mAdapter == null) {
            doOpenFail("当前设备不支持蓝牙功能");
        }
        stopLeScan();
        if (mGatt != null) {
            CloseGatt(mGatt);
        }
        try {
            CloseGatt(mGatt);
            destDeviceBean = new DeviceBean();
            connetDeviceObjectArrayList.clear();
            for (String address : addressList) {
                String m = address.toUpperCase();
                String l = m.substring(9);
                DeviceBean deviceBean = new DeviceBean("NT10G_" + l, m);
                BluetoothDevice device = mAdapter.getRemoteDevice(m);
                BluetoothGatt gatt = device.connectGatt(context, false, gattCallback);
                ConnetDeviceObject connetDeviceObject = new ConnetDeviceObject(deviceBean,gatt,timerOutCallBack);
                connetDeviceObject.setConnectStep("ConnectDevice");
                connetDeviceObject.startTimer();
                connetDeviceObjectArrayList.add(connetDeviceObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    public void connect(@NonNull final String[] deviceAddress) {
        if (mAdapter == null) {
            doOpenFail("当前设备不支持蓝牙功能");
        }
        if (mGatt != null) {
            CloseGatt(mGatt);
        }
        try {
            mGatt = null;
            destDevice = null;
            destDeviceBean = new DeviceBean();
            tryConnectDeviceList.clear();
            for (String mac:deviceAddress) {
                String m = mac.toUpperCase();
                String l = m.substring(9);
                DeviceBean deviceBean = new DeviceBean("NT10G_" + l,m);
                BluetoothDevice device = mAdapter.getRemoteDevice(m);
                tryConnectDeviceList.add(deviceBean);
                device.connectGatt(context, false, mGattCallback); //该函数才是真正的去进行连接
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
       */
    public void write(String data) {
        byte[] bytes = data.getBytes();
        write(bytes);
    }

    public void write(final byte[] data) {
        if (mAdapter == null || mGatt == null || mCharacteristic == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        } else {
            Log.d("蓝牙发送", WorkUtils.toHexString(data, 0, data.length));
            //Log.d(TAG,"writeCharacteristic bWaiting = true");
            WriteBean bean = new WriteBean(mCharacteristic,data);
            write(bean);
        }
    }


    public void disconnect() {
        bWaiting = false;
        showDisconnectInfo = false;
        stopRead();
        if(mConnectionState == STATE_CONNECTED) {
            if(mGatt != null)
                mGatt.disconnect();
        }
        if(mGatt != null) {
            CloseGatt(mGatt);
        }
        mCharacteristic = null;
    }

    public void close() {
        disconnect();
        //if(mAdapter != null)
        //    stopLeScan();
    }

    private void CloseGatt(BluetoothGatt gatt) {
        if(gatt != null) {
            Log.d(TAG,gatt.getDevice().getAddress() + " CloseGatt");
            refreshDeviceCache(gatt);
            gatt.close();
            gatt = null;
        }
        mConnectionState = STATE_DISCONNECTED;
    }

    private boolean refreshDeviceCache(BluetoothGatt gatt) {
        BluetoothGatt localGatt = gatt;
        try {
            Method localMethod = localGatt.getClass().getMethod("refresh",new Class[0]);
            if (localMethod != null) {
                boolean result = ((Boolean) localMethod.invoke(localGatt,
                        new Object[0])).booleanValue();
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void reConnectGatt(ConnetDeviceObject connetDeviceObject) {
        BluetoothGatt gatt = connetDeviceObject.getGatt();
        CloseGatt(gatt);
        if(connetDeviceObject.getTryCount() > MAXTRY || connetDeviceObject.isConnectFail()) {
            Log.d(TAG,connetDeviceObject.getDeviceBean().getDeviceMac() + " QuitConnect");
            connetDeviceObject.setConnectFail();
            if(checkAllConnectFail())
                doConnectFail("蓝牙连接失败");
        } else {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG,connetDeviceObject.getDeviceBean().getDeviceMac() +
                    " TryCount : "+ connetDeviceObject.getTryCount());
            BluetoothDevice device = mAdapter.getRemoteDevice(connetDeviceObject.getDeviceBean().getDeviceMac());
            BluetoothGatt newgatt = device.connectGatt(context, false, gattCallback);
            connetDeviceObject.setGatt(newgatt);
            connetDeviceObject.startTimer();
        }
    }

    private void doOpenFail(String failReason) {
        mConnectionState = STATE_DISCONNECTED;
        if(listener != null)
            listener.onOpenFail(failReason);
    }


    private void doFindNewDevice(BluetoothDevice device) {
        if(listener != null)
            listener.onFindNewDevice(device);
    }

    private void doConnected(DeviceBean deviceBean) {
        mConnectionState = STATE_CONNECTED;
        if(listener != null) {
            listener.onConnected(deviceBean);
        }
    }

    private void doConnectLoss() {
        mConnectionState = STATE_DISCONNECTED;
            if(listener != null) {
            listener.onConnectLoss();
        }
    }

    private void doConnectFail(String failReason) {
        mConnectionState = STATE_DISCONNECTED;
        if(listener != null) {
            listener.onConnectFail(failReason);
        }
    }

    private void doReceiveData(byte[] data) {
        if(listener != null) {
            listener.onBluetoothRead(data);
        }
    }

    private void startReadCharacteristic(final BluetoothGattCharacteristic characteristic, int intReadinterval) {
        if (mAdapter == null || mGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        BluetoothLeHelper.intReadinterval = intReadinterval;
        if(readThread == null) {
            Runnable readRunnable = new Runnable() {
                @Override
                public void run() {
                    bReading = true;
                    while(bReading) {
                        doRWwork(READWORK,null,characteristic);
                        try {
                            Thread.sleep(BluetoothLeHelper.intReadinterval);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            readThread = new Thread(readRunnable);
        }
        if(!readThread.isAlive()) {
            readThread.start();
        }
    }

    private void stopRead() {
        bReading = false;
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(readThread != null && readThread.isAlive()) {
            readThread.interrupt();
        }
        readThread = null;
    }

    private synchronized void write(WriteBean writeBean) {
        if(sWriteQueue.isEmpty() && !sIsWriting) {
            doWrite(writeBean);
        } else {
            sWriteQueue.add(writeBean);
        }
    }

    private synchronized void doWrite(WriteBean bean) {
        Object o = bean.getWriteObjcet();
        if(o != null) {
            if (o instanceof BluetoothGattCharacteristic) {
                sIsWriting = true;
                BluetoothGattCharacteristic characteristic = (BluetoothGattCharacteristic) o;
                characteristic.setValue(bean.getWriteValue());
                mGatt.writeCharacteristic(characteristic);
            } else if (o instanceof BluetoothGattDescriptor) {
                sIsWriting = true;
                BluetoothGattDescriptor descriptor = (BluetoothGattDescriptor) o;
                descriptor.setValue(bean.getWriteValue());
                mGatt.writeDescriptor(descriptor);
            } else {
                nextWrite();
            }
        }
    }

    private synchronized void nextWrite() {
        if(!sWriteQueue.isEmpty() && !sIsWriting) {
            doWrite(sWriteQueue.poll());
        }
    }

    private void doRWwork(int worktype,byte[] data,BluetoothGattCharacteristic characteristic) {
        Log.d(TAG,"doRWwork Start");
        synchronized (RSlock) {
            bWaiting = true;
            if(worktype == SENDWORK) {

            } else {
                Log.d(TAG,"readCharacteristic start");
                if(!mGatt.readCharacteristic(characteristic)) {
                    Log.e(TAG, "readCharacteristic执行失败");
                    return;
                }
            }
            while(bWaiting) {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Log.d(TAG,"doRWwork Complete");

        }
    }

    private void enableNotification(BluetoothGattCharacteristic characteristic,boolean b) {
        boolean isEnableNotification = mGatt.setCharacteristicNotification(characteristic, b);
        if(isEnableNotification) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(UUID_GattDescriptor));
            if(descriptor != null) {
                WriteBean writeBean = new WriteBean();
                writeBean.setWriteObjcet(descriptor);
                if(b)
                    //descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    writeBean.setWriteValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                else
                    //descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                    writeBean.setWriteValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                write(writeBean);
            }

            /*List<BluetoothGattDescriptor> descriptorList = characteristic.getDescriptors();
            if(descriptorList != null && descriptorList.size() > 0) {
                for(BluetoothGattDescriptor descriptor : descriptorList) {
                    if(b)
                        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    else
                        descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                    mGatt.writeDescriptor(descriptor);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }*/
        }
    }

    private ConnetDeviceObject getConnetDeviceObjectByAddress(String address) {
        if(connetDeviceObjectArrayList.size() >0) {
            for(int i=0;i<connetDeviceObjectArrayList.size();i++) {
                ConnetDeviceObject connetDeviceObject = connetDeviceObjectArrayList.get(i);
                if(address.equals(connetDeviceObject.getDeviceBean().getDeviceMac())) {
                    return connetDeviceObject;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    private boolean checkAllConnectFail() {
        if(connetDeviceObjectArrayList.size() >0) {
            for(ConnetDeviceObject connetDeviceObject : connetDeviceObjectArrayList) {
                if(!connetDeviceObject.isConnectFail()) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }

    private void stopOtherConnect(ConnetDeviceObject connetDeviceObject) {
        String deviceaddress = connetDeviceObject.getDeviceBean().getDeviceMac();
        for(ConnetDeviceObject object : connetDeviceObjectArrayList) {
            object.stopTimer();
            String m = object.getDeviceBean().getDeviceMac();
            if(!m.equals(deviceaddress)) {
                object.setConnectFail();
                CloseGatt(object.getGatt());
            }
        }
    }
    /**
     * 字节数组转16进制
     * @param bytes 需要转换的byte数组
     * @return  转换后的Hex字符串
     */
    private  String bytesToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if(hex.length() < 2){
                sb.append("0x0");
            } else {
                sb.append("0x");
            }
            sb.append(hex + " ");
        }
        return sb.toString();
    }
}
