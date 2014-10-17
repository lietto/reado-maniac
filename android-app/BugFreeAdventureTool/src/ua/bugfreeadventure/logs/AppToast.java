package ua.bugfreeadventure.logs;

import android.content.Context;

/**
 * Created by lietto on 11.09.2014.
 */
public abstract class AppToast {

    protected Context context;

    public AppToast(Context ctx) {
        context = ctx;
    }


    public abstract void showRed(String text);

    public abstract void showYellow(String text);

    public abstract void showGreen(String text);
}
