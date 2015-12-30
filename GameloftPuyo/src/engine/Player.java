package engine;

import game.GameField;

public class Player {
	
	private int score;
	private GameField gameField;
	private boolean ready= false;
	
	public void updateScore(int bonusPoints) {
		score+=bonusPoints;
	}
	
	public int getScore() {
		return score;
	}
	
	public boolean dispatchTick() {
		if (!gameField.dispatchTick()) return false;
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
	boolean isReady() {
		return ready;
	}
		
}
