package game;

import java.util.Random;

import engine.GameSession;

public class Puyo extends GameObject{
	
	//idea behind using this seed is to let players with the same sessionId generate identical puyos
	//though it sounds balanced, both players getting the same puyo color pairs could be boring
	//and later this behavior could be improved (e.g. by selecting game mode in game session options etc)
	private static Random colorRandomizer = new Random(GameSession.getSessionId());

	public static final int RED = 0x00FF0000;
	public static final int GREEN = 0x0000FF00;
	public static final int BLUE = 0x000000FF;
	public static final int YELLOW = 0x00FFFF00;
	
	private int[] colors = {RED, GREEN, BLUE, YELLOW};
	
	public Puyo() {
		super();
		this.type = colors[colorRandomizer.nextInt(Integer.MAX_VALUE)%colors.length] | GameObject.PUYO_TYPE_MASK;
		System.out.println(Integer.toHexString(type));
	}
	
	public Puyo(int color) {
		super();
		this.type = color | GameObject.PUYO_TYPE_MASK;
	}
	
}
