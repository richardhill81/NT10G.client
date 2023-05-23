package com.shwangce.nt10g.client.application;

import android.app.Application;

/**
 * Created by Administrator on 2017/2/8 0008.
 */

public class NT10GApplication extends Application {
    private static NT10GApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static NT10GApplication getInstance(){
        return instance;
    }


}
