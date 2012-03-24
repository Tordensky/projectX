package com.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

/**
 * Helper function class.
 */
public class HelperFunctions {
	static ProgressDialog progDialog;

	/**
	 * Start "signing in" progress thread.
	 */
	public static void startProgressThread(Context c, final Handler handler, String title, String message){
		progDialog = ProgressDialog.show(
				c, 
				title, message,
				true);
		
		new Thread() {
			public void run() {
				try{
					sleep(7000);	// sleep some until progress is done
				} 
				catch (Exception e) { e.printStackTrace(); }
				handler.sendEmptyMessage(10);
			}
		}.start();
	}

	/**
	 * Dismiss progress dialog.
	 * @param progressDialog the progress dialog
	 */
	public static void dissMissProgressDialog(){
		progDialog.dismiss();
		progDialog = null;
	}
}
