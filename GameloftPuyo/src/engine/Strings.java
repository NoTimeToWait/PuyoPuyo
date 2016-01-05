package engine;
/**
 * a simple class to handle strings and localization
 * a bit crude right now, though could be improved by reading strings from file
 *
 */

public class Strings {
	protected String TITLE = "Gameloft PuyoPuyo";
	protected String VERSION = "v0.9";
	protected String AUTHOR = "Sergey Lazarev";
	protected String YEAR = "2016";
	protected String LANGUAGES_LBL_TEXT = "Language";
	protected String LANGUAGES_LBL_ENG = "English";
	protected String LANGUAGES_LBL_RUS = "Russian";
	protected String CONTINUE_BTN_TEXT = "CONTINUE";
	protected String OPTIONS_BTN_TEXT = "OPTIONS";
	protected String START_BTN_TEXT = "NEW GAME";
	protected String NEXT_BTN_TEXT = "NEXT";
	protected String EXIT_BTN_TEXT = "EXIT";
	protected String BACK_BTN_TEXT = "BACK";
	protected String APPLY_BTN_TEXT = "APPLY";
	protected String SCORE_LBL_TEXT = "SCORE";
	protected String DEFAULT_PLAYER_NAME = "PLAYER";
	protected String CHAIN_COMBO_STRING = "CHAIN COMBO";
	protected String GAME_SPEED_LBL = "Game Speed";
	protected String GAME_OVER_MSG = "Game Over \n Your score is ";
	protected String[] GAME_SPEED = {"Fast", "Normal", "Slow"};
	
	public String getTitle() { return TITLE;}
	public String getVersion() { return VERSION;}
	public String getAuthor() {return AUTHOR;}
	public String getYear() {return YEAR;}
	public String getLanguagesLblText() {return LANGUAGES_LBL_TEXT;}
	public String getLanguagesLblEng() {return LANGUAGES_LBL_ENG;}
	public String getLanguagesLblRus() {return LANGUAGES_LBL_RUS;}
	public String getContinueBtnText() {return CONTINUE_BTN_TEXT;}
	public String getStartBtnText() { return START_BTN_TEXT;}
	public String getOptionsBtnText() { return OPTIONS_BTN_TEXT;}
	public String getNextBtnText() {return NEXT_BTN_TEXT;}
	public String getExitBtnText() {return EXIT_BTN_TEXT;}
	public String getBackBtnText() {return BACK_BTN_TEXT;}
	public String getApplyBtnText() {return APPLY_BTN_TEXT;}
	public String getScoreLblText() {return SCORE_LBL_TEXT;}
	public String getDefaultPlayerName() {return DEFAULT_PLAYER_NAME;}
	public String getChainComboString() {return CHAIN_COMBO_STRING;}
	public String getGameSpeedLblText() {return GAME_SPEED_LBL;}
	public String getGameOverMsg() {return GAME_OVER_MSG;}
	public String[] getGameSpeedStrings() {return GAME_SPEED;}
	
}


