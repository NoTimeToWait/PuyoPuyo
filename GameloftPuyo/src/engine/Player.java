package engine;

import game.GameEvent;
import game.GameField;
import game.GameObject;
import visuals.FieldView;

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
	
	public int[] getPuyos() {
		return gameField.getNextTuple();
	}
	
	public boolean dispatchTick(NetworkPlayer dispatcher) {
		//we need to verify the player who issued this call (that he is actually the host)
		//and ensure that the receiver is ready to receive the call
		//right now it will end the game,
		//but later it is possible to add handling different situations in different ways
		if (!GameSession.isHost(dispatcher) || !isReady() || !gameField.dispatchTick()) return false;
		return true;
	}
	
	public boolean dispatchEvent(NetworkPlayer dispatcher, GameEvent event) {
		if (!(dispatcher==GameContext.getPlayer()||GameSession.isHost(dispatcher)) || !isReady() || !gameField.dispatchEvent(event)) return false;
		return true;
	}
	
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

	@Override
	public GameObject[] getGameObjects(boolean allObjects) {
		return gameField.getObjectsToDraw(allObjects);
	}
		
}
