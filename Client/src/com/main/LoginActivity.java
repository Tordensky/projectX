/**
 * 
 */
package com.main;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

/**
 * The Class LoginActivity. Main class for logging in
 * to become a user. Logging means that other users 
 * can see that you are at the event.
 */
public class LoginActivity extends Activity {
	private static final int CHECK_LOGIN = 1;

	private EditText username;
	private static Handler msgHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		username = (EditText) findViewById(R.id.eTextLoginUsername);

		msgHandler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
				case CHECK_LOGIN:

				}
			}
		};
	}

	public void postLoginInfo(View v){

		/* Username length bigger than zero */
		if(username.getText().length() > 0){
			
			HelperFunctions.startProgressThread(LoginActivity.this, 
					msgHandler, 
					"Signing in", 
					"Verifying, please wait...");

		}

		else {
			new Alert("Error", 
					"Email and password field has to be filled out", 
					LoginActivity.this);
		}
	}
}
