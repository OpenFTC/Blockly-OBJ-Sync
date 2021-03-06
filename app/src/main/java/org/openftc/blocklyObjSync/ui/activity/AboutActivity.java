package org.openftc.blocklyObjSync.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.openftc.blocklyObjSync.R;
import org.openftc.blocklyObjSync.ui.UI_Utils;

public class AboutActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        UI_Utils.colorStatusBar(this);
        getSupportActionBar().setTitle("About");
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

    public void launchOslActivity(View v)
    {
        Intent intent = new Intent(this, OpenSourceLicensesActivity.class);
        startActivity(intent);
    }
}
