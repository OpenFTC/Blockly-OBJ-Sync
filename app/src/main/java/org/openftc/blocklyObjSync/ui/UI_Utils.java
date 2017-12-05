package org.openftc.blocklyObjSync.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import org.openftc.blocklyObjSync.R;

public class UI_Utils
{
    public static void showRcAppNotInstalledDaialog(final Activity activity)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);

        builder.setTitle("Ruh-roh!")
                .setMessage(Html.fromHtml(activity.getString(R.string.msg_rc_not_installed)))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finish();

                    }
                })
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .show();
    }

    public static void colorStatusBar(Activity activity)
    {
        /*
         * Set the status bar color to colorPrimaryDark
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
        }
    }
}
