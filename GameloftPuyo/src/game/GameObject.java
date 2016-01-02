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
	protected int type=UNKNOWN_TYPE_MASK;
	
	public static final int UNKNOWN_TYPE_MASK = 0x00000000;
	public static final int PUYO_TYPE_MASK = 0x01000000;
	
	public GameObject() {
		//this.id = 
		state = GameObjectState.UNKNOWN;
	}
	
	protected void setCoordinates(int column, int line) {
		x = column; y = line*Options.FALL_ITERATIONS_COUNT;
	}
	
	protected void drop() {
		y++;
		if (state.equals(GameObjectState.FALLING_FAST) && Options.FALL_ITERATIONS_COUNT>1)
			setCoordinates(getColumn(), getLine()+1);
	}
	
	protected int getFallIteration(){
		return y;
	}
	
	public int getX() {
		return x*Options.CELL_WIDTH;
	}
	
	public int getY() {
		return y*Options.CELL_WIDTH/Options.FALL_ITERATIONS_COUNT;
	}
	
	public int getColumn() {
		return x;
	}
	
	public int getLine() {
		//return y/Options.FALL_ITERATIONS_COUNT;
		return (int)Math.ceil((double)getFallIteration()/Options.FALL_ITERATIONS_COUNT);
	}
	
	public GameObjectState getState() { 
		return state;
	}
	
	protected void setState(GameObjectState state) {
		this.state = state;
	}
	
	public int getType() {
		return type;
	}
}
