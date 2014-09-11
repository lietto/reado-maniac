package ua.devhelp.fragment;

import android.app.Fragment;
import android.util.Log;
import ua.devhelp.constants.DevLogSwitch;
import ua.devhelp.logs.DevToast;
import ua.devhelp.notifications.LookAtMe;

/**
 * Created by lietto on 11.09.2014.
 */
public class ParentFragment extends Fragment{

    protected String TAG = this.getClass().getSimpleName();

    protected void showErrorDevToast(String text) {
        DevToast.context(getActivity()).showRed(text);
    }

    protected void showWarningDevToast(String text) {
        DevToast.context(getActivity()).showYellow(text);
    }

    protected void showSuccessDevToast(String text) {
        DevToast.context(getActivity()).showGreen(text);
    }

    protected void showErrorToastToUser(String text) {
        LookAtMe.context(getActivity()).showRed(text);
    }

    protected void showWarningrToastToUser(String text) {
        LookAtMe.context(getActivity()).showYellow(text);
    }

    protected void showSuccessrToastToUser(String text) {
        LookAtMe.context(getActivity()).showGreen(text);
    }


    protected void printArrayLog(String... text) {
        if (text!=null && DevLogSwitch.DEVELOPING) {
            for (String str : text) {
                Log.e(TAG, " --> " + str);
            }
        }

    }

    protected void printLineLog(String... text) {
        if (text!=null && DevLogSwitch.DEVELOPING) {
            String tmp = "";
            for (String str : text) {
                tmp += " " + str + " |";
            }
            Log.e(TAG, "<|--|" + tmp + "--|>");
        }

    }

    protected void printSingleTextLog(String text) {
        if (text!=null && DevLogSwitch.DEVELOPING) {
            Log.e(TAG, "|__>  - " + text);
        }

    }

}
