package ua.lietto.devhelp.logs;

import android.util.Log;
import ua.lietto.devhelp.constants.DevLogSwitcher;

/**
 * Created by lietto on 11.09.2014.
 * Show Me This Later class for simple, fast and custom logcat logs
 */
public class SMTL {

    private static SMTL sLog = new SMTL();

    private String TAG;

    public static SMTL log(String tag) {

        if (sLog == null) {
            sLog = new SMTL();
        }
        sLog.TAG = tag;

        return sLog;
    }

    public static SMTL log(Object object) {

        if (sLog == null) {
            sLog = new SMTL();
        }

        sLog.TAG = object.getClass().getSimpleName();

        return sLog;
    }

    public static SMTL log() {

        if (sLog == null) {
            sLog = new SMTL();
        }

        sLog.TAG = "ReadoManiac";

        return sLog;
    }

    public void printValue(String name, Object value) {
        if (DevLogSwitcher.DEVELOPING) {
                Log.e(TAG, " Print Value\n -- " + name + " : " + value);

        }

    }

    public void printArrayLog(String... text) {
        if (text!=null && DevLogSwitcher.DEVELOPING) {
            for (String str : text) {
                Log.e(TAG, " --> " + str);
            }
        }

    }

    public void printLineLog(String... text) {
        if (text!=null && DevLogSwitcher.DEVELOPING) {
            String tmp = "";
            for (String str : text) {
                tmp += " " + str + " |";
            }
            Log.e(TAG, "<|--|" + tmp + "--|>");
        }

    }

    public void printSingleTextLog(String text) {
        if (text!=null && DevLogSwitcher.DEVELOPING) {
            Log.e(TAG, "|__>  - " + text);
        }

    }

}
