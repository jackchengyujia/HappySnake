package net.chengyujia.happysnake;

import android.app.Application;


/**
 * Created by ChengYuJia on 2016/8/10.
 */
public class MyApp extends Application {
    public static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        instance = this;
    }
}