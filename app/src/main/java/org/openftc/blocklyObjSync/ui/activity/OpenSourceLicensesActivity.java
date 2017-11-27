package org.openftc.blocklyObjSync.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import org.openftc.blocklyObjSync.R;
import org.openftc.blocklyObjSync.ui.UI_Utils;

public class OpenSourceLicensesActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source_licenses);
        UI_Utils.colorStatusBar(this);
        getSupportActionBar().setTitle("Open source licenses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /*
     * The method that's called when the user presses the title back button
     */
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }
}
