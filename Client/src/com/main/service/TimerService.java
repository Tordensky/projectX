package com.main.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import uit.nfc.AsynchronousSender;
import uit.nfc.ResponseListener;
import com.main.activitys.domain.Login;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;


public class TimerService extends Service {

	private static final String TAG = "MyService";
	public static final String BROADCAST_ACTION = "com.main.activitys.displayUpdate";
	public static String URL = "URL";

	public String gamesUrl;

	public static final int INTERVAL = 10*1000;		// 10 seconds
	private ScheduledExecutorService executor;

	public ResponseListener responseListener;
	Intent callBackIntent;

	@Override
	public void onCreate() {
		super.onCreate();

		Log.d("SERVICE", "onCreate");

		SharedPreferences loginSettings = getSharedPreferences(Login.PREFS_NAME, 0);
		
		responseListener = new ResponseListener() {

			@Override
			public void onResponseReceived(HttpResponse response, String message) {
				Log.d("SERVICE - MESSAGE RESPONSE", message);
				callBackIntent.putExtra("data", message);
				sendBroadcast(callBackIntent);
			}
		};

		gamesUrl = gamesUrl + Login.getUsername(loginSettings);		// TODO get url from caller
		callBackIntent = new Intent(BROADCAST_ACTION);	

		executor = Executors.newSingleThreadScheduledExecutor();

		executor.scheduleAtFixedRate(new TimerTask() {
			public void run() {				
				pollServer();
			}
		}, 0, INTERVAL, TimeUnit.MILLISECONDS);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("SERVICE", "onDestroy");
		executor.shutdownNow();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		gamesUrl = intent.getExtras().getString(URL);
		Log.d("SERVICE", "onStart");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void pollServer(){
		Log.d("SERVICE", "Polling the server - url: " + gamesUrl);
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			Log.d("SERVICE", "done reque111st");

			HttpGet httpGet;
			httpGet = new HttpGet(new URI(gamesUrl));

			HttpResponse response;

			response = client.execute((HttpUriRequest)httpGet);
			String message = AsynchronousSender.readResponse(response);
			Log.d("Response", message);
			callBackIntent.putExtra("data", message);
			sendBroadcast(callBackIntent);
		}

		catch (ClientProtocolException e1) { e1.printStackTrace(); }
		catch (IOException e1) { e1.printStackTrace(); }
		catch (URISyntaxException e) { e.printStackTrace(); }

		//		try {
		//			Log.d("SERVICE", "done reque111st");
		//
		//			AsynchronousHttpClient a = new AsynchronousHttpClient();
		//			Log.d("SERVICE", "done request");
		//			Log.d("RESPONSELIST", responseListener.toString());
		//			a.sendRequest(httpGet, responseListener);
		//			
		//		}
		//		catch(URISyntaxException e) { e.printStackTrace(); }

	}
}