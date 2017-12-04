package org.openftc.blocklyObjSync.ui.explorer;

import java.io.File;

public class BlocklyFileObject
{
    File blkFile;
    File jsFile;

    public BlocklyFileObject(File blkFile, File jsFile)
    {
        this.blkFile = blkFile;
        this.jsFile = jsFile;
    }

    public String getName()
    {
        return blkFile.getName().substring(0, blkFile.getName().length() - 4);
    }
}
