package cn.com.unionman.umtvsetting.system.logic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.util.Log;


import cn.com.unionman.umtvsetting.system.R;
import cn.com.unionman.umtvsetting.system.interfaces.InterfaceValueMaps;
import cn.com.unionman.umtvsetting.system.interfaces.PictureInterface;
import cn.com.unionman.umtvsetting.system.logic.factory.InterfaceLogic;
import cn.com.unionman.umtvsetting.system.model.WidgetType;
import cn.com.unionman.umtvsetting.system.model.WidgetType.AccessSysValueInterface;
import cn.com.unionman.umtvsetting.system.util.Constant;
import cn.com.unionman.umtvsetting.system.util.Util;

/**
 * PictureNRLogic
 *
 * @author wangchuanjian
 *
 */
public class PictureNRLogic implements InterfaceLogic {

    private static final String TAG = "PictureNRLogic";
    private Context mContext;

    // private WidgetType mPictureNR;// PictureNRLogic
    // private List<WidgetType> mWidgetList = null;
    // private int[][] mPictureNRValue = InterfaceValueMaps.NR;

    public PictureNRLogic(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public List<WidgetType> getWidgetTypeList() {
        List<WidgetType> mWidgetList = new ArrayList<WidgetType>();
        Resources res = mContext.getResources();

        // PictureNR
        WidgetType mPictureNR = new WidgetType();
        // set name for PictureNR
        mPictureNR.setName(res.getStringArray(R.array.pic_setting)[3]);
        // set type for PictureNR
        mPictureNR.setType(WidgetType.TYPE_SELECTOR);
        mPictureNR.setmAccessSysValueInterface(new AccessSysValueInterface() {

            @Override
            public int setSysValue(int i) {
                if (Constant.LOG_TAG) {
                    Log.d(TAG, "set nr value =" + i);
                }
                return PictureInterface.setNR(InterfaceValueMaps.NR[i][0]);
            }

            @Override
            public int getSysValue() {
                int mode = PictureInterface.getNR();
                return Util.getIndexFromArray(mode, InterfaceValueMaps.NR);
            }
        });
        // set data for PictureNR
        mPictureNR.setData(Util.createArrayOfParameters(InterfaceValueMaps.NR));
        mWidgetList.add(mPictureNR);
        return mWidgetList;
    }

    @Override
    public void setHandler(Handler handler) {
        // TODO Auto-generated method stub

    }

}
