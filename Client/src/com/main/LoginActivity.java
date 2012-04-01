/**
 * 
 */
package com.main;


import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * The Class LoginActivity. Main class for logging in
 * to become a user. Logging means that other users 
 * can see that you are at the event.
 */
public class LoginActivity extends Activity {

	private String PREFS_NAME = "loginInfo";
	private String registerUrl = "http://restserver.herokuapp.com/user/new";

	private EditText username;
	private ProgressDialogClass progDialog;

	private ResponseListener responseListener;
	public SharedPreferences loginSettings;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		username = (EditText) findViewById(R.id.eTextLoginUsername);
		loginSettings = getSharedPreferences(PREFS_NAME, 0);
		
		responseListener = new ResponseListener() {

			@Override
			public void onResponseReceived(HttpResponse response, String message) {
				Log.i("Response", response.toString());
				ProgressDialogClass.dissMissProgressDialog();
				confirmLogin(message);
			}
		};
	}

	public void postLoginInfo(View v){

		/* Username length bigger than zero */
		if(username.getText().length() > 0){

			progDialog = new 
					ProgressDialogClass(this, 
							"Signing in", 
							"Verifying, please wait...");

			progDialog.run();
			
			HttpPost httpPost = null;
			
			try {

				JSONObject registerInfo = new JSONObject();

				httpPost = new HttpPost(new URI(registerUrl));
				registerInfo.put("Username", username.getText());
				
				registerInfo.put("Password", Login.getStoredPassword(loginSettings));

				StringEntity se = new StringEntity(registerInfo.toString());
				se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

				httpPost.setEntity(se);
			}

			catch (URISyntaxException e) { e.printStackTrace(); }
			catch (JSONException e) { e.printStackTrace(); }
			catch (UnsupportedEncodingException e) { e.printStackTrace(); }

			AsynchronousHttpClient a = new AsynchronousHttpClient();
			a.sendRequest(httpPost, responseListener);
			
		}

		else {
			new Alert("Error", 
					"The username field has to be filled out", 
					LoginActivity.this);
		}
	}

	public void confirmLogin(String responseBody){
		Log.i("ConfirmLogin", responseBody);
		
		JSONObject response = null;
		
		try {
			response = new JSONObject(responseBody);

		
			if(response.has("id")){
				Login.setLoggedIn(loginSettings);

				Intent allGamesActivity = new Intent().setClass(this, AllGamesActivity.class);
				startActivity(allGamesActivity);
				finish();			// can't return to this activity when signed in
			}
		}
		catch (JSONException e) { e.printStackTrace(); }
		
		new Alert("Error", "Couldn't find the username you typed in", this);
	}
}