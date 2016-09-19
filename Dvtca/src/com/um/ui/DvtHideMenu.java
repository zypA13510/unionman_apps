package com.um.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.um.controller.AppBaseActivity;
import com.um.dvbstack.Ca;
import com.um.dvbstack.DVB;
import com.um.dvtca.R;

public class DvtHideMenu extends AppBaseActivity{
	final String TAG = "DvtHideMenu";
	private Button btnLibPrint;
	private Button btnMainFre;
	private Button btnExit;
	private Context mContext = DvtHideMenu.this;
	int cmdType;
	int lparam;
	int rparam;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dvt_hide_menu);
		
		findBtn();

        btnLibPrint.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				openCloseLibPrint();
			}
		});
        
        btnMainFre.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent it1 = new Intent(DvtHideMenu.this,DvtSetMainFreq.class);
				startActivity(it1);
			}
		}); 
	}
	
	@Override
	protected void onPause(){
        super.onPause();
        finish();
	}
	
	private void findBtn(){
		btnLibPrint = (Button)findViewById(R.id.hide_menu_button1);
		btnMainFre = (Button)findViewById(R.id.hide_menu_button2);
	}
	
	private void openCloseLibPrint(){
		
		final Ca ca = new Ca(DVB.getInstance());
		
		AlertDialog.Builder libPrintBuilder = new AlertDialog.Builder(DvtHideMenu.this);
		LinearLayout layout = (LinearLayout) LayoutInflater.from(DvtHideMenu.this).inflate(R.layout.dvt_ca_lib_print_tip, null);
		libPrintBuilder.setView(layout);
		
		Button okButton = (Button)layout.findViewById(R.id.ok_btn);
		Button cancleButton = (Button) layout.findViewById(R.id.cancle_btn);
		final TextView text2 = (TextView) layout.findViewById(R.id.lib_printf_text);
		text2.setText(R.string.dvt_open_close_lib_print);
		final AlertDialog libPrintDialog = libPrintBuilder.create();
		libPrintDialog.show();	
		
		View.OnClickListener onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.ok_btn:
				{	
					Log.i(TAG, "open ca lib print");
					cmdType = 42;//open/close lib print
					lparam = 1;
					rparam = 0;
					int ret = ca.CaCmdProcess(cmdType, lparam, rparam);
					if (0 == ret){
						Toast.makeText(mContext, mContext.getResources().getText(R.string.open_lib_print_success), Toast.LENGTH_LONG).show();
					}else {
						Toast.makeText(mContext, mContext.getResources().getText(R.string.open_lib_print_fail), Toast.LENGTH_LONG).show();
					}
					libPrintDialog.hide();
				}
					break;
				case R.id.cancle_btn:
				{	
					Log.i(TAG, "close ca lib print");
					cmdType = 42;//open/close lib print
					lparam = 0;
					rparam = 0;
					int ret = ca.CaCmdProcess(cmdType, lparam, rparam);
					if (0 == ret){
						Toast.makeText(mContext, mContext.getResources().getText(R.string.close_lib_print_success), Toast.LENGTH_LONG).show();
					}else {
						Toast.makeText(mContext, mContext.getResources().getText(R.string.close_lib_print_fail), Toast.LENGTH_LONG).show();
					}
		
					libPrintDialog.hide();
				}	
					break;
				default:
					break;
				}
			}
		};
		
		okButton.setOnClickListener(onClickListener);
		cancleButton.setOnClickListener(onClickListener);
		
	}
}
