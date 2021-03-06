package engine;

import game.GameEvent;
import game.GameObject;

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
	
	public GameObject[] getGameObjects(boolean allObjects);
		
}
