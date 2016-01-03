package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;

import engine.GameContext;
import engine.Options;
import engine.Player;
import visuals.FieldView;

public class GameField {
	private Player player;
	private int width;
	private int height;
	public static int tickCount;
	public static int spawnTick;
	/**
	 * game field represented as an array of cells
	 */
	public static FieldCell[][] cells;
	/**
	 * area where all puyos are prepared to drop onto the game field
	 */
	//private FieldCell[][] stagingArea; 
	
	private HashSet<GameObject> gameObjects;
	private ArrayList<Puyo> playerPuyo;
	private ArrayList<Point> rotationPoints;
	
	private ChainCombo chainCombo;
	
	public GameField(Player player) {
		this(player, Options.DEFAULT_FIELD_WIDTH, Options.DEFAULT_FIELD_HEIGHT);
	}
	
	public GameField(Player player, int width, int height) {
		this.player = player;
		this.width = width;
		this.height = height;
		cells = new FieldCell[width][height];
		for (int i=0; i<width; i++)
			for (int j=0; j<height; j++)
				cells[i][j] = new FieldCell();
		playerPuyo = new ArrayList<Puyo>();
		gameObjects = new HashSet<GameObject>();
		rotationPoints = new ArrayList<Point>();
		rotationPoints.add(new Point(1,0));
		rotationPoints.add(new Point(0,1));
		rotationPoints.add(new Point(-1,0));
		rotationPoints.add(new Point(0,-1));
	}
	
	
	
	/**
	 * dispatch an event to the game field
	 */
	public synchronized boolean dispatchEvent(GameEvent event) {
		if (event.equals(GameEvent.USERINPUT_LEFT)) {
			for (Puyo puyo:playerPuyo)
				if (puyo.getColumn()-1<0
					|| !(cells[puyo.getColumn()-1][puyo.getLine()].isEmpty()||playerPuyo.contains(cells[puyo.getColumn()-1][puyo.getLine()].gameObject)))
						return false;
			for (Puyo puyo:playerPuyo) cells[puyo.getColumn()][puyo.getLine()].release();
			for (Puyo puyo:playerPuyo) {
				cells[puyo.getColumn()-1][puyo.getLine()].fill(puyo,puyo.getColumn()-1, puyo.getLine());
				puyo.setCoordinates(puyo.getColumn()-1, puyo.getLine());
			}
		}
		if (event.equals(GameEvent.USERINPUT_RIGHT)) {
			for (Puyo puyo:playerPuyo)
				if (puyo.getColumn()+1>=width
					|| !(cells[puyo.getColumn()+1][puyo.getLine()].isEmpty()||playerPuyo.contains(cells[puyo.getColumn()+1][puyo.getLine()].gameObject)))
						return false;
			for (Puyo puyo:playerPuyo) cells[puyo.getColumn()][puyo.getLine()].release();
			for (Puyo puyo:playerPuyo) {
				cells[puyo.getColumn()+1][puyo.getLine()].fill(puyo,puyo.getColumn()+1, puyo.getLine());
				puyo.setCoordinates(puyo.getColumn()+1, puyo.getLine());
			}
		}
		if (event.equals(GameEvent.USERINPUT_UP)) {
			int difX = playerPuyo.get(1).getColumn()-playerPuyo.get(0).getColumn();
			int difY = playerPuyo.get(1).getLine()-playerPuyo.get(0).getLine();
			int index = rotationPoints.indexOf(new Point(difX, difY));
			for (int i=0; i<4; i++) {
				Point rotationPoint = rotationPoints.get((index+1+i)%rotationPoints.size());
				int col = playerPuyo.get(1).getColumn()+rotationPoint.x;
				int line = playerPuyo.get(1).getLine()+rotationPoint.y;
				if (isOutOfBounds(col, line)|| !cells[col][line].isEmpty()) continue;
				cells[playerPuyo.get(0).getColumn()][playerPuyo.get(0).getLine()].release();
				cells[col][line].fill(playerPuyo.get(0), col, line);
				playerPuyo.get(0).setCoordinates(col, line);
				break;
			}
			FieldView.shift(playerPuyo.get(0));
		}
		if (event.equals(GameEvent.USERINPUT_DOWN)) 
			for (Puyo puyo:playerPuyo)
				puyo.setState(GameObjectState.FALLING_FAST);
				
		return true;
	}
	
	/**
	 * dispatch a new tick to the game field
	 */
	public synchronized boolean dispatchTick() {
		tickCount++;
		if (chainCombo!=null && Math.abs(tickCount-chainCombo.tick)>2) {
			player.updateScore(chainCombo.getScore());
			chainCombo=null;
		}
		boolean nextPlayerTuple = false;
		for (Puyo puyo:playerPuyo)
			if (puyo.getState().isFixed()) {
				nextPlayerTuple = true; break;
			}
		if (nextPlayerTuple) 
			for (Puyo puyo:playerPuyo)
				if (!puyo.getState().isFixed()) 
					puyo.setState(GameObjectState.FALLING_FAST);
		if (tickCount%Options.SLOW_DROP_TICKS==1 && (nextPlayerTuple || playerPuyo.isEmpty())) spawnPlayerTuple(0,0);
		updateField();
		return true;
	}
	
	/**
	 * get game tick count
	 * @return elapsed game time in ticks
	 */
	public int getTickCount() {
		return tickCount;
	}
	
	public synchronized GameObject[] getObjectsToDraw(boolean all) {
		if (all) return gameObjects.toArray(new GameObject[0]);
		ArrayList<GameObject> result = new ArrayList<GameObject>();
		for (GameObject obj:gameObjects)
			if (!obj.getState().isFixed()) result.add(obj);
		return result.toArray(new GameObject[0]);
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
		boolean released = false;
		for (PuyoChain chain:chains) {
			if (chain.size()>3) {
				released = true;
				if (chainCombo==null) chainCombo = new ChainCombo(chain, tickCount);
				else chainCombo.add(chain);
				for (Puyo puyo:chain.getChain()) 
					release(puyo);
			}
		}
		

		//update graphics
		
		//change state to FALLING_FAST for all puyos hanging in air after chain release
		if (released)
			for (int j=height-2; j>=0; j--)
				for (int i=0; i<width; i++) 
					if (!cells[i][j].isEmpty() && !playerPuyo.contains(cells[i][j].gameObject))
						if (cells[i][j+1].isEmpty() || cells[i][j+1].getState().isFalling()) {
							cells[i][j].gameObject.setState(GameObjectState.FALLING_FAST);
						
							for (PuyoChain chain:chains) 
								if (chain.contains((Puyo)cells[i][j+1].gameObject) && chain.size()==2)
									chain.unchain();
						}
				
		//process fall
		for (int j=height-2; j>=0; j--)
			for (int i=0; i<width; i++)
				if (!cells[i][j].isEmpty()) 
					drop(cells[i][j].gameObject);
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
			if (neighbour.isPuyo()
					&& neighbour.gameObject.getType()==source.getType() 
					&& neighbour.getState().isFixed()) {
						neighbour.gameObject.setState(GameObjectState.CHAINED);
						chainmap[neighbour_col][neighbour_line]=chainNum;
						findAdjacentPuyos(chainmap, (Puyo)neighbour.gameObject, adjacentPuyos);
			}
		}
	}
	
	private boolean drop(GameObject obj) {
		if (!obj.getState().isFalling()) return false;
		//slow drop is 3 times slower 
		if (obj.getState().equals(GameObjectState.FALLING_SLOW)&&tickCount%Options.SLOW_DROP_TICKS!=0) return false;
		if (cells[obj.getColumn()][obj.getLine()+1].fill(obj, obj.getColumn(), obj.getLine()+1)) {
			cells[obj.getColumn()][obj.getLine()].release();
			obj.setCoordinates(obj.getColumn(), obj.getLine()+1);
			return true;
		}
		//System.out.println("Tick "+tickCount+" "+Integer.toHexString(obj.getType())+" "+obj.x+" "+obj.y);
		return false;
	}
	
	private boolean isOutOfBounds(int w, int h) {
		return (w<0||h<0||w>=width||h>=height);
	}
	
	private void release(GameObject object) {
		if (playerPuyo.contains(object)) prepareNextTuple();
		gameObjects.remove(object);
		cells[object.getColumn()][object.getLine()].release();
	}
	
	private boolean spawn(GameObject object, int column, int line, boolean isUnderControl) {
		if (cells[column][line].fill(object, column, line)) {
			gameObjects.add(object);
			object.setCoordinates(column, line);
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
		playerPuyo = new ArrayList<Puyo>();
		playerPuyo.add(color1==0? new Puyo() : new Puyo(color1));
		if (spawn(playerPuyo.get(0), width/2, 0, true)) {
			playerPuyo.add(color2==0? new Puyo() : new Puyo(color2));
				if (spawn(playerPuyo.get(1), width/2, 1, true)) {
					spawnTick = tickCount;
					return true;
				}
				else release(playerPuyo.get(0));
		}
		playerPuyo = new ArrayList<Puyo>();
		return false;
	}
	
	public void prepareNextTuple() {
		playerPuyo = new ArrayList<Puyo>();
	}
	
	public class FieldCell {
		/**
		 * gameObject game object in this cell
		 */
		public GameObject gameObject;
		/**
		 * check whether this cell contains any game object
		 * @return true if cell is empty
		 */
		public boolean isEmpty() {
			if (gameObject==null) return true;
			return false;
		}
		
		public boolean isPuyo() {
			return (!isEmpty()&&((gameObject.getType()&GameObject.PUYO_TYPE_MASK)==GameObject.PUYO_TYPE_MASK));
		}
			
		public GameObjectState getState() {
			if (isEmpty()) return GameObjectState.UNKNOWN;
			return gameObject.getState();
		}
		
		//public GameObject getObject() {
		//	return gameObject();
		//}
		
		public boolean fixate(){
			//if last fall iteration of last cell row
			if (isEmpty()) return false;
			if (getState().equals(GameObjectState.FALLING_SLOW)&&tickCount%Options.SLOW_DROP_TICKS!=0) return false;
			gameObject.setState(GameObjectState.FIXED);
			//System.out.println("Fixate"+Integer.toHexString(gameObject.getType()));
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
			return true;
		}
	}

}
