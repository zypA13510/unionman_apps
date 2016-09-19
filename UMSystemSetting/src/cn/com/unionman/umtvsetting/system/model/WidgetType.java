package cn.com.unionman.umtvsetting.system.model;

import android.view.View;

/**
 * The type of control entity class
 *
 * @author wangchuanjian
 *
 */
public class WidgetType {

    public static final int TYPE_SELECTOR = 0;
    public static final int TYPE_PROGRESS = 1;
    public static final int TYPE_VGA = 2;
	public static final int TYPE_TEXTVIEW = 3;
    public static final int TYPE_ONLYSELECTOR = 4;
    public static final int TYPE_LONG_TEXT = 5;

    // number of type
    private int mType;
    // name of widget
    private String mWidgetName;
    // data of widget
    private int[] mWidgetData;
    // have arrow or not
    private boolean ishaveArrow = true;
    // state of VGA
    private boolean isVGAState = true;
    // The progress of the maximum value
    private int mMaxProgress = 100;
    // The progress of the bottom layer and the interfacial offset
    private int mOffSet = 0;
    // Goal setting options can be selected: used in SRS
    private boolean isEnable = true;
    public interface AccessOnClickInterface {

        void onClickEvent(View v);

    }

    public interface AccessSysValueInterface {

        int getSysValue();

        int setSysValue(int i);
    }

    public interface AccessProgressInterface {
        int getProgress();

        int setProgress(int i);
    }

    public interface Refreshable {
        void refreshUI();

        WidgetType getWidgetType();

        boolean getIsFocus();
    }

 	private String mInfo;
	private Object mTag;
    public Object getTag() {
        return mTag;
    }

    public void setTag(Object tag) {
        this.mTag = tag;
    }

    public boolean isVGAstate() {
        return isVGAState;
    }

    public void setVGAstate(boolean VGAstate) {
        this.isVGAState = VGAstate;
    }

    private String onlySelectorName;

    public String getOnlySelectorName() {
        return onlySelectorName;
    }

    public void setOnlySelectorName(String onlySelectorName) {
        this.onlySelectorName = onlySelectorName;
    }
    public String getInfo() {
        return mInfo;
    }

    public void setInfo(String info) {
        this.mInfo = info;
    }
    public boolean isIshaveArrow() {
        return ishaveArrow;
    }

    public void setIshaveArrow(boolean ishavearrow) {
        this.ishaveArrow = ishavearrow;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    private AccessOnClickInterface mAccessOnClickInterface;

    private AccessSysValueInterface mAccessSysValueInterface;

    private AccessProgressInterface mAccessProgressInterface;

    public WidgetType() {
        super();
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getName() {
        return mWidgetName;
    }

    public void setName(String name) {
        this.mWidgetName = name;
    }

    public int[] getData() {
        return mWidgetData;
    }

    public void setData(int[] data) {
        this.mWidgetData = data;
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.mMaxProgress = maxProgress;
    }

    public int getOffset() {
        return mOffSet;
    }

    public void setOffset(int offset) {
        this.mOffSet = offset;
    }

    public AccessOnClickInterface getmAccessOnClickInterface() {
        return mAccessOnClickInterface;
    }

    public void setmAccessOnClickInterface(
            AccessOnClickInterface mAccessOnClickInterface) {
        this.mAccessOnClickInterface = mAccessOnClickInterface;
    }

    public AccessSysValueInterface getmAccessSysValueInterface() {
        return mAccessSysValueInterface;
    }

    public void setmAccessSysValueInterface(
            AccessSysValueInterface mAccessSysValueInterface) {
        this.mAccessSysValueInterface = mAccessSysValueInterface;
    }

    public AccessProgressInterface getmAccessProgressInterface() {
        return mAccessProgressInterface;
    }

    public void setmAccessProgressInterface(
            AccessProgressInterface mAccessProgressInterface) {
        this.mAccessProgressInterface = mAccessProgressInterface;
    }
}
