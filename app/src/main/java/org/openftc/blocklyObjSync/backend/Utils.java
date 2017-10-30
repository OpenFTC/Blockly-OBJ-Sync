package org.openftc.blocklyObjSync.backend;

import android.content.pm.PackageManager;

public class Utils
{
    public static boolean isFtcRobotControllerInstalled(PackageManager packageManager)
    {
        try
        {
            packageManager.getPackageInfo("com.qualcomm.ftcrobotcontroller", 0);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
}
