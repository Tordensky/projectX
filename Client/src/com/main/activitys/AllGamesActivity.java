/**
 * 
 */
package com.main.activitys;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.main.activitys.domain.Game;
import com.main.activitys.domain.GameAdapter;
import com.main.activitys.domain.Login;
import com.main.activitys.domain.SeparatedListAdapter;
import com.main.service.TimerService;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;


public class AllGamesActivity extends ListActivity {

	private static final String TAG = "BroadcastTest";
	private Intent serviceintent;
	private SharedPreferences loginSettings;
	
	public static int RANDOM_GAMEREQ_RESP = 1;

	private SeparatedListAdapter adapter; 
	ArrayList<Game> mineTurnList;
	ArrayList<Game> opponentTurnList;
	ArrayList<Game> finishedGamesList;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_games);
		
		loginSettings = getSharedPreferences(Login.PREFS_NAME, 0);

		mineTurnList = new ArrayList<Game>();
		opponentTurnList = new ArrayList<Game>();
		finishedGamesList = new ArrayList<Game>();

    // create our list and custom adapter  
    adapter = new SeparatedListAdapter(this);  
    
    adapter.addSection("Your turn",new GameAdapter (this, android.R.layout.activity_list_item, mineTurnList));  
    adapter.addSection("Opponents turn", new GameAdapter (this, android.R.layout.activity_list_item, opponentTurnList)); 
    adapter.addSection("Finished games", new GameAdapter (this, android.R.layout.activity_list_item, finishedGamesList));
		
		setListAdapter(adapter);
		serviceintent = new Intent("com.main.service.TimerService");
		serviceintent.putExtra(TimerService.URL, "http://restserver.herokuapp.com/games/" + Login.getUsername(loginSettings));
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("OnResume", "are youuuuuuu here");

		startService(serviceintent);
		registerReceiver(broadcastReceiver, new IntentFilter(TimerService.BROADCAST_ACTION));
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(broadcastReceiver);
		stopService(serviceintent);
	}

	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);

		Intent gameIntent = new Intent(this, GameActivity.class);
		Log.d("adapter", adapter.getItem(position).toString());
		Game game = (Game) adapter.getItem(position);
		gameIntent.putExtra(GameActivity.SELECTED_GAME_ID, game.getGameId());

		startActivity(gameIntent);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    Log.i("activityresult", "reeesponse");
	    if(resultCode == RANDOM_GAMEREQ_RESP){
	    	Game game = new Game();
	    	game.setGameId("0");
	    	game.setOpponentsUsername("Searching for opponent..");
	    	game.setLastAction("");
				opponentTurnList.add(game);
	    }
	}

	public void startNewGame(View v){
		Intent newGameIntent = new Intent(this, NewGameActivity.class);
		startActivityForResult(newGameIntent, RANDOM_GAMEREQ_RESP);
	}


	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			updateUI(intent);
		}
	};


	private void updateUI(Intent intent) {
		try {
			Log.d("UPDATEUI", intent.getExtras().getString("data"));
			JSONArray jarray = new JSONArray((String)intent.getExtras().getString("data"));

			for(int counter = 0; counter < jarray.length(); counter ++){
				createGameObjAndAddToView((JSONObject) jarray.get(counter));
			}
			adapter.notifyDataSetChanged();
		}

		catch (JSONException e) { e.printStackTrace(); }
	}

	private void createGameObjAndAddToView(JSONObject obj){
		Game game = new Game();

		try {
			String gameId = obj.getString("gid");
			long playersTurn = obj.getLong("playersTurn");
			int userId = Login.getUserId(loginSettings);
			
			game.setGameId(gameId);
			game.setLastAction(obj.getString("action"));
			setOpponents(game, obj);
			
			
			// If it is the users turn for this game. Update mineTurnList 
			// and remove from opponentsTurnList if exists
			
			Log.d("playersTurn", obj.getString("playersTurn"));
			Log.d("userId", Integer.toString(Login.getUserId(loginSettings)));
			
			addToList(game, playersTurn, userId, gameId);
			
		}

		catch (JSONException e) { e.printStackTrace(); }
	}
	
	private void addToList(Game game, long playersTurn, int userId, String gameId){
		int pos;
		
		if(playersTurn == userId){
			Log.d("MYTURN", "ITS MY TURN!");
			if((pos = findPosInListFromGameId(opponentTurnList, gameId)) >= 0){
				opponentTurnList.remove(pos);
			}
			
			if((pos = findPosInListFromGameId(mineTurnList, gameId)) >= 0)
				mineTurnList.set(pos, game);
			else
				mineTurnList.add(game);
			
			return;
		}
		
		// If it is not the users turn for this game. Update opponentsTurnList
		// and remove from mineTurnList if exists
		
		Log.d("NOT MY TURN", "ITS NOOOOOOOT MY TURN!");

		if((pos = findPosInListFromGameId(mineTurnList, gameId)) >= 0){
			mineTurnList.remove(pos);
		}
		
		if((pos = findPosInListFromGameId(opponentTurnList, gameId)) >= 0)
			opponentTurnList.set(pos, game);
		
		else 
			opponentTurnList.add(game);
	
	}
	
	private void setOpponents(Game game, JSONObject obj) throws JSONException{
		JSONArray opponents = obj.getJSONArray("opponents");

		for(int each = 0; each < opponents.length(); each++){
			game.setOpponentsUsername(opponents.getString(each));
		}

	}
	
	private int findPosInListFromGameId(ArrayList<Game> gamesList, String gameId){
		
		Game gameObjInList;
	
		for(int iter = 0; iter < gamesList.size(); iter++){
			gameObjInList = gamesList.get(iter);
			
			Log.d("inList gameID", gameObjInList.getGameId());
			Log.d("fromServer gameID", gameId);
			
			if(gameId.equals(gameObjInList.getGameId())){
				return iter;
			}
		}
		return -1;
	}
}