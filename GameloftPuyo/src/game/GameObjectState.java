package game;

public enum GameObjectState {
	UNKNOWN, FIXED, CHAINED, FALLING_SLOW, FALLING_FAST;
	
	public boolean isFixed() {
		return equals(FIXED)||equals(CHAINED);
	}
}
