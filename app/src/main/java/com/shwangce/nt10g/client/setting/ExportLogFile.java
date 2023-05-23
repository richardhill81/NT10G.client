package com.shwangce.nt10g.client.setting;

import android.os.Environment;

import com.shwangce.nt10g.client.util.Log;
import com.shwangce.nt10g.client.util.ProjectUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * create by hzj on 20191022
 *
 * 接收完盒子上传的日志文件数据后，写入手机指定目录
 *
 */

public class ExportLogFile {

    private static final String TAG = "ExportLogFile";

    private String fileName = "";
    private static final String CLIENT_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Shwangce";
    private File logFile;
    private ExportLogFileListener listener;

    public ExportLogFile(ExportLogFileListener listener){
        this.listener = listener;

        //如果不存在此目录，则创建
        File file = new File(CLIENT_FILE_PATH);
        if(!file.exists()){
            file.mkdir();
        }
        //获取设备名
        String deviceName = ProjectUtil.ConnectedDeviceName.substring(0,15);
        Log.i(TAG,"deviceName:" + deviceName);
        fileName = CLIENT_FILE_PATH + "/" + deviceName +"_log.txt";
        logFile = new File(fileName);

    }

    public void clearFile(){
        if(logFile.exists()){
            logFile.delete();
            Log.i(TAG,"删除当前文件！");
        }
    }

    public void writeDataToFile(String data){
        try{
            RandomAccessFile raf = new RandomAccessFile(logFile, "rwd");
            raf.seek(logFile.length());
            raf.write(data.getBytes());
            raf.close();
        }catch (FileNotFoundException e){
            listener.onFileDataWriteError("日志文件创建失败：" + e.getMessage());
            e.printStackTrace();

        }catch (IOException e){
            listener.onFileDataWriteError("日志文件写入失败：" + e.getMessage());
            e.printStackTrace();
        }

        listener.onFileDataWriteComplete("日志文件数据" + data + "写入成功");
    }

    //    @Override
//    protected Void doInBackground(Void... params) {
//
//        File file = new File(CLIENT_FILE_PATH);
//        if(!file.exists()){
//            file.mkdir();
//        }
//
//        String deviceName = ProjectUtil.ConnectedDeviceName.substring(0,15);
//
//        Log.i(TAG,"deviceName:" + deviceName);
//
//        fileName = CLIENT_FILE_PATH + "/" + deviceName +"_log.txt";
//        logFile = new File(fileName);
//        Log.i(TAG,"文件目录：" + fileName);
//        Log.i(TAG,"文件大小：" + logFile.length());
//        if(logFile.exists()){
//            logFile.delete();
//            Log.i(TAG,"删除当前文件！");
//            logFile = new File(fileName);
//            Log.i(TAG,"文件大小：" + logFile.length());
//        }
//
//        try{
//            RandomAccessFile raf = new RandomAccessFile(logFile, "rwd");
//            raf.seek(logFile.length());
//            raf.write(logData.getBytes());
//            raf.close();
//        }catch (FileNotFoundException e){
//            listener.onFileDataWriteError("日志文件创建失败：" + e.getMessage());
//            e.printStackTrace();
//
//        }catch (IOException e){
//            listener.onFileDataWriteError("日志文件写入失败：" + e.getMessage());
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void o) {
//        Log.i(TAG,"文件生成完成！");
//        listener.onFileDataWriteComplete("日志文件" + logFile.getName() + "导出完成");
//        super.onPostExecute(o);
//    }
}
