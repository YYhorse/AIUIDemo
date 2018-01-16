package com.aiuidemo.www;

import android.app.Application;

/**
 * Created by yy on 2018/1/8.
 */
public class BaseApplication extends Application {
    public static BaseApplication instance;
    @Override
    public void onCreate() {
        instance = this;
        AIUIUtils.SpeechInit();
    }
    public static BaseApplication getInstance() {
        return instance;
    }
}
