package engine;

import game.GameEvent;
import game.GameObject;

/**
 *  an interface to represent a player with as few data fields as possible to restrict data access
 *  this interface provides information about current player to other players
 *	other players will see only this interface, which is why it should be more secure
 */
public interface NetworkPlayer {
		
	public String getName();
	public int getScore();
	public boolean dispatchTick(NetworkPlayer dispatcher);
	public boolean dispatchEvent(NetworkPlayer dispatcher, GameEvent event);
	/**
	 * check if Player is ready
	 * @return false if player is not ready (not connected, not answering or they have paused the game)
	 */
	public boolean isReady();
	
	/**
	 * retrieve objects on the current player's game field
	 * @param allObjects signifies whether to retrieve all objects or only recently updated objects
	 * @return an array of game objects
	 */
	public GameObject[] getGameObjects(boolean allObjects);
		
}
