package engine;

import java.util.ArrayList;

/**
 * in this class we could implement logging and any anti-cheat measures 
 * as well as controlling Player connections, if for example
 * players connect to the game over the network
 * 
 * Before the game could start all players should connect to GameSession and be ready
 *
 * each game must be represented by different GameSession
 */
public class GameSession {
	//collision is improbable in single player mode
	//TODO:in multiplayer mode is quite improbable, though this issue should be thoroughly considered
	private static final long sessionId = System.currentTimeMillis();
	private boolean closed = false;
	
	public static final long getSessionId() {
		return sessionId;
	}
	
	private ArrayList<NetworkPlayer> players = new ArrayList<NetworkPlayer>();
	
	public void addPlayer(NetworkPlayer player) {
		players.add(player);
	}
	
	public ArrayList<NetworkPlayer> getPlayers() {
		return players;
	}
	
	/**
	 * check player permissions
	 * @param player player, whose credentials are being verified
	 * @return true if player is host
	 */
	public static boolean isHost(NetworkPlayer player) {
		return true;
	}
	
	/**
	 * verifies whether game session was closed 
	 * 
	 */
	public boolean isClosed() {
		return closed;
	}
	
	/**
	 * explicit way to close GameSession
	 * @param player a player who issued an end of the GameSession
	 */
	public void close(NetworkPlayer player) {
		closed = true;
	}

}
