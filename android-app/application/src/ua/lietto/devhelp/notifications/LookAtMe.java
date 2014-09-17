package ua.lietto.devhelp.notifications;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import ua.lietto.devhelp.constants.DevLogSwitcher;
import ua.lietto.devhelp.logs.AppToast;

/**
 * Created by lietto on 11.09.2014.
 */
public class LookAtMe extends AppToast {

    public LookAtMe(Context ctx) {
        super(ctx);
    }

    private static LookAtMe userToast;

    public static LookAtMe context(Context ctx) {
        if (userToast == null) {
            userToast = new LookAtMe(ctx);
        }
        return userToast;
    }

    public void showRed(String text) {
        if (DevLogSwitcher.DEVELOPING && context != null) {
            Toast toast = getToast(text);

            toast.getView().setBackgroundColor(Color.RED);

            toast.show();
        }
    }

    public void showYellow(String text) {
        if (DevLogSwitcher.DEVELOPING && context != null) {
            Toast toast = getToast(text);

            toast.getView().setBackgroundColor(Color.YELLOW);

            TextView.class.cast(toast.getView()).setTextColor(Color.BLACK);

            toast.show();
        }
    }

    public void showGreen(String text) {
        if (DevLogSwitcher.DEVELOPING && context != null) {
            Toast toast = getToast(text);

            toast.getView().setBackgroundColor(Color.GREEN);

            toast.show();
        }
    }

    private Toast getToast(String text) {
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);

        TextView view = new TextView(context);

        view.setPadding(15, 15, 15, 15);

        view.setText(text);

        ViewGroup.LayoutParams params =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        view.setLayoutParams(params);

        view.setTextColor(Color.BLACK);

        toast.setView(view);

        return toast;
    }

}
