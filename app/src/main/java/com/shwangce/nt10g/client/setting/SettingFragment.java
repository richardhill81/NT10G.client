package com.shwangce.nt10g.client.setting;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;

import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.speedtest.HxBoxBean;
import com.shwangce.nt10g.client.speedtest.SpeedTestKind;
import com.shwangce.nt10g.client.sweetalert.SweetAlertDialog;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;
import com.shwangce.nt10g.client.util.SharedPreferencesUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import static com.shwangce.nt10g.client.util.client.ProjectUtil.ftpServerBean;

/**
 * Created by Administrator on 2017/2/8 0008.
 */

public class SettingFragment extends Fragment implements SettingContract.View {

    private final static String ITEM_SPEEDTEST = "测速设置";
    private final static String ITEM_BOXUPDATE = "测速盒升级";
    private final static String ITEM_BOXINFO = "当前版本";
    private final static String ITEM_HXINFO = "线务员设置";
    private final static String ITEM_FILEMANAGER = "文件清理";
    // add by hzj on 20191014 新增日志导出功能
    private final static String ITEM_LOGEXPORT = "日志导出";


    //add by hzj in 2018.12.13
    private final static int MESSAGE_SHOW_QUERYMEMORY_RESULT = 1;
    private final static int MESSAGE_SHOW_QUERYMEMORY_ERROR = 2;

    private final static int MESSAGE_SHOW_QUERYPACKAGEFILE_RESULT = 3;
    private final static int MESSAGE_SHOW_QUERYPACKAGEFILE_ERROR = 4;

    private final static int MESSAGE_SHOW_QUERYLOGFILE_RESULT = 5;
    private final static int MESSAGE_SHOW_QUERYLOGFILE_ERROR = 6;

    private final static int MESSAGE_SHOW_DELETEFILE_RESULT = 7;
    private final static int MESSAGE_SHOW_DELETEFILE_ERROR = 8;
    private final static int MESSAGE_SHOW_DELETEFILE_PROCESS = 9;

    // add by hzj on 20191018
    private final static int MESSAGE_QUERY_LOGFILE_SIZE = 10;
    private final static int MESSAGE_EXPORTLOG = 11;
    private final static int MESSAGE_EXPORTLOGCOMPLETE = 12;
    private final static int MESSAGE_EXPORTLOGERROR = 13;

    private final static int MESSAGE_TASK_TIMEOUT = 14;

    private final static int MESSAGE_SHOW_EXPORT_PROGRESS = 15;

    private final static int MESSAGE_FILEDATA_WRITE_ERROR = 16;
    private final static int MESSAGE_FILEDATA_WRITE_COMPLETE = 17;

    private final static int MESSAGE_STOP_EXPORTLOGFILE = 18;


    public static final DecimalFormat df2 = new DecimalFormat("######0.00");

    // add by hzj on 20191022
    private long logFileSize = 0;

    private StringBuffer buffer_logFile = new StringBuffer();
    private ExportLogFile exportLogFile;

    private boolean isStopExportLogFile = false;    //是否停止发送请求日志文件数据的标志


    @BindView(R.id.settings_listview)
    ListView lv_settingItem;
    @BindView(R.id.setting_llyt_speedtest)
    LinearLayout llyt_speedtest;
    //add by hzj in 2018.12.13
    @BindView(R.id.setting_llyt_filemanager)
    LinearLayout llyt_filemanager;

    //add by hzj in 2018.12.13 新增日志导出
    @BindView(R.id.setting_llyt_logexport)
    LinearLayout llyt_logexport;

    @BindView(R.id.setting_llyt_hxinfo)
    LinearLayout llyt_hxinfo;
    @BindView(R.id.setting_llyt_boxinfo)
    LinearLayout llyt_boxinfo;

    @BindView(R.id.testsource_button_submit)
    Button testsource_button_submit;
    @BindView(R.id.testsource_button_back)
    Button testsource_button_back;
    @BindView(R.id.testsource_radiogroup1)
    RadioGroup testsource_radiogroup1;
    @BindView(R.id.testsource_radiogroup2)
    RadioGroup testsource_radiogroup2;
    @BindView(R.id.testsource_radiogroup3)
    RadioGroup testsource_radiogroup3;
    @BindView(R.id.testsource_radiogroup4)
    RadioGroup testsource_radiogroup4;
    @BindView(R.id.testsource_radio_gd10000)
    RadioButton radio_gd10000;
    @BindView(R.id.testsource_radio_hunandx)
    RadioButton radio_hunan10000;
    @BindView(R.id.testsource_radio_http)
    RadioButton radio_http;
    @BindView(R.id.testsource_radio_hxbox)
    RadioButton radio_hxbox;
    @BindView(R.id.testsource_radio_speedtest)
    RadioButton radio_speedtest;
    @BindView(R.id.testsource_radio_ftp)
    RadioButton radio_ftpdownload;
    @BindView(R.id.testsource_radio_jiangsu)
    RadioButton radio_jiangsu;
    @BindView(R.id.testsource_edittext_httpdownloadUrl)
    EditText httpdownloadurlEditText;
    @BindView(R.id.testsource_edittext_httpuploadUrl)
    EditText httpuploadurlEditText;
    @BindView(R.id.testsource_http_layout)
    LinearLayout httpLinearLayout;
    @BindView(R.id.testsource_ftp_layout)
    LinearLayout ftpLinearLayout;
    @BindView(R.id.testsource_edittext_ftpUrl)
    EditText ftpurlEditText;
    @BindView(R.id.testsource_edittext_ftpUserName)
    EditText ftpUserNameEditText;
    @BindView(R.id.testsource_edittext_ftpPassword)
    EditText ftpPasswordEditText;

    @BindView(R.id.boxinfo_textview_clientversion)
    TextView boxinfo_textview_clientversion;
    @BindView(R.id.boxinfo_textview_boxversion)
    TextView boxinfo_textview_boxversion;
    @BindView(R.id.boxinfo_checkbox_boxworkmode)
    CheckBox boxinfo_checkbox_boxworkmode;
    @BindView(R.id.boxinfo_radiogroup_tcpdumpsave)
    RadioGroup boxinfo_radiogroup_tcpdumpsave;
    @BindView(R.id.boxinfo_radiobutton_tcpdumpsaveatsdcard0)
    RadioButton boxinfo_radiobutton_tcpdumpsaveatsdcard0;
    @BindView(R.id.boxinfo_radiobutton_tcpdumpsaveatsdcard1)
    RadioButton boxinfo_radiobutton_tcpdumpsaveatsdcard1;
    @BindView(R.id.boxinfo_button_submit)
    Button boxinfo_button_submit;
    @BindView(R.id.boxinfo_button_back)
    Button boxinfo_button_back;

    @BindView(R.id.hxinfo_edittext_username)
    EditText hxinfo_edittext_username;
    @BindView(R.id.hxinfo_edittext_userid)
    EditText hxinfo_edittext_userid;
    @BindView(R.id.hxinfo_edittext_userpwd)
    EditText hxinfo_edittext_userpwd;
    @BindView(R.id.hxinfo_edittext_worksheetnum)
    EditText hxinfo_edittext_worksheetnum;
    @BindView(R.id.hxinfo_button_submit)
    Button hxinfo_button_submit;
    @BindView(R.id.hxinfo_button_back)
    Button hxinfo_button_back;

    @BindView(R.id.fileManager_bt_back)
    Button fileManager_bt_back;
    @BindView(R.id.fileManager_bt_delete)
    Button fileManager_bt_delete;
    @BindView(R.id.fileManager_expandableListView_showFile)
    ExpandableListView fileManager_expandableListView_showFile;
    @BindView(R.id.fileManager_textView_showSpace)
    TextView fileManager_textView_showSpace;

    // add by hzj on 20191014
    @BindView(R.id.logExport_bt_export)
    Button logExport_bt_export;
    @BindView(R.id.logExport_bt_back)
    Button getLogExport_bt_back;


    private Context context;
    private SpeedTestKind selectTestType = ProjectUtil.speedTestKind;
    private SettingListAdapter listAdapter;
    private ArrayList<String> itemList = new ArrayList<>();
    private SweetAlertDialog mySweetALertDialog = null;

    private String httpdownloadurl = ProjectUtil.Httpdownloadurl;
    private String httpuploadurl = ProjectUtil.Httpuploadurl;

    private String newWorkMode = "0";
    private boolean needchangegroup = false;

    //add by hzj in2018.12.13
    private List<String> listFileTypes;
    private List<List<String>> listFiles;

    private List<List<Boolean>> list_isFileChecked;
    private List<Boolean> list_isFileTypeAllChecked;

    private SweetAlertDialog sweetAlertDialog;

    private ShowFilesExpandableListViewApdapter expandableListViewApdapter;

    private ExpandListViewListener expandListViewListener = new ExpandListViewListener() {

        @Override
        public void doGroupCheckClick(boolean isChecked, int groupPos) {
            list_isFileTypeAllChecked.set(groupPos, isChecked);

            int len = list_isFileChecked.get(groupPos).size();
            for (int i = 0; i < len; i++) {
                list_isFileChecked.get(groupPos).set(i, isChecked);
            }
            expandableListViewApdapter.notifyDataSetChanged();
        }

        @Override
        public void doChildCheckClick(boolean isChecked, int groupPos, int childPos) {
            list_isFileChecked.get(groupPos).set(childPos, isChecked);
            expandableListViewApdapter.notifyDataSetChanged();
        }
    };

    private final RadioGroup.OnCheckedChangeListener checkedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
            if (!needchangegroup) {
                needchangegroup = true;
                switch (i) {

                    case R.id.testsource_radio_jiangsu:
                        selectTestType = SpeedTestKind.JIANGSU10000;
                        //                    testsource_radiogroup1.setVisibility(View.GONE);
                        //                    testsource_radiogroup2.setVisibility(View.GONE);
                        //                    testsource_radiogroup3.setVisibility(View.GONE);
                        httpLinearLayout.setVisibility(View.GONE);
                        ftpLinearLayout.setVisibility(View.GONE);
                        break;
                    case R.id.testsource_radio_hxbox:
                        selectTestType = SpeedTestKind.HXBOX;
                        httpLinearLayout.setVisibility(View.GONE);
                        ftpLinearLayout.setVisibility(View.GONE);
                        //if(ProjectUtil.hxBoxBean == null) {
                        //   ProjectUtil.hxBoxBean = new HxBoxBean("sj","111111", "60101","32131");
                        //}
                        break;
                    case R.id.testsource_radio_speedtest:
                        selectTestType = SpeedTestKind.TCP_SPEEDTEST;
                        httpLinearLayout.setVisibility(View.GONE);
                        ftpLinearLayout.setVisibility(View.GONE);
                        break;
                    case R.id.testsource_radio_http:
                        selectTestType = SpeedTestKind.HTTP_DOWNLOAD;
                        httpLinearLayout.setVisibility(View.VISIBLE);
                        ftpLinearLayout.setVisibility(View.GONE);
                        if (httpdownloadurlEditText.length() == 0)
                            httpdownloadurlEditText.setText(httpdownloadurl);
                        //httpdownloadurl =httpdownloadurlEditText.getText().toString().trim();
                        if (httpuploadurlEditText.length() == 0)
                            httpuploadurlEditText.setText(httpuploadurl);
                        ProjectUtil.Httpdownloadurl = httpdownloadurl;
                        ProjectUtil.Httpuploadurl = httpuploadurl;
                        break;
                    case R.id.testsource_radio_gd10000:
                        selectTestType = SpeedTestKind.GD10000;
                        httpLinearLayout.setVisibility(View.GONE);
                        ftpLinearLayout.setVisibility(View.GONE);
                        break;
                    case R.id.testsource_radio_hunandx:
                        selectTestType = SpeedTestKind.HUNAN10000;
                        httpLinearLayout.setVisibility(View.GONE);
                        ftpLinearLayout.setVisibility(View.GONE);
                        break;
                    case R.id.testsource_radio_ftp:
                        selectTestType = SpeedTestKind.FTP_DOWNLOAD;
                        httpLinearLayout.setVisibility(View.GONE);
                        ftpLinearLayout.setVisibility(View.VISIBLE);
                        testsource_button_submit.setVisibility(View.VISIBLE);
                        /*
                        if (ftpServerBean != null && ftpServerBean.getServerhost().length() > 0) {
                            if (ftpServerBean.getRemotePath().length() > 0) {
                                ftpurlEditText.setText("ftp://" + ftpServerBean.getServerhost() + "/" + ftpServerBean.getRemotePath() + "/" + ftpServerBean.getRemoteFileName());
                            } else {
                                ftpurlEditText.setText("ftp://" + ftpServerBean.getServerhost() + "/" + ftpServerBean.getRemoteFileName());
                            }
                        } else {
                            ftpurlEditText.setText("ftp://192.168.1.2/Download.zip");
                        }

                        ftpUserNameEditText.setText(ftpServerBean.getUsername());
                        ftpPasswordEditText.setText(ftpServerBean.getPassword());

                         */
                        break;
                }
                ProjectUtil.speedTestKind = selectTestType;
                SharedPreferencesUtil.setTestType(context, selectTestType);
                if (radioGroup == testsource_radiogroup1) {
                    testsource_radiogroup2.clearCheck();
                    testsource_radiogroup3.clearCheck();
                    testsource_radiogroup4.clearCheck();
                } else if (radioGroup == testsource_radiogroup2) {
                    testsource_radiogroup1.clearCheck();
                    testsource_radiogroup3.clearCheck();
                    testsource_radiogroup4.clearCheck();
                } else if (radioGroup == testsource_radiogroup3) {
                    testsource_radiogroup1.clearCheck();
                    testsource_radiogroup2.clearCheck();
                    testsource_radiogroup4.clearCheck();
                } else if (radioGroup == testsource_radiogroup4) {
                    testsource_radiogroup1.clearCheck();
                    testsource_radiogroup2.clearCheck();
                    testsource_radiogroup3.clearCheck();
                }
                needchangegroup = false;
            }

        }
    };

    private SettingContract.Presenter mPresenter;

    private View.OnClickListener backClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            onBackClick();
        }
    };

    private View.OnClickListener deleteClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("提示");
            builder.setMessage("是否删除选中文件？");

            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    StringBuffer buffer = new StringBuffer();
                    int groupLen = list_isFileTypeAllChecked.size();
                    for(int i=0;i<groupLen;i++){
                        int childLen = list_isFileChecked.get(i).size();
                        for(int j = 0;j<childLen;j++){
                            if(list_isFileChecked.get(i).get(j)){
                                String fileMsg = listFiles.get(i).get(j);
                                String fileName = fileMsg.substring(0, fileMsg.lastIndexOf("("));
                                buffer.append(fileName + "|");
                            }
                        }
                    }

                    if(buffer.toString().equals("")){
                        Toast.makeText(context,"请先勾选需要删除的文件！",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    //查询开始让按钮不可点击。
                    fileManager_bt_back.setEnabled(false);
                    fileManager_bt_delete.setEnabled(false);
                    fileManager_expandableListView_showFile.setEnabled(false);

                    String fileNames = buffer.substring(0,buffer.lastIndexOf("|"));
                    mPresenter.doDeleteFile(fileNames);
                }
            });
            builder.setNegativeButton("取消", null);

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    };

    // add by hzj on 20191023]
    private ExportLogFileListener exportLogFileListener = new ExportLogFileListener() {
        @Override
        public void onFileDataWriteComplete(String info) {
            mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FILEDATA_WRITE_COMPLETE,info));
        }

        @Override
        public void onFileDataWriteError(String errorInfo) {
            mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FILEDATA_WRITE_ERROR,errorInfo));
        }
    };

    // add by hzj on 20191014
    private View.OnClickListener logExportClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("提示");
            builder.setMessage("是否导出盒子测试日志至手机？");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mPresenter.doQueryLogFileSize();
                }
            });
            builder.setNegativeButton("返回",null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this.getActivity();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SHOW_QUERYMEMORY_RESULT:
                    String queryMemoryResult = msg.obj.toString();
                    fileManager_textView_showSpace.setText(queryMemoryResult);
                    mPresenter.doStartQueryPackageFile();
                    break;
                case MESSAGE_SHOW_QUERYMEMORY_ERROR:
                    String queryMemoryErrorInfo = msg.obj.toString();
                    Toast.makeText(context, queryMemoryErrorInfo, Toast.LENGTH_SHORT).show();
                    break;

                case MESSAGE_SHOW_QUERYPACKAGEFILE_RESULT:
                    expandableListViewApdapter.notifyDataSetChanged();
                    mPresenter.doStartQueryLogFile();
                    break;
                case MESSAGE_SHOW_QUERYPACKAGEFILE_ERROR:
                    String queryPackageFileErrorInfo = msg.obj.toString();
                    Toast.makeText(context, queryPackageFileErrorInfo, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_SHOW_QUERYLOGFILE_RESULT:
                    expandableListViewApdapter.notifyDataSetChanged();

                    //查询完成后才让按钮可点击。
                    fileManager_bt_back.setEnabled(true);
                    fileManager_bt_delete.setEnabled(true);
                    fileManager_expandableListView_showFile.setEnabled(true);
                    break;
                case MESSAGE_SHOW_QUERYLOGFILE_ERROR:
                    String queryLogFileErrorInfo = msg.obj.toString();
                    Toast.makeText(context, queryLogFileErrorInfo, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_SHOW_DELETEFILE_RESULT:
                    String DeleteFileResultInfo = msg.obj.toString();
                    sweetAlertDialog.setTitleText("删除文件完成。");

                    expandableListViewApdapter.notifyDataSetChanged();
                    if(sweetAlertDialog.isShowing()){
                        sweetAlertDialog.dismiss();
                    }
                    Toast.makeText(context,"删除文件完成。",Toast.LENGTH_SHORT).show();
                    //刪除成功后重新查詢盒子內部存儲
                    mPresenter.doStartQueryMemory();
                    break;
                case MESSAGE_SHOW_DELETEFILE_ERROR:
                    String DeleteFileErrorInfo = msg.obj.toString();
                    Toast.makeText(context, DeleteFileErrorInfo, Toast.LENGTH_SHORT).show();
                    if(sweetAlertDialog.isShowing()){
                        sweetAlertDialog.dismiss();
                        sweetAlertDialog = null;
                    }
                    sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("")
                            .setContentText(DeleteFileErrorInfo)
                            .setConfirmText("关闭");
                    sweetAlertDialog.show();
                    break;
                case MESSAGE_SHOW_DELETEFILE_PROCESS:
                    String deleteFileProcInfo = msg.obj.toString();
                    sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText(deleteFileProcInfo);
                    sweetAlertDialog.show();
                    sweetAlertDialog.setCancelable(false);
                    break;

                    // add by hzj on 20191018
                case MESSAGE_QUERY_LOGFILE_SIZE:
                    mPresenter.stopTaskTimeOut();
                    mPresenter.setTaskTimeOut(5000);
                    isStopExportLogFile = false;
                    Log.i("QUERY_LOGFILE_SIZE","读取数据");
                    mPresenter.doExportLog();       //获取盒子日志文件数据
                    Log.i("EXPORTLOG","LogSize" + msg.obj.toString());
                    //收到日志文件大小后，先清除手机内日志文件数据
                    exportLogFile = new ExportLogFile(exportLogFileListener);
                    exportLogFile.clearFile();
                    //存下日志文件大小
                    logFileSize = Integer.parseInt(msg.obj.toString());
                    //开启显示上传文件进度线程
                    mPresenter.startTimerShowExportLogFileProgress();

                    buffer_logFile = new StringBuffer();

                    sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                            .setTitleText("正在导出日志文件……");
                    sweetAlertDialog.show();
                    sweetAlertDialog.setCancelable(false);

                    break;
                case MESSAGE_EXPORTLOG:

                    //将接收到的日志文件数据追加到日志文件中。
//                    if(isStopExportLogFile){
//                        return;
//                    }
                    exportLogFile.writeDataToFile(msg.obj.toString());

                    Log.i("EXPORTLOG","上传内容:" + msg.obj.toString());
                    buffer_logFile.append(msg.obj.toString());

                    break;
                case MESSAGE_EXPORTLOGCOMPLETE:
                    mPresenter.stopTaskTimeOut();
                    mPresenter.stopTimerShowExportLogFileProgress();
                    Log.i("EXPORTLOG","Complete" + msg.obj.toString());
                    Log.i("EXPORTLOG","文件内容：" + buffer_logFile.toString());

                    if(sweetAlertDialog.isShowing()){
                        sweetAlertDialog.dismiss();
                        sweetAlertDialog = null;
                    }
                    sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("")
                            .setContentText(msg.obj.toString())
                            .setConfirmText("关闭");
                    sweetAlertDialog.show();

                    break;
                case MESSAGE_EXPORTLOGERROR:
                    mPresenter.stopTaskTimeOut();
                    mPresenter.stopTimerShowExportLogFileProgress();
                    isStopExportLogFile = true;

                    Log.i("EXPORTLOG","Error" + msg.obj.toString());

                    if(sweetAlertDialog.isShowing()){
                        sweetAlertDialog.dismiss();
                        sweetAlertDialog = null;
                    }
                    sweetAlertDialog = new SweetAlertDialog(context,SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("文件上传失败")
                            .setContentText(msg.obj.toString())
                            .setConfirmText("关闭");
                    sweetAlertDialog.show();
                    break;

                case MESSAGE_TASK_TIMEOUT:
                    mPresenter.stopTimerShowExportLogFileProgress();
                    isStopExportLogFile = true;
                    Log.i("timeout","isStopExportLogFile: "+ isStopExportLogFile);

                    if(sweetAlertDialog.isShowing()){
                        sweetAlertDialog.dismiss();
                        sweetAlertDialog = null;
                    }
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("")
                            .setContentText("文件导出超时")
                            .setConfirmText("关闭")
                            .show();
                    break;

                case MESSAGE_SHOW_EXPORT_PROGRESS:
                    String tempStr = buffer_logFile.toString();

                    double progress = tempStr.getBytes().length * 100.0/logFileSize;
                    if(sweetAlertDialog == null) return;
                    sweetAlertDialog.setTitleText("正在导出日志文件……" + df2.format(progress) + "%"); //显示下载进度
                    sweetAlertDialog.setCancelText("取消");
                    sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("提示");
                            builder.setMessage("是否取消导出日志文件？");
                            builder.setPositiveButton("返回",null);
                            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Log.i("CANCEl","正在取消导出日志文件。");
//                                    mPresenter.doStopExportLog();
                                    mPresenter.stopTaskTimeOut();

                                    isStopExportLogFile = true;
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();

                        }
                    });
                    break;

                case MESSAGE_FILEDATA_WRITE_ERROR:
                    if(sweetAlertDialog.isShowing()){
                        sweetAlertDialog.dismiss();
                        sweetAlertDialog = null;
                    }
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("文件写入失败")
                            .setContentText(msg.obj.toString())
                            .setConfirmText("关闭")
                            .show();
                    break;

                case MESSAGE_FILEDATA_WRITE_COMPLETE:
                    mPresenter.stopTaskTimeOut();
                    Log.i("WRITE_COMPLETE","isStopExportLogFile: "+ isStopExportLogFile);
                    if(!isStopExportLogFile){
                        mPresenter.setTaskTimeOut(5000);
                        Log.i("WRITE_COMPLETE","继续读取数据");
                        mPresenter.doExportLog();   //继续读取数据
                    }else{
                        if(sweetAlertDialog != null){
                            if(sweetAlertDialog.isShowing()){
                                sweetAlertDialog.dismiss();
                                sweetAlertDialog = null;
                            }
                        }

                    }

                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);
        itemList.add(ITEM_SPEEDTEST);
        //itemList.add(ITEM_BOXUPDATE);
        itemList.add(ITEM_BOXINFO);
        //itemList.add(ITEM_HXINFO);
        itemList.add(ITEM_FILEMANAGER);
        itemList.add(ITEM_LOGEXPORT); //add by hzj on 20191014

        //add by hzj in 2018.12.14
//        fileManager_bt_back.setEnabled(false);
//        fileManager_bt_delete.setEnabled(false);
//        fileManager_expandableListView_showFile.setEnabled(false);

        listFileTypes = new ArrayList<>();
        listFileTypes.add("抓包记录");
        listFileTypes.add("测试日志");

        list_isFileTypeAllChecked = new ArrayList<>();
        list_isFileTypeAllChecked.add(true);
        list_isFileTypeAllChecked.add(true);

        listFiles = new ArrayList<List<String>>();
        listFiles.add(new ArrayList<String>());
        listFiles.add(new ArrayList<String>());

        list_isFileChecked = new ArrayList<List<Boolean>>();
        list_isFileChecked.add(new ArrayList<Boolean>());
        list_isFileChecked.add(new ArrayList<Boolean>());

        expandableListViewApdapter = new ShowFilesExpandableListViewApdapter(context, listFileTypes, list_isFileTypeAllChecked, listFiles, list_isFileChecked, expandListViewListener);
        fileManager_expandableListView_showFile.setAdapter(expandableListViewApdapter);

        listAdapter = new SettingListAdapter(context, itemList);
        lv_settingItem.setAdapter(listAdapter);
        lv_settingItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String itemname = listAdapter.getItem(i);
                if (itemname.equals(ITEM_SPEEDTEST)) {
                    lv_settingItem.setVisibility(View.GONE);
                    llyt_hxinfo.setVisibility(View.GONE);
                    llyt_boxinfo.setVisibility(View.GONE);
                    llyt_speedtest.setVisibility(View.VISIBLE);

                    llyt_filemanager.setVisibility(View.GONE);      //add by hzj in 2018.12.13
                    llyt_logexport.setVisibility(View.GONE);    //add by hzj on 20191014

                } else if (itemname.equals(ITEM_BOXUPDATE)) {

                } else if (itemname.equals(ITEM_BOXINFO)) {
 /*                   new SweetAlertDialog(context,SweetAlertDialog.NORMAL_TYPE)
                            .setTitleText("当前版本：" + ProjectUtil.VersionName)
                            .setContentText("")
                            .show();*/
                    lv_settingItem.setVisibility(View.GONE);
                    llyt_hxinfo.setVisibility(View.GONE);
                    llyt_boxinfo.setVisibility(View.VISIBLE);
                    llyt_speedtest.setVisibility(View.GONE);
                    llyt_filemanager.setVisibility(View.GONE);      //add by hzj in 2018.12.13
                    llyt_logexport.setVisibility(View.GONE);    //add by hzj on 20191014

                } else if (itemname.equals(ITEM_HXINFO)) {
                    lv_settingItem.setVisibility(View.GONE);
                    llyt_hxinfo.setVisibility(View.VISIBLE);
                    llyt_boxinfo.setVisibility(View.GONE);
                    llyt_speedtest.setVisibility(View.GONE);
                    llyt_filemanager.setVisibility(View.GONE);      //add by hzj in 2018.12.13
                    llyt_logexport.setVisibility(View.GONE);    //add by hzj on 20191014

                } else if (itemname.equals(ITEM_FILEMANAGER)) {       //add by hzj in 2018.12.13
                    //TODO
                    mPresenter.doStartQueryMemory();
                    lv_settingItem.setVisibility(View.GONE);
                    llyt_hxinfo.setVisibility(View.GONE);
                    llyt_boxinfo.setVisibility(View.GONE);
                    llyt_speedtest.setVisibility(View.GONE);
                    llyt_filemanager.setVisibility(View.VISIBLE);
                    llyt_logexport.setVisibility(View.GONE);    //add by hzj on 20191014
                } else if(itemname.equals(ITEM_LOGEXPORT)){     //add by hzj on 20191014
                    //TODO
                    lv_settingItem.setVisibility(View.GONE);
                    llyt_hxinfo.setVisibility(View.GONE);
                    llyt_boxinfo.setVisibility(View.GONE);
                    llyt_speedtest.setVisibility(View.GONE);
                    llyt_filemanager.setVisibility(View.GONE);
                    llyt_logexport.setVisibility(View.VISIBLE);    //add by hzj on 20191014
                }
            }
        });
        testsource_radiogroup1.setOnCheckedChangeListener(checkedChangeListener);
        testsource_radiogroup2.setOnCheckedChangeListener(checkedChangeListener);
        testsource_radiogroup3.setOnCheckedChangeListener(checkedChangeListener);
        testsource_radiogroup4.setOnCheckedChangeListener(checkedChangeListener);
        switch (ProjectUtil.speedTestKind) {
            case HTTP_DOWNLOAD:
                radio_http.setChecked(true);
                break;
            case HXBOX:
                radio_hxbox.setChecked(true);
                break;
            case TCP_SPEEDTEST:
                radio_speedtest.setChecked(true);
                break;
            case GD10000:
                radio_gd10000.setChecked(true);
                break;
            case HUNAN10000:
                radio_hunan10000.setChecked(true);
                break;
            case FTP_DOWNLOAD:
                radio_ftpdownload.setChecked(true);
                break;
            case JIANGSU10000:
                radio_jiangsu.setChecked(true);
                break;
        }

        //江苏电信，只显示江苏电信测速选项
        if("js10000".equals(ProjectUtil.channel) || "ah10086".equals(ProjectUtil.channel)) {
            testsource_radiogroup1.setVisibility(View.GONE);
            testsource_radiogroup2.setVisibility(View.GONE);
            testsource_radiogroup3.setVisibility(View.GONE);
            testsource_radiogroup4.setVisibility(View.VISIBLE);
            radio_jiangsu.setChecked(true);
            if("js10000".equals(ProjectUtil.channel))
                radio_jiangsu.setText("江苏电信测速");
            else if("ah10086".equals(ProjectUtil.channel))
                radio_jiangsu.setText("安徽移动测速");
            else
                radio_jiangsu.setText("江苏电信测速");
        }
        if("sh10000".equals(ProjectUtil.channel)) {
            testsource_radiogroup1.setVisibility(View.VISIBLE);
            testsource_radiogroup2.setVisibility(View.GONE);
            testsource_radiogroup3.setVisibility(View.GONE);
            testsource_radiogroup4.setVisibility(View.GONE);
            radio_hxbox.setChecked(true);
            radio_gd10000.setVisibility(View.GONE);
        }

        boxinfo_textview_boxversion.setText(ProjectUtil.boxInfoBean.getVersion());
        boxinfo_textview_clientversion.setText(ProjectUtil.clientPackageInfo.versionName);
        if (ProjectUtil.boxInfoBean.getWorkmode().equals("0")) {
            boxinfo_checkbox_boxworkmode.setChecked(false);
        } else {
            boxinfo_checkbox_boxworkmode.setChecked(true);
        }
        if (ProjectUtil.boxInfoBean.getTcpdumpsave().equals("0")) {
            boxinfo_radiobutton_tcpdumpsaveatsdcard0.setChecked(true);
        } else {
            boxinfo_radiobutton_tcpdumpsaveatsdcard1.setChecked(true);
        }
        if (ProjectUtil.hxBoxBean != null) {
            hxinfo_edittext_username.setText(ProjectUtil.hxBoxBean.getUserName());
            hxinfo_edittext_userpwd.setText(ProjectUtil.hxBoxBean.getPwd());
            hxinfo_edittext_userid.setText(ProjectUtil.hxBoxBean.getUserid());
            hxinfo_edittext_worksheetnum.setText(ProjectUtil.hxBoxBean.getWorksheetnum());
        }
        hxinfo_button_back.setOnClickListener(backClickListener);
        boxinfo_button_back.setOnClickListener(backClickListener);
        testsource_button_back.setOnClickListener(backClickListener);
        //add by hzj in 2018.12.14
        fileManager_bt_back.setOnClickListener(backClickListener);
        fileManager_bt_delete.setOnClickListener(deleteClickListener);

        //add by hzj on 20191014
        logExport_bt_export.setOnClickListener(logExportClickListener);
        getLogExport_bt_back.setOnClickListener(backClickListener);
        return view;
    }

    @OnClick(R.id.testsource_button_submit)
    void onTestsourceSubmit() {
        switch (selectTestType) {
            case HTTP_DOWNLOAD:
                httpdownloadurl = httpdownloadurlEditText.getText().toString().trim();
                httpuploadurl = httpuploadurlEditText.getText().toString().trim();
                if (httpdownloadurl.length() == 0) {
                    httpdownloadurl = context.getString(R.string.default_download_url);
                }
                ProjectUtil.Httpdownloadurl = httpdownloadurl;
                if (httpuploadurl.length() == 0) {
                    httpuploadurl = context.getString(R.string.default_download_url);
                }
                ProjectUtil.Httpuploadurl = httpuploadurl;
                SharedPreferencesUtil.setHttpDownloadUrl(context, httpdownloadurl);
                SharedPreferencesUtil.setHttpUploadUrl(context, httpuploadurl);
                break;
            case HXBOX:
/*                if(ProjectUtil.hxBoxBean == null) {
                    ProjectUtil.hxBoxBean = new HxBoxBean("sj","111111", "60101","32131");
                }
                SharedPreferencesUtil.setHxUserInfo(context,ProjectUtil.hxBoxBean);*/
                break;

            case FTP_DOWNLOAD:
                String ftpurl = ftpurlEditText.getText().toString().trim();
                String ftpusername = ftpUserNameEditText.getText().toString().trim();
                String ftppassword = ftpPasswordEditText.getText().toString().trim();
                String ftpserver = "";
                String remotepath = "";
                String remotefilename = "";
                if (ftpurl.toUpperCase().startsWith("FTP://")) {
                    ftpurl = ftpurl.substring(6);
                }
                int l = ftpurl.indexOf("/");
                if (l > 0) {
                    ftpserver = ftpurl.substring(0, l);
                    ftpurl = ftpurl.substring(l + 1);
                }
                l = ftpurl.lastIndexOf("/");
                if (l >= 0) {
                    remotepath = ftpurl.substring(0, l);
                    remotefilename = ftpurl.substring(l + 1);
                } else {
                    remotepath = "";
                    remotefilename = ftpurl;
                }
                /*
                ProjectUtil.ftpServerBean = new FtpServerBean();
                ProjectUtil.ftpServerBean.setServerhost(ftpserver);
                ProjectUtil.ftpServerBean.setServerport(21);
                ProjectUtil.ftpServerBean.setUsername(ftpusername);
                ProjectUtil.ftpServerBean.setPassword(ftppassword);
                ProjectUtil.ftpServerBean.setRemotePath(remotepath);
                ProjectUtil.ftpServerBean.setRemoteFileName(remotefilename);

                 */
                //SharedPreferencesUtil.setFtpDownloadServerBean(context, ProjectUtil.ftpServerBean);
                break;

        }
        ProjectUtil.speedTestKind = selectTestType;

        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("")
                .setContentText("保存成功")
                .show();
    }

    @OnClick(R.id.hxinfo_button_submit)
    void onHxinfoSubmit() {
        String hxusername = hxinfo_edittext_username.getText().toString().trim();
        String hxuserpwd = hxinfo_edittext_userpwd.getText().toString().trim();
        String hxuserid = hxinfo_edittext_userid.getText().toString().trim();
        String hxworksheetnum = hxinfo_edittext_worksheetnum.getText().toString().trim();
        HxBoxBean hxBoxBean = null;
        if (hxusername != "") {
            hxBoxBean = new HxBoxBean(hxusername, hxuserpwd, hxuserid, hxworksheetnum);
        }
        ProjectUtil.hxBoxBean = hxBoxBean;
        SharedPreferencesUtil.setHxUserInfo(context, hxBoxBean);
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("")
                .setContentText("保存成功")
                .show();
    }

    @OnClick(R.id.boxinfo_button_submit)
    void onBoxinfoSubmit() {
        String boxworkmode = "0";
        String boxtcpdumpsave = "0";
        if (boxinfo_checkbox_boxworkmode.isChecked())
            boxworkmode = "1";
        else
            boxworkmode = "0";
        if (boxinfo_radiobutton_tcpdumpsaveatsdcard0.isChecked()) {
            boxtcpdumpsave = "0";
        } else {
            boxtcpdumpsave = "1";
        }
        ProjectUtil.boxInfoBean.setTcpdumpsave(boxtcpdumpsave);
        /*
        if (boxworkmode != ProjectUtil.boxInfoBean.getWorkmode()) {
            newWorkMode = boxworkmode;
            mPresenter.doSetBoxWorkMode(boxworkmode);
            mySweetALertDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
                    .setTitleText("")
                    .setContentText("正在更新配置");
            mySweetALertDialog.show();
            mySweetALertDialog.setCancelable(false);
        } else {
            SharedPreferencesUtil.setBoxInfo(context, ProjectUtil.boxInfoBean);
            new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("")
                    .setContentText("保存成功")
                    .show();
        }
        */
        SharedPreferencesUtil.setBoxInfo(context, ProjectUtil.boxInfoBean);
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("")
                .setContentText("保存成功")
                .show();
    }

    private void onBackClick() {
        llyt_speedtest.setVisibility(View.GONE);
        llyt_boxinfo.setVisibility(View.GONE);
        llyt_hxinfo.setVisibility(View.GONE);
        llyt_filemanager.setVisibility(View.GONE);      //add by hzj in 2018.12.13
        llyt_logexport.setVisibility(View.GONE);        //add by jzj on 20191014
        lv_settingItem.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void doSetBoxWorkModeResult(String result) {
        final String workmoderesult = result;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mySweetALertDialog.dismissWithAnimation();
                if (workmoderesult.equals("1")) {
                    ProjectUtil.boxInfoBean.setWorkmode(newWorkMode);
                    SharedPreferencesUtil.setBoxInfo(context, ProjectUtil.boxInfoBean);
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("")
                            .setContentText("保存成功")
                            .show();
                } else {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("")
                            .setContentText("工作模式切换失败！")
                            .show();
                }
            }
        });

    }

    @Override
    public void doShowQueryMemoryResult(String result) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SHOW_QUERYMEMORY_RESULT, result));
    }

    @Override
    public void doShowQueryMemoryError(String errorInfo) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SHOW_QUERYMEMORY_ERROR, errorInfo));
    }

    @Override
    public void doShowQueryPackageFileResult(String result) {
        String[] fileNames = result.split("\\|");
        listFiles.get(0).clear();   //第二次接收到数据是先清空list
        list_isFileChecked.get(0).clear();
        for (String fileName : fileNames) {
            if(fileName.contains(".cap")){
                listFiles.get(0).add(fileName);
                list_isFileChecked.get(0).add(true);
            }
        }
        Log.d("doShowQueryPackageFileResult", "listFiles.get(0).size():" + listFiles.get(0).size());
        mHandler.sendEmptyMessage(MESSAGE_SHOW_QUERYPACKAGEFILE_RESULT);
    }

    @Override
    public void doShowQueryPackageFileError(String errorInfo) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SHOW_QUERYPACKAGEFILE_ERROR, errorInfo));
    }

    @Override
    public void doShowQueryLogFileResult(String result) {
        String[] fileMsgs = result.split("\\|");
        listFiles.get(1).clear();   //第二次接收到数据是先清空list
        list_isFileChecked.get(1).clear();
        for (String fileMsg : fileMsgs) {
            if(fileMsg.contains(".txt")){
                listFiles.get(1).add(fileMsg);
                list_isFileChecked.get(1).add(true);
            }
        }
        Log.d("doShowQueryPackageFileResult", "listFiles.get(0).size():" + listFiles.get(1).size());
        mHandler.sendEmptyMessage(MESSAGE_SHOW_QUERYLOGFILE_RESULT);
    }

    @Override
    public void doShowQueryLogFileError(String errorInfo) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SHOW_QUERYLOGFILE_ERROR, errorInfo));
    }

    @Override
    public void doShowDeleteFileResult(String result) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SHOW_DELETEFILE_RESULT, result));
    }

    @Override
    public void doShowDeleteFileError(String errorInfo) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SHOW_DELETEFILE_ERROR, errorInfo));
    }

    @Override
    public void doShowDeleteFileProcess(String procInfo) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SHOW_DELETEFILE_PROCESS, procInfo));
    }

    @Override
    public void doSetQueryLogSize(String size) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_QUERY_LOGFILE_SIZE, size));
    }

    @Override
    public void doOnExportLog(String data) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_EXPORTLOG, data));
    }

    @Override
    public void doShowExportLogComplete(String info) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_EXPORTLOGCOMPLETE, info));
    }

    @Override
    public void doShowExportLogError(String errorInfo) {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_EXPORTLOGERROR, errorInfo));
    }

    @Override
    public void doTaskTimeOut() {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_TASK_TIMEOUT));
    }

    @Override
    public void doShowExportProgress() {
        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SHOW_EXPORT_PROGRESS));
    }

}
