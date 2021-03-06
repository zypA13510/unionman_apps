package cn.com.unionman.umtvsetting.picture.logic;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import cn.com.unionman.umtvsetting.picture.interfaces.ATVChannelInterface;
import cn.com.unionman.umtvsetting.picture.interfaces.InterfaceValueMaps;
import cn.com.unionman.umtvsetting.picture.interfaces.PictureInterface;
import cn.com.unionman.umtvsetting.picture.interfaces.SourceManagerInterface;
import cn.com.unionman.umtvsetting.picture.logic.factory.InterfaceLogic;
import cn.com.unionman.umtvsetting.picture.model.WidgetType;
import cn.com.unionman.umtvsetting.picture.model.WidgetType.AccessProgressInterface;
import cn.com.unionman.umtvsetting.picture.model.WidgetType.AccessSysValueInterface;
import cn.com.unionman.umtvsetting.picture.util.Constant;
import cn.com.unionman.umtvsetting.picture.util.Util;

import com.hisilicon.android.tvapi.constant.EnumSourceIndex;
import cn.com.unionman.umtvsetting.picture.R;
import com.hisilicon.android.tvapi.constant.EnumColorSystem;

/**
 * PictureModeLogic
 *
 * @author wangchuanjian
 *
 */

public class PictureModeLogic implements InterfaceLogic {

    private static final String TAG = "PictureModeLogic";
    private Context mContext;
    // private List<WidgetType> mWidgetList = null;

    // private WidgetType mHiHue;

    private Handler mHandler;

    // private int[][] mPictureModeValue = InterfaceValueMaps.picture_mode;
    // private int[][] mColorSystemValue = InterfaceValueMaps.color_system;

    private int mBrightness = 0;
    private int mContrast = 0;
    private int mSharpness = 0;
    private int mSaturation = 0;
    private int mHue = 0;

    public PictureModeLogic(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public List<WidgetType> getWidgetTypeList() {
        if (Constant.LOG_TAG) {
            Log.d("test", "PictureLogic ");
        }
        List<WidgetType> mWidgetList = new ArrayList<WidgetType>();
        Resources res = mContext.getResources();
        // PictureMode
        WidgetType mPictureMode = new WidgetType();
        // set name for PictureMode
        mPictureMode
                .setName(res.getStringArray(R.array.picture_mode_setting)[0]);
        // set type for PictureMode
        mPictureMode.setType(WidgetType.TYPE_SELECTOR);
        mPictureMode.setmAccessSysValueInterface(new AccessSysValueInterface() {

            @Override
            public int setSysValue(int i) {
                if (Constant.LOG_TAG) {
                    Log.d(TAG, "setSysValue i = " + i);
                }
                int ret = PictureInterface
                        .setPictureMode(InterfaceValueMaps.picture_mode[i][0]);
                return ret;

            }

            @Override
            public int getSysValue() {
                if (Constant.LOG_TAG) {
                }
                int mode = PictureInterface.getPictureMode();
                return Util.getIndexFromArray(mode,
                        InterfaceValueMaps.picture_mode);
            }
        });
        // set data for PictureMode
        mPictureMode.setData(Util
                .createArrayOfParameters(InterfaceValueMaps.picture_mode));
        mWidgetList.add(mPictureMode);

//        // Brightness
//        WidgetType HiBrightness = new WidgetType();
//        // set name for Brightness
//        HiBrightness
//                .setName(res.getStringArray(R.array.picture_mode_setting)[1]);
//        // set type for Brightness
//        HiBrightness.setType(WidgetType.TYPE_PROGRESS);
//        HiBrightness.setmAccessProgressInterface(new AccessProgressInterface() {
//
//            @Override
//            public int setProgress(int i) {
//                if (Constant.LOG_TAG) {
//                    Log.d(TAG, "setProgress i = " + i);
//                }
//
//                // long pretime = System.currentTimeMillis();
//
//                mContrast = PictureInterface.getContrast();
//                mSaturation = PictureInterface.getSaturation();
//                if (isHueEnable()) {
//                    mHue = PictureInterface.getHue();
//                }
//                mSharpness = PictureInterface.getSharpness();
//
//                PictureInterface
//                        .setPictureMode(InterfaceValueMaps.picture_mode[3][0]);
//                RefreshPictureSelector();
//
//                PictureInterface.setBrightness(i);
//                PictureInterface.setContrast(mContrast);
//                PictureInterface.setSaturation(mSaturation);
//                PictureInterface.setSharpness(mSharpness);
//                if (isHueEnable()) {
//                    PictureInterface.setHue(mHue);
//                }
//                return i;
//            }
//
//            @Override
//            public int getProgress() {
//                return PictureInterface.getBrightness();
//            }
//        });
//        mWidgetList.add(HiBrightness);
//
//        // Contrast
//        WidgetType HiContrast = new WidgetType();
//        // set name for Contrast
//        HiContrast.setName(res.getStringArray(R.array.picture_mode_setting)[2]);
//        // set type for Contrast
//        HiContrast.setType(WidgetType.TYPE_PROGRESS);
//        HiContrast.setmAccessProgressInterface(new AccessProgressInterface() {
//
//            @Override
//            public int setProgress(int i) {
//                if (Constant.LOG_TAG) {
//                    Log.d(TAG, "setProgress i = " + i);
//                }
//
//                mSaturation = PictureInterface.getSaturation();
//                mSharpness = PictureInterface.getSharpness();
//                mBrightness = PictureInterface.getBrightness();
//                if (isHueEnable()) {
//                    mHue = PictureInterface.getHue();
//                }
//
//                PictureInterface
//                        .setPictureMode(InterfaceValueMaps.picture_mode[3][0]);
//                RefreshPictureSelector();
//
//                PictureInterface.setContrast(i);
//                PictureInterface.setSaturation(mSaturation);
//                PictureInterface.setSharpness(mSharpness);
//                PictureInterface.setBrightness(mBrightness);
//                if (isHueEnable()) {
//                    PictureInterface.setHue(mHue);
//                }
//
//                return i;
//            }
//
//            @Override
//            public int getProgress() {
//                return PictureInterface.getContrast();
//            }
//        });
//        mWidgetList.add(HiContrast);
//
//        // Saturation
//        WidgetType HiSaturation = new WidgetType();
//        // set name for Saturation
//        HiSaturation
//                .setName(res.getStringArray(R.array.picture_mode_setting)[3]);
//        // set type for Saturation
//        HiSaturation.setType(WidgetType.TYPE_PROGRESS);
//        HiSaturation.setmAccessProgressInterface(new AccessProgressInterface() {
//
//            @Override
//            public int setProgress(int i) {
//
//                if (Constant.LOG_TAG) {
//                    Log.d(TAG, "setProgress i = " + i);
//                }
//
//                mContrast = PictureInterface.getContrast();
//                mSharpness = PictureInterface.getSharpness();
//                mBrightness = PictureInterface.getBrightness();
//                if (isHueEnable()) {
//                    mHue = PictureInterface.getHue();
//                }
//
//                PictureInterface
//                        .setPictureMode(InterfaceValueMaps.picture_mode[3][0]);
//                RefreshPictureSelector();
//
//                PictureInterface.setSaturation(i);
//                PictureInterface.setContrast(mContrast);
//                PictureInterface.setSharpness(mSharpness);
//                PictureInterface.setBrightness(mBrightness);
//                if (isHueEnable()) {
//                    PictureInterface.setHue(mHue);
//                }
//                return i;
//            }
//
//            @Override
//            public int getProgress() {
//                return PictureInterface.getSaturation();
//            }
//        });
//        mWidgetList.add(HiSaturation);
//
//        // Hue
//        WidgetType mHiHue = new WidgetType();
//        // set name for Hue
//        mHiHue.setName(res.getStringArray(R.array.picture_mode_setting)[4]);
//        // set type for Hue
//        mHiHue.setType(WidgetType.TYPE_PROGRESS);
//
//        if (!isHueEnable()) {
//            mHiHue.setEnable(false);
//        }
//        mHiHue.setmAccessProgressInterface(new AccessProgressInterface() {
//
//            @Override
//            public int setProgress(int i) {
//                if (Constant.LOG_TAG) {
//                    Log.d(TAG, "setProgress i = " + i);
//                }
//
//                if (isHueEnable()) {
//                    mContrast = PictureInterface.getContrast();
//                    mSaturation = PictureInterface.getSaturation();
//                    mSharpness = PictureInterface.getSharpness();
//                    mBrightness = PictureInterface.getBrightness();
//
//                    PictureInterface
//                            .setPictureMode(InterfaceValueMaps.picture_mode[3][0]);
//                    RefreshPictureSelector();
//
//                    PictureInterface.setHue(i);
//                    PictureInterface.setContrast(mContrast);
//                    PictureInterface.setSaturation(mSaturation);
//
//                    PictureInterface.setSharpness(mSharpness);
//                    PictureInterface.setBrightness(mBrightness);
//                }
//
//                return i;
//            }
//
//            @Override
//            public int getProgress() {
//                int hue = PictureInterface.getHue();
//                return hue;
//            }
//        });
//        mWidgetList.add(mHiHue);
//
//        // Sharpness
//        WidgetType HiSharpness = new WidgetType();
//        // set name for Sharpness
//        HiSharpness
//                .setName(res.getStringArray(R.array.picture_mode_setting)[5]);
//        // set type for Sharpness
//        HiSharpness.setType(WidgetType.TYPE_PROGRESS);
//        HiSharpness.setmAccessProgressInterface(new AccessProgressInterface() {
//
//            @Override
//            public int setProgress(int i) {
//                if (Constant.LOG_TAG) {
//                    Log.d(TAG, "setProgress i = " + i);
//                }
//
//                mBrightness = PictureInterface.getBrightness();
//                mContrast = PictureInterface.getContrast();
//                mSaturation = PictureInterface.getSaturation();
//                if (isHueEnable()) {
//                    mHue = PictureInterface.getHue();
//                }
//
//                PictureInterface
//                        .setPictureMode(InterfaceValueMaps.picture_mode[3][0]);
//                RefreshPictureSelector();
//
//                PictureInterface.setSaturation(mSaturation);
//                PictureInterface.setContrast(mContrast);
//                PictureInterface.setBrightness(mBrightness);
//                PictureInterface.setSharpness(i);
//
//
//                if (isHueEnable()) {
//                    PictureInterface.setHue(mHue);
//                }
//                return i;
//
//            }
//
//            @Override
//            public int getProgress() {
//
//                return PictureInterface.getSharpness();
//            }
//        });
//        mWidgetList.add(HiSharpness);

        return mWidgetList;
    }

    @Override
    public void setHandler(Handler handler) {
        mHandler = handler;
    }

    /**
     * refresh selector of Picture
     */
    private void RefreshPictureSelector() {
        Message msg = new Message();
        msg.what = Constant.SETTING_UI_REFRESH_VIEWS;
        List<String> Stringlist = new ArrayList<String>();
        Stringlist.add(mContext.getResources().getStringArray(
                R.array.picture_mode_setting)[0]);
        msg.obj = Stringlist;
        mHandler.sendMessage(msg);
    }

    /**
     * is Hue enable or not
     *
     * @return
     */
    private boolean isHueEnable() {
        int mCurSourceIdx = SourceManagerInterface.getSelectSourceId();
        int colorsystem = 0;
        if(mCurSourceIdx == EnumSourceIndex.SOURCE_ATV){
            colorsystem = ATVChannelInterface.getCurrentColorSystem();
            Log.d(TAG,"atv current program colorsystem = "+colorsystem);
            if(colorsystem == EnumColorSystem.CLRSYS_NTSC || colorsystem == EnumColorSystem.CLRSYS_NTSC443
               ||colorsystem == EnumColorSystem.CLRSYS_NTSC_50){

                 return true;
            }
            else{
                return false;
            }

        }
        else if(mCurSourceIdx == EnumSourceIndex.SOURCE_CVBS1 || mCurSourceIdx == EnumSourceIndex.SOURCE_CVBS2){

            colorsystem = PictureInterface.getColorSystem();

            Log.d(TAG,"av current  colorsystem = "+colorsystem);

            if(colorsystem == EnumColorSystem.CLRSYS_NTSC || colorsystem == EnumColorSystem.CLRSYS_NTSC443
               ||colorsystem == EnumColorSystem.CLRSYS_NTSC_50){

                 return true;
            }
            else{
                return false;
            }

        }
        else{

            return false;
        }

    }
    
    public boolean isHueMode() {
    	// TODO Auto-generated method stub
    	if(TAG.equals("HueModeLogic")){
    		return true;
    	}else{
    		return false;
    	}
    	
    }
}
