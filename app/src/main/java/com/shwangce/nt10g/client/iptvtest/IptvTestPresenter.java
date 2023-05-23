package com.shwangce.nt10g.client.iptvtest;

import androidx.annotation.NonNull;

import com.shwangce.nt10g.client.iptvtest.IptvTestResultBean.EthernetInfoBean;
import com.shwangce.nt10g.client.iptvtest.IptvTestResultBean.IPv4InfoBean;
import com.shwangce.nt10g.client.iptvtest.IptvTestResultBean.QualityInfoBean;
import com.shwangce.nt10g.client.iptvtest.IptvTestResultBean.UdpInfoBean;
import com.shwangce.nt10g.client.iptvtest.IptvTestResultBean.VlanInfoBean;
import com.shwangce.nt10g.client.main.MainPresenter;
import com.shwangce.nt10g.client.main.MainPresenterListener;
import com.shwangce.nt10g.client.util.Log;

/**
 * Created by Yannik on 2017/11/27.
 */

public class IptvTestPresenter implements IptvTestContract.Presenter {

    private final IptvTestContract.View mView;
    private final MainPresenter mainPresenter;
    private final MainPresenterListener.IptvTestListener iptvTestListener = new MainPresenterListener.IptvTestListener() {

        @Override
        public void showIptv_StartSuccess() {
            mView.doShowIptvStartSuccess();
        }

        @Override
        public void showIptv_StartFail(String failInfo) {
            mView.doShowIptvStartFail(failInfo);
        }

        @Override
        public void showIptv_Result_EthnetInfo(String resultString) {
            Log.d("showIptv_Result_EthnetInfo",resultString);
            String[] params = resultString.split(",");
            try {
                if (params.length >=2) {
                    EthernetInfoBean bean = new EthernetInfoBean(params[0], params[1]);
                    mView.doShowIptvEthernetInfo(bean);
                }
            } catch (Exception e) {
                Log.d("showIptv_Result_EthnetInfo",e.getMessage());
            }
        }

        @Override
        public void showIptv_Result_VlanInfo(String resultString) {
            Log.d("showIptv_Result_VlanInfo",resultString);
            String[] params = resultString.split(",");
            try {
                if (params.length >=2) {
                    VlanInfoBean bean = new VlanInfoBean(params[0], params[1]);
                    mView.doShowIptvVlanInfo(bean);
                }
            } catch (Exception e) {
                Log.d("showIptv_Result_VlanInfo",e.getMessage());
            }
        }

        @Override
        public void showIptv_Result_Ipv4Info(String resultString) {
            Log.d("showIptv_Result_Ipv4Info",resultString);
            String[] params = resultString.split(",");
            try {
                if (params.length >=4) {
                    IPv4InfoBean bean = new IPv4InfoBean(params[0], params[1],params[2], params[3]);
                    mView.doShowIptvIPv4Info(bean);
                }
            } catch (Exception e) {
                Log.d("showIptv_Result_Ipv4Info",e.getMessage());
            }
        }

        @Override
        public void showIptv_Result_UdpInfo(String resultString) {
            Log.d("showIptv_Result_UdpInfo",resultString);
            String[] params = resultString.split(",");
            try {
                if (params.length >=2) {
                    UdpInfoBean bean = new UdpInfoBean(params[0], params[1]);
                    mView.doShowIptvUdpInfo(bean);
                }
            } catch (Exception e) {
                Log.d("showIptv_Result_UdpInfo",e.getMessage());
            }
        }

        @Override
        public void showIptv_Result_QualityInfo(String resultString) {
            Log.d("showIptv_Result_QualityInfo",resultString);
            String[] params = resultString.split(",");
            try {
                if (params.length >=9) {
                    QualityInfoBean bean = new QualityInfoBean(params[0], params[1],params[2], params[3],params[4], params[5],params[6],params[7],params[8]);
                    mView.doShowIptvQualityInfo(bean);
                }
            } catch (Exception e) {
                Log.d("showIptv_Result_QualityInfo",e.getMessage());
            }
        }

        /*
        @Override
        public void showIptv_Result_1(String iptvresult) {
            Log.d("showIptv_Result_1",iptvresult);
            String[] params = iptvresult.split(",");
            try {
                if (params.length >= 4) {
                    final String tos16 = params[2];
                    String tos10 = "";
                    if (tos16.startsWith("0x")) {
                        long out = Integer.parseInt(tos16.substring(2, tos16.length()), 16);
                        tos10 = String.valueOf(out);
                    } else {
                        tos10 = params[2];
                    }
                    final String tosfinalresult = tos10;
                    IptvTestResult1Bean bean = new IptvTestResult1Bean(params[0], params[1], params[2], params[3]);
                    mView.doShowIptvResult1(bean);
                }
            } catch (Exception e) {
                Log.d("showIptv_Result_1",e.getMessage());
            }
        }

        @Override
        public void showIptv_Result_2(String iptvresult) {
            Log.d("showIptv_Result_2",iptvresult);
            int p = iptvresult.indexOf(",");
            if(p > 0){
                try {
                    String leftstr = iptvresult.substring(0, p).trim();
                    String rightstr = iptvresult.substring(p + 1).trim();
                    int l = leftstr.lastIndexOf(".");
                    int r = rightstr.lastIndexOf(".");
                    if( l > 0 && r > 0) {
                        String sourceIP = leftstr.substring(0, leftstr.lastIndexOf("."));
                        String sourceport = leftstr.substring(leftstr.lastIndexOf(".") + 1);
                        String destIP = rightstr.substring(0, rightstr.lastIndexOf("."));
                        String destport = rightstr.substring(rightstr.lastIndexOf(".") + 1);
                        IptvTestResult2Bean bean = new IptvTestResult2Bean(sourceIP,sourceport,destIP,destport);
                        mView.doShowIptvResult2(bean);
                    }
                } catch (Exception e) {
                    Log.e("showIptv_Result_2",e.getMessage());
                }
            }
        }

        @Override
        public void showIptv_Result_3(String iptvresult) {
            Log.d("showIptv_Result_3",iptvresult);
            String[] results = iptvresult.split(",");
            if(results.length >= 4) {
                try {
                    String ipnumbers = results[0];
                    String transferSpeed = results[1];
                    String maxspeed = results[2];
                    String minspeed = results[3];
                    IptvTestResult3Bean bean = new IptvTestResult3Bean(ipnumbers, transferSpeed, maxspeed, minspeed);
                    mView.doShowIptvResult3(bean);
                } catch (Exception e) {
                    Log.e("showIptv_Result_3",e.getMessage());
                }
            }

        }

        @Override
        public void showIptv_Result_4(String iptvresult) {
            Log.d("showIptv_Result_4",iptvresult);
            String[] results = iptvresult.split(",");
            if(results.length >= 3) {
                try {
                    String vlanpriority = results[0];
                    String lostUdp = results[1];
                    String dithering = results[2];
                    IptvTestResult4Bean bean = new IptvTestResult4Bean(vlanpriority,lostUdp,dithering );
                    mView.doShowIptvResult4(bean);
                } catch (Exception e) {
                    Log.e("showIptv_Result_4",e.getMessage());
                }
            }
        }

        @Override
        public void showIptv_Result_5(String iptvresult) {
            Log.d("showIptv_Result_5",iptvresult);
            String[] results = iptvresult.split(",");
            if(results.length >= 0) {
                try {
                    IptvTestResult5Bean bean = new IptvTestResult5Bean();
                    mView.doShowIptvResult5(bean);
                } catch (Exception e) {
                    Log.e("showIptv_Result_5",e.getMessage());
                }
            }
        }
        */
    };

    public IptvTestPresenter(IptvTestContract.View view, @NonNull MainPresenter mainPresenter) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mainPresenter = mainPresenter;
    }
    @Override
    public void start() {
        mainPresenter.setIptvTestListener(iptvTestListener);
    }

    @Override
    public void stop() {
        mainPresenter.removeIptvTestListener();
    }


    @Override
    public void startIptvTest() {
        //mainPresenter.doSendCommand(CommandValue.IPTV_START,"");
    }

    @Override
    public void stopIptvTest() {
        //mainPresenter.doSendCommand(CommandValue.IPTV_STOP,"");
    }
}
