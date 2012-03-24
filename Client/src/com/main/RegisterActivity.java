package com.main;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

/**
 * The Class RegisterActivity. User registration
 * 
 */
public class RegisterActivity extends Activity{
	private static final int CHECK_REG = 1;
	private EditText email;
	private EditText password;
	private EditText username;
	private static Handler msgHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		email = (EditText) findViewById(R.id.eTextRegEmail);
		username = (EditText) findViewById(R.id.eTextRegUsername);

		msgHandler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
				case CHECK_REG:
					String responseBody = (String)msg.obj;
					confirmRegistration(responseBody);
					break;
				}
			}
		};
	}
	
	/**
	 * Confirms that the registration went OK
	 * @param responseBody
	 */
	public void confirmRegistration(String responseBody){
		
	}
	
	/**
	 * Called when a user press the register button
	 * @param v
	 */
	public void postRegisterInfo(View v){
		// do something with email.getText() and username.getText(). 
		// Validate with Reggex expression?

	}
}
