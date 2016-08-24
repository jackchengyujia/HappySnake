package net.chengyujia.happysnake.androidTool;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import net.chengyujia.happysnake.MyApp;

/**
 * Created by ChengYuJia on 2016/8/10.
 */
public class SharedPreferenceTool {
    private static final String NAME = "snake";

    private static SharedPreferences getSharedPreferences() {
        return MyApp.instance.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    private static Editor getEditor() {
        return getSharedPreferences().edit();
    }

    public static String getString(String key) {
        try {
            return getSharedPreferences().getString(key, null);
        } catch (Exception e) {
            LogTool.exception(e);
            return null;
        }
    }

    public static void putString(String key, String value) {
        Editor e = getEditor();
        e.putString(key, value);
        e.commit();
    }

    public static int getInt(String key) {
        try {
            return getSharedPreferences().getInt(key, 0);
        } catch (Exception e) {
            LogTool.exception(e);
            return 0;
        }
    }

    public static void putInt(String key, int value) {
        Editor e = getEditor();
        e.putInt(key, value);
        e.commit();
    }
}
