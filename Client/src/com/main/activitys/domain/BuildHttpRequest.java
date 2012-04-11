/**
 * 
 */
package com.main.activitys.domain;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

/**
 * @author Simen
 *
 */
public class BuildHttpRequest {

	public static HttpPost setEntity(JSONObject postBody, String url){
		HttpPost httpPost = null;
		
		try {
  		httpPost = new HttpPost(new URI(url));
  
  		StringEntity se = new StringEntity(postBody.toString());
  		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
  
  		httpPost.setEntity(se);
		}
		
		catch (URISyntaxException e) { e.printStackTrace(); }
		catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		
		return httpPost;
	}
}
