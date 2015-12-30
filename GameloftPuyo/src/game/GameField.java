package game;

import java.util.ArrayList;

import engine.Options;
import engine.Player;

public class GameField {
	private Player player;
	private int width;
	private int height;
	private int tickCount;
	/**
	 * game field represented as an array of cells
	 */
	private FieldCell[][] cells;
	/**
	 * area where all puyos are prepared to drop onto the game field
	 */
	private FieldCell[][] stagingArea; 
	
	private ArrayList<GameObject> gameObjects; 
	
	private ChainCombo chainCombo;
	
	public GameField(Player player) {
		this(player, Options.DEFAULT_FIELD_WIDTH, Options.DEFAULT_FIELD_HEIGHT);
	}
	
	public GameField(Player player, int width, int height) {
		this.player = player;
		this.width = width;
		this.height = height;
		cells = new FieldCell[width][height];
	}
	
	/**
	 * get game tick count
	 * @return elapsed game time in ticks
	 */
	public int getTickCount() {
		return tickCount;
	}
	
	/**
	 * dispatch an event to the game field
	 */
	public void dispatchEvent(GameEvent event) {
		
	}
	
	/**
	 * dispatch a new tick to the game field
	 */
	public void dispatchTick() {
		tickCount++;
		if (Math.abs(tickCount-chainCombo.tick)>2) {
			player.updateScore(chainCombo.getScore());
			chainCombo=null;
		}
		updateField();
		
	}
	
	private void updateField() {
		
		
		
		//fixate objects
		for (int j=height-1; j>=0; j--)
			for (int i=0; i<width; i++)
				if (j==height-1 || cells[i][j+1].getState().isFixed())
					cells[i][j].fixate();
		
		//look for chains of puyos
		ArrayList<PuyoChain> chains = new ArrayList<PuyoChain>();
		int[][] chainmap = new int[width][height];
		for (int j=height-1; j>=0; j--)
			for (int i=0; i<width; i++) {
				// we want to skip CHAINED puyos so we don't use cells[i][j].getState().isFixed() here
				if (cells[i][j].isPuyo() && cells[i][j].getState().equals(GameObjectState.FIXED)) {
					ArrayList<Puyo> adjacent = new ArrayList<Puyo>();
					chainmap[i][j]=chains.size()+1; //mark source with a new chain number
					findAdjacentPuyos(chainmap, (Puyo)cells[i][j].gameObject, adjacent); 
					if (adjacent.size()>1) {
						cells[i][j].gameObject.setState(GameObjectState.CHAINED);
						chains.add(new PuyoChain(adjacent));	
					} else chainmap[i][j]=0; 
				}
			}
		
		//find chains with 4 elements or longer and release them
		//boolean released = false;
		for (PuyoChain chain:chains) {
			if (chain.size()>3) {
				if (chainCombo==null) chainCombo = new ChainCombo(chain, tickCount);
				else chainCombo.add(chain);
				for (Puyo puyo:chain.getChain()) 
					release(puyo);
			}
		}
		

		//update graphics
		
		//drop down after release
		for (int j=height-2; j>=0; j--)
			for (int i=0; i<width; i++)
				if (cells[i][j].isPuyo() && !cells[i][j+1].isEmpty()) {
					cells[i][j].gameObject.setState(GameObjectState.FALLING_FAST);
					cells[i][j+1].fill(cells[i][j].gameObject, i, j);
					cells[i][j].release();
					for (PuyoChain chain:chains) 
						if (chain.contains((Puyo)cells[i][j+1].gameObject) && chain.size()==2)
							chain.unchain();
				}
		
		
	}
	
	/**
	 * find any adjacent Puyo. 
	 * Puyo is considered adjacent if it has the same color and is a vertical or horizontal neighbour
	 * @param col column number of a field cell
	 * @param line line number of a field cell
	 * @return returns adjacent puyos (puyo in this field cell and neighbour puyos with the same color)
	 */
	private void findAdjacentPuyos(int[][] chainmap, Puyo source, ArrayList<Puyo> adjacentPuyos) {		
		int chainNum = chainmap[source.getColumn()][source.getLine()];
		adjacentPuyos.add(source);
		for (int i=0; i<4; i++) {
			int neighbour_col = source.getColumn();
			int neighbour_line = source.getLine();
			if (i%2==0) neighbour_col += i-1;
			else neighbour_line += i-2;
			if (isOutOfBounds(neighbour_col, neighbour_line)
					||chainmap[neighbour_col][neighbour_line]!=0) continue;
			FieldCell neighbour = cells[neighbour_col][neighbour_line];
			if (neighbour.isPuyo() && neighbour.gameObject.getColor()==source.getColor()) {
				neighbour.gameObject.setState(GameObjectState.CHAINED);
				chainmap[neighbour_col][neighbour_line]=chainNum;
				findAdjacentPuyos(chainmap, (Puyo)neighbour.gameObject, adjacentPuyos);
			}
		}
	}
	
	private boolean isOutOfBounds(int w, int h) {
		return (w<0||h<0||w>=width||h>=height);
		
	}
	
	private void release(GameObject object) {
		gameObjects.remove(object);
		cells[object.getColumn()][object.getLine()].release();
	}
	
	private boolean spawn(GameObject object, int column, int line, boolean isUnderControl) {
		if (cells[column][line].fill(object, column, line)) {
			gameObjects.add(object);
			object.setState(isUnderControl? GameObjectState.FALLING_SLOW:GameObjectState.FALLING_FAST);
			return true;
		}
		return false;
	}
	
	/**
	 * TODO: make spawn as transaction
	 * @param color1
	 * @param color2
	 * @return
	 */
	public boolean spawnPlayerTuple(int color1, int color2) {
		Puyo puyo = new Puyo(color1);
		if (spawn(puyo, width/2, 0, true)) {
				if (spawn(new Puyo(color2), width/2, 1, true))
					return true;
				else release(puyo);
		}
		return false;
	}
	
	public void prepareNextTuple() {
		
	}
	
	public class FieldCell {
		/**
		 * gameObject game object in this cell
		 */
		private GameObject gameObject;
		/**
		 * check whether this cell contains any game object
		 * @return true if cell is empty
		 */
		public boolean isEmpty() {
			if (gameObject==null) return true;
			return false;
		}
		
		public boolean isPuyo() {
			return (!isEmpty()&&gameObject.getColor()>0);
		}
			
		public GameObjectState getState() {
			if (isEmpty()) return GameObjectState.UNKNOWN;
			return gameObject.getState();
		}
		
		//public GameObject getObject() {
		//	return gameObject();
		//}
		
		public boolean fixate(){
			if (isEmpty()) return false;
			gameObject.setState(GameObjectState.FIXED);
			return true;
		}
		
		/**
		 * releases any gameObject in this cell
		 * @return true if object was successfully released 
		 */
		protected boolean release() {
			gameObject = null;
			return true;
		}
		/**
		 * put a game object in this cell
		 * @param gameObject game object to contain 
		 * @return false if cell is not empty and true otherwise
		 */
		protected boolean fill(GameObject gameObject, int column, int line) {
			if (!isEmpty()) return false;
			this.gameObject = gameObject;
			gameObject.setCoordinates(column, line);
			return true;
		}
	}

}
