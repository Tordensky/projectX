package uit.nfc;

import org.apache.http.HttpRequest;

import android.os.Handler;

public class AsynchronousHttpClient {
	
	public void sendRequest(final HttpRequest request, ResponseListener callback) {
		(new AsynchronousSender(request, new Handler(), new CallbackWrapper(callback))).start();
	}
	
}
