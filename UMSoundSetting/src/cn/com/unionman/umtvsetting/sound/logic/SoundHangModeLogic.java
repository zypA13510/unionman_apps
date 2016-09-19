package cn.com.unionman.umtvsetting.sound.logic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;

import cn.com.unionman.umtvsetting.sound.interfaces.AudioInterface;
import cn.com.unionman.umtvsetting.sound.interfaces.InterfaceValueMaps;
import cn.com.unionman.umtvsetting.sound.logic.factory.InterfaceLogic;
import cn.com.unionman.umtvsetting.sound.model.WidgetType;
import cn.com.unionman.umtvsetting.sound.model.WidgetType.AccessSysValueInterface;
import cn.com.unionman.umtvsetting.sound.util.Constant;
import cn.com.unionman.umtvsetting.sound.util.Util;

import cn.com.unionman.umtvsetting.sound.R;

/**
 * SoundHangModeLogic
 *
 * @author wangchuanjian
 *
 */
public class SoundHangModeLogic implements InterfaceLogic {

    private Context mContext;

    // private WidgetType mHangMode;// SoundHangMode
    // private List<WidgetType> mWidgetList = null;
    // private int[][] mHangModeValue = InterfaceValueMaps.hang_mode;

    public SoundHangModeLogic(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public List<WidgetType> getWidgetTypeList() {
        List<WidgetType> mWidgetList = new ArrayList<WidgetType>();
        Resources res = mContext.getResources();
        // SoundHangMode
        WidgetType mHangMode = new WidgetType();
        // set name for SoundHangMode
        mHangMode.setName(res.getStringArray(R.array.voice_setting)[6]);
        // set name for SoundHangMode
        mHangMode.setType(WidgetType.TYPE_SELECTOR);
        mHangMode.setmAccessSysValueInterface(new AccessSysValueInterface() {

            @Override
            public int setSysValue(int i) {
                int result = AudioInterface
                        .setHangMode(InterfaceValueMaps.hang_mode[i][0]);
                if (Constant.LOG_TAG) {
                    Log.d("    AudioInterface.setHangMode(mHangMode[i][0])", ""
                            + result);
                }
                return result;
            }

            @Override
            public int getSysValue() {
                int mode = AudioInterface.getHangMode();
                return Util.getIndexFromArray(mode,
                        InterfaceValueMaps.hang_mode);
            }
        });
        // set data for SoundHangMode
        mHangMode.setData(Util
                .createArrayOfParameters(InterfaceValueMaps.hang_mode));
        mWidgetList.add(mHangMode);
        return mWidgetList;
    }

    @Override
    public void setHandler(Handler handler) {

    }

}
