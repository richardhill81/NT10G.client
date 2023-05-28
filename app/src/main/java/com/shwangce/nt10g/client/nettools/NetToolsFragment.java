package com.shwangce.nt10g.client.nettools;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.shwangce.nt10g.client.R;
import com.shwangce.nt10g.client.databinding.FragmentNettoolsBinding;
import com.shwangce.nt10g.client.library.WorkUtils;
import com.shwangce.nt10g.client.nettools.webtest.ResultListAdapter;
import com.shwangce.nt10g.client.nettools.webtest.WebNameListAdapter;
import com.shwangce.nt10g.client.nettools.webtest.WebTestListViewListener;
import com.shwangce.nt10g.client.sweetalert.SweetAlertDialog;
import com.shwangce.nt10g.client.util.DateFormatUtil;
import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;
import com.shwangce.nt10g.client.util.SharedPreferencesUtil;
import com.shwangce.nt10g.client.util.TimerUtils;
import com.shwangce.nt10g.client.util.TrancerouteAndPingLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/7 0007.
 */

public class NetToolsFragment extends Fragment implements NetToolsContract.View {

    private Context context;

    //private FragmentNettoolsBinding binding;
    private Calendar calendar;
    private LinearLayout llyt_iperf_result;
    private EditText et_ping_deshost,et_ping_count,et_traceroute_host,et_pingandtraceroute_deshost,et_reping_deshost,et_timerscheduletest_deshost,et_interval,et_threshold;
    private EditText et_iperf_serverhost,et_iperf_testtime,et_iperf_testparallel;
    private TextView tv_iperf_downbandwidth,tv_iperf_upbandwidth,tv_iperf_downbandwidth_peak,tv_iperf_upbandwidth_peak,tvstartdate,tvstarttime,tvdeaddate,tvdeadtime,tvlasttesttime;


    private SweetAlertDialog mySweetALertDialog;

    // add by huangzhijie at 2018.12.3
    private WebNameListAdapter webNameListAdapter;
    private static final int LISTWEBNAME_MAXNUMBER = 5;
    private List<String> list_webNames;
    private List<Integer> list_numbers;

    private boolean isEnable = true;

    private LinearLayout ll_webTest_tabs;
    private ListView listView_webTest_showResult;
    private ResultListAdapter adapter_showResult;
    private List<String> resultList_one,resultList_two,resultList_three,resultList_four,resultList_five;
    private List<String> resultList;
    private TextView tab_one,tab_two,tab_three,tab_four,tab_five;
    private Button btn_add;
    private ListView lv_webName;

    private int test_error_count = 0;
    private int test_success_count = 0;

    private int currentTabSelect = -1;

    private Map<Integer ,String> map_ResolveDelay = new HashMap<Integer, String>() ;


    private NetToolsFragment instance;
    private NetToolsContract.Presenter mPresenter;
    private WorkUtils.NetToolsType testType = WorkUtils.NetToolsType.NULL;
    private ArrayList<Float> iperf_upbandwidthList = new ArrayList<>();
    private ArrayList<Float> iperf_downbandwidthList = new ArrayList<>();
    private float iperf_upbandwidth_peak = 0f;
    private float iperf_downbandwidth_peak = 0f;
    private boolean bTestError = false;
    private String[] trancerouteLine0=new String[0] ;
    private String[] trancerouteLine1=new String[0] ;
    private String[] trancerouteLine2=new String[0] ;
    private String[] pingReturnValue=new String[0] ;
    private String[] unknowhost=new String[0] ;

    private Button bt_start = null;
    private Button bt_back = null;
    private Button bt_save = null;
    private Button bt_startdate = null;
    private Button bt_starttime = null;
    private Button bt_deaddate = null;
    private Button bt_deadtime = null;
    private Button bt_add = null;
    private Button bt_delete = null;
    private ToolsListAdapter toolsListAdapter;
    private ArrayList<String> toolsList = new ArrayList<>();
    private ToolsListAdapter ipListAdapter;
    private ArrayList<String> ipList = new ArrayList<>();
    private HashMap<String ,String> IPMap = new HashMap<String ,String >();
    private ListView lv_progressinfo;
    private ArrayAdapter<String> infoAdapter;
    private SimpleAdapter NanjinginfoAdapter;
    private ArrayAdapter<String> pinginfoAdapter;
    private ArrayAdapter<String> NanjingPinginfoAdapter;
    private List<String> infodata = new ArrayList<>();
    private List<String> pinginfodata = new ArrayList<>();
    private List<String> repinginfodata = new ArrayList<>();
    private List<Map<String, Object>> trancerouteListitem = new ArrayList<Map<String, Object>>();
    private Spinner spiEdu = null;
    private ArrayAdapter<CharSequence> adapteEdu = null;
    private List<CharSequence> tranceRouteList = null;//定义一个集合数据
    private String selected;
    private int startdt =0;
    private int deaddt =0;
    private final int MESSAGE_IPERF_DOWNLOADING = 1;
    private final int MESSAGE_IPERF_DOWNLOADED = 2;
    private final int MESSAGE_IPERF_UPLOADING = 3;
    private final int MESSAGE_IPERF_UPLOADED = 4;
    private final int MESSAGE_IPERF_ERROR = 5;
    private final int MESSAGE_IPERF_COMPLETE = 6;

    private final int MESSAGE_UPDATE_INFO = 11;
    private final int MESSAGE_TEST_COMPLETE = 12;
    private final int MESSAGE_TEST_FAIL = 13;
    private final int MESSAGE_UPDATE_INFO_NANJING = 14;
    private final int MESSAGE_UPDATE_INFO_NANJING_COMPLETE = 15;
    private final int MESSAGE_UPDATE_INFONANJINGPING = 16;
    private final int MESSAGE_TIMERSCHEDULESUCCESS = 17;
    private final int MESSAGE_GETIP_FAIL = 18;

    private final int MESSAGE_DATA_NOTIFY_ONE = 19;
    private final int MESSAGE_DATA_NOTIFY_TWO = 20;
    private final int MESSAGE_DATA_NOTIFY_THREE = 21;
    private final int MESSAGE_DATA_NOTIFY_FOUR = 22;
    private final int MESSAGE_DATA_NOTIFY_FIVE = 23;

    private final int MESSAGE_WEBTEST_FINISH = 24;

    private final int MESSAGE_CHANGETABNAME = 25;

    private final int MESSAGE_CHANGE_TAB_STATE = 26;

    //add by hzj in 2018.12.25
//    private final int TAB_STATE_NULL = Color.CYAN;
    private final int TAB_STATE_TESTING = Color.LTGRAY;
    private final int TAB_STATE_TESTERROR = Color.RED;
    private final int TAB_STATE_TESTSUCCESS = Color.BLACK;


    private final Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_IPERF_DOWNLOADING:
                    if(mySweetALertDialog!=null &&mySweetALertDialog.isShowing()) {
                        mySweetALertDialog.setTitleText(msg.obj + "");
                        mySweetALertDialog.setContentText("正在测试下行带宽");
                    }
                    break;

                case MESSAGE_IPERF_DOWNLOADED:
                    if(mySweetALertDialog!=null &&mySweetALertDialog.isShowing()) {
                        mySweetALertDialog.setTitleText("");
                        mySweetALertDialog.setContentText("下行测试完毕，准备测试上行带宽");
                    }
                    if(tv_iperf_downbandwidth != null)
                        tv_iperf_downbandwidth.setText(msg.obj + "");
                    if(tv_iperf_downbandwidth_peak != null)
                        tv_iperf_downbandwidth_peak.setText(iperf_downbandwidth_peak + " Mbits/sec");
                    break;

                case MESSAGE_IPERF_UPLOADING:
                    if(mySweetALertDialog!=null &&mySweetALertDialog.isShowing()) {
                        mySweetALertDialog.setTitleText(msg.obj + "");
                        mySweetALertDialog.setContentText("正在测试上行带宽");
                    }
                    break;

                case MESSAGE_IPERF_UPLOADED:
                    if(mySweetALertDialog!=null &&mySweetALertDialog.isShowing()) {
                        mySweetALertDialog.setTitleText("");
                        mySweetALertDialog.setContentText("上行测试完毕");
                    }
                    if(tv_iperf_upbandwidth != null)
                        tv_iperf_upbandwidth.setText(msg.obj + "");
                    if(tv_iperf_upbandwidth_peak != null)
                        tv_iperf_upbandwidth_peak.setText(iperf_upbandwidth_peak + " Mbits/sec");
                    break;

                case MESSAGE_IPERF_ERROR:
                    if(mySweetALertDialog != null &&mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                    mySweetALertDialog = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("")
                            .setContentText(msg.obj + "")
                            .setConfirmText("关闭");
                    mySweetALertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                        }
                    });
                    mySweetALertDialog.show();
                    break;

                case MESSAGE_IPERF_COMPLETE:
                    if(!bTestError) {
                        if (mySweetALertDialog != null && mySweetALertDialog.isShowing())
                            mySweetALertDialog.dismiss();
                        if(llyt_iperf_result != null)
                            llyt_iperf_result.setVisibility(View.VISIBLE);
                    }
                    if(bt_start != null)
                        bt_start.setEnabled(true);
                    if(bt_back != null)
                        bt_back.setEnabled(true);
                    break;


                case MESSAGE_UPDATE_INFO:

                    infodata.add(msg.obj + "");
                    infoAdapter.notifyDataSetChanged();
                    break;

                case MESSAGE_UPDATE_INFONANJINGPING:

                    repinginfodata.add(msg.obj + "");
                    NanjingPinginfoAdapter.notifyDataSetChanged();
                    break;

                case MESSAGE_TEST_COMPLETE:
                    if(bt_start != null)
                        bt_start.setEnabled(true);
                    if(bt_back != null)
                        bt_back.setEnabled(true);
                    doUpdateProgressInfo("测试完成！");
                    break;

                case MESSAGE_TEST_FAIL:
                    String failString = msg.obj + "";
                    if(bt_start != null)
                        bt_start.setEnabled(true);
                    if(bt_back != null)
                        bt_back.setEnabled(true);
                    doUpdateProgressInfo(failString);
                    doUpdateProgressInfo("测试完成！");
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("")
                            .setContentText(failString)
                            .setConfirmText("关闭")
                            .show();
                    break;

                case MESSAGE_UPDATE_INFO_NANJING:
                    String returnValue = msg.obj + "";
//                    trancerouteinfodata.add(msg.obj + "");
                    if(mySweetALertDialog!=null &&mySweetALertDialog.isShowing()) {
                        mySweetALertDialog.setTitleText("正在ping测试");
                        //mySweetALertDialog.setContentText("正在ping测试");
                    }
                    for (int i = 0; i < trancerouteLine0.length; i++) {
                        Map<String, Object> showitem = new HashMap<String, Object>();
                        showitem.put("count_traceroute", trancerouteLine0[i]);
                        showitem.put("ip_traceroute", trancerouteLine1[i]);
                        showitem.put("dalaytime_traceroute", trancerouteLine2[i]);
                        trancerouteListitem.add(showitem);
                        NanjinginfoAdapter.notifyDataSetChanged();
                    }

//                    if (returnValue.contains("|")){
//
//                        trancerouteinfodata.add(msg.obj + "");
//                        NanjinginfoAdapter.notifyDataSetChanged();
//                    }else if (returnValue.contains("@")){
//                        infodata.add(msg.obj + "");
//                        //infoAdapter.notifyDataSetChanged();
//                    }
                    break;
                case MESSAGE_UPDATE_INFO_NANJING_COMPLETE:
                    if(bt_start != null)
                        bt_start.setEnabled(true);
                    if(bt_back != null)
                        bt_back.setEnabled(true);
                    if(mySweetALertDialog != null &&mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                    //doUpdateProgressInfo("测试完成！");
                    break;

                case MESSAGE_TIMERSCHEDULESUCCESS:
                    String timerscheduleresult = msg.obj + "";
                    String[] result = timerscheduleresult.split("\\|");

                    if (timerscheduleresult.contains("删除成功")){
                        mySweetALertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("删除成功！");
                        mySweetALertDialog.show();
                        mySweetALertDialog.setCancelable(true);
                        tvstartdate.setText(" ");
                        tvstarttime.setText(" ");
                        tvdeaddate.setText(" ");
                        tvdeadtime.setText(" ");
                        et_timerscheduletest_deshost.setText("");
                        et_interval.setText("");
                        et_threshold.setText("");
                    }else if (timerscheduleresult.contains("设定成功")){
                        mySweetALertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("设定成功！");
                        mySweetALertDialog.show();
                        mySweetALertDialog.setCancelable(true);
                        bt_start.setEnabled(false);
                    }
                    else if (!result[0].equals(" ")&&!result[1].equals(" ")&&!result[2].equals(" ")&&!result[3].equals(" ")&&!result[4].equals(" ")) {
                        startdt = Integer.parseInt(result[0]);
                        deaddt = Integer.parseInt(result[1]);
                        String lastdt = String.valueOf(result[5]);
                        String interval = String.valueOf(result[2]);
                        String threshold = String.valueOf(result[3]);
                        String desthost = String.valueOf(result[4]);
                        //String lastTestTime = result[5];

                        String startdateAndtime = DateFormatUtil.transForDate(startdt,"yyyy年MM月dd日HH时mm分");
                        String deaddateAndtime = DateFormatUtil.transForDate(deaddt,"yyyy年MM月dd日HH时mm分");
                        String lastTestTime =" ";
                        if (!lastdt.equals(" ")){
                            int lasttesttime = DateFormatUtil.transForMilliSecond(lastdt,"yyyyMMddHHmmss");
                            lastTestTime = DateFormatUtil.transForDate(lasttesttime,"yyyy年MM月dd日HH时mm分");
                        }

                        int startri = startdateAndtime.indexOf("日");
                        int deadri = deaddateAndtime.indexOf("日");
                        tvstartdate.setText(startdateAndtime.substring(0,startri+1));
                        tvstarttime.setText(startdateAndtime.substring(startri+1));
                        tvdeaddate.setText(deaddateAndtime.substring(0,deadri+1));
                        tvdeadtime.setText(deaddateAndtime.substring(deadri+1));
                        tvlasttesttime.setText( lastTestTime+"");
                        et_timerscheduletest_deshost.setText(desthost);
                        et_interval.setText(interval);
                        et_threshold.setText(threshold);
                    } else {

                    }

                    break;

                case MESSAGE_GETIP_FAIL:
                    if(bt_start != null)
                        bt_start.setEnabled(true);
                    if(bt_back != null)
                        bt_back.setEnabled(true);
                    failString = msg.obj + "";
                    if(mySweetALertDialog != null && mySweetALertDialog.isShowing())
                        mySweetALertDialog.dismiss();
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("")
                            .setContentText(failString)
                            .setConfirmText("关闭")
                            .show();
                    //doUpdateProgressInfo("测试完成！");
                    break;

                case MESSAGE_DATA_NOTIFY_ONE:
                    resultList.clear();
                    resultList.addAll(resultList_one);
                    adapter_showResult.notifyDataSetChanged();
                    int number_one = resultList.size();
                    listView_webTest_showResult.setSelection(number_one - 1);
                    break;
                case MESSAGE_DATA_NOTIFY_TWO:
                    resultList.clear();
                    resultList.addAll(resultList_two);
                    adapter_showResult.notifyDataSetChanged();
                    int number_two = resultList.size();
                    listView_webTest_showResult.setSelection(number_two - 1);
                    break;
                case MESSAGE_DATA_NOTIFY_THREE:
                    resultList.clear();
                    resultList.addAll(resultList_three);
                    adapter_showResult.notifyDataSetChanged();
                    int number_three = resultList.size();
                    listView_webTest_showResult.setSelection(number_three - 1);
                    break;
                case MESSAGE_DATA_NOTIFY_FOUR:
                    resultList.clear();
                    resultList.addAll(resultList_four);
                    adapter_showResult.notifyDataSetChanged();
                    int number_four = resultList.size();
                    listView_webTest_showResult.setSelection(number_four - 1);
                    break;
                case MESSAGE_DATA_NOTIFY_FIVE:
                    resultList.clear();
                    resultList.addAll(resultList_five);
                    adapter_showResult.notifyDataSetChanged();
                    int number_five = resultList.size();
                    listView_webTest_showResult.setSelection(number_five - 1);
                    break;

                case MESSAGE_WEBTEST_FINISH:
                    test_error_count = 0;
                    test_success_count = 0;
                    bt_start.setEnabled(true);
                    bt_back.setEnabled(true);
                    btn_add.setEnabled(true);
                    isEnable = true;
                    webNameListAdapter.setEnable(isEnable);
                    webNameListAdapter.notifyDataSetChanged();
                    break;

                case MESSAGE_CHANGETABNAME:
                    Bundle bundle = (Bundle) msg.obj;
                    int number = (int)bundle.get("number");
                    String title = bundle.getString("title");
                    doChangeTabName(number,title);
                    break;

                case MESSAGE_CHANGE_TAB_STATE:
                    Bundle bundle_tabState = (Bundle) msg.obj;
                    int number_tabState = bundle_tabState.getInt("number");
                    int color = bundle_tabState.getInt("color");
                    Log.d("MESSAGE_CHANGE_TAB_STATE","number_tabState"+ number_tabState + "---color" + color);
                    switch (number_tabState){
                        case 0:
                            tab_one.setTextColor(color);
                            break;
                        case 1:
                            tab_two.setTextColor(color);
                            break;
                        case 2:
                            tab_three.setTextColor(color);
                            break;
                        case 3:
                            tab_four.setTextColor(color);
                            break;
                        case 4:
                            tab_five.setTextColor(color);
                            break;
                        default:
                            break;
                    }
                    break;

                default:
                    super.handleMessage(msg);
                    break;
            }

        }
    };

    private FrameLayout flyt_testinfo,flyt_nanjingping;
    private RelativeLayout rlyt_testtype;
    private ListView lv_tools;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this.getActivity();
        this.instance = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_nettools,container,false);
        /*
        binding = FragmentNettoolsBinding.inflate(inflater,container,true);
        flyt_testinfo =  binding.nettoolsFlytTestinfo;
        rlyt_testtype = binding.nettoolsRlytTesttype;
        flyt_nanjingping = binding.nettoolsFlytNanjingPing;
        lv_tools = binding.nettoolsListviewTools;
        */
        flyt_testinfo = view.findViewById(R.id.nettools_flyt_testinfo);
        rlyt_testtype = view.findViewById(R.id.nettools_rlyt_testtype);
        flyt_nanjingping = view.findViewById(R.id.nettools_flyt_nanjing_ping);
        lv_tools = view.findViewById(R.id.nettools_listview_tools);
        rlyt_testtype.setVisibility(View.VISIBLE);
        flyt_testinfo.setVisibility(View.GONE);
/*        tv_ping.setOnClickListener(this);
        tv_traceroute.setOnClickListener(this);*/
        toolsList.add("PING测试");
        toolsList.add("TRACEROUTE测试");
        toolsList.add("对测");
        toolsList.add("路由跟踪");
        toolsList.add("定时测试");
        toolsList.add("网站测试");
        toolsListAdapter = new ToolsListAdapter(context, toolsList);
        lv_tools.setAdapter(toolsListAdapter);
        lv_tools.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTestView(toolsListAdapter.getItem(i));
            }
        });
        infoAdapter = new ArrayAdapter<>(context, R.layout.adapter_nettools_info, infodata);
        pinginfoAdapter = new ArrayAdapter<>(context, R.layout.adapter_nettools_info, pinginfodata);
        //NanjinginfoAdapter = new ArrayAdapter<>(context, R.layout.adapter_nettools_info,trancerouteinfodata);
        NanjinginfoAdapter = new SimpleAdapter(context, trancerouteListitem,R.layout.adapter_nettools_info_nanjing,new String[]{"count_traceroute", "ip_traceroute", "dalaytime_traceroute"}, new int[]{R.id.count_traceroute, R.id.ip_traceroute, R.id.dalaytime_traceroute});

        return view;
    }

    @Override
    public void setPresenter(NetToolsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void doUpdateProgressInfo(String info) {
        if (info.contains("|")){
            String[] infos = info.split("\\|");

            for (int i=0;i<trancerouteLine1.length;i++){

                if (infos[1].equals(trancerouteLine1[i])){
                    TrancerouteAndPingLog.insertOperatePing(infos[0],infos[1],infos[2],"0");
                //IPMap.put(trancerouteLine1[i],infos[0]);
                }
            }
        }

        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_UPDATE_INFO,info));
    }


    @Override
    public void doUpdateProgressInfoNanJingRePing(String info) {

        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_UPDATE_INFONANJINGPING,info));
    }

    @Override
    public void doUpdateTimerScheduleResult(String info) {
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_TIMERSCHEDULESUCCESS,info));

    }

    //add by hzj at 2018.12.4

    private void doNotifyListView(int number){
        Log.d("doNotifyListView()","currentTabSelect:" + currentTabSelect+ " -----number" + number);
        if(currentTabSelect != number) return; //
        switch (number){
            case 0:
                myHandler.sendEmptyMessage(MESSAGE_DATA_NOTIFY_ONE);
                break;
            case 1:
                myHandler.sendEmptyMessage(MESSAGE_DATA_NOTIFY_TWO);
                break;
            case 2:
                myHandler.sendEmptyMessage(MESSAGE_DATA_NOTIFY_THREE);
                break;
            case 3:
                myHandler.sendEmptyMessage(MESSAGE_DATA_NOTIFY_FOUR);
                break;
            case 4:
                myHandler.sendEmptyMessage(MESSAGE_DATA_NOTIFY_FIVE);
                break;
        }
    }

    private void doAddInfoToResultList(int number,String msg){
        switch (number){
            case 0:
                resultList_one.add(msg);
                break;
            case 1:
                resultList_two.add(msg);
                break;
            case 2:
                resultList_three.add(msg);
                break;
            case 3:
                resultList_four.add(msg);
                break;
            case 4:
                resultList_five.add(msg);
                break;
        }
        doNotifyListView(number);
    }

    @Override
    public void doWebTestResolveError(int number, String errorInfo) {
        doAddInfoToResultList(number,errorInfo);

        Bundle bundle = new Bundle();
        bundle.putInt("number",number);
        bundle.putInt("color",TAB_STATE_TESTERROR);
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_CHANGE_TAB_STATE,bundle));

        test_error_count ++;
        if(test_error_count + test_success_count >= list_webNames.size()){
            myHandler.sendEmptyMessage(MESSAGE_WEBTEST_FINISH);
        }
    }

    @Override
    public void doWebTestResolveSuccess(int number, String ip,String resolveTimeDelay,String title) {
        Log.d("doWebTestResolveSuccess","number"+number);
        map_ResolveDelay.put(number,resolveTimeDelay);
        doAddInfoToResultList(number,"域名解析完成。");
        doAddInfoToResultList(number,ip);

        if(title.equals(" ")) return;

        Bundle bundle = new Bundle();
        bundle.putInt("number",number);
        bundle.putString("title",title);
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_CHANGETABNAME,bundle));

    }

    @Override
    public void doWebTestPingProcess(int number, int childNumber, String info) {
        doAddInfoToResultList(number,info);
    }

    @Override
    public void doWebTestPingError(int number, int childNumber, String errorInfo) {
        doAddInfoToResultList(number,errorInfo);
//        doChangeTabState(number,TAB_STATE_TESTERROR);
        Bundle bundle = new Bundle();
        bundle.putInt("number",number);
        bundle.putInt("color",TAB_STATE_TESTERROR);
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_CHANGE_TAB_STATE,bundle));

        test_error_count ++;
        if(test_error_count + test_success_count >= list_webNames.size()){
            myHandler.sendEmptyMessage(MESSAGE_WEBTEST_FINISH);
        }
    }

    @Override
    public void doWebTestPingSucess(int number, int childNumber, String info) {
        doAddInfoToResultList(number,info);
    }

    @Override
    public void doWebTestTracerouteProcess(int number, String info) {
        doAddInfoToResultList(number,info);
    }

    @Override
    public void doWebTestTracerouteError(int number, String errorInfo) {
        doAddInfoToResultList(number,errorInfo);

        Bundle bundle = new Bundle();
        bundle.putInt("number",number);
        bundle.putInt("color",TAB_STATE_TESTERROR);
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_CHANGE_TAB_STATE,bundle));

        test_error_count ++;
        if(test_error_count + test_success_count >= list_webNames.size()){
            myHandler.sendEmptyMessage(MESSAGE_WEBTEST_FINISH);
        }
    }

    @Override
    public void doWebTestTracerouteSucess(int number, String info) {
        doAddInfoToResultList(number,info);
        doAddInfoToResultList(number,"正在计算延时时长......");
    }

    @Override
    public void doWebTestWebDelayError(int number, String errorInfo) {
        doAddInfoToResultList(number,errorInfo);

        Bundle bundle = new Bundle();
        bundle.putInt("number",number);
        bundle.putInt("color",TAB_STATE_TESTERROR);
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_CHANGE_TAB_STATE,bundle));

        test_error_count ++;
        if(test_error_count + test_success_count >= list_webNames.size()){
            myHandler.sendEmptyMessage(MESSAGE_WEBTEST_FINISH);
        }
    }

    @Override
    public void doWebTestWebDelaySuccess(int number, String info) {
        String resolveTimeDelay = map_ResolveDelay.get(number);

        Bundle bundle = new Bundle();
        bundle.putInt("number",number);
        bundle.putInt("color",TAB_STATE_TESTSUCCESS);
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_CHANGE_TAB_STATE,bundle));

        doAddInfoToResultList(number,"域名解析延时时长：" + resolveTimeDelay + "ms");
        doAddInfoToResultList(number,info);

        test_success_count ++;
        if(test_success_count + test_error_count >= list_webNames.size()){
            myHandler.sendEmptyMessage(MESSAGE_WEBTEST_FINISH);
        }
    }

    @Override
    public void doTestComplete() {
        myHandler.sendEmptyMessage(MESSAGE_TEST_COMPLETE);
    }

    @Override
    public void doTestCompleteNanjing(String info) {
        if (info.contains("ping测试完成")){

                myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_UPDATE_INFO_NANJING_COMPLETE));
                //myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_UPDATE_INFO_NANJING,Urlnumber));
//写入txt
        }
        myHandler.sendEmptyMessage(MESSAGE_TEST_COMPLETE);
    }

    @Override
    public void doTestFail(String failString) {
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_TEST_FAIL,failString));
    }

    @Override
    public void doShowIperfDownloadBandwidth(String bandwidth) {
        //53.5 Mbits/sec
        Float f = getBandwidth_Float(bandwidth);
        if(f != null) {
            iperf_downbandwidthList.add(f);
            if(f > iperf_downbandwidth_peak)
                iperf_downbandwidth_peak = f;
        }
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_IPERF_DOWNLOADING,bandwidth));
    }

    @Override
    public void doShowIperfUploadBandwidth(String bandwidth) {
        Float f = getBandwidth_Float(bandwidth);
        if(f != null) {
            iperf_upbandwidthList.add(f);
            if(f > iperf_upbandwidth_peak)
                iperf_upbandwidth_peak = f;
        }
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_IPERF_UPLOADING,bandwidth));
    }

    @Override
    public void doIperfDownloadComplete(String bandwidth) {
        String dBandwidthString = "";
        float dBandwidth = 0f;
        if (bandwidth.length() > 0) {
            dBandwidthString = bandwidth;
        } else {
            for (int i = 0; i < iperf_downbandwidthList.size(); i++) {
                dBandwidth += iperf_downbandwidthList.get(i);
            }
            dBandwidthString = ProjectUtil.df2.format(dBandwidth / (float) iperf_downbandwidthList.size()) + "Mbps";
        }
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_IPERF_DOWNLOADED,dBandwidthString));
    }

    @Override
    public void doIperfUploadComplete(String bandwidth) {
        String uBandwidthString = "";
        float uBandwidth = 0f;
        if (bandwidth.length() > 0) {
            uBandwidthString = bandwidth;
        } else {
            for (int i = 0; i < iperf_upbandwidthList.size(); i++) {
                uBandwidth += iperf_upbandwidthList.get(i);
            }
            uBandwidthString = ProjectUtil.df2.format(uBandwidth / (float) iperf_upbandwidthList.size()) + "Mbps";
        }
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_IPERF_UPLOADED,uBandwidthString));
    }

    @Override
    public void doIperfTestComplete() {
        myHandler.sendEmptyMessage(MESSAGE_IPERF_COMPLETE);
    }

    @Override
    public void doIperfTestFail(String failReason) {
        bTestError = true;
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_IPERF_ERROR,failReason));
    }

    @Override
    public void doNanJingPingAndTraceroute(String info) {
        //System.out.print("nanjinginfo"+info);
        //TrancerouteAndPingLog.insertOperate(info,);
        //if (info.contains("|")){
            String[] infos = info.split("\\|");
        TrancerouteAndPingLog.insertOperate(infos[0],infos[1],infos[2],"0");
        //}

        if (infos[0].contains("ms")){
            //int q = info.indexOf("|");
            int w = infos[0].indexOf("(");
            int e = infos[0].indexOf(")");
            //int r = info.indexOf("@");
            String Line0 = infos[0].substring(0,3);//序号
            String Line1 = infos[0].substring(w+1,e);//ipe
            String Line2 = infos[0].substring(e+1);//时间
            List<String> list0 = new ArrayList(Arrays.asList(trancerouteLine0));
            list0.add(Line0);
            trancerouteLine0= list0.toArray(new String[0]);
            List<String> list1 = new ArrayList(Arrays.asList(trancerouteLine1));
            list1.add(Line1);
            trancerouteLine1= list1.toArray(new String[0]);
            List<String> list2 = new ArrayList(Arrays.asList(trancerouteLine2));
            list2.add(Line2);
            trancerouteLine2= list2.toArray(new String[0]);

            //ipList.add(Line1);
        }else if (infos[0].contains("* * *")){
            //int q = info.indexOf("|");
            String Line0 = info.substring(0,3);
            String Line1 = "*  *  *";
            String Line2 = " ";
            List<String> list0 = new ArrayList(Arrays.asList(trancerouteLine0));
            list0.add(Line0);
            trancerouteLine0= list0.toArray(new String[0]);
            List<String> list1 = new ArrayList(Arrays.asList(trancerouteLine1));
            list1.add(Line1);
            trancerouteLine1= list1.toArray(new String[0]);
            List<String> list2 = new ArrayList(Arrays.asList(trancerouteLine2));
            list2.add(Line2);
            trancerouteLine2= list2.toArray(new String[0]);
            List<String> list3 = new ArrayList(Arrays.asList(unknowhost));
            list3.add(Line0);
            unknowhost = list3.toArray(new String[0]);
            //ipList.add(Line1);
        }

        if (infos[0].contains("测试完成")){
//            ipListAdapter = new ToolsListAdapter(context, ipList);
//            lv_progressinfo.setAdapter(ipListAdapter);
            //int q = info.indexOf("@");
            //String Urlnumber = info.substring(q+1);
            //if (Urlnumber.equals("0")){
                //myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_UPDATE_INFO_NANJING_COMPLETE));
//            for (int i=0;i<trancerouteLine1.length;i++){
//                IPMap.put(trancerouteLine1[i]," ");
//            }

                myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_UPDATE_INFO_NANJING));
            //}

        }
        //myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_UPDATE_INFO_NANJING));
    }

    @Override
    public void doGetIpFail(String failString) {
        bTestError = true;
        myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_GETIP_FAIL,failString));
    }

    private void showTestView(WorkUtils.NetToolsType testType) {
        this.testType = testType;
        clearInfo();
        switch (testType) {
            case PING:
                showPingView();
                break;

            case TRACEROUTE:
                showTracerouteView();
                break;

            case IPERF:
                if(ProjectUtil.isBoxSe()) {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("SE版本暂不支持对测！")
                            .show();
                    return;
                }
                showIperfView();
                break;

            case PINGANDTRACEROUTE:
                showPingAndTracerouteView();
                break;

            case TIMERSCHEDULE:
                showTimerSchduleTestView();
                break;

            case WEBSITETEST:
                if(ProjectUtil.isBoxSe()) {
                    new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("SE版本暂不支持对测！")
                            .show();
                    return;
                }
                showWebTestView();
                break;
        }
        if(bt_back != null)
            bt_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lv_progressinfo = null;
                    bt_back = null;
                    bt_start = null;
                    rlyt_testtype.setVisibility(View.VISIBLE);
                    flyt_testinfo.setVisibility(View.GONE);
                    flyt_testinfo.removeAllViews();
                }
            });
        rlyt_testtype.setVisibility(View.GONE);
        flyt_testinfo.setVisibility(View.VISIBLE);

    }

    private void showPingView() {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.content_pingtest,null);
        bt_start = (Button) view.findViewById(R.id.pingsource_button_start);
        bt_back = (Button) view.findViewById(R.id.pingsource_button_back);
        et_ping_deshost = (EditText) view.findViewById(R.id.pingsource_edittext_uri);
        et_ping_count = (EditText) view.findViewById(R.id.pingsource_edittext_count);
        lv_progressinfo = (ListView) view.findViewById(R.id.nettoolsprogress_listview_info);
        lv_progressinfo.setVisibility(View.GONE);
        lv_progressinfo.setAdapter(infoAdapter);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String host;
                String count;
                lv_progressinfo.setVisibility(View.VISIBLE);
                bt_start.setEnabled(false);
                bt_back.setEnabled(false);
                host = et_ping_deshost.getText().toString();
                count = et_ping_count.getText().toString();
                if(null == host || "".equals(host))
                    host = "www.baidu.com";
                if(null == count || "".equals(count))
                    count = "10";
//                if(et_ping_deshost != null)
//                    host = et_ping_deshost.getText().toString();
//                else
//                    host = "www.baidu.com";
//                if(et_ping_count != null)
//                    count = Integer.parseInt(et_ping_count.getText().toString());
//                else
//                    count = 10;
                clearInfo();
                mPresenter.doStartPing(host,count);
            }
        });
        flyt_testinfo.addView(view);
    }

    private void showTracerouteView() {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.content_traceroutetest,null);
        bt_start = (Button) view.findViewById(R.id.traceroutesource_button_start);
        bt_back = (Button) view.findViewById(R.id.traceroutesource_button_back);
        et_traceroute_host = (EditText) view.findViewById(R.id.traceroutesource_edittext_uri);
        lv_progressinfo = (ListView) view.findViewById(R.id.nettoolsprogress_listview_info);
        lv_progressinfo.setVisibility(View.GONE);
        lv_progressinfo.setAdapter(infoAdapter);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String host;
                lv_progressinfo.setVisibility(View.VISIBLE);
                bt_start.setEnabled(false);
                bt_back.setEnabled(false);
                host = et_traceroute_host.getText().toString();
                if(null == host || "".equals(host))
                    host = "www.baidu.com";
//                if(et_traceroute_host != null)
//                    host = et_traceroute_host.getText().toString();
//                else
//                    host = "www.baidu.com";
                clearInfo();
                mPresenter.doStartTraceroute(host);
            }
        });
        flyt_testinfo.addView(view);
    }

    private void showIperfView() {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.content_iperftest,null);
        llyt_iperf_result = (LinearLayout) view.findViewById(R.id.llyt_iperf_result);
        bt_start = (Button) view.findViewById(R.id.btn_iperf_start);
        bt_back = (Button) view.findViewById(R.id.btn_iperf_back);
        et_iperf_serverhost = (EditText) view.findViewById(R.id.edittext_iperf_serverhost);
        et_iperf_testtime = (EditText) view.findViewById(R.id.edittext_iperf_testtime);
        et_iperf_testparallel = (EditText) view.findViewById(R.id.edittext_iperf_testparallel);
        tv_iperf_downbandwidth = (TextView) view.findViewById(R.id.textview_iperf_downbandwidth);
        tv_iperf_upbandwidth = (TextView) view.findViewById(R.id.textview_iperf_upbandwidth);
        tv_iperf_downbandwidth_peak = (TextView) view.findViewById(R.id.textview_iperf_downbandwidthpeak);
        tv_iperf_upbandwidth_peak = (TextView) view.findViewById(R.id.textview_iperf_upbandwidthpeak);
        et_iperf_serverhost.setText("192.168.1.4");
        et_iperf_testtime.setText("10");
        et_iperf_testparallel.setText("4");
        llyt_iperf_result.setVisibility(View.GONE);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bt_start.setEnabled(false);
                bt_back.setEnabled(false);
                bTestError = false;
                tv_iperf_downbandwidth.setText("");
                tv_iperf_upbandwidth.setText("");
                tv_iperf_upbandwidth_peak.setText("");
                tv_iperf_downbandwidth_peak.setText("");
                iperf_downbandwidth_peak = 0f;
                iperf_upbandwidth_peak = 0f;
                iperf_downbandwidthList.clear();
                iperf_upbandwidthList.clear();
                String serverhost = et_iperf_serverhost.getText().toString();
                int testtime = Integer.parseInt(et_iperf_testtime.getText().toString());
                int testparallel = Integer.parseInt(et_iperf_testparallel.getText().toString());
                mySweetALertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("准备测试");
                mySweetALertDialog.show();
                mySweetALertDialog.setCancelable(false);
                mPresenter.doStartIperf(serverhost,testtime,testparallel);
            }
        });
        flyt_testinfo.addView(view);
    }

    private void showPingAndTracerouteView() {
        //created by yangke 2018/07/10
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.content_pingandtraceroutetest,null);
        bt_start = (Button) view.findViewById(R.id.pingandtraceroute_button_start);
        bt_back = (Button) view.findViewById(R.id.pingandtraceroute_button_back);
        bt_save = (Button) view.findViewById(R.id.pingandtraceroute_button_save);
        //bt_add = (Button) view.findViewById(R.id.pingandtraceroute_button_set);
        et_pingandtraceroute_deshost = (EditText) view.findViewById(R.id.pingandtraceroute_edittext_uri);
//        et_ping_count = (EditText) view.findViewById(R.id.pingsource_edittext_count);
        lv_progressinfo = (ListView) view.findViewById(R.id.fujiaziduanlist);
        lv_progressinfo.setVisibility(View.GONE);
        lv_progressinfo.setAdapter(NanjinginfoAdapter);
        //lv_progressinfo.setOnItemClickListener((AdapterView.OnItemClickListener) context);
        //contentView = inflater.inflate(R.layout.fragment_fujiaziduan, null);
        //dataList = new ArrayList<Map<String, Object>>();
        //listview = (ListView) contentView.findViewById(R.id.fujiaziduanlist);
        //网址下拉框
//        tranceRouteList = new ArrayList<CharSequence>();
//        tranceRouteList.add("www.baidu.com");
//        tranceRouteList.add("www.taobao.com");
//        spiEdu = (Spinner) view.findViewById(R.id.pingandtraceroute_spinner);
//        spiEdu.setPrompt("请选择网址");
//        adapteEdu = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, tranceRouteList);
//        adapteEdu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spiEdu.setAdapter(adapteEdu);
//        spiEdu.setOnItemSelectedListener(new SpinnerListener());

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String host;
                int count;
                //lv_progressinfo=null;
                int size=trancerouteListitem.size();//获取数据集的个数
                if(size>0){
                    System.out.println(size);
                    trancerouteListitem.removeAll(trancerouteListitem);//清空数据集
                    NanjinginfoAdapter.notifyDataSetChanged();//通知下观察者我更改了数据
                    lv_progressinfo.setAdapter(NanjinginfoAdapter);//重新设置adapter
                }
                trancerouteLine0=new String[0];
                trancerouteLine1=new String[0];
                trancerouteLine2=new String[0];
                //NanjinginfoAdapter = new SimpleAdapter(context, trancerouteListitem,R.layout.adapter_nettools_info_nanjing,new String[]{"count_traceroute", "ip_traceroute", "dalaytime_traceroute"}, new int[]{R.id.count_traceroute, R.id.ip_traceroute, R.id.dalaytime_traceroute});
                //trancerouteListitem.clear();
                lv_progressinfo.setVisibility(View.VISIBLE);
                bt_start.setEnabled(false);
                bt_back.setEnabled(false);
                mySweetALertDialog = new SweetAlertDialog(context,SweetAlertDialog.PROGRESS_TYPE)
                        .setTitleText("正在tranceroute测试");
                mySweetALertDialog.show();
                mySweetALertDialog.setCancelable(false);
                host = et_pingandtraceroute_deshost.getText().toString();
                if(null == host || "".equals(host))
                    host = "www.baidu.com";
//                if(et_pingandtraceroute_deshost != null)
//                    host = et_pingandtraceroute_deshost.getText().toString();
//                else
//                    host = "www.baidu.com";
                String[]ddd = {host};
                //String[]ddd = new String[1];
                //ddd[1]=host;
//                if(et_ping_count != null)
//                    count = Integer.parseInt(et_ping_count.getText().toString());
//                else
//                    count = 10;
                clearInfo();
                mPresenter.doStartPingAndTraceroute(ddd);
            }
        });
        lv_progressinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                int[] arr = new int[unknowhost.length];
                for (int i = 0; i < unknowhost.length; i++) {
                    int unknowhostNumber = Integer.parseInt(unknowhost[i].trim());
                    arr[i] = unknowhostNumber;
                }
                int result = Arrays.binarySearch(arr, position+1);
                    if (result>=0){

                    }else {
                        Log.i("tag"," P:p: "+position);
                        showTranceroutePingView(position);
                        //Log.i("tag"," P: "+unknowhostNumber);
                    }

            }
        });
//        bt_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String hostvalue = "|"+trancerouteLine1[position];
//                String value=" ";
//                String systime=" ";
//                //infodata.contains(host);
//                for (int i =0;i<infodata.size();i++){
//                    if (infodata.get(i).contains(hostvalue)){
//                        int q1 = infodata.get(i).indexOf("|");
//                        int q2 = infodata.get(i).lastIndexOf("|");
//                        value = infodata.get(i).substring(0,q1);
//                        systime = infodata.get(i).substring(q2+1);
//                        //hostvalue = hostvalue.substring(1);
//                        pinginfodata.add(value);
//                        TrancerouteAndPingLog.insertOperatePing(value,trancerouteLine1[position],systime,"0");
//                    }
//                }
//
//            }
//        });
        //网址新增按钮
//        bt_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showNanjingTrancerouteUrlView();
//
//            }
//        });

        flyt_testinfo.addView(view);
    }

    private void showTimerSchduleTestView() {
        //created by yangke 2018/07/10
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.content_timerscheduletest,null);
        calendar = Calendar.getInstance();
        tvlasttesttime = (TextView) view.findViewById(R.id.timerscheduletest_tv_lasttesttime);
        tvstartdate = (TextView) view.findViewById(R.id.timerscheduletest_tv_startdate);
        tvstarttime = (TextView) view.findViewById(R.id.timerscheduletest_tv_starttime);
        tvdeaddate = (TextView) view.findViewById(R.id.timerscheduletest_tv_deaddate);
        tvdeadtime = (TextView) view.findViewById(R.id.timerscheduletest_tv_deadtime);
        bt_startdate = (Button) view.findViewById(R.id.timerscheduletest_button_startdate);
        bt_starttime = (Button) view.findViewById(R.id.timerscheduletest_button_starttime);
        bt_deaddate = (Button) view.findViewById(R.id.timerscheduletest_button_deaddate);
        bt_deadtime = (Button) view.findViewById(R.id.timerscheduletest_button_deadtime);
        bt_start = (Button) view.findViewById(R.id.timerscheduletest_button_start);
        bt_back = (Button) view.findViewById(R.id.timerscheduletest_button_back);
        bt_delete = (Button) view.findViewById(R.id.timerscheduletest_button_delete);
        et_timerscheduletest_deshost = (EditText) view.findViewById(R.id.timerscheduletest_edittext_uri);
        et_interval = (EditText) view.findViewById(R.id.timerscheduletest_edittext_interval);
        et_threshold = (EditText) view.findViewById(R.id.timerscheduletest_edittext_threshold);
        mPresenter.doSearchTimerSchedule();
        long timeStamp = System.currentTimeMillis()/1000;//当前时间戳ms
        long delay = startdt - timeStamp;
        long deadtimecurrenttime = deaddt - timeStamp;
        if (delay <= 0L && deadtimecurrenttime >= 0L){
            //mPresenter.doDeleteTimerSchedule();//先删掉上一次的定时任务
            bt_start.setEnabled(false);
        }
        bt_startdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerUtils.showDatePickerDialog((Activity) context,tvstartdate,calendar);
            }
        });
        bt_starttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerUtils.showTimePickerDialog((Activity) context,tvstarttime,calendar);
            }
        });
        bt_deaddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerUtils.showDatePickerDialog((Activity) context,tvdeaddate,calendar);
            }
        });
        bt_deadtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimerUtils.showTimePickerDialog((Activity) context,tvdeadtime,calendar);
            }
        });
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String desthost = et_timerscheduletest_deshost.getText().toString();
                String startdate = tvstartdate.getText().toString();
                String starttime = tvstarttime.getText().toString();
                String deaddate = tvdeaddate.getText().toString();
                String deadtime = tvdeadtime.getText().toString();
                String interval = et_interval.getText().toString();
                String threshold = et_threshold.getText().toString();
                String storage = ProjectUtil.boxInfoBean.getTcpdumpsave();

                if(null == desthost || "".equals(desthost))
                    desthost = "www.baidu.com";

                if(null == interval || "".equals(interval))
                    interval = "10";

                if(null == threshold || "".equals(threshold))
                    threshold = "10";

                if (startdate.equals(" ")||starttime.equals(" ")||deaddate.equals(" ")||deadtime.equals(" ")){
                    mySweetALertDialog = new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("请选择时间");
                    mySweetALertDialog.show();
                    mySweetALertDialog.setCancelable(true);
                }else {

                    long starttimestamp = DateFormatUtil.transForMilliSecond(startdate+starttime,"yyyy年MM月dd日HH时mm分");
                    long deadtimestamp = DateFormatUtil.transForMilliSecond(deaddate+deadtime,"yyyy年MM月dd日HH时mm分");

                    mPresenter.doStartTimerScheduleTest(starttimestamp,deadtimestamp,interval,threshold,desthost,storage);
                    mPresenter.doSearchTimerSchedule();
//                    mySweetALertDialog = new SweetAlertDialog(context,SweetAlertDialog.SUCCESS_TYPE)
//                            .setTitleText("设定成功");
//                    mySweetALertDialog.show();
//                    mySweetALertDialog.setCancelable(true);
//                    bt_start.setEnabled(false);
                }

            }
        });
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.doDeleteTimerSchedule();
                bt_start.setEnabled(true);
                startdt = 0 ;
                deaddt = 0;
//                bt_back = null;
//                bt_start = null;
//                rlyt_testtype.setVisibility(View.VISIBLE);
//                flyt_testinfo.setVisibility(View.GONE);
//                flyt_testinfo.removeAllViews();
            }
        });
        flyt_testinfo.addView(view);
    }
    private void showTranceroutePingView( int position ) {
//created by yangke 2018/07/17
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.content_nanjing_pingtest,null);
        bt_start = (Button) view.findViewById(R.id.nanjing_pingsource_button_start);
        bt_back = (Button) view.findViewById(R.id.nanjing_pingsource_button_back);
        flyt_testinfo.setVisibility(View.GONE);
        flyt_nanjingping.setVisibility(View.VISIBLE);
        String hostvalue = "|"+trancerouteLine1[position];
        String value=" ";
        //String systime=" ";
        //infodata.contains(host);
        for (int i =0;i<infodata.size();i++){
            if (infodata.get(i).contains(hostvalue)){
                int q1 = infodata.get(i).indexOf("|");
                int q2 = infodata.get(i).lastIndexOf("|");
                value = infodata.get(i).substring(0,q1);
                //systime = infodata.get(i).substring(q2+1);
                //hostvalue = hostvalue.substring(1);
                pinginfodata.add(value);
                //TrancerouteAndPingLog.insertOperatePing(value,trancerouteLine1[position],systime,"0");
            }
        }
        et_reping_deshost = (EditText) view.findViewById(R.id.nanjing_pingsource_edittext_uri);
        et_reping_deshost.setText(trancerouteLine1[position]);
        final String host = et_reping_deshost.getText().toString();
//        et_ping_count = (EditText) view.findViewById(R.id.pingsource_edittext_count);
        lv_progressinfo = (ListView) view.findViewById(R.id.nettoolsprogress_listview_info);
        lv_progressinfo.setVisibility(View.VISIBLE);
        lv_progressinfo.setAdapter(pinginfoAdapter);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //infodata.clear();
                List<String> nanjingpinginfodata = new ArrayList<>();
//                nanjingpinginfodata=infodata;
//                nanjingpinginfodata.clear();
                NanjingPinginfoAdapter = new ArrayAdapter<>(context, R.layout.adapter_nettools_info, repinginfodata);
                lv_progressinfo.setAdapter(NanjingPinginfoAdapter);
                bt_start.setEnabled(false);
                bt_back.setEnabled(false);
                et_reping_deshost.setText(host);
                if(null == host || "".equals(host)){
                    mySweetALertDialog = new SweetAlertDialog(context,SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("请输入正确的IP地址");
                    mySweetALertDialog.show();
                    mySweetALertDialog.setCancelable(true);
                }else {
                    mPresenter.doStartRePing(host,"20");
                }
                //clearInfo();

            }
        });
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lv_progressinfo = null;
                //bt_back = null;
                //bt_start = null;
                et_reping_deshost.setText("");
                flyt_nanjingping.setVisibility(View.GONE);
                //flyt_nanjingping.removeAllViews();
                flyt_testinfo.setVisibility(View.VISIBLE);
                lv_progressinfo.setAdapter(pinginfoAdapter);
                pinginfodata.clear();
                repinginfodata.clear();
            }
        });
        flyt_nanjingping.addView(view);
    }

    //add by hzj at 2018.12.3
    private void showWebTestView(){
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.content_webtest,null);

        lv_webName = (ListView) view.findViewById(R.id.webtest_listView_webName);

        ll_webTest_tabs = (LinearLayout) view.findViewById(R.id.ll_webTest_tabs);
        btn_add = (Button) view.findViewById(R.id.webtest_btn_addDomainName);

        listView_webTest_showResult = (ListView) view.findViewById(R.id.listView_webtest_showResult);
        bt_back = (Button) view.findViewById(R.id.webtest_btn_back);
        bt_start = (Button) view.findViewById(R.id.webtest_btn_start);

        tab_one = (TextView) view.findViewById(R.id.tab_webtest_one);
        tab_two = (TextView) view.findViewById(R.id.tab_webtest_two);
        tab_three = (TextView) view.findViewById(R.id.tab_webtest_three);
        tab_four = (TextView) view.findViewById(R.id.tab_webtest_four);
        tab_five = (TextView) view.findViewById(R.id.tab_webtest_five);

        resultList_one = new ArrayList<String>();
        resultList_two = new ArrayList<String>();
        resultList_three = new ArrayList<String>();
        resultList_four = new ArrayList<String>();
        resultList_five = new ArrayList<String>();

        resultList = new ArrayList<String>();
        resultList.add("test");

        adapter_showResult = new ResultListAdapter(context,resultList);

        listView_webTest_showResult.setAdapter(adapter_showResult);

        currentTabSelect = 0;//默认选择第一个tab.
        onWhichTabSelected(1,0);

        tab_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWhichTabSelected(currentTabSelect,0);
            }
        });
        tab_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWhichTabSelected(currentTabSelect,1);
//                resultList.clear();
//                resultList.addAll(resultList_two);
//                adapter_showResult.notifyDataSetChanged();
            }
        });
        tab_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWhichTabSelected(currentTabSelect,2);
//                resultList.clear();
//                resultList.addAll(resultList_three);
//                adapter_showResult.notifyDataSetChanged();
            }
        });
        tab_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onWhichTabSelected(currentTabSelect,3);
//                resultList.clear();
//                resultList.addAll(resultList_four);
//                adapter_showResult.notifyDataSetChanged();
            }
        });
        tab_five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWhichTabSelected(currentTabSelect,4);
//                resultList.clear();
//                resultList.addAll(resultList_five);
//                adapter_showResult.notifyDataSetChanged();
            }
        });

        lv_progressinfo = (ListView)view.findViewById(R.id.webtest_listView_webName);



        list_numbers = new ArrayList<Integer>();

        list_webNames = SharedPreferencesUtil.getHistoryWebNames(context);
        if(list_webNames.size() == 0){
            list_webNames.add("www.baidu.com");
            list_webNames.add("www.sina.com");
            list_webNames.add("www.taobao.com");

            list_numbers.add(0);
            list_numbers.add(1);
            list_numbers.add(2);
        }else{
            int count = list_webNames.size();
            for(int i= 0;i<count;i++){
                list_numbers.add(i);
            }
            Log.d("测试项","xiangshu:" + count);
        }

        WebTestListViewListener listener = new WebTestListViewListener() {
            @Override
            public void onTextChanged(int pos, String webName) {
                list_webNames.set(pos,webName);
//                doChangeTabName(pos,webName);
                webNameListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClickDelete(int pos) {
                if(list_numbers.size() == 1){
                    Toast.makeText(context,"至少添加一个测试域名！",Toast.LENGTH_LONG).show();
                    return;
                }
                list_numbers.remove(pos);
                list_webNames.remove(pos);
                webNameListAdapter.notifyDataSetChanged();

//                int size = list_webNames.size();
//
//                if(currentTabSelect == size){
//                    Log.e("点击删除","currentTabSelect:"+currentTabSelect+"---size:"+size);
//                    onWhichTabSelected(currentTabSelect,size-1);
//                }

//                switch (size){
//                    case 1:
//                        tab_two.setVisibility(View.INVISIBLE);
//                        break;
//                    case 2:
//                        tab_three.setVisibility(View.INVISIBLE);
//                        break;
//                    case 3:
//                        tab_four.setVisibility(View.INVISIBLE);
//                        break;
//                    case 4:
//                        tab_five.setVisibility(View.INVISIBLE);
//                        break;
//                }
            }
        };


        webNameListAdapter = new WebNameListAdapter(context,list_numbers,list_webNames,isEnable,listener);

        lv_webName.setAdapter(webNameListAdapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(list_numbers.size() >= LISTWEBNAME_MAXNUMBER) {
                    Toast.makeText(context,"可测试域名已达到最大值！",Toast.LENGTH_LONG).show();
                    return;
                }

                list_webNames.add("");
                list_numbers.add(list_webNames.size()-1);
                adapter_showResult.notifyDataSetChanged();
                int number = list_webNames.size();
                lv_webName.setSelection(number - 1);

//                switch (number){
//                    case 1:
//                        tab_one.setVisibility(View.VISIBLE);
//                        doChangeTabName(0,"tab1");
//                        break;
//                    case 2:
//                        tab_two.setVisibility(View.VISIBLE);
//                        doChangeTabName(1,"tab2");
//                        break;
//                    case 3:
//                        tab_three.setVisibility(View.VISIBLE);
//                        doChangeTabName(2,"tab3");
//                        break;
//                    case 4:
//                        tab_four.setVisibility(View.VISIBLE);
//                        doChangeTabName(3,"tab4");
//                        break;
//                    case 5:
//                        tab_five.setVisibility(View.VISIBLE);
//                        doChangeTabName(4,"tab5");
//                        break;
//                }

            }
        });

        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listView_webTest_showResult.requestFocus();
                resultList_one.clear();
                resultList_two.clear();
                resultList_three.clear();
                resultList_four.clear();
                resultList_five.clear();
                resultList.clear();

                int count = list_webNames.size();
                switch (count){
                    case 1:
                        tab_one.setVisibility(View.VISIBLE);
                        tab_two.setVisibility(View.INVISIBLE);
                        tab_three.setVisibility(View.INVISIBLE);
                        tab_four.setVisibility(View.INVISIBLE);
                        tab_five.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        tab_one.setVisibility(View.VISIBLE);
                        tab_two.setVisibility(View.VISIBLE);
                        tab_three.setVisibility(View.INVISIBLE);
                        tab_four.setVisibility(View.INVISIBLE);
                        tab_five.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        tab_one.setVisibility(View.VISIBLE);
                        tab_two.setVisibility(View.VISIBLE);
                        tab_three.setVisibility(View.VISIBLE);
                        tab_four.setVisibility(View.INVISIBLE);
                        tab_five.setVisibility(View.INVISIBLE);
                        break;
                    case 4:
                        tab_one.setVisibility(View.VISIBLE);
                        tab_two.setVisibility(View.VISIBLE);
                        tab_three.setVisibility(View.VISIBLE);
                        tab_four.setVisibility(View.VISIBLE);
                        tab_five.setVisibility(View.INVISIBLE);
                        break;
                    case 5:
                        tab_one.setVisibility(View.VISIBLE);
                        tab_two.setVisibility(View.VISIBLE);
                        tab_three.setVisibility(View.VISIBLE);
                        tab_four.setVisibility(View.VISIBLE);
                        tab_five.setVisibility(View.VISIBLE);
                        break;
                }

                for (int i = 0;i < list_webNames.size();i++){
                    String host = list_webNames.get(i);

                    if(host.equals("") || host == null){
                        Toast.makeText(context,"测试域名不能为空！",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!host.startsWith("www.")){
                        Toast.makeText(context,"请输入正确的域名格式！(www.xxxx.xxx)",Toast.LENGTH_LONG).show();
                        return;
                    }

                    for(int j = i+1;j<list_webNames.size();j++){
                        if(list_webNames.get(i).equals(list_webNames.get(j))){
                            Toast.makeText(context,"第"+(i+1)+"行与第"+(j+1)+"行测试域名相同！",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    doChangeTabName(i,host.substring(host.indexOf(".")+1));

                    doAddInfoToResultList(i,"网站测试开始");
                    doAddInfoToResultList(i,"开始解析：" + list_webNames.get(i));

                    Bundle bundle = new Bundle();
                    bundle.putInt("number",i);
                    bundle.putInt("color",TAB_STATE_TESTING);
                    myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_CHANGE_TAB_STATE,bundle));
                }


                listView_webTest_showResult.setVisibility(View.VISIBLE);
                ll_webTest_tabs.setVisibility(View.VISIBLE);
                adapter_showResult.notifyDataSetChanged();
                mPresenter.doStartWebTest(list_webNames);

                bt_start.setEnabled(false);
                bt_back.setEnabled(false);
                btn_add.setEnabled(false);
                isEnable = false;
                webNameListAdapter.setEnable(isEnable);
                webNameListAdapter.notifyDataSetChanged();


                SharedPreferencesUtil.setHistoryWebNames(context,list_webNames);
            }
        });

        flyt_testinfo.addView(view);

    }

    private void doChangeTabName(int pos,String title){

        switch (pos){
            case 0:
                tab_one.setText(title);
                break;
            case 1:
                tab_two.setText(title);
                break;
            case 2:
                tab_three.setText(title);
                break;
            case 3:
                tab_four.setText(title);
                break;
            case 4:
                tab_five.setText(title);
                break;
            default:
                break;

        }
    }

    private void onWhichTabSelected(int currentTab,int newTab){
        Log.d("onWhichTabSelected()","currentTab:"+currentTab+"------newTab:"+newTab);
        if(currentTab == newTab){
            return;
        }
        if(currentTab == 0){
            tab_one.setTextSize(14);
        }else if(currentTab == 1){
            tab_two.setTextSize(14);
//            tab_two.setTextColor(Color.parseColor("#999494"));
        }else if(currentTab == 2){
            tab_three.setTextSize(14);
//            tab_three.setTextColor(Color.parseColor("#999494"));
        }else if(currentTab == 3){
            tab_four.setTextSize(14);
//            tab_four.setTextColor(Color.parseColor("#999494"));
        }else if(currentTab == 4){
            tab_five.setTextSize(14);
//            tab_five.setTextColor(Color.parseColor("#999494"));
        }

        if(newTab == 0){
            tab_one.setTextSize(18);
//            tab_one.setTextColor(Color.parseColor("#000000"));
        }else if(newTab == 1){
            tab_two.setTextSize(18);
//            tab_two.setTextColor(Color.parseColor("#000000"));
        }else if(newTab == 2){
            tab_three.setTextSize(18);
//            tab_three.setTextColor(Color.parseColor("#000000"));
        }else if(newTab == 3){
            tab_four.setTextSize(18);
//            tab_four.setTextColor(Color.parseColor("#000000"));
        }else if(newTab == 4){
            tab_five.setTextSize(18);
//            tab_five.setTextColor(Color.parseColor("#000000"));
        }

        currentTabSelect = newTab;
        Log.d("currentTabSelect","currentTabSelect:"+currentTabSelect);
        doNotifyListView(newTab);   //刷新列表数据

    }

    private void addTrancerouteUrlView() {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.content_nanjing_addtrancerouteurl,null);
        bt_start = (Button) view.findViewById(R.id.traceroutesource_button_start);
        bt_back = (Button) view.findViewById(R.id.traceroutesource_button_back);
        et_traceroute_host = (EditText) view.findViewById(R.id.traceroutesource_edittext_uri);
//        lv_progressinfo = (ListView) view.findViewById(R.id.nettoolsprogress_listview_info);
//        lv_progressinfo.setVisibility(View.GONE);
//        lv_progressinfo.setAdapter(infoAdapter);
        bt_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String host;
                lv_progressinfo.setVisibility(View.VISIBLE);
                bt_start.setEnabled(false);
                bt_back.setEnabled(false);
                if(et_traceroute_host != null)
                    host = et_traceroute_host.getText().toString();
                else
                    host = "www.baidu.com";
                clearInfo();
                mPresenter.doStartTraceroute(host);
            }
        });
        flyt_testinfo.addView(view);
    }
    private void clearInfo() {
        infodata.clear();
        infoAdapter.notifyDataSetChanged();
        //trancerouteinfodata.clear();
        trancerouteListitem.clear();
        NanjinginfoAdapter.notifyDataSetChanged();
    }


    private Float getBandwidth_Float(String bandwidthString) {
        Float f = null;
        int p = bandwidthString.indexOf("Mbits/sec");
        String b = bandwidthString.substring(0,p);
        try {
            f = Float.parseFloat(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    private class SpinnerListener implements AdapterView.OnItemSelectedListener {
        //created by yangke 2018/07/18
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                   long arg3) {
            //selected = arg0.getItemAtPosition(arg2).toString();
            int selectid = arg0.getSelectedItemPosition();
            myHandler.sendMessage(myHandler.obtainMessage(MESSAGE_UPDATE_INFO_NANJING,selectid));
            //Toast.makeText(SsxxInfoUpdateActivity.this, "what you selected is :"+selected, Toast.LENGTH_LONG).show();
            android.util.Log.d("test", "what you selected is :" + selectid);
        }
        public void onNothingSelected(AdapterView<?> arg0) {
            //Toast.makeText(SsxxInfoUpdateActivity.this, "you have selected nothing", Toast.LENGTH_LONG).show();
            Log.d("test", "you have selected nothing");
        }
    }



//    public static boolean useLoop(String[] arr,String targetValue){
//        for(String s:arr){
//            if(s.equals(targetValue))
//                return true;
//        }
//        return false;
//    }

}
