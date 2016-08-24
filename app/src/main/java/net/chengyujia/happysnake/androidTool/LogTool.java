package net.chengyujia.happysnake.androidTool;

import android.util.Log;

import net.chengyujia.happysnake.Config;
import net.chengyujia.happysnake.MyApp;
import net.chengyujia.happysnake.R;

/**
 * Created by ChengYuJia on 2016/8/10.
 */
public class LogTool {

    private static final boolean DEBUG = Config.DEBUG;
    private static final String DEFAULT_TAG = MyApp.instance.getString(R.string.app_name);

    public static void log(String msg) {
        log(msg, null);
    }

    public static void log(String msg, Object caller) {
        if (DEBUG) {
            String tag = DEFAULT_TAG;
            if (caller != null) {
                tag = caller.getClass().getName();
            }
            Log.d(tag, msg);
        }
    }

    public static void exception(Exception e) {
        if (DEBUG) {
            log("异常：");
            e.printStackTrace();
        }
    }
}
