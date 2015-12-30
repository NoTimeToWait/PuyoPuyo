package engine;

import java.util.ArrayList;

/**
 * in this class we can implement logging and any anti-cheat measures 
 * as well as controlling Player connections, if for example
 * players connect to the game over the network
 * 
 * Before the game could start all players should connect to GameSession and be ready
 * @author Сергей Лазарев
 *
 */
public class GameSession {
	
	private long sessionId;
	
	private ArrayList<Player> players = new ArrayList<Player>();
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

}
