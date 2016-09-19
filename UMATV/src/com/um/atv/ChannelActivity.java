package com.um.atv;

import com.um.atv.R;
import com.um.atv.util.Constant;
import com.um.atv.widget.ChannelListLayout;
import com.um.atv.widget.RecentPlayListLayout;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.app.Activity;
import android.app.Instrumentation;

public class ChannelActivity extends Activity {
    private ChannelListLayout mRecentPlayListLay;
    private static final String TAG = "ChannelActivity";
    private static final int ACTIVITY_FINISH = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        mRecentPlayListLay = (ChannelListLayout) findViewById(R.id.recentplaylistlayout);
        delay();
    }
     @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            Log.d(TAG, "onKeyDown= keyCode:" + keyCode);
            mRecentPlayListLay.onKeyDown(keyCode, event);
            switch (keyCode) {
   	     case KeyEvent.KEYCODE_MENU:
	    	  new Thread() {
	    		   public void run() {
	    		    try {
	    		     Instrumentation inst = new Instrumentation();
	    		     inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
	    		    } catch (Exception e) {
	    		     Log.i(TAG,"Exception when sendKeyDownUpSync e="+e.toString());
	    		    }
	    		   }
	    		  }.start();
			break;
		 case KeyEvent.KEY_SOURCEENTER:	
			 Log.i(TAG, "ChannelActivity KEY_SOURCEENTER is click");

    		 return true;
			}
            return super.onKeyDown(keyCode, event);
        }
       @Override
        public boolean dispatchKeyEvent(KeyEvent event) {
            Log.d(TAG, "dispatchKeyEvent= keyCode:" + event.getKeyCode());
            delay();
            if (event.getAction() == KeyEvent.ACTION_UP) {
                mRecentPlayListLay.dispatchEvent(event);
            }
            return super.dispatchKeyEvent(event);
        }
     private Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                case ACTIVITY_FINISH:
                    if (Constant.LOG_TAG) {
                        Log.d("ACTIVITY_FINISH", ACTIVITY_FINISH + "");
                    }
                    finish();
                    break;
                default:
                    break;
                }
            }
        };
        public void delay() {
            mHandler.removeMessages(ACTIVITY_FINISH);
           mHandler.sendEmptyMessageDelayed(ACTIVITY_FINISH, Constant.DISPEAR_TIME_30s);
        }
}
