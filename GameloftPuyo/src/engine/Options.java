package engine;

import java.awt.event.KeyEvent;

/**
 * this class contains options and configuration data.
 * Later a "save/load from file" feature could be implemented
 * to provide persistence of the config data
 *
 */

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
	
	/**
	 * how much slower puyos controlled by player fall
	 */
	public static int SLOW_DROP_TICKS = 3;
	
	/**
	 *  default game field dimensions
	 */
	public static int DEFAULT_FIELD_WIDTH = 6;
	public static int DEFAULT_FIELD_HEIGHT = 12;
	public static int CELL_WIDTH = 32;
	
	/**
	 * game version
	 */
	public static int NUMERIC_VERSION = 10;
	
	/**
	 * default video parameters
	 */
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
	
	public enum Languages {
		ENGLISH, RUSSIAN
	}
	
	/**
	 * selected language	
	 */
	public static Languages LANGUAGE = Languages.ENGLISH;
	
	/**
	 * get Strings for currently selected language
	 * @return localized strings wrapper object
	 */
	public static Strings getStrings() {
		if (LANGUAGE.equals(Languages.RUSSIAN)) 
			return new StringsRU();
		return new Strings();
	}
	
	/**
	 * Images
	 */
	
	public final static String[] imgLinks = new String[] {
			"puyo_red.png",
			"puyo_green.png",
			"puyo_blue.png",
			"puyo_yellow.png",
			"unknown.png",
	};
	
	public static String[] getImageLinks() {
		return imgLinks;
	}
}
