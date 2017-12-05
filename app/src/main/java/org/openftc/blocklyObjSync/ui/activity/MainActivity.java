package org.openftc.blocklyObjSync.ui.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.openftc.blocklyObjSync.R;
import org.openftc.blocklyObjSync.backend.Utils;
import org.openftc.blocklyObjSync.backend.WiFiDirectBroadcastReceiver;
import org.openftc.blocklyObjSync.ui.UI_Utils;
import org.openftc.blocklyObjSync.ui.explorer.ExplorerActivity;

public class MainActivity extends AppCompatActivity implements WifiP2pManager.PeerListListener
{
    Button btnDiscoverPeersTest;
    Button btnAddToQueue;

    ProgressDialog progressDialog;

    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver broadcastReceiver;
    IntentFilter intentFilter;

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

        //Set up the UI buttons for the different sync options
        hookUpTheButtons();

        /*
         * Set up the Wi-Fi Direct stuff
         */
        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);

        /*
         * Set up the intent filter for Wi-Fi Direct
         */
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        broadcastReceiver = new WiFiDirectBroadcastReceiver(wifiP2pManager, channel, this);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void hookUpTheButtons()
    {
        btnDiscoverPeersTest = (Button) findViewById(R.id.discoverPeersTestButton);
        btnAddToQueue = (Button) findViewById(R.id.addFileToQueueButton);

        btnDiscoverPeersTest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Please wait");
                progressDialog.setMessage("Scanning for peers...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener()
                {
                    @Override
                    public void onSuccess()
                    {
                        Toast.makeText(MainActivity.this, "Succeeded!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reasonCode)
                    {
                        progressDialog.hide();
                        Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnAddToQueue.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /*Intent intent = new Intent(MainActivity.this, ToOrFromActivity.class);
                startActivity(intent);*/

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add file to queue");

                // add a list
                String[] periods = {"OBJ OpMode", "Blockly OpMode", "XML Config File"};
                builder.setItems(periods, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        Bundle bundle = new Bundle();
                        Intent intent = new Intent(MainActivity.this, ExplorerActivity.class);

                        switch (which)
                        {
                            case 0:
                                bundle.putInt(ExplorerActivity.KEY_FILE_CHOOSER_MODE, ExplorerActivity.SHOW_ONBOTJ_FILE_CHOOSER);
                                break;

                            case 1:
                                bundle.putInt(ExplorerActivity.KEY_FILE_CHOOSER_MODE, ExplorerActivity.SHOW_BLOCKLY_FILE_CHOOSER);
                                break;

                            case 2:
                                bundle.putInt(ExplorerActivity.KEY_FILE_CHOOSER_MODE, ExplorerActivity.SHOW_XML_CONFIG_FILE_CHOOSER);
                                break;
                        }

                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });

                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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

        /*
         * A menu item was pressed; figure out which one
         * and take the appropriate action
         */
        if (id == R.id.aboutMenuItem)
        {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.preferencesMenuItem)
        {
            Intent intent = new Intent(this, PreferencesActivity.class);
            startActivity(intent);

            return true;
        }
        else if (id == R.id.explorerTestMenuItem)
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

    @Override
    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList)
    {
        progressDialog.hide();
        System.out.println(wifiP2pDeviceList.toString());
    }
}
