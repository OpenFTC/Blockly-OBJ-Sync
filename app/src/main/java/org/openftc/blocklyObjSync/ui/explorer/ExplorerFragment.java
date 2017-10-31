package org.openftc.blocklyObjSync.ui.explorer;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.openftc.blocklyObjSync.R;
import java.io.File;
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
 * Display the contents of a directory
 */
public class ExplorerFragment extends ListFragment implements ActionMode.Callback, DirectoryAdapter.Listener
{

    public static final String DIRECTORY = "directory";

    public interface Listener
    {
        void onBrowseDirectory(String directory);

        void onSendUris(ArrayList<Uri> uris);
    }

    private Listener mListener;
    private DirectoryAdapter mDirectoryAdapter;
    private ActionMode mActionMode;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        String directory = null;
        if (getArguments() != null)
        {
            directory = getArguments().getString(DIRECTORY);
        }
        if (directory == null)
        {
            directory = Environment.getExternalStorageDirectory().getPath();
        }

        mDirectoryAdapter = new DirectoryAdapter(directory, getActivity(), this);
        setListAdapter(mDirectoryAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mListener = (Listener) getActivity();

        // Watch for long presses on directories
        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                mDirectoryAdapter.activateCheckboxes(position);
                mActionMode = getActivity().startActionMode(ExplorerFragment.this);
                return true;
            }
        });
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id)
    {
        if (mActionMode == null)
        {
            File file = mDirectoryAdapter.getItem(position);
            //noinspection ConstantConditions
            if (file.isDirectory())
            {
                mListener.onBrowseDirectory(file.getPath());
            }
            else
            {
                ArrayList<Uri> uris = new ArrayList<>();
                uris.add(Uri.fromFile(file));
                mListener.onSendUris(uris);
            }
        }
        else
        {
            mDirectoryAdapter.toggleItem(position);
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu)
    {
        MenuInflater inflator = mode.getMenuInflater();
        inflator.inflate(R.menu.menu_explorer_actions, menu);
        mode.setTitle("Multi-select");
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu)
    {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_send:
                mListener.onSendUris(mDirectoryAdapter.getUris());
                mActionMode.finish();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onDestroyActionMode(ActionMode mode)
    {
        mDirectoryAdapter.deactivateCheckboxes();
        mActionMode = null;
    }

    @Override
    public void onAllItemsDeselected()
    {
        mActionMode.finish();
    }

    @Override
    public void onError(String message)
    {
        setEmptyText(message);
    }
}