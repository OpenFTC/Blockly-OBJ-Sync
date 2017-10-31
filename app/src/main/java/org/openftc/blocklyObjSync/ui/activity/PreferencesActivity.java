package org.openftc.blocklyObjSync.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import org.openftc.blocklyObjSync.R;
import org.openftc.blocklyObjSync.ui.UI_Utils;

public class PreferencesActivity extends AppCompatPreferenceActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        UI_Utils.colorStatusBar(this);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
        getSupportActionBar().setTitle("Preferences");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }

    /*
     * The method that's called when the user presses the title back button
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}