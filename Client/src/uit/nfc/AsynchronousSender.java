package uit.nfc;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.Handler;
import android.util.Log;
 
public class AsynchronousSender extends Thread {
 
	private static final DefaultHttpClient httpClient =
		new DefaultHttpClient();
 
	private HttpRequest request;
	private Handler handler;
	private CallbackWrapper wrapper;
 
	protected AsynchronousSender(HttpRequest request,
			Handler handler, CallbackWrapper wrapper) {
		this.request = request;
		this.handler = handler;
		this.wrapper = wrapper;
	}
 
	public void run() {
		try {
			Log.d("HTTPCLIENT", "LALALALALA!");

			final HttpResponse response;
			synchronized (httpClient) {
				response = getClient().execute((HttpUriRequest) request);
			}
			String message = readResponse(response);
			wrapper.setResponse(response, message);
			handler.post(wrapper);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
	private HttpClient getClient() {
		return httpClient;
	}
	
	public static String readResponse(HttpResponse response) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String returnValue = "";
			String line = "";
			while ((line = br.readLine()) != null) {
				returnValue += line;
			}
			br.close();
			
			return returnValue;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
 
}