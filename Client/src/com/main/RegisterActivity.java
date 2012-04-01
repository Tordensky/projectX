package com.main;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import uit.nfc.AsynchronousHttpClient;
import uit.nfc.ResponseListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * The Class RegisterActivity. User registration
 * 
 */
public class RegisterActivity extends Activity{
	
	private String registerUrl = "http://restserver.herokuapp.com/user/new";
	private String PREFS_NAME = "loginInfo";
	protected String password;
	
	private EditText email;
	private EditText username;
	
	private ResponseListener responseListener;
	public SharedPreferences loginSettings;
	private ProgressDialogClass progDialog;

	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		email = (EditText) findViewById(R.id.eTextRegEmail);
		username = (EditText) findViewById(R.id.eTextRegUsername);
		
		loginSettings = getSharedPreferences(PREFS_NAME, 0);
		
		/* Redirect to all games activity if signed in */
		if(loginSettings.getBoolean("loggedIn", false))
			startAllGamesActivity(this);
				
		responseListener = new ResponseListener() {
			
			@Override
			public void onResponseReceived(HttpResponse response, String message) {
				Log.i("Response", response.toString());
				ProgressDialogClass.dissMissProgressDialog();
				confirmRegistration(message);
			}
		};
	}
	
	/**
	 * Confirms that the registration went OK
	 * @param responseBody
	 */
	
	public void confirmRegistration(String responseBody){
		Log.i("ConfirmRegistartion", responseBody);
		
		JSONObject response = null;
		
		try {
			response = new JSONObject(responseBody);
		}
		catch (JSONException e) { e.printStackTrace(); }
		
		if(response.has("id")){
			Login.setLoggedIn(loginSettings);
			Login.storePassword(loginSettings, password);
			startAllGamesActivity(this);
		}
		else{
			if((response.has("username")) && (response.has("email")))
				new Alert("Error", "Email and username already taken", this);
			else if(response.has("username"))
				new Alert("Error", "Username already taken", this);
			else if(response.has("email"))
				new Alert("Error", "Email already taken", this);
			else
				new Alert("Something went wrong", "Check your internet connection and try again", this);
		}
	}
	
	public void toLoginActivity(View v){
		Intent toLogin = new Intent().setClass(this, LoginActivity.class);
		startActivity(toLogin);
	}
	
	/**
	 * Starts the all games activity
	 *
	 */
	public void startAllGamesActivity(Context c){
		Intent allGamesActivity = new Intent().setClass(c, AllGamesActivity.class);
		startActivity(allGamesActivity);
		finish();			// can't return to this activity when signed in
	}
	
	
	/**
	 * Called when a user press the register button
	 * @param v
	 */
	
	public void postRegisterInfo(View v){
		
		JSONObject registerInfo = new JSONObject();
		HttpPost httpPost = null;
		
		try {
			httpPost = new HttpPost(new URI(registerUrl));
		} catch (URISyntaxException e1) { e1.printStackTrace(); }
		
		if((username.getText().length() > 0) && (email.getText().length() > 0)){
			try {
				registerInfo.put("Username", username.getText());
				registerInfo.put("Email", email.getText());

				password = UUID.randomUUID().toString();		// random generated password
				registerInfo.put("Password", password);

				StringEntity se = new StringEntity(registerInfo.toString());
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

				httpPost.setEntity(se);
				
				progDialog = new 
						ProgressDialogClass(this, 
								"Signing in", 
								"Creating user, please wait...");

				progDialog.run();
			}

			catch (JSONException e) { e.printStackTrace(); }
			catch (UnsupportedEncodingException e) { e.printStackTrace(); }

			AsynchronousHttpClient a = new AsynchronousHttpClient();
			a.sendRequest(httpPost, responseListener);
		}
		
		else
			new Alert("Warning", "Both email and username has to be filled out", this);
	}
}
