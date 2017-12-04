package org.openftc.blocklyObjSync.ui.explorer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.format.DateUtils;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import org.openftc.blocklyObjSync.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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
 * Adapter for displaying directory contents
 */
class DirectoryAdapter extends ArrayAdapter<File>
{

    interface Listener
    {
        void onAllItemsDeselected();

        void onError(String message);
    }

    private Context mContext;
    private Listener mListener;
    private String mDirectory;
    private boolean mShowHidden = false;
    private boolean mCheckboxes = false;
    private SparseArray<File> mChecked = new SparseArray<>();

    private int mColor;

    /**
     * Initialize the list of files
     */
    private void init(int mode)
    {
        File[] files = new File(mDirectory).listFiles();

        if(mode == ExplorerActivity.SHOW_BLOCKLY_FILE_CHOOSER)
        {
            getBlocklyFiles(files);
        }
        else if(mode == ExplorerActivity.SHOW_ONBOTJ_FILE_CHOOSER)
        {
            getObjFiles(files);
        }
        else if(mode == ExplorerActivity.SHOW_XML_CONFIG_FILE_CHOOSER)
        {
            getXmlConfigFiles(files);
        }
    }

    private void getBlocklyFiles(File[] files)
    {
        ArrayList<BlocklyFileObject> blocklyFileObjects = new ArrayList<>();

        Arrays.sort(files, new Comparator<File>()
        {
            @Override
            public int compare(File o1, File o2)
            {
                if (o1.isDirectory() == o2.isDirectory())
                {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
                else
                {
                    return o1.isDirectory() ? -1 : 1;
                }
            }
        });

        ArrayList<File> blkFiles = new ArrayList<>();
        ArrayList<File> jsFiles = new ArrayList<>();

        for (File file : files)
        {
            String name = file.getName();
            if(name.endsWith(".blk"))
            {
                blkFiles.add(file);
            }
            else if(name.endsWith(".js"))
            {
                jsFiles.add(file);
            }
        }

        for (File blkFile : blkFiles)
        {
            String blkFileNameWithoutExtension = truncateExtension(blkFile.getName(), ".blk");
            for (File jsFile : jsFiles)
            {
                String jsFileNameWithoutExtension = truncateExtension(jsFile.getName(), ".js");

                if(blkFileNameWithoutExtension.equals(jsFileNameWithoutExtension))
                {
                    blocklyFileObjects.add(new BlocklyFileObject(blkFile, jsFile));
                }
            }
        }

        for (BlocklyFileObject file : blocklyFileObjects)
        {
            super.add(new File("/test/", file.getName()));
        }
    }

    private void getObjFiles(File[] files)
    {
        ArrayList<File> javaSourceFiles = new ArrayList<>();

        Arrays.sort(files, new Comparator<File>()
        {
            @Override
            public int compare(File o1, File o2)
            {
                if (o1.isDirectory() == o2.isDirectory())
                {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
                else
                {
                    return o1.isDirectory() ? -1 : 1;
                }
            }
        });

        for (File file : files)
        {
            String name = file.getName();
            if (name.endsWith(".java"))
            {
                javaSourceFiles.add(file);
            }
        }

        for (File file : javaSourceFiles)
        {
            super.add(file);
        }
    }

    private void getXmlConfigFiles(File[] files)
    {
        ArrayList<File> xmlConfigFiles = new ArrayList<>();

        Arrays.sort(files, new Comparator<File>()
        {
            @Override
            public int compare(File o1, File o2)
            {
                if (o1.isDirectory() == o2.isDirectory())
                {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
                else
                {
                    return o1.isDirectory() ? -1 : 1;
                }
            }
        });

        for (File file : files)
        {
            String name = file.getName();
            if (name.endsWith(".xml"))
            {
                xmlConfigFiles.add(file);
            }
        }

        for (File file : xmlConfigFiles)
        {
            super.add(file);
        }
    }

    private String truncateExtension(String filename, String extension)
    {
        return filename.substring(0, filename.length() - extension.length());
    }

    DirectoryAdapter(String directory, Context context, Listener listener, int mode)
    {
        super(context, R.layout.view_simple_list_item_explorer, android.R.id.text1);
        mContext = context;
        mListener = listener;
        mDirectory = directory;

        TypedValue typedValue = new TypedValue();
        mContext.getTheme().resolveAttribute(R.attr.colorControlNormal, typedValue, true);
        mColor = typedValue.data;

        init(mode);
    }

    /**
     * Activate checkboxes for the list view items
     */
    void activateCheckboxes(int position)
    {
        mCheckboxes = true;
        mChecked.put(position, getItem(position));
        notifyDataSetChanged();
    }

    /**
     * Deactivate checkboxes for the list view items
     */
    void deactivateCheckboxes()
    {
        mCheckboxes = false;
        mChecked.clear();
        notifyDataSetChanged();
    }

    /**
     * Toggle the checkbox for the item at position
     */
    void toggleItem(int position)
    {
        if (mChecked.indexOfKey(position) < 0)
        {
            mChecked.put(position, getItem(position));
        }
        else
        {
            mChecked.remove(position);
        }
        notifyDataSetChanged();
    }

    /**
     * Retrieve a list of URIs containing all checked items
     */
    ArrayList<Uri> getUris()
    {
        ArrayList<Uri> uris = new ArrayList<>();
        for (int i = 0; i < mChecked.size(); ++i)
        {
            uris.add(Uri.fromFile(mChecked.valueAt(i)));
        }
        return uris;
    }

    private String getDirectorySummary(File directory)
    {
        File files[] = directory.listFiles();
        int numItems = 0;
        if (files != null)
        {
            numItems = files.length;
        }
        return mContext.getResources().getQuantityString(R.plurals.activity_explorer_folder, numItems, numItems);
    }

    private String getFileSummary(File file)
    {
        long size = file.length();
        if (size < 1000)
        {
            return size + " bytes";
        }
        else if (size < 1000000)
        {
            return size / 1000 + " KB";
        }
        else
        {
            return size / 1000000 + " MB";
        }
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent)
    {
        View view = super.getView(position, convertView, parent);
        File file = getItem(position);
        //noinspection ConstantConditions
        ((TextView) view.findViewById(android.R.id.text1)).setText(file.getName());
        ((TextView) view.findViewById(android.R.id.text2)).setText(
                file.isDirectory() ? getDirectorySummary(file) : getFileSummary(file)
        );
        ((TextView) view.findViewById(R.id.last_modified)).setText(
                DateUtils.getRelativeDateTimeString(
                        mContext,
                        file.lastModified(),
                        DateUtils.MINUTE_IN_MILLIS,
                        DateUtils.WEEK_IN_MILLIS,
                        0
                )
        );
        final ImageView imageView = (ImageView) view.findViewById(android.R.id.icon);
        imageView.setColorFilter(mColor);
        if(file.isDirectory())
        {
            imageView.setImageResource(R.drawable.ic_folder);
        }
        else
        {
            imageView.setImageResource(R.drawable.ic_file);
        }
        View spacer = view.findViewById(R.id.spacer);
        CheckBox checkBox = (CheckBox) view.findViewById(android.R.id.checkbox);
        if (mCheckboxes)
        {
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(mChecked.indexOfKey(position) >= 0);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if (isChecked)
                    {
                        mChecked.put(position, getItem(position));
                    }
                    else
                    {
                        mChecked.remove(position);
                        if (mChecked.size() == 0)
                        {
                            mListener.onAllItemsDeselected();
                        }
                    }
                }
            });
            spacer.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.VISIBLE);
        }
        else
        {
            spacer.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
        }
        return view;
    }
}