/**
 * 
 */
package com.main.domain;

/**
 * @author Simen
 * Game object that holds information about the game
 */
public class Game {
	
	private int gameId;
	private String userNameOpponent;
	private String lastAction;
	
	public int getGameId(){
		return gameId;
	}
	
	public void setGameId(int id){
		this.gameId = id;
	}
	
	public String getOpponentUsernamename(){
		return userNameOpponent;
	}
	
	public void setOpponentUsername(String username){
		this.userNameOpponent = username;
	}
	
	public String getLastAction(){
		return this.lastAction;
	}
	
	public void setLastAction(String lastAction){
		this.lastAction = lastAction;
	}
}
