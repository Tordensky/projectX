package com.main.activitys.domain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Alert {
	public Alert(String title, String message, Context context){
		new AlertDialog.Builder(context)
   	.setIcon(android.R.drawable.ic_dialog_alert)
   	.setTitle(title)
   	.setMessage(message)
   	.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
     	@Override
     	public void onClick(DialogInterface dialog, int which){
     		dialog.cancel();
     	}
   	})
 	.show();
	}
}
