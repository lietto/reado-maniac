package ua.bugfreeadventure.utils;

import android.os.Build;

/**
 * Created by lietto on 12.09.2014.
 */
public class ApplicationUtil {

    public static int getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean isJellyBean() {
        return Build.VERSION.SDK_INT > 15;
    }

}
