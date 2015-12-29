package game;

import engine.Options;

public class GameObject {
	protected int id;
	protected GameObjectState state;
	protected int x;
	protected int y;
	/**
	 * color attribute which is 0 for any non-Puyo object
	 */
	protected int color=0;
	public GameObject() {
		//this.id = 
		state = GameObjectState.UNKNOWN;
	}
	
	public void setCoordinates(int column, int line) {
		x = column; y = line*Options.FALL_ITERATIONS_COUNT;
	}
	
	public int getColumn() {
		return x;
	}
	
	public int getLine() {
		return y/Options.FALL_ITERATIONS_COUNT;
	}
	
	public GameObjectState getState() { 
		return state;
	}
	
	public void setState(GameObjectState state) {
		this.state = state;
	}
	
	public int getColor() {
		return color;
	}
}
