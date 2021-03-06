package com.um.videoplayer.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

//import com.um.videoplayer.activity.activity.ActivityVideo;
import com.um.videoplayer.R;
import com.um.videoplayer.activity.BDActivityNavigation;
import com.um.videoplayer.activity.DVDActivityNavigation;
import com.um.videoplayer.activity.base.ActivityFrame;
import com.um.videoplayer.utility.LogTool;

public class ReceiverNetwork extends BroadcastReceiver {
    private Activity mActivity;

    public ReceiverNetwork(Activity pActivity) {
        mActivity = pActivity;
    }

    @Override
    public void onReceive(Context pContext, Intent pIntent) {
        boolean _IsConnect = true;
        final String _Action = pIntent.getAction();
        LogTool.d(_Action);

        if (_Action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager _ConnectivityManager =
                (ConnectivityManager) pContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo _Info = _ConnectivityManager.getActiveNetworkInfo();
            _IsConnect = (_Info != null) && _Info.isConnected();
        }

        if (!_IsConnect) {
            doNetworkInterrupt();
        }
    }

    private void doNetworkInterrupt() {
        LogTool.d("");

        if ((mActivity instanceof BDActivityNavigation) || (mActivity instanceof DVDActivityNavigation)) {
            if (((ActivityFrame) mActivity).isNetworkFile()) {
                LogTool.e("network interrupt");
                Toast.makeText(mActivity, R.string.toastNetworkInterrupt, Toast.LENGTH_SHORT)
                .show();
                ((ActivityFrame) mActivity).finishActivityWithAnim();
            }
        }
    }
}
