/**
 * 
 */
package com.main.activitys;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;
import uit.nfc.AsynchronousHttpClient;
import uit.nfc.ResponseListener;
import com.main.activitys.domain.BuildHttpRequest;
import com.main.activitys.domain.Login;
import com.main.activitys.domain.ProgressDialogClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

/**
 * @author Simen
 *
 */
public class NewGameActivity extends Activity {
	public static String randomUrl = "http://restserver.herokuapp.com/game/new_random";
	public static String usernameUrl = "http://restserver.herokuapp.com/game/new_username";
	public static String emailUrl = "http://restserver.herokuapp.com/game/new_email";

	private String username;
	private String email;

	private ResponseListener responseListener;
	public SharedPreferences loginSettings;

	public ProgressDialogClass progDialog;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_game);

		//		Bundle extras = getIntent().getExtras();
		//		String gameId = extras.getString();

		loginSettings = getSharedPreferences(Login.PREFS_NAME, 0);

		responseListener = new ResponseListener() {

			@Override
			public void onResponseReceived(HttpResponse response, String message) {
				Log.i("Response", response.toString());
				ProgressDialogClass.dissMissProgressDialog();
				evaluateResponse(message);
			}
		};
	}

	private void evaluateResponse(String message){
		Log.i("evaluateResponse", message);

		try {
			JSONObject response = new JSONObject(message);

			if(response.has("error") && response.has("username")){
				EditText editText = new EditText(this);
				editText.setText(username);
				specialAlert("Create game", response.getString("error"), this, editText, true);
			}
			
			else if(response.has("error") && response.has("email")){
				EditText editText = new EditText(this);
				editText.setText(email);
				specialAlert("Create game", response.getString("error"), this, editText, false);
			}
			
			else if(response.has("warning")){
				Intent i = new Intent();

				Log.i("evaluateResponse", message);
				setResult(AllGamesActivity.RANDOM_GAMEREQ_RESP, i);
				finish();
			}
			else{
				finish();
			}
		}

		catch (JSONException e) { e.printStackTrace(); }

	}

	public void findOpponentByEmail(View v){
		specialAlert("Create game", "Enter your opponents email address", this, new EditText(this), false);
	}	

	public void findOpponentByUsername(View v){
		specialAlert("Create game", "Enter your opponents username", this, new EditText(this), true);
	}

	public void findOpponentRandom(View v){
		JSONObject postBody = new JSONObject();

		try {
			postBody.put("userId", Login.getUserId(loginSettings));
		}
		catch (JSONException e) { e.printStackTrace(); }

		HttpPost httpPost = BuildHttpRequest.setEntity(postBody, randomUrl);

		AsynchronousHttpClient a = new AsynchronousHttpClient();
		a.sendRequest(httpPost, responseListener);

		progDialog = new 
				ProgressDialogClass(NewGameActivity.this, 
						"Creating", 
						"Wait a moment, confirming game settings..");

		progDialog.run();

		//alert("Finding you an opponent", "Searching for an opponent to play against. This can take a while...", this);
	}

	private void sendEmail(String eMail){
		email = eMail;
		JSONObject postBody = new JSONObject();

		try {
			postBody.put("userId", Login.getUserId(loginSettings));
			postBody.put("email", eMail);
		}

		catch (JSONException e) { e.printStackTrace(); }
		
		HttpPost httpPost = BuildHttpRequest.setEntity(postBody, emailUrl);

		AsynchronousHttpClient a = new AsynchronousHttpClient();
		a.sendRequest(httpPost, responseListener);
	}
	
	private void sendUsername(String us){
		username = us;
		JSONObject postBody = new JSONObject();

		try {
			postBody.put("userId", Login.getUserId(loginSettings));
			postBody.put("opnUsername", us);
		}

		catch (JSONException e) { e.printStackTrace(); }
		
		HttpPost httpPost = BuildHttpRequest.setEntity(postBody, usernameUrl);

		AsynchronousHttpClient a = new AsynchronousHttpClient();
		a.sendRequest(httpPost, responseListener);
	}

	private void specialAlert(String title, String message, Context context, final EditText input, final boolean username){

		new AlertDialog.Builder(this)
		.setTitle(title)
		.setMessage(message)
		.setView(input)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				Editable value = input.getText();
				if(username)
					sendUsername(value.toString());
				else
					sendEmail(value.toString());
				
				progDialog = new 
						ProgressDialogClass(NewGameActivity.this, 
								"Signing in", 
								"Creating user, please wait...");

				progDialog.run();
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Do nothing.
			}
		}).show();
	}

	private void alert(String title, String message, Context context){
		new AlertDialog.Builder(context)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle(title)
		.setMessage(message)
		.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.cancel();
				finish();
			}
		})
		.show();
	}
}
