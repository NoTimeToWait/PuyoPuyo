package game;

import java.util.Random;

import engine.GameContext;
import engine.GameSession;
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
	
	private static Random idGenerator = new Random(GameSession.getSessionId() + GameContext.getPlayer().hashCode());
	
	public GameObject() {
		this.id = idGenerator.nextInt();
		state = GameObjectState.UNKNOWN;
	}
	
	protected void setCoordinates(int column, int line) {
		x = column; y = line;
	}
			
	public int getDrawX() {
		return x*Options.CELL_WIDTH;
	}
	
	public int getDrawY() {
		return y*Options.CELL_WIDTH;
	}
	
	public int getColumn() {
		return x;
	}
	
	public int getLine() {
		return y;
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass())
			return false;
		GameObject other = (GameObject) obj;
		if (id!=other.id)	return false;
		return true;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(id);
	}
}
