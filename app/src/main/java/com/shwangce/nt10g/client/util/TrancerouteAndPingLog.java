package com.shwangce.nt10g.client.util;

import java.io.FileOutputStream;


/**
 * Created by Administrator on 2016/8/24 0024.
 */

public abstract class TrancerouteAndPingLog {

    //private Context context;
    //private static final String FILENAME = Utils.SDCardDir +  "/nt10glog.txt";
    //Map TimerSchedule = SharedPreferencesUtil.getTimerSchedule( context);
    //String ddd = TimerSchedule.get("storage").toString();

    //String starttimeStamp = TimerSchedule.get("StartTimeStamp").toString();//开始时间，用来生成文件名
    //private static String SystemTime = getSystemTimeString();
    //private static final String TRANCEROUTEFILENAME = Utils.SDCardDir + "/" + SystemTime + "路由跟踪" + ".txt";
    //private static final String PINGFILENAME = Utils.SDCardDir + "/" +"Ping"+ ".txt";

    public static void insertOperate(String operatething,String systemtime,String url,String storage) {
        String writedata =  operatething + "\r\n";
        if(storage.equals("0")){
            String FILENAME = ProjectUtil.SDCardDir + "/" + systemtime +" 路由跟踪 " + url + ".txt";
            writeData(FILENAME,writedata);
        }else {
            String FILENAME = "/storage/sdcard1/" + systemtime +" 路由跟踪 " + url + ".txt";
            try{
                writeData(FILENAME,writedata);
            }catch (Exception e){
                e.printStackTrace();

            }
        }
        //Utils.ExternalFilesDir
        //String operatetime = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis()));




    }

    public static void insertOperatePing(String operatething,String desthost,String systemtime,String storage) {
        //String operatetime = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").format(new Date(System.currentTimeMillis()));
        String writedata =  operatething + "\r\n";
        if(storage.equals("0")){
            String FILENAME = ProjectUtil.SDCardDir + "/" +systemtime+" Ping"+desthost+ ".txt";
            writeData(FILENAME,writedata);
        }else {

            String FILENAME = "/storage/sdcard1/" +systemtime+" Ping"+desthost+ ".txt";
            try{
                writeData(FILENAME,writedata);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //writeData(Utils.SDCardDir + "/" +systemtime+" Ping"+desthost+ ".txt",writedata);
    }

    private static void writeData(String fileName, String message) {
        try {
            FileOutputStream fout = new FileOutputStream(fileName,true);
            byte [] bytes = message.getBytes();
            fout.write(bytes);
            fout.flush();
            fout.close();
            fout = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
