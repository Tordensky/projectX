/**
 * 
 */
package com.main.activitys.domain;

import java.util.ArrayList;

import com.main.activitys.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * @author Simen
 *
 */
public class GameAdapter extends ArrayAdapter<Game> {
		
	public GameAdapter(Context context, int textViewResourceId, ArrayList<Game> gameList) {
		super(context, textViewResourceId, gameList);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = 
					(LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.games_row, null);
		}
		
		Log.d("GAMEADAPTER", "inside getview");
		Game game = getItem(position);
		
		if(game != null){
      TextView username = (TextView) v.findViewById(R.id.gameRowUsername);
      TextView message = (TextView) v.findViewById(R.id.gameRowLastAction);
      
      if (username != null) {
      	Log.d("username", game.getOpponentsUsername().toString());
        username.setText(game.getOpponentsUsername().toString());
      }

      if(message != null) {
      	Log.d("lastaction", game.getLastAction());
        message.setText(game.getLastAction());
      }
		}
		return v;
	}
}
