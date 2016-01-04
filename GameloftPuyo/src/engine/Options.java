package engine;

import java.awt.event.KeyEvent;

public class Options {
	/**
	 * time in milliseconds between game ticks
	 */
	public static int GAME_TICK_TIME = 250; 
	
	/**
	 *  number of steps it takes to animate drop down from one field cell to another below
	 */
	public static int ANIMATION_ITERATIONS = 16;
	
	public static int getFramesPerTick() {
		return ANIMATION_ITERATIONS;//return Math.min(ANIMATION_ITERATIONS,  GAME_TICK_TIME*FRAMERATE/1000);
	}
	
	public static int SLOW_DROP_TICKS = 3;
	
	/**
	 *  default game field dimensions
	 */
	public static int DEFAULT_FIELD_WIDTH = 6;
	public static int DEFAULT_FIELD_HEIGHT = 12;
	public static int CELL_WIDTH = 32;
	
	
	public static int NUMERIC_VERSION = 1;
	public static int WINDOW_WIDTH = 640;
	public static int WINDOW_HEIGHT = 480;
	
	public static int FRAMERATE = 30;
	
	/**
	 * Keybindings
	 */
	
	public static int PLAYER1_ESC_KEY = KeyEvent.VK_ESCAPE;
	public static int PLAYER1_UP_KEY = KeyEvent.VK_UP;
	public static int PLAYER1_DOWN_KEY = KeyEvent.VK_DOWN;
	public static int PLAYER1_LEFT_KEY = KeyEvent.VK_LEFT;
	public static int PLAYER1_RIGHT_KEY = KeyEvent.VK_RIGHT;
	public static int PLAYER2_UP_KEY = KeyEvent.VK_W;
	public static int PLAYER2_DOWN_KEY = KeyEvent.VK_S;
	public static int PLAYER2_LEFT_KEY = KeyEvent.VK_A;
	public static int PLAYER2_RIGHT_KEY = KeyEvent.VK_D;
	
	/**
	 * Languages
	 */
	
	public static String LANGUAGE = "ENG";
	
	/**
	 * get Strings for currently selected language
	 * @return localized strings wrapper object
	 */
	public static Strings getStrings() {
		//if (LANGUAGE.equals("RUS")) return new StringsRU();
		return new Strings();
	}
	
	/**
	 * Images
	 */
	
	public final static String[] imgLinks = new String[] {
			"img/puyo_red.png",
			"img/puyo_green.png",
			"img/puyo_blue.png",
			"img/puyo_yellow.png",
			"img/unknown.png",
	};
	
	public static String[] getImageLinks() {
		return imgLinks;
	}
}
