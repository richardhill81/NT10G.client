package com.shwangce.nt10g.client.library.AppUpdate;

import android.os.AsyncTask;
import android.os.Environment;

import com.shwangce.nt10g.client.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateWork {

    private final String TAG = "UpdateWork";

    private String updateInfoUrl = "";

    private static final String CLIENT_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Shwangce";
    private static final String CLIENT_FILE_NAME = CLIENT_FILE_PATH + "/AutoUpdate.apk";
    private static final String BOX_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Shwangce/nt201";
    private static final String OTA_FILE_NAME = BOX_FILE_PATH + "/update.zip";
    private static final String BOXAPP_FILE_NAME = BOX_FILE_PATH + "/boxupdate.apk";
    private String filename = "";
    private static final int INSTALL_TOKEN = 1;

    private String appname = "";
    private String appversion = "";
    private int appcode = -1;
    private String downloadurl = "";

    private UpdateBean updateBean = null;
    private int curProgress = 0;
    private TimerTask timerTask =  new TimerTask() {
        @Override
        public void run() {
            if (updateListener != null)
                updateListener.onDownloading(curProgress);
        }
    };

    private int updatetype = 0;

    public static final int CLIENT_UPDATE = 1;
    public static final int BOXAPP_UPDATE = 2;
    public static final int BOXOTA_UPDATE = 3;

    public static interface OnUpdateListener {
        void onGetUpdateInfo(UpdateBean updateBean);
        void onDownloading(int progressvalue);
        void onDownloadComplete(String fileName,int updatetype);
    }

    private OnUpdateListener updateListener = null;

    public UpdateWork(String updateInfoUrl) {
        this.updateInfoUrl = updateInfoUrl;
    }

    public void setUpdateListener(OnUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public void startUpdate(int update) {
        updatetype = update;
        switch (update) {
            case CLIENT_UPDATE:
                new downloadAsyncTask().execute(updateBean.getClient_url());
                break;
            case BOXAPP_UPDATE:
                new downloadAsyncTask().execute(updateBean.getServer_url());
                break;
            case BOXOTA_UPDATE:
                new downloadAsyncTask().execute(updateBean.getOta_url());
                break;
        }
    }

    public void getUpdateInfo() {
        getUpdateInfo(updateInfoUrl);
    }

    private void getUpdateInfo(String updateInfoUrl) {
        class mAsyncTask extends AsyncTask<String,Integer,String> {
            @Override
            protected String doInBackground(String... strings) {
                String url = strings[0];
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Log.i("getResponse", url);
                    Response response = client.newCall(request).execute();
                    return response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                    return "";
                }
            }

            @Override
            protected void onPostExecute(String s) {
                Log.d("Response", s);
                if(s.length() == 0) {   //http连接失败
                    if (updateListener != null)
                        updateListener.onGetUpdateInfo(null);
                } else {
                    Gson gson = new Gson();
                    try {
                        updateBean = gson.fromJson(s, UpdateBean.class);
                        if (updateListener != null)
                            updateListener.onGetUpdateInfo(updateBean);
                    } catch (Exception e) {
                        Log.w("UpdateWork", e.getMessage());
                        if (updateListener != null)
                            updateListener.onGetUpdateInfo(null);
                    }
                }
            }
        }
        new mAsyncTask().execute(updateInfoUrl);
    }

    private class downloadAsyncTask extends AsyncTask<String, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "执行至--onPreExecute");
        }

        @Override
        protected Integer doInBackground(String... params) {
            String urlString = params[0];
            File file_path;
            switch (updatetype){
                case CLIENT_UPDATE:
                    file_path = new File(CLIENT_FILE_PATH);
                    filename = CLIENT_FILE_NAME;
                    break;
                case BOXAPP_UPDATE:
                    file_path = new File(BOX_FILE_PATH);
                    filename = BOXAPP_FILE_NAME;
                    break;
                case BOXOTA_UPDATE:
                    file_path = new File(BOX_FILE_PATH);
                    filename = OTA_FILE_NAME;
                    break;

                default:
                    return -1;
            }
            URL url = null;
            HttpURLConnection connection = null;
            InputStream in = null;
            FileOutputStream out = null;
            curProgress = 0;
            try {
                url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();
                in = connection.getInputStream();
                long fileLength = connection.getContentLength();
                if (!file_path.exists()) {
                    file_path.mkdir();
                }
                File downloadfile  = new File(filename);
                if(downloadfile.exists()) {
                    downloadfile.delete();
                }
                out = new FileOutputStream(downloadfile);//为指定的文件路径创建文件输出流
                byte[] buffer = new byte[1024 * 1024];
                int len = 0;
                long readLength = 0;
                new Timer().schedule(timerTask,0,1000);
                Log.d(TAG, "执行至--readLength = 0");
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);//从buffer的第0位开始读取len长度的字节到输出流
                    readLength += len;
                    curProgress = (int) (((float) readLength / fileLength) * 100);
                    Log.d(TAG, "当前下载进度：" + curProgress);
                    if (readLength >= fileLength) {
                        Log.d(TAG, "执行至--readLength >= fileLength");
                        break;
                    }
                }
                out.flush();
                return INSTALL_TOKEN;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                timerTask.cancel();
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Log.d(TAG,"下载完毕");
            if(updateListener != null)
                updateListener.onDownloadComplete(filename,updatetype);
            updatetype = 0;
        }
    }


}
