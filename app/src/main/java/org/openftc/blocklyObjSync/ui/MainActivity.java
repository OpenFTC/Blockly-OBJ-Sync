package org.openftc.blocklyObjSync.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.openftc.blocklyObjSync.R;
import org.openftc.blocklyObjSync.backend.Utils;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UI_Utils.colorStatusBar(this);

        /*
         * Check to see if the RC app is installed. If it
         * isn't, then show a dialog to the user explaining
         * that, and exit the app when they click ok
         */
        if (!Utils.isFtcRobotControllerInstalled(getPackageManager()))
        {
            UI_Utils.showRcAppNotInstalledDaialog(this);

            /*
             * Call return so that the rest of onCreate() will be
             * skipped. No need to keep going since the app is
             * about to shut down
             */
            return;
        }
    }
}
