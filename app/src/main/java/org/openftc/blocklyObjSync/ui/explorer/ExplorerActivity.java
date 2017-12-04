package org.openftc.blocklyObjSync.ui.explorer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import org.openftc.blocklyObjSync.R;
import org.openftc.blocklyObjSync.ui.UI_Utils;
import org.openftc.blocklyObjSync.ui.explorer.ExplorerFragment;

import java.util.ArrayList;

/*
 * Original work Copyright (c) 2017 Nathan Osman
 * Modified work Copyright (c) 2017 OpenFTC Developers
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * Explorer for browsing directories
 */
public class ExplorerActivity extends AppCompatActivity implements ExplorerFragment.Listener
{

    private static final int SHARE_REQUEST = 1;
    public static final String KEY_FILE_CHOOSER_MODE = "fileChooserMode";
    public static final int SHOW_ONBOTJ_FILE_CHOOSER = 1;
    public static final int SHOW_BLOCKLY_FILE_CHOOSER = 2;
    public static final int SHOW_XML_CONFIG_FILE_CHOOSER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);

        UI_Utils.colorStatusBar(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Choose OpModes to share");

        showDirectory(modeToDir(getIntent().getExtras().getInt(KEY_FILE_CHOOSER_MODE)));

        if (savedInstanceState == null)
        {
            Toast.makeText(this, "Hello",
                           Toast.LENGTH_SHORT).show();
        }
    }

    private void showDirectory(String directory)
    {
        ExplorerFragment explorerFragment = new ExplorerFragment();
        if (directory != null)
        {
            int mode = 0;
            if(directory.equals(modeToDir(SHOW_ONBOTJ_FILE_CHOOSER)))
            {
                mode = SHOW_ONBOTJ_FILE_CHOOSER;
            }
            else if(directory.equals(modeToDir(SHOW_BLOCKLY_FILE_CHOOSER)))
            {
                mode = SHOW_BLOCKLY_FILE_CHOOSER;
            }
            else if(directory.equals(modeToDir(SHOW_XML_CONFIG_FILE_CHOOSER)))
            {
                mode = SHOW_XML_CONFIG_FILE_CHOOSER;
            }
            Bundle arguments = new Bundle();
            arguments.putString(ExplorerFragment.DIRECTORY, directory);
            arguments.putInt(KEY_FILE_CHOOSER_MODE, mode);
            explorerFragment.setArguments(arguments);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (directory != null)
        {
            transaction.setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
            );
        }
        transaction.replace(R.id.directory_container, explorerFragment);
        if (directory != null)
        {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onBrowseDirectory(String directory)
    {
        showDirectory(directory);
    }

    @Override
    public void onSendUris(ArrayList<Uri> uris)
    {
        /*Intent shareIntent = new Intent(this, ShareActivity.class);
        shareIntent.setAction("android.intent.action.SEND_MULTIPLE");
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        startActivityForResult(shareIntent, SHARE_REQUEST);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SHARE_REQUEST)
        {
            if (resultCode == RESULT_OK)
            {
                finish();
            }
        }
    }

    private String modeToDir(int mode)
    {
        if (mode == 1)
        {
            return "/sdcard/FIRST/java/src/org/firstinspires/ftc/teamcode/";
        }
        else if (mode == 2)
        {
            return "/sdcard/FIRST/blocks/";
        }
        else if (mode == 3)
        {
            return "/sdcard/FIRST/";
        }
        return null;
    }

    @Override
    public void onBackPressed()
    {
        finish();
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