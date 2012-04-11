/**
 * 
 */
package com.main.activitys.domain;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author Simen
 * Game object that holds information about the game
 */
public class Game {
	
	private String gameId;
	private ArrayList<String> userNameOpponent;
	private String lastAction;
	private Date timeSinceLastMove;
	
	
	public Game(){
		this.userNameOpponent = new ArrayList<String>();
	}
	
	public String getGameId(){
		return gameId;
	}
	
	public void setGameId(String string){
		this.gameId = string;
	}
	
	public ArrayList<String> getOpponentsUsername(){
		return userNameOpponent;
	}
	
	public void setOpponentsUsername(String username){
		this.userNameOpponent.add(username);
	}
	
	public String getLastAction(){
		return this.lastAction;
	}
	
	public void setLastAction(String lastAction){
		this.lastAction = lastAction;
	}
	
	public void setTimeSinceLastMove(Date date){
		this.timeSinceLastMove = date;
	}
	
	public Date getTimeSinceLastMove(){
		return this.timeSinceLastMove;
	}
	
}
