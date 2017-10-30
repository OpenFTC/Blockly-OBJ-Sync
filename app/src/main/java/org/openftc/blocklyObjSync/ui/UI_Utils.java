package org.openftc.blocklyObjSync.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import org.openftc.blocklyObjSync.R;

class UI_Utils
{
    static void showRcAppNotInstalledDaialog(final Activity activity)
    {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);

        String test = "<font color='#FF0000'>This device does not have the FTC Robot Controller app installed.</font><br><br>This app is useless without it. If you have no idea what the aforementioned app is, then this app will probably be useless to you.";

        builder.setTitle("Ruh-roh!")
                .setMessage(Html.fromHtml(test))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        activity.finish();

                    }
                })
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .show();
    }

    static void colorStatusBar(Activity activity)
    {
        /*
         * Set the status bar color to colorPrimaryDark
         * No need to worry about checking API version
         * since minSdk is 19
         */

        Window w = activity.getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        int statusBarHeight = getStatusBarHeight(activity);

        View view = new View(activity);
        view.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.getLayoutParams().height = statusBarHeight;
        ((ViewGroup) w.getDecorView()).addView(view);
        view.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimaryDark));
    }

    private static int getStatusBarHeight(Activity activity)
    {
        int result = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
