package com.shwangce.nt10g.client.deviceselect;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.shwangce.nt10g.client.BaseDialogFragment;
import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.library.WorkUtils;
import com.shwangce.nt10g.client.library.bluetoothLe.DeviceBean;
import com.shwangce.nt10g.client.sweetalert.SweetAlertDialog;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;
import com.shwangce.nt10g.client.util.SharedPreferencesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class DeviceSelectDialogFragment extends BaseDialogFragment implements DeviceSelectContract.View{

    private DeviceSelectDialogFragment instance;
    private DeviceSelectContract.Presenter mPresenter;
    private Context context;

    private LinearLayout ll_devicelist;

    private LinearLayout llyt_finddevice;
    private ListView historyDeviceListView;
    private ListView findDeviceListView;
    private Button btn_scanBle;
    private Button btn_quit,btn_scanQR;
    private Button btn_clearHistory;
    private Button btn_enterDevice;
    private DeviceListAdapter historyDeviceListAdapter;
    private DeviceListAdapter findDeviceListAdapter;
    private ArrayList<DeviceBean> historyDeviceList = new ArrayList<>();
    private ArrayList<DeviceBean> findDeviceList = new ArrayList<>();
    private SweetAlertDialog sweetAlertDialog;

    //add by hzj 2018-11-15*************/
    //private  boolean firstConnect;    //程序第一次启动时设置自动连接，并设置15秒超时。
    private SweetAlertDialog sweetAlertDialogLookingForDevice;  //序第一次启动时自动查找设备对话框
    private boolean isDeviceFound;
    private int devicePositionInList;

    /****************/

    private String lastEnterDeviceHistory = "";

    private final int MESSAGE_FINDNEWDEVICE = 1;
    private final int MESSAGE_CONNECTED = 2;
    private final int MESSAGE_SCANFINISHED = 3;
    private final int MESSAGE_GETBOXVERSION_SUCCESS = 4;
    private final int MESSAGE_GETBOXVERSION_FAIL = 5;
    private final int MESSAGE_CONNECT_FAIL = 6;
    //private final int MESSAGE_SETWORKMODE_SUCCESS = 7;
    //private final int MESSAGE_SETWORKMODE_FAIL = 8;
    private final int MESSAGE_DEVICESELECT_SUCCESS = 9;
    private final int MESSAGE_DEVICE_ENTERERROR = 10;

    private final int MESSAGE_SELF_START_SCAN = 11;
    private final int MESSAGE_DEVICE_NT201L_IS_FOUND = 12;

    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_FINDNEWDEVICE:
                    findDeviceList.add((DeviceBean)msg.obj);
                    findDeviceListAdapter.notifyDataSetChanged();
                    if(ProjectUtil.isFirstConnect){
                        ProjectUtil.isFirstConnect = false;
                        if(sweetAlertDialogLookingForDevice != null && sweetAlertDialogLookingForDevice.isShowing())
                            sweetAlertDialogLookingForDevice.dismiss();
                        sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                                .setTitleText("正在连接，请稍候...");
                        sweetAlertDialog.show();
                        sweetAlertDialog.setCancelable(false);
                        mPresenter.doConnectFindedDevice((DeviceBean)msg.obj);
                    }
                    break;

                case MESSAGE_SCANFINISHED:
                    btn_scanBle.setEnabled(true);

                    /******add by hzj 2018-11-15********/
                    if(!isDeviceFound && ProjectUtil.isFirstConnect){
                        sweetAlertDialogLookingForDevice.dismiss();
                        Toast.makeText(context,"未找到可连接设备！",Toast.LENGTH_LONG).show();
                    }
                    ProjectUtil.isFirstConnect = false;
                    /**************/
                    break;

                case MESSAGE_CONNECTED:
                    if(sweetAlertDialog!=null && sweetAlertDialog.isShowing())
                        sweetAlertDialog.setTitleText("正在认证设备");
                    break;

                case MESSAGE_GETBOXVERSION_SUCCESS:
                    //if(sweetAlertDialog!=null && sweetAlertDialog.isShowing())
                    //    sweetAlertDialog.setTitleText("正在设置工作模式");
                    break;

                case MESSAGE_GETBOXVERSION_FAIL:
                    showErrorDialog(msg.obj + ",请将测速盒断电重启！");
                    break;

                case MESSAGE_CONNECT_FAIL:
                    showErrorDialog(msg.obj + "");
                    break;
                /*
                case MESSAGE_SETWORKMODE_SUCCESS:
                    if(sweetAlertDialog!=null && sweetAlertDialog.isShowing())
                        sweetAlertDialog.dismiss();
                    instance.dismiss();
                    break;

                case MESSAGE_SETWORKMODE_FAIL:
                    showErrorDialog("设置失败，将使用默认工作模式！");
                    break;
                */

                case MESSAGE_DEVICESELECT_SUCCESS:
                    if(sweetAlertDialog!=null && sweetAlertDialog.isShowing())
                        sweetAlertDialog.dismiss();
                    try{
                        if(instance!=null && instance.isVisible())
                            instance.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case MESSAGE_DEVICE_ENTERERROR:
                    if(sweetAlertDialog!=null && sweetAlertDialog.isShowing())
                        sweetAlertDialog.dismiss();
                    showErrorDialog("设备名称必须以NT201L_开头，或者 MAC地址格式错误（以:分隔）!");
                case MESSAGE_SELF_START_SCAN:
                    btn_scanBle.performClick();

                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    };

    public DeviceSelectDialogFragment() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.device_list, container, false);
        instance = this;
        context = getActivity();
        initView(v);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void setPresenter(DeviceSelectContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void doFindNewDevice(DeviceBean deviceBean) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_FINDNEWDEVICE,deviceBean));
    }

    @Override
    public void doScanfinished() {
        handler.sendEmptyMessage(MESSAGE_SCANFINISHED);
    }

    @Override
    public void doConnected(DeviceBean deviceBean) {
        String devicemac = deviceBean.getDeviceMac();
        boolean addNew = true;
        for(DeviceBean bean : historyDeviceList) {
            if(bean.getDeviceMac().equals(devicemac)) {
                addNew = false;
                break;
            }
        }
        if(addNew) {
            historyDeviceList.add(deviceBean);
            SharedPreferencesUtil.addDeviceHistory(context,deviceBean);
        }
        handler.sendMessage(handler.obtainMessage(MESSAGE_CONNECTED,null));
    }

    @Override
    public void doConnectError(String failString) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_CONNECT_FAIL,failString));
    }

    @Override
    public void doGetVersionSuccess() {
        handler.sendMessage(handler.obtainMessage(MESSAGE_GETBOXVERSION_SUCCESS,null));
    }

    @Override
    public void doGetVersionFail(String failString) {
        handler.sendMessage(handler.obtainMessage(MESSAGE_GETBOXVERSION_FAIL,failString));
    }

    /*
    @Override
    public void doSetBoxWorkModeResult(String result) {
        if (result.equals("0")) {
            handler.sendEmptyMessage(MESSAGE_SETWORKMODE_FAIL);
        } else {
            handler.sendEmptyMessage(MESSAGE_SETWORKMODE_SUCCESS);
        }
    }
    */

    @Override
    public void doDeviceSelectComplete() {
        handler.sendEmptyMessage(MESSAGE_DEVICESELECT_SUCCESS);
    }

    private void initView(View v) {
        ll_devicelist = (LinearLayout)v.findViewById(R.id.ll_devicelist);
        llyt_finddevice = (LinearLayout)v.findViewById(R.id.devicelist_llyt_finddevices);
        llyt_finddevice.setVisibility(View.GONE);
        btn_scanBle = (Button)v.findViewById(R.id.btn_scan);
        btn_scanQR = (Button)v.findViewById(R.id.btn_scanQR);
        btn_quit = (Button)v.findViewById(R.id.btn_quit);
        btn_clearHistory = (Button)v.findViewById(R.id.btn_clearhistory);
        btn_enterDevice = (Button)v.findViewById(R.id.btn_enterdevice);
        historyDeviceListView = (ListView) v.findViewById(R.id.history_devices);
        historyDeviceListAdapter = new DeviceListAdapter(context, historyDeviceList);
        historyDeviceListView.setAdapter(historyDeviceListAdapter);
        historyDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("正在连接，请稍候...");
                sweetAlertDialog.show();
                sweetAlertDialog.setCancelable(false);
                DeviceBean deviceBean = historyDeviceListAdapter.getItem(i);
                mPresenter.doConnectDeviceByMac(deviceBean.getDeviceMac());
                //mPresenter.doDeviceItemClicked(deviceListAdapter.getItem(i));
            }
        });

        findDeviceListView = (ListView)v.findViewById(R.id.find_devices);
        findDeviceListAdapter = new DeviceListAdapter(context, findDeviceList);
        findDeviceListView.setAdapter(findDeviceListAdapter);
        findDeviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("正在连接，请稍候...");
                sweetAlertDialog.show();
                sweetAlertDialog.setCancelable(false);
                DeviceBean deviceBean = findDeviceListAdapter.getItem(i);
                mPresenter.doConnectFindedDevice(deviceBean);
                //mPresenter.doDeviceItemClicked(deviceListAdapter.getItem(i));

            }
        });

        btn_scanBle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                llyt_finddevice.setVisibility(View.VISIBLE);
//                findDeviceList.clear();
//                findDeviceListAdapter.notifyDataSetChanged();
//                btn_scanBle.setEnabled(false);
//                mPresenter.doBleScan();
                //add by hzj 2018-11-15
                if(ProjectUtil.isFirstConnect){
                    sweetAlertDialogLookingForDevice = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("正在搜索设备，请稍候...");
                    sweetAlertDialogLookingForDevice.show();;
                    sweetAlertDialogLookingForDevice.setCancelable(false);
//                    ll_devicelist.setVisibility(View.INVISIBLE);
                }else{
                    llyt_finddevice.setVisibility(View.VISIBLE);
                }
                findDeviceList.clear();
                findDeviceListAdapter.notifyDataSetChanged();
                btn_scanBle.setEnabled(false);
                mPresenter.doBleScan();

            }
        });

        btn_scanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 0);
                //mPresenter.doConnectDeviceByName("NT201L_88:03:79");
                //mPresenter.doConnectDeviceByMac("20:C3:8F:88:03:79");
            }
        });

        btn_clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.clearDeviceHistory(context);
                historyDeviceList.clear();
                historyDeviceListAdapter.notifyDataSetChanged();
            }
        });
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.doQuitClicked();
            }
        });
        btn_enterDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(context);
                et.setSingleLine(true);
                et.setText(lastEnterDeviceHistory);

                new AlertDialog.Builder(context).setTitle("请输入设备名称或MAC地址")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String dv = et.getText().toString().toUpperCase();
                                SharedPreferencesUtil.setLastEnterDeviceHistory(context,dv);
                                if(WorkUtils.isValidDeviceName(dv)) {    //是设备名称
                                    sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                                            .setTitleText("正在连接，请稍候...");
                                    sweetAlertDialog.show();
                                    sweetAlertDialog.setCancelable(false);
                                    mPresenter.doConnectDeviceByName(dv);
                                } else if(WorkUtils.isValidMac(dv)) {
                                    sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                                            .setTitleText("正在连接，请稍候...");
                                    sweetAlertDialog.show();
                                    sweetAlertDialog.setCancelable(false);
                                    mPresenter.doConnectDeviceByMac(dv);
                                } else {
                                    handler.sendEmptyMessage(MESSAGE_DEVICE_ENTERERROR);
                                }

                            }
                        }).setNegativeButton("取消",null).show();
                }
        });
        DeviceBean[] deviceBeans = SharedPreferencesUtil.getDeviceHistory(context);
        lastEnterDeviceHistory = SharedPreferencesUtil.getLastEnterDeviceHistory(context);
        if(deviceBeans != null) {
            historyDeviceList.addAll(Arrays.asList(deviceBeans));
            historyDeviceListAdapter.notifyDataSetChanged();
        }
    }


    //*****add by hzj 2018-11-15******//


    @Override
    public void onResume() {
        super.onResume();
    }

    public List<DeviceBean> getDeviceList(){
        return findDeviceList;
    }

    private boolean isCheckLocationOver = false;

    public final boolean isLocationEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (networkProvider || gpsProvider) return true;
        return false;
    }

    public void doSelfStartScan(){

        Log.d("DeviceSelect","doSelfStartScan()");
        if(!ProjectUtil.isFirstConnect)
            return;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(ProjectUtil.isFirstConnect){
                    handler.sendEmptyMessage(MESSAGE_SELF_START_SCAN);
                };
            }
        },100);
    }

    public void setIsDeviceFound(boolean isDeviceFound){
        this.isDeviceFound = isDeviceFound;
        Log.d("setIsDeviceFound","设备是否被找到" + isDeviceFound);
    }

    public void setDevicePositionInList(int positionInList){
        this.devicePositionInList = positionInList;
    }
    //*****add by hzj 2018-11-15******//

    private void showErrorDialog(String errorString) {
        if(sweetAlertDialog!= null && sweetAlertDialog.isShowing())
            sweetAlertDialog.dismiss();
        sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("")
                .setContentText(errorString)
                .setConfirmText("关闭");
        sweetAlertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String barcode = data.getExtras().getString("result");
            Log.d("scanQR",barcode);
            if (barcode != null) {
                sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("正在连接，请稍候...");
                sweetAlertDialog.show();
                sweetAlertDialog.setCancelable(false);
                if(barcode.startsWith("NT201L_")) {
                    mPresenter.doConnectDeviceByName(barcode);
                } else
                    mPresenter.doConnectDeviceByMac(barcode);
/*                btn_refresh.setText("刷新");
                btn_refresh.setEnabled(true);
                mPresenter.doDeviceItemClicked(barcode);*/

            }


        }
    }
}
