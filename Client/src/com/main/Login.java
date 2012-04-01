/**
 * 
 */
package com.main;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author Simen
 *
 */
public class Login {
	
	public static void setLoggedIn(SharedPreferences loginSettings){
		Editor editor = loginSettings.edit();
		editor.putBoolean("loggedIn", true);
		editor.commit();
	}
	
	public static void storePassword(SharedPreferences loginSettings, String pw){
		Editor editor = loginSettings.edit();
		editor.putString("password", pw);		// TODO crypt password
		editor.commit();
	}
	
	public static String getStoredPassword(SharedPreferences loginSettings){
		return loginSettings.getString("password", "null");
	}
	
	public static void storeSessionId(SharedPreferences loginSettings, String sessionId){
		Editor editor = loginSettings.edit();
		editor.putString("sessionId", sessionId);
		editor.commit();
	}
	
	public static String getSessionId(SharedPreferences loginSettings){
		return loginSettings.getString("sessionId", "null");
	}
}
