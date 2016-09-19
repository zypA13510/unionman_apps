package com.um.tv.menu.model;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.hisilicon.android.tvapi.CusFactory;
import com.um.tv.menu.ui.FactoryWindow;
import com.um.tv.menu.utils.Utils;

public class UMNetUpgradeModel extends Model {
	private static final String TAG = "UMNetUpgradeModel";
	
	public UMNetUpgradeModel(Context context, FactoryWindow window,
			CusFactory factory) {
		super(context, window, factory);
		// TODO Auto-generated constructor stub
		mName = Utils.DisplayNameUMNetUpgrade;
	}

	@Override
	public View getView(Context context, int position, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
        return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d(TAG,"leon...onItemClick");
		
	}

	@Override
	public void changeValue(int direct, int position, View view) {
		// TODO Auto-generated method stub
		
	}
	
}
