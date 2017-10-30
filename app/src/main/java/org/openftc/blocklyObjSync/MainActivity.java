package org.openftc.blocklyObjSync;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
         * Set the status bar color to colorPrimaryDark
         * No need to worry about checking API version
         * since minSdk is 19
         */
        UI_Utils.colorStatusBar(this);
    }
}
