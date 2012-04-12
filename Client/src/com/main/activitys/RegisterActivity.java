package com.main.activitys;

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
import com.main.activitys.domain.Alert;
import com.main.activitys.domain.Login;
import com.main.activitys.domain.ProgressDialogClass;
import uit.nfc.AsynchronousHttpClient;
import uit.nfc.ResponseListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * The Class RegisterActivity. User registration
 * 
 */
public class RegisterActivity extends Activity {

	private String registerUrl = "http://restserver.herokuapp.com/user/new";
	protected String password;
	private String username;

	private EditText editEmail;
	private EditText editUsername;

	private ResponseListener responseListener;
	public SharedPreferences loginSettings;
	public ProgressDialogClass progDialog;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);

		editEmail = (EditText) findViewById(R.id.eTextRegEmail);
		editUsername = (EditText) findViewById(R.id.eTextRegUsername);

		loginSettings = getSharedPreferences(Login.PREFS_NAME, 0);

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

			if(response.has("id")){

				Login.setLoggedIn(loginSettings);
				Login.setUserId(loginSettings, response.getInt("id"));
				Login.setUsername(loginSettings, username);
				Login.storePassword(loginSettings, password);

				startAllGamesActivity(this);
			}
			else {
//				if((response.has("username")) && (response.has("email")))
					new Alert("Email or username taken", "The email or username you typed in is already occupied", this);
//				else if(response.has("username"))
//					new Alert("Error", "Username already taken", this);
//				else if(response.has("email"))
//					new Alert("Error", "Email already taken", this);
//				else
//					new Alert("Something went wrong", "Check your internet connection and try again", this);
			}
		}
		catch(Exception e) { 
			new Alert("Email or username taken", "The email or username you typed in is already occupied", this);
			e.printStackTrace(); 
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

		if((editUsername.getText().length() > 0) && (editEmail.getText().length() > 0)){
			try {				
				if(!isValidEmail(editEmail.getText().toString())){
					new Alert("Email not valid", "The email you typed in is not valid. Please type in a valid email address", this);
					return;
				}
				
				username = editUsername.getText().toString().trim();
				
				if(username.contains(" ")){
					new Alert("Username not valid", "The username you typed in contatins whitespaces Please remove them and try again", this);
					return;
				}
				
				registerInfo.put("Username", username);
				registerInfo.put("Email", editEmail.getText());

				password = Secure.getString(getBaseContext().getContentResolver(),
            Secure.ANDROID_ID);
				
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
	
	public final static boolean isValidEmail(CharSequence target) {
    try {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    catch( NullPointerException exception ) {
        return false;
    }
}
}
