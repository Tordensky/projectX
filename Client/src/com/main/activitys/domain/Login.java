/**
 * 
 */
package com.main.activitys.domain;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author Simen
 *
 */
public class Login {
	public static String PREFS_NAME = "loginInfo";

	
	public static void setLoggedIn(SharedPreferences loginSettings){
		Editor editor = loginSettings.edit();
		editor.putBoolean("loggedIn", true);
		editor.commit();
	}
	
	public static void setUserId(SharedPreferences loginSettings, int id){
		Editor editor = loginSettings.edit();
		editor.putInt("userId", id);
		editor.commit();
	}
	
	public static int getUserId(SharedPreferences loginSettings){
		return loginSettings.getInt("userId", 0);
	}
	
	public static String getUsername(SharedPreferences loginSettings){
		return loginSettings.getString("username", "null");
	}
	
	public static void setUsername(SharedPreferences loginSettings, String username){
		Editor editor = loginSettings.edit();
		editor.putString("username", username);
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
