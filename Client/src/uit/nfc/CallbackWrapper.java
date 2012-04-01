package uit.nfc;

import org.apache.http.HttpResponse;
 
public class CallbackWrapper implements Runnable {
 
	private ResponseListener callbackActivity;
	private HttpResponse response;
	private String message;
 
	public CallbackWrapper(ResponseListener callbackActivity) {
		this.callbackActivity = callbackActivity;
	}
 
	public void run() {
		callbackActivity.onResponseReceived(response, message);
	}
 
	public void setResponse(HttpResponse response, String message) {
		this.response = response;
		this.message = message;
	}
}