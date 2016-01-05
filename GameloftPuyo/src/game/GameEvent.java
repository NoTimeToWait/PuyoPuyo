package game;
/**
 * an enumeration of in-game events
 *	these and only these events are consumed by Player and GameField classes
 */
public enum GameEvent {
	USERINPUT_UP, USERINPUT_DOWN, USERINPUT_DOWN_RELEASE, USERINPUT_LEFT, USERINPUT_RIGHT, STATUS_WIN, STATUS_LOSE
}
