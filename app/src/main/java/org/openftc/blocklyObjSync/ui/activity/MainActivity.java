package org.openftc.blocklyObjSync.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.openftc.blocklyObjSync.R;
import org.openftc.blocklyObjSync.backend.Utils;
import org.openftc.blocklyObjSync.ui.UI_Utils;
import org.openftc.blocklyObjSync.ui.explorer.ExplorerActivity;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if (id == R.id.aboutMenuItem)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        else if(id == R.id.preferencesMenuItem)
        {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);

            return true;
        }
        else if(id == R.id.explorerTestMenuItem)
        {
            Bundle bundle = new Bundle();
            bundle.putInt(ExplorerActivity.KEY_FILE_CHOOSER_MODE, ExplorerActivity.SHOW_ONBOTJ_FILE_CHOOSER);
            Intent intent = new Intent(this, ExplorerActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
