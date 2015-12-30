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
	protected int color=UNKNOWN;
	
	public static int UNKNOWN = 0x00000000;
	public static int RED = 0x00FF0000;
	public static int GREEN = 0x0000FF00;
	public static int BLUE = 0x000000FF;
	public static int YELLOW = 0x00FFFF00;
	
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
