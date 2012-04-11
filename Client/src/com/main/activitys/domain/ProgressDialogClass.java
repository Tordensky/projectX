package com.main.activitys.domain;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Helper function class.
 */
public class ProgressDialogClass implements Runnable {
	public static ProgressDialog progDialog;
	private Context callingActivity;
	private String title;
	private String message;

	/**
	 * Constructor
	 */
	public ProgressDialogClass(Context c, String title, String message){
		this.callingActivity = c;
		this.title = title;
		this.message = message;
	}

	/**
	 * Dismiss progress dialog.
	 * @param progressDialog the progress dialog
	 */

	public static void dissMissProgressDialog(){
		progDialog.dismiss();
		progDialog = null;
	}

	/* 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		progDialog = ProgressDialog.show(callingActivity, 
				title, message,
				true);
	}
}
