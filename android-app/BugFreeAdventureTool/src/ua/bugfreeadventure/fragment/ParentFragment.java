package ua.bugfreeadventure.fragment;

import android.app.Fragment;
import ua.bugfreeadventure.activity.ParentActivity;

/**
 * Created by lietto on 11.09.2014.
 */
public class ParentFragment extends Fragment{

    protected String TAG = this.getClass().getSimpleName();


    protected ParentActivity getOwner() {
        return (ParentActivity) getActivity();
    }


    protected void showErrorDevToast(String text) {
        getOwner().showErrorDevToast(text);
    }

    protected void showWarningDevToast(String text) {
        getOwner().showWarningDevToast(text);
    }

    protected void showSuccessDevToast(String text) {
        getOwner().showSuccessDevToast(text);
    }

    protected void showErrorToastToUser(String text) {
        getOwner().showErrorToastToUser(text);
    }

    protected void showWarningrToastToUser(String text) {
        getOwner().showWarningToastToUser(text);
    }

    protected void showSuccessrToastToUser(String text) {
        getOwner().showSuccessToastToUser(text);
    }




}
