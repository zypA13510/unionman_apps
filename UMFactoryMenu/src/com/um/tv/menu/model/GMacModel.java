package com.um.tv.menu.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hisilicon.android.tvapi.CusFactory;
import com.um.tv.menu.ui.FactoryWindow;
import com.um.tv.menu.utils.Utils;

public class GMacModel extends Model {
    public String mDisplayName = "GMAC";
    public String[] mItemNames = Utils.ItemsGMAC;

    public GMacModel(Context context, FactoryWindow window, CusFactory factory) {
        super(context, window, factory);
        mName = mDisplayName;

        initChildren();
    }

    private void initChildren() {
        ChoiceModel gmac = new ChoiceModel(mContext, mWindow, mFactory, ChoiceModel.TypeGMACEnable);
        gmac.mName = mItemNames[0];
        addChild(gmac);

        ChoiceModel spread = new ChoiceModel(mContext, mWindow, mFactory, ChoiceModel.TypeGMACSpreadEnable);
        spread.mName = mItemNames[1];
        addChild(spread);

        RangeModel ratio = new RangeModel(mContext, mWindow, mFactory, RangeModel.TypeGMACSpreadRatio);
        ratio.mName = mItemNames[2];
        addChild(ratio);
        
        RangeModel freq = new RangeModel(mContext, mWindow, mFactory, RangeModel.TypeGMACSpreadFreq);
        freq.mName = mItemNames[3];
        addChild(freq);
        
        RangeModel drvCurrent = new RangeModel(mContext, mWindow, mFactory, RangeModel.TypeGMACDrvCurrent);
        drvCurrent.mName = mItemNames[4];
        addChild(drvCurrent);
        
    }

    @Override
    public View getView(Context context, int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        return mChildrenList.get(position).getView(context, position, convertView, parent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        // TODO Auto-generated method stub
//        mChildrenList.get(position).onItemClick(parent, view, position, id);
        mWindow.updateItems(this);
    }

    @Override
    public void changeValue(int direct, int position, View view) {
        // TODO Auto-generated method stub
        if(position >= mChildrenList.size()) return ;
        mChildrenList.get(position).changeValue(direct, position, view);
    }
}
