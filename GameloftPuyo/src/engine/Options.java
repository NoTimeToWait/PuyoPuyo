package engine;

public class Options {
	/**
	 * time in milliseconds between game ticks
	 */
	public static int GAME_TICK_TIME = 500; 
	
	public static final int GRAPHICS_UPDATE_DELAY = 100;
	
	/**
	 *  number of steps it takes to drop puyo down from one field cell to another below
	 */
	public static int FALL_ITERATIONS_COUNT = 1;
	
	/**
	 *  default game field dimensions
	 */
	public static int DEFAULT_FIELD_WIDTH = 6;
	public static int DEFAULT_FIELD_HEIGHT = 12;
	public static int CELL_WIDTH = 32;
	
	
	public static int NUMERIC_VERSION = 1;
	public static int WINDOW_WIDTH = 192;
	public static int WINDOW_HEIGHT = 384;
	public static String LANGUAGE = "ENG";
	
	/**
	 * get Strings for currently selected language
	 * @return localized strings wrapper object
	 */
	public static Strings getStrings() {
		//if (LANGUAGE.equals("RUS")) return new StringsRU();
		return new Strings();
	}
}
