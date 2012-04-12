/**
 * 
 */
package com.main.activitys;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import com.main.activitys.domain.Login;
import com.main.activitys.domain.ProgressDialogClass;

import uit.nfc.AsynchronousHttpClient;
import uit.nfc.ResponseListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * @author Simen
 *
 */
public class GameActivity extends Activity {
	
	public static String SELECTED_GAME_USERNAME_OPPONENT = "selectedGameUsername";
	public static String SELECTED_GAME_ID = "selectedGameId";
	
	public String gameUrl;
	

	private ResponseListener responseListener;
	public SharedPreferences loginSettings;
	private ProgressDialogClass progDialog;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		
		Bundle extras = getIntent().getExtras();
		String gameId = extras.getString(SELECTED_GAME_ID);
		
		gameUrl = "http://restserver.herokuapp.com/game/" + gameId;
		
		loginSettings = getSharedPreferences(Login.PREFS_NAME, 0);

		Log.d("gameid", gameId);
		
		
		responseListener = new ResponseListener() {

			@Override
			public void onResponseReceived(HttpResponse response, String message) {
				Log.i("Response", response.toString());
				ProgressDialogClass.dissMissProgressDialog();
				confirmPlay(message);
			}
		};
		
		
	}
	
	public void playMove(View v){
		
		// fast and furious code! test
		HttpPost httpPost = null;
		
		try {
			httpPost = new HttpPost(new URI(gameUrl));
		}
		catch (URISyntaxException e1) {
			e1.printStackTrace();
		}
		
		JSONObject jobj = new JSONObject();
		
		try {
			jobj.put("userId", Login.getUserId(loginSettings));
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		
		StringEntity se = null;
		
		try {
			se = new StringEntity(jobj.toString());
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

		httpPost.setEntity(se);
		
		
		AsynchronousHttpClient a = new AsynchronousHttpClient();
		a.sendRequest(httpPost, responseListener);
		
		progDialog = new 
				ProgressDialogClass(this, 
						"Validating", 
						"Validating your move, please wait a moment...");

		progDialog.run();
	}
	
	public void confirmPlay(String msg){
		finish();
	}
}
