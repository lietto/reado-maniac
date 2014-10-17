package ua.bugfreeadventure.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import ua.bugfreeadventure.logs.DevToast;
import ua.bugfreeadventure.notifications.LookAtMe;

/**
 * Created by lietto on 11.09.2014.
 */
public class ParentActivity extends Activity{

    protected String TAG = this.getClass().getSimpleName();
    protected ProgressDialog progressDialog;

    public void showErrorDevToast(String text) {
        DevToast.context(this).showRed(text);
    }

    public void showWarningDevToast(String text) {
        DevToast.context(this).showYellow(text);
    }

    public void showSuccessDevToast(String text) {
        DevToast.context(this).showGreen(text);
    }

    public void showErrorToastToUser(String text) {
        LookAtMe.context(this).showRed(text);
    }

    public void showWarningToastToUser(String text) {
        LookAtMe.context(this).showYellow(text);
    }

    public void showSuccessToastToUser(String text) {
        LookAtMe.context(this).showGreen(text);
    }


    protected void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
        }
        progressDialog.setMessage("Загрузка...");
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
        }

    }
}
