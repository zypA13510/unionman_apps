package com.um.atv.logic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;

import com.hisilicon.android.tvapi.constant.EnumPictureDemo;
import com.um.atv.R;
import com.um.atv.interfaces.InterfaceValueMaps;
import com.um.atv.interfaces.PictureInterface;
import com.um.atv.logic.factory.InterfaceLogic;
import com.um.atv.model.WidgetType;
import com.um.atv.model.WidgetType.AccessSysValueInterface;
import com.um.atv.util.Util;

/**
 * ColorLogic
 *
 * @author wangchuanjian
 *
 */
public class ColorDemoLogic implements InterfaceLogic {

    private static final String TAG = "COLORDemoLogic";
    private Context mContext;

    // private WidgetType mDemoColor;// Color
    // private List<WidgetType> mWidgetList = null;
    // private int[][] mDemoColorValue = InterfaceValueMaps.on_off;

    public ColorDemoLogic(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public List<WidgetType> getWidgetTypeList() {
        List<WidgetType> mWidgetList = new ArrayList<WidgetType>();
        Resources res = mContext.getResources();

        // Color
        WidgetType mDemoColor = new WidgetType();
        // set name for Color
        mDemoColor.setName(res.getStringArray(R.array.demo_mode_setting)[1]);
        // set type for Color
        mDemoColor.setType(WidgetType.TYPE_SELECTOR);
        mDemoColor.setmAccessSysValueInterface(new AccessSysValueInterface() {

            @Override
            public int setSysValue(int i) {
                int res = EnumPictureDemo.DEMO_COLOR;
                boolean flag = true;
                if (i == 0) {
                    flag = false;
                } else {
                    flag = true;
                }
                return PictureInterface.setDemoMode(res, flag);
            }

            @Override
            public int getSysValue() {
                int res = EnumPictureDemo.DEMO_COLOR;
                boolean i = PictureInterface.getDemoMode(res);
                int a = 0;
                if (i) {
                    a = 1;
                } else {
                    a = 0;
                }
                return a;
            }
        });
        // set data for Color
        mDemoColor.setData(Util
                .createArrayOfParameters(InterfaceValueMaps.on_off));
        mWidgetList.add(mDemoColor);
        return mWidgetList;
    }

    @Override
    public void setHandler(Handler handler) {

    }

}
