package engine;

import game.GameEvent;
import game.GameField;
import game.GameObject;
import visuals.FieldView;

/**
 * a class that represents player entity
 *
 */

public class Player implements NetworkPlayer {
	
	private String name;
	private int score;
	private GameField gameField;
	private boolean ready= false;
	
	
	public void updateScore(int bonusPoints) {
		score+=bonusPoints;
	}
	
	public int getScore() {
		return score;
	}
	/**
	 * get player puyos staged to spawn
	 * @return an array of game object types
	 */
	public int[] getPuyos() {
		return gameField.getNextTuple();
	}
	
	/**
	 * dispatches tick to a current player
	 * @param dispatcher a player who issued this command
	 */
	public boolean dispatchTick(NetworkPlayer dispatcher) {
		//we need to verify the player who issued this call (that he is actually the host)
		//and ensure that the receiver is ready to receive the call
		//right now it will end the game,
		//but later it is possible to add handling different situations in different ways
		if (!GameSession.isHost(dispatcher) || !isReady() || !gameField.dispatchTick()) return false;
		return true;
	}
	/**
	 * dispatch game event. 
	 * Some players and different game situations could generate events that should be distributed between player
	 * this method is a simple way of such distribution 
	 * as well as a way to deliever events from other threads (e.g. keyboard events)
	 */
	public boolean dispatchEvent(NetworkPlayer dispatcher, GameEvent event) {
		if (!(dispatcher==GameContext.getPlayer()||GameSession.isHost(dispatcher)) || !isReady() || !gameField.dispatchEvent(event)) return false;
		return true;
	}
	
	/**
	 * mark current player as ready for the game
	 */
	public void setReady() {
		gameField = new GameField(this);
		ready = true;
	}
	/**
	 * check if Player is ready
	 * @return false if player is not ready (not connected, not answering or they paused the game)
	 */
	public boolean isReady() {
		return ready;
	}
	
	@Override
	public String getName() {
		return name;
	}

	/**
	 * retrieve objects on the current player's game field
	 * @param allObjects signifies whether to retrieve all objects or only recently updated objects
	 * @return an array of game objects
	 */
	@Override
	public GameObject[] getGameObjects(boolean allObjects) {
		return gameField.getObjectsToDraw(allObjects);
	}
		
}
