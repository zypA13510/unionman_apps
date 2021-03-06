package com.um.ui;

import java.util.Arrays;
import com.um.dvtca.R;
import com.um.dvbstack.Ca;
import com.um.dvbstack.DVB;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.um.controller.AppBaseActivity;

public class Dvt_change_pin extends AppBaseActivity{
	private Button enter_btn;
	private EditText present_pin;
	private EditText new_pin;
	private EditText pin_verify;
	
	@Override
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dvt_change_pin);

		
		/*Pin EditText*/
		present_pin = (EditText)findViewById(R.id.editText1);
		present_pin.setText(present_pin.getText().toString());
		present_pin.setSelection(present_pin.getText().toString().length());
		
		new_pin = (EditText)findViewById(R.id.editText2);
		new_pin.setText(new_pin.getText().toString());
		new_pin.setSelection(new_pin.getText().toString().length());
		
		pin_verify = (EditText)findViewById(R.id.editText3);
		pin_verify.setText(pin_verify.getText().toString());
		pin_verify.setSelection(pin_verify.getText().toString().length());
		
		/*affirm button*/
		enter_btn = (Button)findViewById(R.id.button1);		
		
		enter_btn.setText(R.string.confirm_ok);		
		
		enter_btn.setOnClickListener(new Button.OnClickListener()
		{

			public void onClick(View arg0) {
				Ca ca = new Ca(DVB.getInstance());
				// TODO Auto-generated method stub
				
			    String newpinstr = String.valueOf(new_pin.getText());
				String oldpinstr = String.valueOf(present_pin.getText());
				String verifypinstr = String.valueOf(pin_verify.getText());
				
				byte[] newpintmp = newpinstr.getBytes();	
				byte[] oldpintmp = oldpinstr.getBytes();
				byte[] verifypintmp = verifypinstr.getBytes();
				
				 if((newpintmp.length != 8)||(oldpintmp.length != 8)||(verifypintmp.length != 8))
				 {
//					 new AlertDialog.Builder(Dvt_change_pin.this).setMessage(R.string.pin_len_err).setPositiveButton("ok", null).show();
                     Toast.makeText(Dvt_change_pin.this, getResources().getText(R.string.pin_len_err), Toast.LENGTH_LONG).show();
                     return;
				 }
				
				boolean same_flag = Arrays.equals(newpintmp, verifypintmp);	

				if(same_flag == false)//0x8000000d
				{
					Log.i("CA","CA new pin dismatch");	
//					new AlertDialog.Builder(Dvt_change_pin.this).setMessage(R.string.dvt_pin_not_paired).setPositiveButton("ok", null).show();
                    Toast.makeText(Dvt_change_pin.this, getResources().getText(R.string.dvt_pin_not_paired), Toast.LENGTH_LONG).show();
                    return;
				}
				else
				{
					if(getCardStatus() != true){
						new AlertDialog.Builder(Dvt_change_pin.this).setMessage(R.string.ca_insert_card).setPositiveButton("ok", null).show();
						return;
					}
					
					Log.i("CA","CA new pin match");
					int pinRet = ca.CaChangePin(newpintmp,oldpintmp,8);
					
					if(pinRet == 0) 
					{
						Log.i("CA","CA pin change success");
                        Toast.makeText(Dvt_change_pin.this, getResources().getText(R.string.dvt_change_pin_correctly), Toast.LENGTH_LONG).show();
//						new AlertDialog.Builder(Dvt_change_pin.this).setMessage(R.string.dvt_change_pin_correctly).setPositiveButton("ok", null).show();
					}
					else if (pinRet == 0x8000002d)
					{
						Log.i("CA","CA pin lock");
                        Toast.makeText(Dvt_change_pin.this, getResources().getText(R.string.pin_is_locked), Toast.LENGTH_LONG).show();
//						new AlertDialog.Builder(Dvt_change_pin.this).setMessage(R.string.pin_is_locked).setPositiveButton("ok", null).show();
					}
					else
					{
						Log.i("CA","CA old pin dismatch");
                        Toast.makeText(Dvt_change_pin.this, getResources().getText(R.string.dvt_pin_err), Toast.LENGTH_LONG).show();
//						new AlertDialog.Builder(Dvt_change_pin.this).setMessage(R.string.dvt_pin_err).setPositiveButton("ok", null).show();
					}
				}
			}
			}
		);

        setAutoFinish(Constant.AUTO_FINISH_WAIT_TIME_SHORT, null);
	}
	
	@Override
	protected void onPause(){
        super.onPause();
        finish();
	}
	
    private boolean getCardStatus(){
    	Ca ca = new Ca(DVB.getInstance());
    	boolean []cardStatus = new boolean[1];
    	
    	int ret = ca.CaGetCardStatus(cardStatus);
    	Log.d("Dvt_condition_access", "ret:" +ret);
    	Log.d("Dvt_condition_access", "cardStatus[0]:" +cardStatus[0]);
    	return cardStatus[0];
    }
    
}

