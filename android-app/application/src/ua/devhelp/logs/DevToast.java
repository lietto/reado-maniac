package ua.devhelp.logs;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import ua.devhelp.constants.DevLogSwitch;

/**
 * Created by lietto on 11.09.2014.
 */
public class DevToast extends AppToast{

    public DevToast(Context ctx) {
        super(ctx);
    }

    private static DevToast devToast;

    public static DevToast context(Context ctx) {
        if (devToast == null) {
            devToast = new DevToast(ctx);
        }
        return devToast;
    }

   public void showRed(String text) {
        if (DevLogSwitch.DEVELOPING && context != null) {
            Toast toast = getToast(text);

            toast.getView().setBackgroundColor(Color.RED);

            toast.show();
        }
    }

    public void showYellow(String text) {
        if (DevLogSwitch.DEVELOPING && context != null) {
            Toast toast = getToast(text);

            toast.getView().setBackgroundColor(Color.YELLOW);



            toast.show();
        }
    }

    public void showGreen(String text) {
        if (DevLogSwitch.DEVELOPING && context != null) {
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
