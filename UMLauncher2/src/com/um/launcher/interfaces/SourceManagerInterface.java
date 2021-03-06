
package com.um.launcher.interfaces;

import java.util.ArrayList;

import android.util.Log;

import com.hisilicon.android.tvapi.UmtvManager;
import com.hisilicon.android.tvapi.CusSourceManager;
import com.hisilicon.android.tvapi.vo.RectInfo;
import com.hisilicon.android.tvapi.vo.TimingInfo;
import com.um.launcher.util.Constant;

public class SourceManagerInterface {

    public static final String TAG = "SourceManagerInterface";

    public static CusSourceManager getSourceManager() {
        return UmtvManager.getInstance().getSourceManager();
    }

    public static int deselectSource(int srcId, boolean bDestroy) {
        if (Constant.LOG_TAG) {
            Log.d(TAG, "deselectSource(int srcId = " + srcId
                    + ", boolean bDestroy = " + bDestroy + ")  begin");
        }

        int value = getSourceManager().deselectSource(srcId, bDestroy);

        if (Constant.LOG_TAG) {
            Log.d(TAG, "deselectSource(int srcId = " + srcId
                    + ", boolean bDestroy =" + bDestroy + ") end value = "
                    + value);
        }
        return value;
    }

    /**
     * set enable dualdisplay
     */
    public static int enableDualDisplay(boolean enable) {
        if (Constant.LOG_TAG) {
            Log.d(TAG, "enableDualDisplay(boolean enable = " + enable
                    + ") begin");
        }

        int value = getSourceManager().enableDualDisplay(enable);

        if (Constant.LOG_TAG) {
            Log.d(TAG, "enableDualDisplay(boolean enable = " + enable
                    + ") end value = " + value);
        }
        return value;
    }

    /**
     * get avail source list
     */
    public static ArrayList<Integer> getAvailSourceList() {
        if (Constant.LOG_TAG) {
            Log.d(TAG, "getAvailSourceList() begin");
        }

        ArrayList<Integer> value = getSourceManager().getAvailSourceList();

        if (Constant.LOG_TAG) {
            Log.d(TAG, "getAvailSourceList() end value = " + value);
        }
        return value;
    }

    /**
     * get cursource id
     */
    public static int getCurSourceId() {
        if (Constant.LOG_TAG) {
            Log.d(TAG, "getCurSourceId() begin");
        }

        int value = getSourceManager().getCurSourceId(0);

        if (Constant.LOG_TAG) {
            Log.d(TAG, "getCurSourceId() end value = " + value);
        }
        return value;
    }

    /**
     * Get current SourceId saved
     */
    public static int getSelectSourceId() {
        if (Constant.LOG_TAG) {
            Log.d(TAG, "getSelectSourceId() begin");
        }

        int value = getSourceManager().getSelectSourceId();

        if (Constant.LOG_TAG) {
            Log.d(TAG, "getSelectSourceId() end value = " + value);
        }
        return value;
    }

    /**
     * getLastSourceId
     */
    public static int getLastSourceId() {
        if (Constant.LOG_TAG) {
            Log.d(TAG, "getLastSourceId() begin");
        }

        int value = getSourceManager().getLastSourceId();

        if (Constant.LOG_TAG) {
            Log.d(TAG, "getLastSourceId() end value = " + value);
        }
        return value;
    }
    
    /**
     * get signal status
     */
    public static int getSignalStatus() {
        if (Constant.LOG_TAG) {
            Log.d(TAG, "getSignalStatus() begin");
        }

        int value = getSourceManager().getSignalStatus();

        if (Constant.LOG_TAG) {
            Log.d(TAG, "getSignalStatus() end value = " + value);
        }
        return value;
    }

    /**
     * get source list
     */
    public static ArrayList<Integer> getSourceList() {
        if (Constant.LOG_TAG) {
            Log.d(TAG, "getSourceList() begin");
        }

        ArrayList<Integer> value = getSourceManager().getSourceList();

        if (Constant.LOG_TAG) {
            Log.d(TAG, "getSourceList() end value = " + value);
        }
        return value;
    }

    /**
     * get Timming
     */
    public static TimingInfo getTimingInfo() {
        if (Constant.LOG_TAG) {
            Log.d(TAG, "getTimingInfo() begin");
        }

        TimingInfo value = getSourceManager().getTimingInfo();

        if (Constant.LOG_TAG) {
            Log.d(TAG, "getTimingInfo() end value = " + value);
        }
        return value;
    }

    /**
     * get isDVI model
     */
    public static boolean isDVIMode() {
        if (Constant.LOG_TAG) {
            Log.d(TAG, "isDVIMode() begin");
        }

        boolean value = getSourceManager().isDVIMode();

        if (Constant.LOG_TAG) {
            Log.d(TAG, "isDVIMode() end value = " + value);
        }
        return value;
    }

    public static int setWindowRect(RectInfo rect, int mainWindow) {

        int res = getSourceManager().setWindowRect(rect, mainWindow);

        Log.d(TAG, "SourceManager  setWindowRect ret = " + res);

        return res;

    }

    /**
     * select source
     */
    public static int selectSource(int srcId, int nMainWindow) {
        if (Constant.LOG_TAG) {
            Log.d(TAG, "selectSource(int srcId = " + srcId
                    + ", int nMainWindow =" + nMainWindow + ")  begin");
        }

        int value = getSourceManager().selectSource(srcId, nMainWindow);

        if (Constant.LOG_TAG) {
            Log.d(TAG, "selectSource(int srcId = " + srcId
                    + ", int nMainWindow = " + nMainWindow
                    + ")  end value = " + value);
        }
        return value;
    }

    /**
     * Set the window said displayed in the left or right, if the two terminal
     * display this interface must be set
     */
    public static int setDisplayOnLeft(boolean left, int nMainWindow) {
        if (Constant.LOG_TAG) {
            Log.d(TAG, "setDisplayOnLeft(boolean left = " + left
                    + ", int nMainWindow = " + nMainWindow + ")  begin");
        }

        int value = getSourceManager().setDisplayOnLeft(left, nMainWindow);

        if (Constant.LOG_TAG) {
            Log.d(TAG, "setDisplayOnLeft(boolean left = " + left
                    + ", int nMainWindow = " + nMainWindow
                    + ")  end value = " + value);
        }
        return value;
    }

    /**
     * set focus window
     */
    public static int setFocusWindow(int nMainWindow) {
        if (Constant.LOG_TAG) {
            Log.d(TAG, "setFocusWindow(int nMainWindow = " + nMainWindow
                    + ")  begin");
        }

        int value = getSourceManager().setFocusWindow(nMainWindow);

        if (Constant.LOG_TAG) {
            Log.d(TAG, "setFocusWindow(int nMainWindow = " + nMainWindow
                    + ")  end value = " + value);
        }
        return value;
    }
}
