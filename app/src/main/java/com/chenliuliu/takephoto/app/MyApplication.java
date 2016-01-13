package com.chenliuliu.takephoto.app;

import android.app.Application;

public class MyApplication extends Application {
    private static volatile MyApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public MyApplication() {
        instance = this;
    }

    public static synchronized MyApplication getInstance() {
        if (instance == null) {
            synchronized (MyApplication.class) {
                if (instance == null) {
                    instance = new MyApplication();
                }
            }
        }
        return instance;
    }
}
