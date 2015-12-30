package engine;

public class Player {
	
	private int score;
	
	public void updateScore(int bonusPoints) {
		score+=bonusPoints;
	}
	
	public int getScore() {
		return score;
	}
		
}
