/**
 * 
 */
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

import uit.nfc.AsynchronousHttpClient;
import uit.nfc.ResponseListener;

import com.main.activitys.domain.Alert;
import com.main.activitys.domain.Login;
import com.main.activitys.domain.ProgressDialogClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * @author Simen
 *
 */
public class NewGameActivity extends Activity {
	public static String randomUrl = "http://restserver.herokuapp.com/game/new_random";
	
	private ResponseListener responseListener;
	public SharedPreferences loginSettings;
	private ProgressDialogClass progDialog;
	
	
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
				evaluateResponse(message);
			}
		};
	}
	
	private void evaluateResponse(String message){
		if(message.length() > 0){
			Log.i("evaluateResponse", message);
		}
	}

	public void findOpponentByEmail(View v){

	}	
	
	public void findOpponentByUsername(View v){

	}
	
	public void findOpponentRandom(View v){
		try {
			HttpPost httpPost = new HttpPost(new URI(randomUrl));
			
			JSONObject json = new JSONObject();
			json.put("userId", Login.getUserId(loginSettings));
			
			StringEntity se = new StringEntity(json.toString());
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

			httpPost.setEntity(se);
			AsynchronousHttpClient a = new AsynchronousHttpClient();
			a.sendRequest(httpPost, responseListener);
			
			alert("Finding you an opponent", "Searching for an opponent to play against. This can take a while...", this);
		}
		
		catch (URISyntaxException e) { e.printStackTrace(); }
		catch (JSONException e) { e.printStackTrace(); }
		catch (UnsupportedEncodingException e) { e.printStackTrace(); }
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
